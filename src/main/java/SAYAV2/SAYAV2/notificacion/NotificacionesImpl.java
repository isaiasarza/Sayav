package SAYAV2.SAYAV2.notificacion;

import java.util.List;

import SAYAV2.SAYAV2.model.DispositivoM;
import SAYAV2.SAYAV2.model.Grupo;
import SAYAV2.SAYAV2.service.Application;
import SAYAV2.SAYAV2.service.LayoutController;

public class NotificacionesImpl implements Notificaciones {

	@Override
	public void notificarGrupo(Grupo grupo, String msg) {
		// TODO Auto-generated method stub

	}

	@Override
	public void notificarMovil(DispositivoM movil, String msg) {
		// TODO Auto-generated method stub

	}

	@Override
	public void notificarGrupos(List<Grupo> grupos, String msg) {
		// TODO Auto-generated method stub

	}

	@Override
	public void notificarMoviles(List<DispositivoM> moviles, String msg) {
		// TODO Auto-generated method stub

	}

	public static void mostrar(String string) {
		Application.mostrar(string);
	}

}
