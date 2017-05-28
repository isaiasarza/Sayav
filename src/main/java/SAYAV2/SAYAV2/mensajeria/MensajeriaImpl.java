package SAYAV2.SAYAV2.mensajeria;

import java.io.File;
import java.util.Date;

import javax.xml.bind.JAXBException;

import SAYAV2.SAYAV2.Utils.EstadoUtils;
import SAYAV2.SAYAV2.Utils.TipoMensajeUtils;
import SAYAV2.SAYAV2.bussines.ControllerMQTT;
import SAYAV2.SAYAV2.dao.MensajePendienteDao;
import SAYAV2.SAYAV2.dao.TipoMensajeDao;
import SAYAV2.SAYAV2.model.Grupo;
import SAYAV2.SAYAV2.model.MensajesPendientes;
import SAYAV2.SAYAV2.model.TiposMensajes;
import SAYAV2.SAYAV2.service.JsonTransformer;

public class MensajeriaImpl implements Mensajeria{
	
	private static MensajeriaImpl mensImpl;
	
	private MensajesPendientes mensajes;
	private TipoMensajeDao tipoMensajeDao;
	private MensajePendienteDao mensajesDao;
	private static File tiposFile;
	private static File mensajesFile;
	private TiposMensajes tipos;
	private static ControllerMQTT conn;
	private JsonTransformer json;
	
	
	private MensajeriaImpl() {
		super();
		tiposFile = new File("tipos_mensajes");
		mensajesFile = new File("mensajes");
		this.tipoMensajeDao = TipoMensajeDao.getInstance();
		this.mensajesDao = MensajePendienteDao.getInstance();
		this.json = new JsonTransformer();
	
				
		try {
			if(mensajesFile.exists()){
				setMensajes(this.mensajesDao.cargar(mensajesFile));
			}else{
				this.mensajes = new MensajesPendientes();
			}
			setTipos(this.tipoMensajeDao.cargar(tiposFile));
		} catch (JAXBException e) {
			e.printStackTrace();
		}
		
	}
	
	public static MensajeriaImpl getInstance(){
		if(mensImpl == null){
			mensImpl = new MensajeriaImpl();
			conn = ControllerMQTT.getInstance();
			conn.start();
		}
		
		return mensImpl;
	}
	/**
	 * @author  
	 * @param Mensaje msg
	 * @return Mensaje nuevo mensaje formado en la acción
	 * Este metodo toma un mensaje recibido,
	 * según el tipo de mensaje correspondiente toma la acción debida.
	 */
	@Override
	public Mensaje procesarMensaje(Mensaje msg) {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * @author 
	 * @param Mensaje msg
	 * @param Grupo g
	 * Este metodo propaga un mensaje al grupo recibido por parametro.
	 */
	@Override
	public void propagarMensaje(Mensaje msg, Grupo g) {
		// TODO Auto-generated method stub
		
	}

	/**
	 * @author 
	 * @param Mensaje mensaje a guardar
	 * Guarda el mensaje.
	 */
	@Override
	public void guardarMensaje(Mensaje msg) {
		if(this.mensajes.addMensaje(msg)){
			this.mensajesDao.guardar(this.mensajes, mensajesFile);
		}
	}
	
	/**
	 * 
	 */
	@Override
	public void actualizarMensaje(Mensaje msg){
		Mensaje viejo = this.mensajes.getMensaje(msg.getId());
		viejo.setFecha(msg.getFecha());
		mensajesDao.guardar(this.mensajes, mensajesFile);
	}

	/**
	 * @author 
	 * @param Mensaje
	 * Reenvia el mensaje indicado.
	 */
	@Override
	public void reenviarMensaje(Mensaje msg) {
		Date fechaActual = new Date();
		msg.getFecha().setTime(fechaActual.getTime());
		actualizarMensaje(msg);
		if(msg.getTipoHandshake().equals(TipoMensajeUtils.HANDSHAKE_REQUEST)){
			enviarSolicitud(msg);
			return;
		}
		enviarConfirmacion(msg);		
	}
	/**
	 * @author 
	 * @param Mensaje mensaje a enviar
	 * Envia una solicitud hacia otro destino junto con un mensaje.
	 */
	@Override
	public String enviarSolicitud(Mensaje msg) {
		String topic = msg.getDestino() + "/" + msg.getTipoMensaje().getTipo();
		String m = json.render(msg);
		msg.setEstado(EstadoUtils.PENDIENTE);
		msg.setTipoHandshake(TipoMensajeUtils.HANDSHAKE_REQUEST);
		guardarMensaje(msg);
		conn.send(topic, m, 2);
		return m;
	}

	/**
	 * @author 
	 * @param Mensaje 
	 * Se envia un mensaje de confirmación a otro destino
	 */
	@Override
	public void enviarConfirmacion(Mensaje msg) {
		// TODO Auto-generated method stub
		
	}

	/**
	 * 
	 * Se recibe una solicitud de confimacion desde otro origen
	 */
	@Override
	public void recibirSolicitud(Mensaje msg) {
		Mensaje mensaje = procesarMensaje(msg);	
		mensaje.setTipoHandshake(TipoMensajeUtils.HANDSHAKE_RESPONSE);
		mensaje.setOrigen(msg.getDestino());
		mensaje.setDestino(msg.getOrigen());
		enviarConfirmacion(mensaje);		
	}

	/**
	 * Se recibe una confirmación desde otro origen
	 */
	@Override
	public  void recibirConfirmación(Mensaje msg) {
		// TODO Auto-generated method stub
		
	}
	/**
	 * Se elimina un mensaje
	 */
	@Override
	public void eliminarMensaje(Mensaje msg) {
		// TODO Auto-generated method stub
		
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
	
	

}
