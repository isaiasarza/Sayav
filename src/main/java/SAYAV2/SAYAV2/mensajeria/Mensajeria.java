package SAYAV2.SAYAV2.mensajeria;

import java.util.Date;

import javax.xml.bind.JAXBException;

import SAYAV2.SAYAV2.model.Grupo;

public interface Mensajeria {
	void procesarMensaje(Mensaje msg) throws JAXBException;
	void propagarMensaje(Mensaje msg, Grupo g);
	void guardarMensaje(Mensaje msg);
	boolean reenviarMensaje(Mensaje msg, Date fechaActual);
	String enviarSolicitud(Mensaje msg);
	void enviarConfirmacion(Mensaje msg);
	void recibirSolicitud(Mensaje msg) throws JAXBException;
	void recibirConfirmación(Mensaje msg) throws JAXBException;
	void eliminarMensaje(Mensaje msg);
	void actualizarMensaje(Mensaje msg);
}
