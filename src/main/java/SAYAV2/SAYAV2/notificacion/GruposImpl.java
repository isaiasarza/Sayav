package SAYAV2.SAYAV2.notificacion;

import java.io.File;
import java.util.Date;
import java.util.List;

import javax.xml.bind.JAXBException;

import Datos.DatoGrupo;
import Datos.DatoVoto;
import SAYAV2.SAYAV2.Utils.EstadoUtils;
import SAYAV2.SAYAV2.Utils.TipoMensajeUtils;
import SAYAV2.SAYAV2.dao.TipoMensajeDao;
import SAYAV2.SAYAV2.dao.UsuarioDao;
import SAYAV2.SAYAV2.dao.VotacionesDao;
import SAYAV2.SAYAV2.mensajeria.Mensaje;
import SAYAV2.SAYAV2.mensajeria.MensajeriaImpl;
import SAYAV2.SAYAV2.model.DispositivoM;
import SAYAV2.SAYAV2.model.Grupo;
import SAYAV2.SAYAV2.model.Peer;
import SAYAV2.SAYAV2.model.Usuario;
import SAYAV2.SAYAV2.service.JsonTransformer;

public class GruposImpl implements Grupos, NotificacionesApi {
	private static GruposImpl grupos;
	private boolean init;
	private MensajeriaImpl mensajeria;
	private UsuarioDao usuarioDao;
	private File usuarioFile;
	private File votacionesFile;
	private File votacionesPendientesFile;
	private File tiposFile;
	private JsonTransformer json;
	@SuppressWarnings("unused")
	private Votaciones votaciones;
	private Votaciones votacionesPendientes;
	private VotacionesDao votacionesDao;
	private TipoMensajeDao tiposMensajeDao;

	public GruposImpl() {
		super();
		this.mensajeria = MensajeriaImpl.getInstance();
		this.usuarioDao = UsuarioDao.getInstance();
		this.usuarioFile = new File("SAYAV");
		this.json = new JsonTransformer();
		this.votacionesFile = new File("votaciones");
		this.votacionesPendientesFile = new File("votaciones_pendientes");
		this.votacionesDao = VotacionesDao.getInstance();
		this.votacionesDao.setFile(votacionesFile);
		this.init = false;
		try {
			this.votaciones = votacionesDao.cargar(votacionesFile);
			this.votacionesPendientes = votacionesDao.cargar(votacionesPendientesFile);
		} catch (JAXBException e) {
			e.printStackTrace();
			this.votaciones = new Votaciones();
			this.votacionesPendientes = new Votaciones();
		}
		this.tiposFile = new File("tipos_mensajes");
		this.tiposMensajeDao = TipoMensajeDao.getInstance();
		this.tiposMensajeDao.setFile(tiposFile);
	}

	public static GruposImpl getInstance() {
		if (grupos == null) {
			grupos = new GruposImpl();
		}
		return grupos;
	}

	public void init() {
		this.init = true;
		this.mensajeria.init();
	}

	private void notificarNuevoMiembro(Grupo grupo, Peer miembro, String origen) throws Exception {
		DatoGrupo datos = new DatoGrupo(miembro, grupo);

		Mensaje mensaje = new Mensaje();
		mensaje.setOrigen(origen);
		mensaje.setEstado(EstadoUtils.PENDIENTE);
		mensaje.setTipoMensaje(mensajeria.getTipos().getTipo(TipoMensajeUtils.NUEVO_MIEMBRO));
		mensaje.setTipoHandshake(TipoMensajeUtils.HANDSHAKE_REQUEST);
		mensaje.setDescripcion("El miembro " + miembro.getDireccion() + " es parte del grupo " + grupo.getNombre() + ":");
		mensaje.setDatos(json.render(datos));
		mensajeria.propagarMensaje(mensaje, grupo);
	}

	private boolean notificarNuevoGrupo(Grupo grupo, Peer miembro, String origen) throws Exception {
		grupo.addPeer(origen);
		Mensaje mensaje = new Mensaje();
		DatoGrupo datos = new DatoGrupo(miembro, grupo);

		mensaje.setOrigen(origen);
		mensaje.setDestino(miembro.getDireccion());
		mensaje.setEstado(EstadoUtils.PENDIENTE);
		mensaje.setTipoHandshake(TipoMensajeUtils.HANDSHAKE_REQUEST);
		mensaje.setTipoMensaje(mensajeria.getTipos().getTipo(TipoMensajeUtils.NUEVO_GRUPO));
		mensaje.setFechaCreacion(new Date());
		mensaje.setDescripcion("Usted es parte del grupo " + grupo.getNombre() + ":");
		mensaje.setDatos(json.render(datos));

		if (mensajeria.exist(datos, mensaje.getTipoMensaje(), mensaje.getTipoHandshake(), mensaje.getEstado())) {
			grupo.removePeer(origen);
			return false;
		}
		mensajeria.enviarSolicitud(mensaje);
		grupo.removePeer(origen);
		return true;
	}

	@Override
	public boolean añadirMiembro(Grupo grupo, Peer miembro) throws Exception {
		Usuario usuario;
		try {
			usuario = usuarioDao.cargar(usuarioFile);
			if (notificarNuevoGrupo(grupo, miembro, usuario.getSubdominio())) {
				return true;
			}
		} catch (JAXBException e) {
			e.printStackTrace();
		}
		return false;
	}

	@Override
	public void verMiembros(Grupo grupo) {

	}

	@Override
	public void solicitarBajaMiembro(Grupo grupo, Peer miembro) throws Exception {
		System.out.println("Solicitando Baja de " + miembro);
		Usuario usuario;
		Peer solicitante = new Peer();
		try {
			usuario = usuarioDao.cargar(usuarioFile);
			Votacion votacion = new Votacion(miembro, grupo.getId());
			votacion.setVotantesAFavor(1);
			this.votaciones = this.votacionesDao.agregarVotacion(votacion, votacionesFile);
			solicitante.setDireccion(usuario.getSubdominio());
			votacion.setSolicitante(solicitante);
			votacion.setVotantesAFavor(1);
			Mensaje msg = new Mensaje();
			grupo.removePeer(miembro);
			msg.setDatos(json.render(votacion));
			msg.setTipoMensaje(tiposMensajeDao.cargar(tiposFile).getTipo(TipoMensajeUtils.SOLICITUD_BAJA_MIEMBRO));
			msg.setDescripcion("Se ha solicitado la baja de un miembro");
			msg.setEstado(EstadoUtils.PENDIENTE);
			msg.setTipoHandshake(TipoMensajeUtils.HANDSHAKE_REQUEST);
			msg.setOrigen(usuario.getSubdominio());
			notificarGrupo(grupo, msg);
			grupo.add(miembro);
			notificarMoviles(usuario.getDispositivosMoviles(), msg);

		} catch (JAXBException e1) {
			e1.printStackTrace();
		}

	}

	@Override
	public void abandonarGrupo(Grupo grupo) throws Exception {

		Usuario usuario = usuarioDao.cargar(usuarioFile);

		// Creo un peer para mandarlo como datos
		Peer miembro = new Peer(usuario.getSubdominio());
		DatoGrupo datos = new DatoGrupo(miembro, grupo);

		// Creo el mensaje con los datos correspondientes a un abandonar grupo
		Mensaje mensaje = new Mensaje();
		mensaje.setOrigen(usuario.getSubdominio());
		mensaje.setTipoMensaje(tiposMensajeDao.cargar(tiposFile).getTipo(TipoMensajeUtils.BAJA_MIEMBRO));
		mensaje.setTipoHandshake(TipoMensajeUtils.HANDSHAKE_REQUEST);
		mensaje.setDescripcion("El miembro abandono el grupo el miembro");
		mensaje.setDatos(json.render(datos));

		// Propago el mensaje para informar a todos los miembros sobre la baja
		// de este miembro
		mensajeria.propagarMensaje(mensaje, grupo);
		// Elimino el grupo de mi lista de grupos
		usuarioDao.eliminarGrupo(grupo);
	}

	@Override
	public void aceptarBajaMiembro(Votacion votacion) throws Exception {
		// Voto positivo
		votacion.setVotantesAFavor(votacion.getVotantesAFavor() + 1);

		Usuario usuario = usuarioDao.cargar(usuarioFile);

		// Creo un peer para mandarlo como datos
		DatoVoto datos = new DatoVoto(votacion.getId(), true);

		// Creo el mensaje con los datos correspondientes a un voto a favor
		System.out.println(datos);
		Mensaje mensaje = new Mensaje();
		mensaje.setOrigen(usuario.getSubdominio());
		mensaje.setTipoMensaje(tiposMensajeDao.cargar(tiposFile).getTipo(TipoMensajeUtils.VOTO));
		mensaje.setDescripcion("Voto el miembro");
		mensaje.setDestino(votacion.getSolicitante().getDireccion());
		mensaje.setDatos(json.render(datos));
		mensaje.setTipoHandshake(TipoMensajeUtils.HANDSHAKE_REQUEST);
		mensaje.setDestino(votacion.getSolicitante().getDireccion());
		// Envio mi voto
		mensajeria.enviarSolicitud(mensaje);
	}

	@Override
	public void rechazarBajaMiembro(Votacion votacion) throws Exception {
		// Voto negativo
		votacion.setVotantesEnContra(votacion.getVotantesEnContra() + 1);

		Usuario usuario = usuarioDao.cargar(usuarioFile);

		// Creo un peer para mandarlo como datos
		DatoVoto datos = new DatoVoto(votacion.getId(), false);

		// Creo el mensaje con los datos correspondientes a un voto a favor
		Mensaje mensaje = new Mensaje();
		mensaje.setOrigen(usuario.getSubdominio());
		mensaje.setTipoMensaje(tiposMensajeDao.cargar(tiposFile).getTipo(TipoMensajeUtils.VOTO));
		mensaje.setDescripcion("Voto el miembro");
		mensaje.setDestino(votacion.getSolicitante().getDireccion());
		mensaje.setDatos(json.render(datos));
		mensaje.setTipoHandshake(TipoMensajeUtils.HANDSHAKE_REQUEST);
		mensaje.setDestino(votacion.getSolicitante().getDireccion());

		// Envio mi voto
		mensajeria.enviarSolicitud(mensaje);

	}

	@Override
	public void procesarBajaMiembro(Votacion votacion) throws Exception {
		Grupo grupo = usuarioDao.cargar(usuarioFile).getSingleGrupoById(votacion.getId());
		double minVotantes = grupo.getPeers().size();
		minVotantes /= 2;
		minVotantes = Math.ceil(minVotantes);
		if (votacion.getVotantesEnContra() >= minVotantes) {
			votacion.setFinalizo(true);
			return;
		}
		try {
			if (votacion.getVotantesAFavor() >= minVotantes) {
				votacion.setFinalizo(true);
				bajaMiembro(grupo, votacion.getMiembro());
			}
		} catch (JAXBException e) {
			e.printStackTrace();
		}

	}

	private void bajaMiembro(Grupo grupo, Peer miembro) throws Exception {
		Usuario usuario = usuarioDao.cargar(usuarioFile);
		Peer eliminado;
		Mensaje mensaje = new Mensaje();
		Grupo g = usuario.getSingleGrupoById(grupo.getId());
		DatoGrupo datos = new DatoGrupo(miembro, g);
		mensaje.setDatos(json.render(datos));
		mensaje.setDescripcion("Se voto dar de baja al miembro");
		mensaje.setTipoMensaje(tiposMensajeDao.cargar(tiposFile).getTipo(TipoMensajeUtils.BAJA_MIEMBRO));
		mensaje.setOrigen(usuario.getSubdominio());
		mensaje.setTipoHandshake(TipoMensajeUtils.HANDSHAKE_REQUEST);
		mensaje.setEstado(EstadoUtils.PENDIENTE);
		eliminado = notificarBajaMiembro(g, mensaje, miembro);
		notificarBajaGrupo(mensaje, eliminado);
	}

	private void notificarBajaGrupo(Mensaje mensaje, Peer eliminado) throws Exception {
		mensaje.setDestino(eliminado.getDireccion());
		mensaje.setDescripcion("Usted ha sido dado de baja");
		mensaje.setTipoMensaje(tiposMensajeDao.cargar(tiposFile).getTipo(TipoMensajeUtils.BAJA_GRUPO));
		mensajeria.enviarSolicitud(mensaje);
	}

	private Peer notificarBajaMiembro(Grupo g, Mensaje mensaje, Peer miembro) throws Exception {
		Peer eliminado = usuarioDao.eliminarMiembro(g, miembro);
		g.removePeer(miembro);
		notificarGrupo(g, mensaje);
		return eliminado;
	}

	@Override
	public void notificarGrupo(Grupo grupo, Mensaje msg) throws Exception {
		mensajeria.propagarMensaje(msg, grupo);
	}

	@Override
	public void notificarMovil(DispositivoM movil, Mensaje msg) {
		// TODO Auto-generated method stub

	}

	@Override
	public void notificarGrupos(List<Grupo> grupos, Mensaje msg) throws Exception {
		for (Grupo g : grupos) {
			notificarGrupo(g, msg);
		}
	}

	@Override
	public void notificarMoviles(List<DispositivoM> moviles, Mensaje msg) {
		// TODO Auto-generated method stub

	}

	public void recibirAlerta(Mensaje msg) throws JAXBException {

		Usuario usuario = usuarioDao.cargar(usuarioFile);

		notificarMoviles(usuario.getDispositivosMoviles(), msg);

	}

	public void recibirNuevoMiembro(Mensaje msg) throws JAXBException {
		Usuario usuario = usuarioDao.cargar(usuarioFile);
		DatoGrupo datos = json.getGson().fromJson(msg.getDatos(), DatoGrupo.class);
		usuarioDao.agregarMiembro(usuario.getSingleGrupoById(datos.getGrupo().getId()), datos.getMiembro());
	}

	public void recibirNuevoGrupo(Mensaje msg) throws JAXBException {

		DatoGrupo datos = json.getGson().fromJson(msg.getDatos(), DatoGrupo.class);
		usuarioDao.agregarGrupo(datos.getGrupo());
	}

	public void recibirBajaMiembro(Mensaje msg) throws JAXBException {

		DatoGrupo datos = json.getGson().fromJson(msg.getDatos(), DatoGrupo.class);
		System.out.println(datos);
		usuarioDao.eliminarMiembro(datos.getGrupo(), datos.getMiembro());

	}

	public void recibirSolicitudBaja(Mensaje msg) throws JAXBException {

		Usuario usuario = usuarioDao.cargar(usuarioFile);

		Votacion votacion = json.getGson().fromJson(msg.getDatos(), Votacion.class);
		//TODO verificar que la votacion no exista, si existe se ignora la solicitud
		votacionesPendientes = votacionesDao.agregarVotacion(votacion, votacionesPendientes, votacionesPendientesFile);

		notificarMoviles(usuario.getDispositivosMoviles(), msg);

	}

	public void recibirVoto(Mensaje msg) throws Exception {

		DatoVoto datos = json.getGson().fromJson(msg.getDatos(), DatoVoto.class);
		Votacion votacion = votacionesDao.getVotacion(datos.getIdVotacion(), votacionesFile);

		Peer votante = new Peer(msg.getOrigen());
		if(votacion.getVotantes().contains(votante)){
			return;
		}
		votacion.getVotantes().add(votante);
		if (datos.isVoto()) {
			votacion.setVotantesAFavor(votacion.getVotantesAFavor() + 1);
		} else {
			votacion.setVotantesEnContra(votacion.getVotantesEnContra() + 1);
		}
		votacionesDao.actualizarVotacion(votacion,votacionesFile);
		procesarBajaMiembro(votacion);

	}

	public boolean isInit() {
		return init;
	}

	public void setInit(boolean init) {
		this.init = init;
	}

	public void confirmarAñadirMiembro(Mensaje msg) throws JAXBException {
		DatoGrupo datos = json.getGson().fromJson(msg.getDatos(), DatoGrupo.class);
		String origen = usuarioDao.getSubdominio();
		Grupo grupo = usuarioDao.getGrupo(datos.getGrupo().getId());
		try {
			notificarNuevoMiembro(grupo, datos.getMiembro(), origen);
		} catch (Exception e) {
			e.printStackTrace();
		}
		this.usuarioDao.agregarMiembro(grupo, datos.getMiembro());
	}

}
