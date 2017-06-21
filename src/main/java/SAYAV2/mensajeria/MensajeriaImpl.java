package SAYAV2.mensajeria;

import java.io.File;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.xml.bind.JAXBException;

import SAYAV2.Utils.EstadoUtils;
import SAYAV2.Utils.FechaUtils;
import SAYAV2.Utils.FileUtils;
import SAYAV2.Utils.TipoMensajeUtils;
import SAYAV2.bussines.ControllerMQTT;
import SAYAV2.dao.MensajePendienteDao;
import SAYAV2.dao.NotificacionesDao;
import SAYAV2.dao.TipoMensajeDao;
import SAYAV2.dao.UsuarioDao;
import SAYAV2.datos.DatoGrupo;
import SAYAV2.model.Grupo;
import SAYAV2.model.MensajesPendientes;
import SAYAV2.model.Notificacion;
import SAYAV2.model.Peer;
import SAYAV2.model.TiposMensajes;
import SAYAV2.notificacion.GruposImpl;
import SAYAV2.service.FirebaseCloudMessageController;
import SAYAV2.service.JsonTransformer;

public class MensajeriaImpl implements Mensajeria {

	private static MensajeriaImpl mensImpl;
	private UsuarioDao usuarioDao;
	private File file;
	private NotificacionesDao notificacionesDao;
	private File notificacionesFile;
	private MensajesPendientes mensajes;
	private TipoMensajeDao tipoMensajeDao;
	private MensajePendienteDao mensajesDao;
	private static File tiposFile;
	private static File mensajesFile;
	private TiposMensajes tipos;
	private static ControllerMQTT conn;
	private JsonTransformer json;
	private GruposImpl gruposImpl;

	private MensajeriaImpl() {
		super();
		tiposFile = new File(FileUtils.getTiposMensajesFile());
		mensajesFile = new File(FileUtils.getMensajesFile());
		this.tipoMensajeDao = TipoMensajeDao.getInstance();
		this.mensajesDao = MensajePendienteDao.getInstance();
		this.mensajesDao.setFile(mensajesFile);
		this.file = new File(FileUtils.getUsuarioFile());
		this.usuarioDao = UsuarioDao.getInstance();
		this.usuarioDao.setFile(file);
		this.json = new JsonTransformer();
		this.notificacionesFile = new File(FileUtils.getNotificacionesFile());
		this.notificacionesDao = NotificacionesDao.getInstance();
		this.notificacionesDao.setFile(notificacionesFile);
		try {
			if (mensajesFile.exists()) {
				setMensajes(this.mensajesDao.cargar(mensajesFile));
			} else {
				this.mensajes = new MensajesPendientes();
			}
			setTipos(this.tipoMensajeDao.cargar(tiposFile));
		} catch (JAXBException e) {
			e.printStackTrace();
		}

	}

	public void init() {
		this.gruposImpl = new GruposImpl();
	}

	public GruposImpl getGruposImpl() {
		return gruposImpl;
	}

	public void setGruposImpl(GruposImpl gruposImpl) {
		this.gruposImpl = gruposImpl;
	}

	public static MensajeriaImpl getInstance() {
		if (mensImpl == null) {
			mensImpl = new MensajeriaImpl();
			conn = ControllerMQTT.getInstance();
			conn.start();
		}

		return mensImpl;
	}

	/**
	 * @author
	 * @param Mensaje
	 *            msg
	 * @return Mensaje nuevo mensaje formado en la acción Este metodo toma un
	 *         mensaje recibido, según el tipo de mensaje correspondiente toma
	 *         la acción debida.
	 * @throws Exception
	 * @throws JAXBException
	 */
	@Override
	public void procesarMensaje(Mensaje msg) throws Exception {
		Notificacion notificacion;

		try {
			if (msg.getTipoHandshake().equals(TipoMensajeUtils.HANDSHAKE_REQUEST)) {

				if (msg.getTipoMensaje().getTipo().equals(TipoMensajeUtils.ALERTA)) {
					notificacion = gruposImpl.recibirAlerta(msg);
					notificacionesDao.agregarNotificacion(notificacion);
					gruposImpl.notificarMoviles(null, msg);
					return;
				}
				if (msg.getTipoMensaje().getTipo().equals(TipoMensajeUtils.NUEVO_MIEMBRO)) {
					notificacion = gruposImpl.recibirNuevoMiembro(msg);
					notificacionesDao.agregarNotificacion(notificacion);
					gruposImpl.notificarMoviles(null, msg);

					return;
				}
				if(msg.getTipoMensaje().getTipo().equals(TipoMensajeUtils.VOTO)){
					notificacion = gruposImpl.recibirVoto(msg);
					if(notificacion != null){
						notificacionesDao.agregarNotificacion(notificacion);
					}
					gruposImpl.notificarMoviles(null, msg);
					return;
				}
				if (msg.getTipoMensaje().getTipo().equals(TipoMensajeUtils.NUEVO_GRUPO)) {
					notificacion = gruposImpl.recibirNuevoGrupo(msg);
					notificacionesDao.agregarNotificacion(notificacion);
					gruposImpl.notificarMoviles(null, msg);

					return;
				}
				if (msg.getTipoMensaje().getTipo().equals(TipoMensajeUtils.BAJA_MIEMBRO)) {
					notificacion = gruposImpl.recibirBajaMiembro(msg);
					notificacionesDao.agregarNotificacion(notificacion);
					gruposImpl.notificarMoviles(null, msg);

					return;
				}
				if (msg.getTipoMensaje().getTipo().equals(TipoMensajeUtils.SOLICITUD_BAJA_MIEMBRO)) {
					notificacion = gruposImpl.recibirSolicitudBaja(msg);
					notificacionesDao.agregarNotificacion(notificacion);
					gruposImpl.notificarMoviles(null, msg);

					return;
				}
			}
			if (msg.getTipoMensaje().getTipo().equals(TipoMensajeUtils.VOTO)) {
				return;
			}
			if (msg.getTipoMensaje().getTipo().equals(TipoMensajeUtils.NUEVO_GRUPO)) {
				notificacion = gruposImpl.confirmarAñadirMiembro(msg);
				notificacionesDao.agregarNotificacion(notificacion);
				gruposImpl.notificarMoviles(null, msg);
				Mensaje mensaje = msg.clone();
				mensaje.setOrigen(msg.getDestino());
				mensaje.setDestino(msg.getOrigen());
				mensaje.setTipoHandshake(TipoMensajeUtils.HANDSHAKE_RESPONSE);
				mensaje.setTipoMensaje(tipoMensajeDao.cargar(tiposFile).getTipo(TipoMensajeUtils.OK_CONFIRMACION));
				mensaje.setEstado(EstadoUtils.Estado.CONFIRMADO);	
				enviarConfirmacion(mensaje);
				return;
			}
		} catch (JAXBException e) {
			e.printStackTrace();
		}

	}

	/**
	 * @author
	 * @param Mensaje
	 *            msg
	 * @param Grupo
	 *            g Este metodo propaga un mensaje al grupo recibido por
	 *            parametro.
	 * @throws Exception
	 */
	@Override
	public void propagarMensaje(Mensaje msg, Grupo g) {

		for (Peer miembro : g.getPeers()) {
			Mensaje mensaje = (Mensaje) msg.clone();
			mensaje.setId(mensaje.generateId());
			try {
				mensaje.setDestino(miembro.getDireccion());
			} catch (Exception e) {
				e.printStackTrace();
				break;
			}

			if (msg.getTipoHandshake().equals(TipoMensajeUtils.HANDSHAKE_REQUEST)) {
				enviarSolicitud(mensaje);
			} else {
				enviarConfirmacion(mensaje);
			}
		}
	}

	/**
	 * @author
	 * @param Mensaje
	 *            mensaje a guardar Guarda el mensaje.
	 */
	@Override
	public synchronized void guardarMensaje(Mensaje msg) {
		if (this.mensajes.addMensaje(msg)) {
			this.mensajesDao.guardar(this.mensajes, mensajesFile);
			try {
				this.mensajes = mensajesDao.cargar(mensajesFile);
			} catch (JAXBException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * 
	 */
	@Override
	public synchronized void actualizarMensaje(Mensaje msg) {
		Mensaje viejo = this.mensajes.getMensaje(msg.getId());
		viejo.setFechaReenvio(new Date());
		viejo.setEstado(msg.getEstado());
		mensajesDao.guardar(this.mensajes, mensajesFile);
	}

	/**
	 * @author
	 * @param Mensaje
	 * @return true si hay que eliminar el mensaje
	 *            Reenvia el mensaje indicado.
	 */
	@Override
	public synchronized boolean reenviarMensaje(Mensaje msg, Date fechaActual) {
		long aux = TimeUnit.DAYS.toMinutes(EstadoUtils.TIME_TO_LIVE_CONF);
		long diff = FechaUtils.diffDays(msg.getFechaCreacion(), fechaActual);
		
		if (msg.getEstado().equals(EstadoUtils.Estado.CONFIRMADO) && diff >=  aux) {
			return true;
		}
		
		if (msg.getEstado().equals(EstadoUtils.Estado.CONFIRMADO) || msg.getEstado().equals(EstadoUtils.Estado.ZOMBIE)) {
			return false;
		}
		
		if(msg.getTipoMensaje().getTipo().equals(TipoMensajeUtils.NOTIFICACION_MOVIL) && msg.getEstado().equals(EstadoUtils.Estado.PENDIENTE)){
			if(FirebaseCloudMessageController.post(msg.getTipoMensaje().getTipo(), msg.getDescripcion())){
				msg.setEstado(EstadoUtils.Estado.CONFIRMADO);
				actualizarMensaje(msg);
			}
			return false;
		}		
		
		if (msg.getEstado().equals(EstadoUtils.Estado.PENDIENTE) && diff >= msg.getTipoMensaje().getTimetolive()) {
			msg.setEstado(EstadoUtils.Estado.ZOMBIE);
			actualizarMensaje(msg);
			return false;
		}
		if (diff < msg.getTipoMensaje().getQuantum()) {
			return false;
		}
		msg.getFechaReenvio().setTime(fechaActual.getTime());
		actualizarMensaje(msg);

		if (msg.getTipoHandshake().equals(TipoMensajeUtils.HANDSHAKE_REQUEST)) {
			enviarSolicitud(msg);
			return false;
		}
		enviarConfirmacion(msg);
		return false;
	}

	/**
	 * @author
	 * @param Mensaje
	 *            mensaje a enviar Envia una solicitud hacia otro destino junto
	 *            con un mensaje.
	 */
	@Override
	public String enviarSolicitud(Mensaje msg) throws IllegalArgumentException{
		if(msg.getOrigen().equals(msg.getDestino())){
			throw new IllegalArgumentException("El origen y el miembro son iguales");
		}
		msg.setEstado(EstadoUtils.Estado.PENDIENTE);
		msg.setTipoHandshake(TipoMensajeUtils.HANDSHAKE_REQUEST);
		guardarMensaje(msg);
		String m = json.render(msg);

		String topic = msg.getDestino() + "/" + msg.getTipoHandshake();
		conn.send(topic, m, 2);
		return m;
	}

	/**
	 * @author
	 * @param Mensaje
	 *            Se envia un mensaje de confirmación a otro destino
	 */
	@Override
	public void enviarConfirmacion(Mensaje msg) throws IllegalArgumentException{
		if(msg.getOrigen().equals(msg.getDestino())){
			throw new IllegalArgumentException("El origen y el miembro son iguales");
		}
		msg.setEstado(EstadoUtils.Estado.PENDIENTE);
		msg.setTipoHandshake(TipoMensajeUtils.HANDSHAKE_RESPONSE);
		String topic = msg.getDestino() + "/" + msg.getTipoHandshake();
		String m = json.render(msg);
		guardarMensaje(msg);
		conn.send(topic, m, 2);

	}

	/**
	 * 
	 * Se recibe una solicitud de confimacion desde otro origen
	 * 
	 * @throws Exception
	 * 
	 * @throws JAXBException
	 */
	@Override
	public void recibirSolicitud(Mensaje msg) throws Exception {
		procesarMensaje(msg);
		Mensaje nuevo = (Mensaje) msg.clone();
		nuevo.setTipoHandshake(TipoMensajeUtils.HANDSHAKE_RESPONSE);
		nuevo.setEstado(EstadoUtils.Estado.CONFIRMADO);
		nuevo.setOrigen(msg.getDestino());
		nuevo.setDestino(msg.getOrigen());
		enviarConfirmacion(nuevo);
		
	}

	/**
	 * Se recibe una confirmación desde otro origen
	 * 
	 * @throws JAXBException,Exception
	 */
	@Override
	public void recibirConfirmación(Mensaje msg) throws JAXBException, Exception {
		procesarMensaje(msg);
		msg.setEstado(EstadoUtils.Estado.CONFIRMADO);
		msg.setTipoHandshake(TipoMensajeUtils.HANDSHAKE_RESPONSE);
		String origen = msg.getOrigen();
		String destino = msg.getDestino();
		msg.setOrigen(origen);
		msg.setDestino(destino);
		actualizarMensaje(msg);

	}

	/**
	 * Se elimina un mensaje
	 */
	@Override
	public synchronized boolean eliminarMensaje(Mensaje msg) {
		try {
			return mensajesDao.eliminarMensaje(msg);
		} catch (JAXBException e) {
			e.printStackTrace();
		}
		return false;
	}

	public MensajesPendientes getMensajes() {
		MensajesPendientes mensajes = mensajesDao.cargar();
		return mensajes;
	}

	public void setMensajes(MensajesPendientes mensajes) {
		this.mensajes = mensajes;
	}

	@Override
	public String toString() {
		return "MensajeriaImpl [mensajes=" + mensajes + "]";
	}

	public TiposMensajes getTipos() {
		return tipos;
	}

	public void setTipos(TiposMensajes tipos) {
		this.tipos = tipos;
	}

	public boolean exist(DatoGrupo datos, TipoMensaje tipoMensaje, String tipoHandshake, String estado) {

		if (mensajesDao.exist(datos, tipoMensaje, tipoHandshake, estado)) {
			return true;
		}

		return false;
	}

	public void eliminarMensajes(List<Mensaje> borrados) {
		mensajes.getMensaje().removeAll(borrados);
		mensajesDao.guardar(mensajes, mensajesFile);
	}

}