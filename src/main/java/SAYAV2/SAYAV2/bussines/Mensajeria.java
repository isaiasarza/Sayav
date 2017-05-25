package SAYAV2.SAYAV2.bussines;

import SAYAV2.SAYAV2.model.Grupo;
import SAYAV2.SAYAV2.model.MensajePendiente;

public interface Mensajeria {
	void procesarMensaje(MensajePendiente msg);
	void propagarMensaje(MensajePendiente msg, Grupo g);
	void guardarMensajePendiente(MensajePendiente msg);
	void reenviarMensajePendiente(MensajePendiente msg);
	void enviarSolicitud(MensajePendiente msg, String destino);
	void enviarConfirmacion(MensajePendiente msg,String destino);
	void recibirSolicitudConfirmación(MensajePendiente msg);
	void recibirConfirmación(MensajePendiente msg);
	void eliminarMensajePendiente(MensajePendiente msg);
}
