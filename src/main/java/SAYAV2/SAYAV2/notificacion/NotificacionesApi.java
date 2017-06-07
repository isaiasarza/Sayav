package SAYAV2.SAYAV2.notificacion;

import java.util.List;

import SAYAV2.SAYAV2.mensajeria.Mensaje;
import SAYAV2.SAYAV2.model.DispositivoM;
import SAYAV2.SAYAV2.model.Grupo;

public interface NotificacionesApi {
	void notificarGrupo(Grupo grupo, Mensaje msg) throws Exception;
	void notificarMovil(DispositivoM movil, Mensaje msg);
	void notificarGrupos(List<Grupo> grupos, Mensaje msg) throws Exception;
	void notificarMoviles(List<DispositivoM> moviles, Mensaje msg);
}
