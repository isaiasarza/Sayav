package SAYAV2.SAYAV2.notificacion;

import java.util.List;

import javax.xml.bind.JAXBException;

import SAYAV2.SAYAV2.mensajeria.Mensaje;
import SAYAV2.SAYAV2.model.DispositivoM;

public interface Moviles {
	
	/**
	 * Notifica a un movil
	 * @param movil
	 * @param msg
	 */
	
	void notificarMovil(DispositivoM movil, Mensaje msg);
	/**
	 * Notifica a algunos moviles
	 * @param moviles
	 * @param msg
	 * @throws JAXBException
	 */
	void notificarMoviles(List<DispositivoM> moviles, Mensaje msg) throws JAXBException;
	
	/**
	 * Notifica a todos los moviles del usuario
	 * @param msg
	 * @throws JAXBException
	 */
	void notificarMoviles(Mensaje msg) throws JAXBException;



}
