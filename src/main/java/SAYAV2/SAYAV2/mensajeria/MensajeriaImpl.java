package SAYAV2.SAYAV2.mensajeria;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.JAXBException;

import SAYAV2.SAYAV2.dao.TipoMensajeDao;
import SAYAV2.SAYAV2.model.Grupo;
import SAYAV2.SAYAV2.model.TiposMensajes;

public class MensajeriaImpl implements Mensajeria{
	
	private List<Mensaje> mensajes;
	private TipoMensajeDao tipoMensajeDao;
	private File file = new File("tipos_mensajes");
	private TiposMensajes tipos;

	public MensajeriaImpl() {
		super();
		this.tipoMensajeDao = TipoMensajeDao.getInstance();
		try {
			tipos = this.tipoMensajeDao.cargar(file);
		} catch (JAXBException e) {
			e.printStackTrace();
		}
	}

	/**
	 * @author  
	 * @param Mensaje msg
	 * Este metodo toma un mensaje recibido,
	 * y lo procesa para tomar la acción correspondiente
	 */
	@Override
	public void procesarMensaje(Mensaje msg) {
		// TODO Auto-generated method stub
		
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
		// TODO Auto-generated method stub
		
	}

	/**
	 * @author 
	 * @param Mensaje
	 * Reenvia el mensaje indicado.
	 */
	@Override
	public void reenviarMensaje(Mensaje msg) {
		// TODO Auto-generated method stub
		
	}
	/**
	 * @author 
	 * @param Mensaje mensaje a enviar
	 * Envia una solicitud hacia otro destino junto con un mensaje.
	 */
	@Override
	public void enviarSolicitud(Mensaje msg) {
		// TODO Auto-generated method stub
		
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
	public void recibirSolicitudConfirmación(Mensaje msg) {
		// TODO Auto-generated method stub
		
	}

	/**
	 * Se recibe una confirmación desde otro origen
	 */
	@Override
	public void recibirConfirmación(Mensaje msg) {
		// TODO Auto-generated method stub
		
	}
	/**
	 * Se elimina un mensaje
	 */
	@Override
	public void eliminarMensaje(Mensaje msg) {
		// TODO Auto-generated method stub
		
	}

	public List<Mensaje> getMensajes() {
		return mensajes;
	}

	public void setMensajes(List<Mensaje> mensajes) {
		this.mensajes = mensajes;
	}

	@Override
	public String toString() {
		return "MensajeriaImpl [mensajes=" + mensajes + "]";
	}
	
	

}
