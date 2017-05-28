package SAYAV2.SAYAV2.mensajeria;

import SAYAV2.SAYAV2.model.Grupo;

public interface Mensajeria {
	Mensaje procesarMensaje(Mensaje msg);
	void propagarMensaje(Mensaje msg, Grupo g);
	void guardarMensaje(Mensaje msg);
	void reenviarMensaje(Mensaje msg);
	String enviarSolicitud(Mensaje msg);
	void enviarConfirmacion(Mensaje msg);
	void recibirSolicitud(Mensaje msg);
	void recibirConfirmación(Mensaje msg);
	void eliminarMensaje(Mensaje msg);
	void actualizarMensaje(Mensaje msg);
}
