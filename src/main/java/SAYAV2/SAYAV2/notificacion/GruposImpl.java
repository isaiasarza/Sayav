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

public class GruposImpl implements Grupos, Notificaciones {
	private static GruposImpl grupos;
	private boolean init;
	private MensajeriaImpl mensajeria;
	private UsuarioDao usuarioDao;
	private File usuarioFile;
	private File votacionesFile;
	private File votacionesPendientesFile;
	private File tiposFile;
	private JsonTransformer json;
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

	private void notificarNuevoMiembro(Grupo grupo, Peer miembro, String origen) {
		DatoGrupo datos = new DatoGrupo(miembro, grupo);

		Mensaje mensaje = new Mensaje();
		mensaje.setOrigen(origen);
		mensaje.setDestino(miembro.getDireccion());
		mensaje.setEstado(EstadoUtils.PENDIENTE);
		mensaje.setTipoMensaje(mensajeria.getTipos().getTipo(TipoMensajeUtils.NUEVO_MIEMBRO));
		mensaje.setFechaCreacion(new Date());
		mensaje.setDescripcion("Usted es parte de un nuevo grupo");
		mensaje.setDatos(json.render(datos));
		mensajeria.propagarMensaje(mensaje, grupo);
	}

	private void notificarNuevoGrupo(Grupo grupo, Peer miembro, String origen) {
		Mensaje mensaje = new Mensaje();
		grupo.add(origen);
		DatoGrupo datos = new DatoGrupo(miembro, grupo);

		mensaje.setOrigen(origen);
		mensaje.setDestino(miembro.getDireccion());
		mensaje.setEstado(EstadoUtils.PENDIENTE);
		mensaje.setTipoMensaje(mensajeria.getTipos().getTipo(TipoMensajeUtils.NUEVO_GRUPO));
		mensaje.setFechaCreacion(new Date());
		mensaje.setDescripcion("Usted es parte de un nuevo grupo");
		mensaje.setDatos(json.render(datos));
		mensajeria.enviarSolicitud(mensaje);
		grupo.removePeer(origen);
	}

	@Override
	public void añadirMiembro(Grupo grupo, Peer miembro) {
		Usuario usuario;
		try {
			usuario = usuarioDao.cargar(usuarioFile);
			notificarNuevoGrupo(grupo, miembro, usuario.getSubdominio());
			notificarNuevoMiembro(grupo, miembro, usuario.getSubdominio());
		} catch (JAXBException e) {
			e.printStackTrace();
		}

	}

	@Override
	public void verMiembros(Grupo grupo) {

	}

	@Override
	public void solicitarBajaMiembro(Grupo grupo, Peer miembro) {
		System.out.println("Solicitando Baja de " + miembro);
		Usuario usuario;
		Peer solicitante = new Peer();
		try {
			usuario = usuarioDao.cargar(usuarioFile);
			Votacion votacion = new Votacion(miembro, grupo);
			votacion.setVotantesAFavor(1);
			this.votaciones = this.votacionesDao.agregarVotacion(votacion, votaciones, votacionesFile);
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
	public void abandonarGrupo(Grupo grupo) throws JAXBException {

		Usuario usuario = usuarioDao.cargar(usuarioFile);

		// Creo un peer para mandarlo como datos
		Peer miembro = new Peer();
		DatoGrupo datos = new DatoGrupo(miembro, grupo);

		// Creo el mensaje con los datos correspondientes a un abandonar grupo
		Mensaje mensaje = new Mensaje();
		mensaje.setOrigen(usuario.getSubdominio());
		mensaje.setTipoMensaje(tiposMensajeDao.cargar(tiposFile).getTipo(TipoMensajeUtils.BAJA_MIEMBRO));
		mensaje.setDescripcion("Abandono el grupo el miembro");
		mensaje.setDatos(json.render(datos));

		// Propago el mensaje para informar a todos los miembros sobre la baja
		// de este miembro
		mensajeria.propagarMensaje(mensaje, grupo);
		// Elimino el grupo de mi lista de grupos
		usuarioDao.eliminarGrupo(grupo);
	}

	@Override
	public void aceptarBajaMiembro(Votacion votacion) throws JAXBException {
		// Voto positivo
		votacion.setVotantesAFavor(votacion.getVotantesAFavor() + 1);

		Usuario usuario = usuarioDao.cargar(usuarioFile);

		// Creo un peer para mandarlo como datos
		DatoVoto datos = new DatoVoto(votacion.getId(), true);

		// Creo el mensaje con los datos correspondientes a un voto a favor
		Mensaje mensaje = new Mensaje();
		mensaje.setOrigen(usuario.getSubdominio());
		mensaje.setTipoMensaje(tiposMensajeDao.cargar(tiposFile).getTipo(TipoMensajeUtils.VOTO));
		mensaje.setDescripcion("Voto el miembro");
		mensaje.setDestino(votacion.getSolicitante().getDireccion());
		mensaje.setDatos(json.render(datos));
		mensaje.setTipoHandshake(TipoMensajeUtils.HANDSHAKE_RESPONSE);
        mensaje.setDestino(votacion.getSolicitante().getDireccion());
		// Envio mi voto
		mensajeria.enviarConfirmacion(mensaje);
	}

	@Override
	public void rechazarBajaMiembro(Votacion votacion) throws JAXBException {
		// Voto negativo
		votacion.setVotantesEnContra(votacion.getVotantesEnContra() + 1);

		Usuario usuario = usuarioDao.cargar(usuarioFile);

		// Creo un peer para mandarlo como datos
		DatoVoto datos = new DatoVoto(votacion.getId(), true);

		// Creo el mensaje con los datos correspondientes a un voto a favor
		Mensaje mensaje = new Mensaje();
		mensaje.setOrigen(usuario.getSubdominio());
		mensaje.setTipoMensaje(tiposMensajeDao.cargar(tiposFile).getTipo(TipoMensajeUtils.VOTO));
		mensaje.setDescripcion("Voto el miembro");
		mensaje.setDestino(votacion.getSolicitante().getDireccion());
		mensaje.setDatos(json.render(datos));
		mensaje.setTipoHandshake(TipoMensajeUtils.HANDSHAKE_RESPONSE);
		mensaje.setDestino(votacion.getSolicitante().getDireccion());
		
		// Envio mi voto
		mensajeria.enviarConfirmacion(mensaje);

	}

	@Override
	public void procesarBajaMiembro(Votacion votacion) {
		double minVotantes = votacion.getGrupo().getPeers().size();
		minVotantes /= 2;
		minVotantes = Math.ceil(minVotantes);
		if (votacion.getVotantesAFavor() + votacion.getVotantesEnContra() >= minVotantes) {
			votacion.setFinalizo(true);
			Usuario usuario;
			try {
				usuario = usuarioDao.cargar(usuarioFile);
				if (votacion.getVotantesAFavor() >= minVotantes) {
					Peer eliminado;
					Mensaje mensaje = new Mensaje();
					mensaje.setDescripcion("Se voto dar de baja al miembro");
					mensaje.setTipoMensaje(tiposMensajeDao.cargar(tiposFile).getTipo(TipoMensajeUtils.BAJA_MIEMBRO));
					mensaje.setOrigen(usuario.getSubdominio());
					mensaje.setTipoHandshake(TipoMensajeUtils.HANDSHAKE_REQUEST);
					mensaje.setEstado(EstadoUtils.PENDIENTE);
					Grupo g = usuario.getSingleGrupoById(votacion.getGrupo().getId());
					eliminado = notificarBajaMiembro(g, mensaje, votacion.getMiembro());
					notificarBajaGrupo(mensaje, eliminado);
				}
				// this.votaciones =
				// votacionesDao.eliminarVotacion(votacion,votacionesFile);
			} catch (JAXBException e) {
				e.printStackTrace();
			}
		}
	}

	private void notificarBajaGrupo(Mensaje mensaje, Peer eliminado) throws JAXBException {
		mensaje.setDestino(eliminado.getDireccion());
		mensaje.setTipoMensaje(tiposMensajeDao.cargar(tiposFile).getTipo(TipoMensajeUtils.BAJA_MIEMBRO));
		mensajeria.enviarSolicitud(mensaje);
	}

	private Peer notificarBajaMiembro(Grupo g, Mensaje mensaje, Peer miembro) throws JAXBException {
		Peer eliminado = usuarioDao.eliminarMiembro(g, miembro);
		notificarGrupo(g, mensaje);
		return eliminado;
	}

	@Override
	public void notificarGrupo(Grupo grupo, Mensaje msg) {
		mensajeria.propagarMensaje(msg, grupo);
	}

	@Override
	public void notificarMovil(DispositivoM movil, Mensaje msg) {
		// TODO Auto-generated method stub

	}

	@Override
	public void notificarGrupos(List<Grupo> grupos, Mensaje msg) {
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
		Grupo g = usuarioDao.getGrupo(datos.getGrupo().getId());
		usuarioDao.eliminarMiembro(g, datos.getMiembro());

	}

	public void recibirSolicitudBaja(Mensaje msg) throws JAXBException {

		Usuario usuario = usuarioDao.cargar(usuarioFile);

		Votacion votacion = json.getGson().fromJson(msg.getDatos(), Votacion.class);
		votacionesPendientes = votacionesDao.agregarVotacion(votacion, votacionesPendientes, votacionesPendientesFile);

		notificarMoviles(usuario.getDispositivosMoviles(), msg);

	}

	public void recibirVoto(Mensaje msg) throws JAXBException {

		DatoVoto datos = json.getGson().fromJson(msg.getDatos(), DatoVoto.class);
		Votacion votacion = votacionesDao.getVotacion(datos.getIdVotacion(), votacionesFile);

		if (datos.isVoto()) {
			votacion.setVotantesAFavor(votacion.getVotantesAFavor() + 1);
		} else {
			votacion.setVotantesEnContra(votacion.getVotantesEnContra() + 1);
		}

		procesarBajaMiembro(votacion);

	}

	public boolean isInit() {
		return init;
	}

	public void setInit(boolean init) {
		this.init = init;
	}

}
