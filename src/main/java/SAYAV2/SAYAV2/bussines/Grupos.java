package SAYAV2.bussines;

import SAYAV2.model.Grupo;
import SAYAV2.model.Peer;

public interface Grupos {
	void añadirMiembro(Grupo grupo, Peer miembro);
	void verMiembros(Grupo grupo);
	void solicitarBajaMiembro(Grupo grupo, Peer miembro);
	void abandonarGrupo(Grupo grupo, Peer miembro);
	void aceptarBajaMiembro(Grupo grupo, Peer miembro);
	void rechazarBajaMiembro(Grupo grupo, Peer miembro);
	void procesarBajaMiembro(Grupo grupo, Peer miembro);
}
