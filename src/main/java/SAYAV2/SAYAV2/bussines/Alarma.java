package SAYAV2.SAYAV2.bussines;

import java.util.ArrayList;
import java.util.List;

import SAYAV2.SAYAV2.model.DispositivoM;
import SAYAV2.SAYAV2.model.Sector;
import SAYAV2.SAYAV2.model.Usuario;

public class Alarma {

	public static void notificar(Usuario usuario){
		String mensaje = usuario.toString();
		List<Sector> sensoresActivos = getSectoresActivos(usuario.getSectores());
		mensaje += "Sectores activos: " + sensoresActivos.toString();
		System.out.println(mensaje);
		notificarMoviles(usuario.getDispositivosMoviles(),mensaje);
	}
	
	

	public static void notificarMoviles(List<DispositivoM> dispositivosMoviles, String mensaje){
		for(DispositivoM d: dispositivosMoviles){
			notificarMovil(d,mensaje);
		}
	}
	
	private static void notificarMovil(DispositivoM d, String mensaje) {
		// TODO Auto-generated method stub
		System.out.println(mensaje);
	}



	private static List<Sector> getSectoresActivos(List<Sector> sensores) {
		List<Sector> sensoresActivos= new ArrayList<Sector>();
		for(Sector s: sensores){
			if(s.isActivado()){
				sensoresActivos.add(s);
			}
		}
		return sensoresActivos;
	}
}
