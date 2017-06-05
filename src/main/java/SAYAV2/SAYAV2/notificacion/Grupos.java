package SAYAV2.SAYAV2.notificacion;

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
	 * @param miembro
	 */
	void abandonarGrupo(Grupo grupo, Peer miembro);
	/**
	 * 
	 * @param grupo
	 * @param miembro
	 */
	void aceptarBajaMiembro(Grupo grupo, Peer miembro);
	/**
	 * 
	 * @param grupo
	 * @param miembro
	 */
	void rechazarBajaMiembro(Grupo grupo, Peer miembro);
	/**
	 * 
	 * @param grupo
	 * @param miembro
	 */
	void procesarBajaMiembro(Votacion votacion);
}
