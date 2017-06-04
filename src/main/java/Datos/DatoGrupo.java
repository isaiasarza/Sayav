package Datos;

import SAYAV2.SAYAV2.model.Grupo;
import SAYAV2.SAYAV2.model.Peer;

public class DatoGrupo {
	
	private Peer miembro;
	private Grupo grupo;
	

	public DatoGrupo() {
		super();
	}

	
	
	public DatoGrupo(Peer miembro, Grupo grupo) {
		super();
		this.miembro = miembro;
		this.grupo = grupo;
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



	@Override
	public String toString() {
		return "DatoGrupo [miembro=" + miembro + ", grupo=" + grupo + "]";
	}
	
	

}
