package SAYAV2.SAYAV2.service;

import SAYAV2.SAYAV2.model.Mensaje;
import SAYAV2.SAYAV2.model.Usuario;

public class MensajeService {
	
	
	public Mensaje crearMensajeAlerta(Usuario usuario){
		
		Mensaje mensaje = new Mensaje();
		
		mensaje.setId(1);
        mensaje.setTipo("Alerta");		
		mensaje.setOrigen(usuario.getSubdominio());
        mensaje.setDescripcion("Se disparo una alarma");
		
		return mensaje;
		
	}
	
	
	public Mensaje crearMensajeActualizacionLista(Usuario usuario){
		
		Mensaje mensaje = new Mensaje();
		
		mensaje.setId(2);
        mensaje.setTipo("Actualizacion");		
		mensaje.setOrigen(usuario.getSubdominio());
        mensaje.setDescripcion("Se actualizo lista Peer's");
		
		return mensaje;
		
	}
	
	
	

}
