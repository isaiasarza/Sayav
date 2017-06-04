package Datos;

import SAYAV2.SAYAV2.model.Grupo;
import SAYAV2.SAYAV2.model.Peer;

public class DatoVoto {
	
	private Peer miembro;
	private Grupo grupo;
	private boolean voto;
	
	
	public DatoVoto() {
		super();
	}
	
	public DatoVoto(Peer miembro, Grupo grupo, boolean voto) {
		super();
		this.miembro = miembro;
		this.grupo = grupo;
		this.voto = voto;
	}



	public Peer getMiembro() {
		return miembro;
	}



	public void setMiembro(Peer miembro) {
		this.miembro = miembro;
	}



	public Grupo getGrupo() {
		return grupo;
	}



	public void setGrupo(Grupo grupo) {
		this.grupo = grupo;
	}



	public boolean isVoto() {
		return voto;
	}



	public void setVoto(boolean voto) {
		this.voto = voto;
	}
	
	
	
	
	

}
