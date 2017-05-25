package SAYAV2.SAYAV2.bussines;

import java.util.List;

import SAYAV2.SAYAV2.model.DispositivoM;
import SAYAV2.SAYAV2.model.Grupo;

public interface Notificaciones {
	void notificarGrupo(Grupo grupo, String msg);
	void notificarMovil(DispositivoM movil, String msg);
	void notificarGrupos(List<Grupo> grupos, String msg);
	void notificarMoviles(List<DispositivoM> moviles, String msg);
}
