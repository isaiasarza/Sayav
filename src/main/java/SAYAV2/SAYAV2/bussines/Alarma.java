package SAYAV2.SAYAV2.bussines;

import java.util.ArrayList;
import java.util.List;

import SAYAV2.SAYAV2.model.DispositivosType;
import SAYAV2.SAYAV2.model.SensoresType;
import SAYAV2.SAYAV2.model.Usuario;

public class Alarma {

	public static void notificar(Usuario usuario){
		String mensaje = usuario.toString();
		List<SensoresType> sensoresActivos = getSectoresActivos(usuario.getSensores());
		mensaje += "Sensores activos: " + sensoresActivos.toString();
		System.out.println(mensaje);
		notificarMoviles(usuario.getDispositivosMoviles(),mensaje);
	}
	
	

	public static void notificarMoviles(List<DispositivosType> dispositivosMoviles, String mensaje){
		for(DispositivosType d: dispositivosMoviles){
			notificarMovil(d,mensaje);
		}
	}
	
	private static void notificarMovil(DispositivosType d, String mensaje) {
		// TODO Auto-generated method stub
		System.out.println(mensaje);
	}



	private static List<SensoresType> getSectoresActivos(List<SensoresType> sensores) {
		List<SensoresType> sensoresActivos= new ArrayList<SensoresType>();
		for(SensoresType s: sensores){
			if(s.isActivado()){
				sensoresActivos.add(s);
			}
		}
		return sensoresActivos;
	}
}
