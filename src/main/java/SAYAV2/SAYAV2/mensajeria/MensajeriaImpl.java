package SAYAV2.SAYAV2.mensajeria;

import java.io.File;
import java.util.Date;

import javax.xml.bind.JAXBException;

import Datos.DatoGrupo;
import SAYAV2.SAYAV2.Utils.EstadoUtils;
import SAYAV2.SAYAV2.Utils.FechaUtils;
import SAYAV2.SAYAV2.Utils.TipoMensajeUtils;
import SAYAV2.SAYAV2.bussines.ControllerMQTT;
import SAYAV2.SAYAV2.dao.MensajePendienteDao;
import SAYAV2.SAYAV2.dao.NotificacionesDao;
import SAYAV2.SAYAV2.dao.TipoMensajeDao;
import SAYAV2.SAYAV2.dao.UsuarioDao;
import SAYAV2.SAYAV2.model.Grupo;
import SAYAV2.SAYAV2.model.MensajesPendientes;
import SAYAV2.SAYAV2.model.Notificacion;
import SAYAV2.SAYAV2.model.Peer;
import SAYAV2.SAYAV2.model.TiposMensajes;
import SAYAV2.SAYAV2.notificacion.GruposImpl;
import SAYAV2.SAYAV2.notificacion.Votacion;
import SAYAV2.SAYAV2.service.JsonTransformer;

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
		tiposFile = new File("tipos_mensajes");
		mensajesFile = new File("Mensajes");
		this.tipoMensajeDao = TipoMensajeDao.getInstance();
		this.mensajesDao = MensajePendienteDao.getInstance();
		this.mensajesDao.setFile(mensajesFile);
		this.file = new File("SAYAV");
		this.usuarioDao = UsuarioDao.getInstance();
		this.usuarioDao.setFile(file);
		this.json = new JsonTransformer();
		this.notificacionesFile = new File("notificaciones");
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
		Notificacion notificacion = new Notificacion();
		
		try {
			if (msg.getTipoHandshake().equals(TipoMensajeUtils.HANDSHAKE_REQUEST)) {
				
				if (msg.getTipoMensaje().getTipo().equals(TipoMensajeUtils.ALERTA)) {
					gruposImpl.recibirAlerta(msg);
					notificacion.setDescripcion(msg.getDescripcion());
					notificacionesDao.agregarNotificacion(notificacion);	
					return;
				}
				if (msg.getTipoMensaje().getTipo().equals(TipoMensajeUtils.NUEVO_MIEMBRO)) {
					gruposImpl.recibirNuevoMiembro(msg);
					DatoGrupo datos = this.json.getGson().fromJson(msg.getDatos(), DatoGrupo.class);
					notificacion.setDescripcion("El miembro " +  datos.getMiembro().getDireccion() 
						+ " es parte del grupo " + datos.getGrupo().getNombre());
					notificacionesDao.agregarNotificacion(notificacion);
					return;
				}
				if (msg.getTipoMensaje().getTipo().equals(TipoMensajeUtils.NUEVO_GRUPO)) {
					gruposImpl.recibirNuevoGrupo(msg);
					DatoGrupo datos = this.json.getGson().fromJson(msg.getDatos(), DatoGrupo.class);
					notificacion.setDescripcion("Fue agregado al grupo " + datos.getGrupo().getNombre());
					notificacionesDao.agregarNotificacion(notificacion);
					
					return;
				}
				if (msg.getTipoMensaje().getTipo().equals(TipoMensajeUtils.BAJA_MIEMBRO)) {
					gruposImpl.recibirBajaMiembro(msg);
					DatoGrupo datos = this.json.getGson().fromJson(msg.getDatos(), DatoGrupo.class);
					notificacion.setDescripcion("El miembro " +  datos.getMiembro().getDireccion() 
						+ " dejo de ser parte del grupo " + datos.getGrupo().getNombre() );
					notificacionesDao.agregarNotificacion(notificacion);
					return;
				}
				if (msg.getTipoMensaje().getTipo().equals(TipoMensajeUtils.SOLICITUD_BAJA_MIEMBRO)) {
					gruposImpl.recibirSolicitudBaja(msg);
					Votacion votacion = this.json.getGson().fromJson(msg.getDatos(), Votacion.class);
					notificacion.setDescripcion("Se ha solicitado la baja del miembro " + votacion.getMiembro() + 
							"/nPor favor vaya al menu de votacion");
					notificacionesDao.agregarNotificacion(notificacion);
					return;
				}
			}
			if (msg.getTipoMensaje().getTipo().equals(TipoMensajeUtils.VOTO)) {
				gruposImpl.recibirVoto(msg);
				return;
			}	
			if(msg.getTipoMensaje().getTipo().equals(TipoMensajeUtils.NUEVO_GRUPO)){
				gruposImpl.confirmarAñadirMiembro(msg);
				DatoGrupo datos = this.json.getGson().fromJson(msg.getDatos(), DatoGrupo.class);
				notificacion.setDescripcion("El miembro " +  datos.getMiembro().getDireccion() 
						+ " es parte del grupo " + datos.getGrupo().getNombre());
				notificacionesDao.agregarNotificacion(notificacion);
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
	public void propagarMensaje(Mensaje msg, Grupo g) throws Exception {

		for (Peer miembro : g.getPeers()) {
			Mensaje mensaje = new Mensaje();
			mensaje.setDescripcion(msg.getDescripcion());
			mensaje.setDatos(msg.getDatos());
			mensaje.setOrigen(msg.getOrigen());
			mensaje.setDestino(miembro.getDireccion());
			mensaje.setTipoMensaje(msg.getTipoMensaje());
			mensaje.setTipoHandshake(msg.getTipoHandshake());
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
		viejo.setFechaReenvio(msg.getFechaReenvio());
		viejo.setEstado(msg.getEstado());
		mensajesDao.guardar(this.mensajes, mensajesFile);
	}

	/**
	 * @author
	 * @param Mensaje
	 *            Reenvia el mensaje indicado.
	 */
	@Override
	public boolean reenviarMensaje(Mensaje msg, Date fechaActual) {

		if (FechaUtils.diffDays(msg.getFechaCreacion(), fechaActual) == msg.getTipoMensaje().getTimetolive()) {
			eliminarMensaje(msg);
			return false;
		}
		if (msg.getEstado().equals(EstadoUtils.CONFIRMADO)) {
			//eliminarMensaje(msg);
			return false;
		}
		if (FechaUtils.diffDays(msg.getFechaReenvio(), fechaActual) < msg.getTipoMensaje().getQuantum()) {
			return false;
		}
		msg.getFechaReenvio().setTime(fechaActual.getTime());
		actualizarMensaje(msg);

		if (msg.getTipoHandshake().equals(TipoMensajeUtils.HANDSHAKE_REQUEST)) {
			enviarSolicitud(msg);
			return true;
		}
		enviarConfirmacion(msg);
		return true;
	}

	/**
	 * @author
	 * @param Mensaje
	 *            mensaje a enviar Envia una solicitud hacia otro destino junto
	 *            con un mensaje.
	 */
	@Override
	public String enviarSolicitud(Mensaje msg) {
		msg.setEstado(EstadoUtils.PENDIENTE);
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
	public void enviarConfirmacion(Mensaje msg) {
		msg.setEstado(EstadoUtils.PENDIENTE);
		msg.setTipoHandshake(TipoMensajeUtils.HANDSHAKE_RESPONSE);
		String topic = msg.getDestino() + "/" + msg.getTipoHandshake();
		String m = json.render(msg);
		guardarMensaje(msg);
		conn.send(topic, m, 2);

	}

	/**
	 * 
	 * Se recibe una solicitud de confimacion desde otro origen
	 * @throws Exception 
	 * 
	 * @throws JAXBException
	 */
	@Override
	public void recibirSolicitud(Mensaje msg) throws Exception {
		procesarMensaje(msg);
		Mensaje nuevo =  (Mensaje) msg.clone();
		nuevo.setTipoHandshake(TipoMensajeUtils.HANDSHAKE_RESPONSE);
		nuevo.setOrigen("");
	    nuevo.setDestino("a"); 
		nuevo.setOrigen(msg.getDestino());
		nuevo.setDestino(msg.getOrigen());
		enviarConfirmacion(nuevo);
	}

	/**
	 * Se recibe una confirmación desde otro origen
	 * @throws JAXBException,Exception 
	 */
	@Override
	public void recibirConfirmación(Mensaje msg) throws JAXBException,Exception {
		procesarMensaje(msg);
		msg.setEstado(EstadoUtils.CONFIRMADO);
		msg.setTipoHandshake(TipoMensajeUtils.HANDSHAKE_RESPONSE);
		String origen = msg.getOrigen();
		String destino = msg.getDestino();
		msg.setOrigen("a");
		msg.setDestino("b");
		msg.setOrigen(origen);
		msg.setDestino(destino);
		actualizarMensaje(msg);

	}

	/**
	 * Se elimina un mensaje
	 */
	@Override
	public synchronized void eliminarMensaje(Mensaje msg) {
		try {
			mensajesDao.eliminarMensaje(msg);
		} catch (JAXBException e) {
			e.printStackTrace();
		}

	}

	public MensajesPendientes getMensajes() {
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

	public boolean exist(DatoGrupo datos, TipoMensaje tipoMensaje, String tipoHandshake) {
		try {
			if(mensajesDao.exist(datos,tipoMensaje,tipoHandshake)){
				return true;
			}
		} catch (JAXBException e) {
		}
		return false;
	}

}
