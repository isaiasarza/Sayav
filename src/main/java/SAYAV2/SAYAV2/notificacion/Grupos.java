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
	 */
	void añadirMiembro(Grupo grupo, Peer miembro);
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
	 */
	void solicitarBajaMiembro(Grupo grupo, Peer miembro);
	/**
	 * 
	 * @param grupo
	 * @throws JAXBException 
	 */
	void abandonarGrupo(Grupo grupo) throws JAXBException;
	/**
	 * 
	 * @param votacion
	 * @throws JAXBException 
	 */
	void aceptarBajaMiembro(Votacion votacion) throws JAXBException;
	/**
	 * 
	 * @param votacion
	 * @throws JAXBException 
	 */
	void rechazarBajaMiembro(Votacion votacion) throws JAXBException;
	/**
	 * 
	 * @param votacion
	 */
	void procesarBajaMiembro(Votacion votacion);
}
