package SAYAV2.SAYAV2.mensajeria;

import java.util.Date;
import java.util.List;

import javax.xml.bind.JAXBException;

import SAYAV2.SAYAV2.model.Grupo;




public interface Mensajeria {
	void init();
	void procesarMensaje(Mensaje msg) throws JAXBException, Exception;
	void propagarMensaje(Mensaje msg, Grupo g) throws Exception;
	void propagarMensaje(List<Mensaje> msgs);
	void guardarMensaje(Mensaje msg);
	void guardarMensajes(List<Mensaje> msgs);
	boolean reenviarMensaje(Mensaje msg, Date fechaActual);
	Mensaje enviarSolicitud(Mensaje msg) throws IllegalArgumentException, MensajeNoEnviadoException;
	void enviarConfirmacion(Mensaje msg);
	void recibirSolicitud(Mensaje msg) throws JAXBException, Exception;
	void recibirConfirmación(Mensaje msg) throws JAXBException, Exception;
	boolean eliminarMensaje(Mensaje msg);
	void actualizarMensaje(Mensaje msg);
	void guardarMensaje(Mensaje msg, String ruta);
	void recibirMensaje(Mensaje mensaje);
	/**
	 * 
	 * @param mensaje
	 * @return true si el mensaje no necesita ser confirmado
	 */
	boolean isConfirmable(Mensaje mensaje);
}
