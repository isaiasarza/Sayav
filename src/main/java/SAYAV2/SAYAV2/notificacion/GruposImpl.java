package SAYAV2.notificacion;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.xml.bind.JAXBException;

import SAYAV2.Utils.EstadoUtils;
import SAYAV2.Utils.FileUtils;
import SAYAV2.Utils.TipoMensajeUtils;
import SAYAV2.dao.TipoMensajeDao;
import SAYAV2.dao.UsuarioDao;
import SAYAV2.dao.VotacionesDao;
import SAYAV2.datos.DatoGrupo;
import SAYAV2.datos.DatoVoto;
import SAYAV2.mensajeria.Mensaje;
import SAYAV2.mensajeria.MensajeriaImpl;
import SAYAV2.model.DispositivoM;
import SAYAV2.model.Grupo;
import SAYAV2.model.Notificacion;
import SAYAV2.model.Peer;
import SAYAV2.model.Usuario;
import SAYAV2.service.FirebaseCloudMessageController;
import SAYAV2.service.JsonTransformer;

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
		this.usuarioFile = new File(FileUtils.getUsuarioFile());
		this.json = new JsonTransformer();
		this.votacionesFile = new File(FileUtils.getVotacionesFile());
		this.votacionesPendientesFile = new File(FileUtils.getVotacionesPendientesFile());
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
		this.tiposFile = new File(FileUtils.getTiposMensajesFile());
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
		mensaje.setEstado(EstadoUtils.Estado.PENDIENTE);
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
		mensaje.setEstado(EstadoUtils.Estado.PENDIENTE);
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
			msg.setEstado(EstadoUtils.Estado.PENDIENTE);
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
		Grupo grupo = usuarioDao.cargar(usuarioFile).getSingleGrupoById(votacion.grupoId);
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
		mensaje.setEstado(EstadoUtils.Estado.PENDIENTE);
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
	public void notificarMoviles(List<DispositivoM> moviles, Mensaje msg) throws JAXBException {
	
		String fecha = msg.imprimirFechaCreacion()+ " " + SimpleDateFormat.AM_PM_FIELD;
		msg.setDescripcion(msg.getDescripcion() + " " + fecha);
		if(!FirebaseCloudMessageController.post(msg.getTipoMensaje().getTipo(), msg.getDescripcion())){
			Mensaje m = msg.clone();
			m.setId(m.generateId());
			m.setEstado(EstadoUtils.Estado.PENDIENTE);
			m.setTipoMensaje(tiposMensajeDao.cargar(tiposFile).getTipo(TipoMensajeUtils.NOTIFICACION_MOVIL));
			mensajeria.guardarMensaje(m);
		}

	}

	public Notificacion recibirAlerta(Mensaje msg) throws JAXBException {
		Notificacion notificacion = new Notificacion();
		Usuario usuario = usuarioDao.cargar(usuarioFile);

		notificarMoviles(usuario.getDispositivosMoviles(), msg);
		
		notificacion.setTipo(msg.getTipoMensaje().getTipo());
		notificacion.setDescripcion("Se ha activado la alarma al miembro " + msg.getOrigen());
		notificacion.setDetalle(msg.getDescripcion());
		return notificacion;

	}

	public Notificacion recibirNuevoMiembro(Mensaje msg) throws JAXBException {
		Notificacion notificacion = new Notificacion();
		Usuario usuario = usuarioDao.cargar(usuarioFile);
		DatoGrupo datos = json.getGson().fromJson(msg.getDatos(), DatoGrupo.class);
		usuarioDao.agregarMiembro(usuario.getSingleGrupoById(datos.getGrupo().getId()), datos.getMiembro());
		notificacion.setTipo(msg.getTipoMensaje().getTipo());
		notificacion.setDescripcion("El miembro " + datos.getMiembro().getDireccion()
				+ " es parte del grupo " + datos.getGrupo().getNombre());
		notificacion.setDetalle("Fue agregado por el miembro " + msg.getOrigen());
		return notificacion;
	}

	public Notificacion recibirNuevoGrupo(Mensaje msg) throws JAXBException {
		Notificacion notificacion = new Notificacion();
		DatoGrupo datos = json.getGson().fromJson(msg.getDatos(), DatoGrupo.class);
		notificacion.setTipo(msg.getTipoMensaje().getTipo());
		notificacion.setDescripcion("Fue agregado al grupo " + datos.getGrupo().getNombre());
		notificacion.setDetalle("Fue agregado por el miembro " + msg.getOrigen());
		usuarioDao.agregarGrupo(datos.getGrupo());
		return notificacion;
	}

	public Notificacion recibirBajaMiembro(Mensaje msg) throws JAXBException {
		Notificacion notificacion = new Notificacion();
		DatoGrupo datos = json.getGson().fromJson(msg.getDatos(), DatoGrupo.class);
		usuarioDao.eliminarMiembro(datos.getGrupo(), datos.getMiembro());
		notificacion.setTipo(msg.getTipoMensaje().getTipo());
		notificacion.setDescripcion("El miembro " + datos.getMiembro().getDireccion()
				+ " dejo de ser parte del grupo " + datos.getGrupo().getNombre());
		if (msg.getOrigen().equals(datos.getMiembro())) {
			notificacion.setDetalle("El miembro abandono el grupo por propia voluntad.");
		} else {
			notificacion.setDetalle("El miembro fue dado de baja por acuerdo común en el grupo.");

		}
		return notificacion;
	}

	public Notificacion recibirSolicitudBaja(Mensaje msg) throws JAXBException {
		Notificacion notificacion = new Notificacion();
		Usuario usuario = usuarioDao.cargar(usuarioFile);

		Votacion votacion = json.getGson().fromJson(msg.getDatos(), Votacion.class);
		votacionesPendientes = votacionesDao.agregarVotacion(votacion, votacionesPendientes, votacionesPendientesFile);

		notificacion.setTipo(msg.getTipoMensaje().getTipo());
		notificacion.setDescripcion("Se ha solicitado la baja del miembro " + votacion.getMiembro().getDireccion());
		notificacion.setDetalle(
				"Para determinar si el miembro sera o no dado de baja, usted debera dirigirse al menu de votacion para votar:"
						+ "Si la mitad + 1 de los votantes estan de acuerdo, el miembro sera dado de baja");
	
		notificarMoviles(usuario.getDispositivosMoviles(), msg);
		return notificacion;
	}

	public Notificacion recibirVoto(Mensaje msg) throws Exception {

		DatoVoto datos = json.getGson().fromJson(msg.getDatos(), DatoVoto.class);
		Votacion votacion = votacionesDao.getVotacion(datos.getIdVotacion(), votacionesFile);
		Peer votante = new Peer(msg.getOrigen());
		if(votacion.getVotantes().contains(votante)){
			return null;
		}
		Notificacion notificacion = new Notificacion();
		notificacion.setTipo(TipoMensajeUtils.VOTO);
		notificacion.setDescripcion("Ha recibido un voto del miembro " + msg.getOrigen());
		
		votacion.getVotantes().add(votante);
		if (datos.isVoto()) {
			notificacion.setDetalle("El voto fue positivo");
			votacion.setVotantesAFavor(votacion.getVotantesAFavor() + 1);
		} else {
			notificacion.setDetalle("El voto fue negativo");
			votacion.setVotantesEnContra(votacion.getVotantesEnContra() + 1);
		}
		votacionesDao.actualizarVotacion(votacion,votacionesFile);
		procesarBajaMiembro(votacion);
		
		return notificacion;

	}

	public boolean isInit() {
		return init;
	}

	public void setInit(boolean init) {
		this.init = init;
	}

	public Notificacion confirmarAñadirMiembro(Mensaje msg) throws JAXBException {
		Notificacion notificacion = new Notificacion();
		DatoGrupo datos = json.getGson().fromJson(msg.getDatos(), DatoGrupo.class);
		String origen = usuarioDao.getSubdominio();
		Grupo grupo = usuarioDao.getGrupo(datos.getGrupo().getId());
		try {
			notificarNuevoMiembro(grupo, datos.getMiembro(), origen);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		this.usuarioDao.agregarMiembro(grupo, datos.getMiembro());
		notificacion.setTipo(msg.getTipoMensaje().getTipo());
		notificacion.setDescripcion("El miembro " + datos.getMiembro().getDireccion() + " es parte del grupo "
				+ datos.getGrupo().getNombre());
		notificacion.setDetalle("El grupo ahora contiene " + datos.getGrupo().getPeers().size() + " miembros");
		return notificacion;
	}

}
