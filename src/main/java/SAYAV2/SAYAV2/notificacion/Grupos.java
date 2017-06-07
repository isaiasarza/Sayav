package SAYAV2.SAYAV2.notificacion;

import javax.xml.bind.JAXBException;

import SAYAV2.SAYAV2.model.Grupo;
import SAYAV2.SAYAV2.model.Peer;

public interface Grupos {
	/**
	 * 
	 * @param grupo
	 * @param miembro
	 * Agrega el miembro al grupo.
	 * @return 
	 * @throws Exception 
	 */
	boolean añadirMiembro(Grupo grupo, Peer miembro) throws Exception;
	/**
	 * 
	 * @param grupo
	 * Ver los miembros del grupo
	 */
	void verMiembros(Grupo grupo);
	/**
	 * 
	 * @param grupo
	 * @param miembro
	 * Solicita la baja del miembro enviado
	 * @throws Exception 
	 */
	void solicitarBajaMiembro(Grupo grupo, Peer miembro) throws Exception;
	/**
	 * 
	 * @param grupo
	 * @throws JAXBException 
	 * @throws Exception 
	 */
	void abandonarGrupo(Grupo grupo) throws JAXBException, Exception;
	/**
	 * 
	 * @param votacion
	 * @throws JAXBException 
	 * @throws
	 *  Exception 
	 */
	void aceptarBajaMiembro(Votacion votacion) throws JAXBException, Exception;
	/**
	 * 
	 * @param votacion
	 * @throws JAXBException 
	 * @throws Exception 
	 */
	void rechazarBajaMiembro(Votacion votacion) throws JAXBException, Exception;
	/**
	 * 
	 * @param votacion
	 * @throws Exception 
	 */
	void procesarBajaMiembro(Votacion votacion) throws Exception;
}
