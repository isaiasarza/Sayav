package Datos;

public class DatoVoto {
	
	private String idVotacion;
	private boolean voto;

	
	public DatoVoto() {
		super();
	}
	
	public DatoVoto(String idVotacion, boolean voto) {
		super();
		this.idVotacion = idVotacion;
		this.voto = voto;
	}


	public String getIdVotacion() {
		return idVotacion;
	}

	public void setIdVotacion(String idVotacion) {
		this.idVotacion = idVotacion;
	}

	public boolean isVoto() {
		return voto;
	}



	public void setVoto(boolean voto) {
		this.voto = voto;
	}

	@Override
	public String toString() {
		return "DatoVoto [idVotacion=" + idVotacion + ", voto=" + voto + "]";
	}


	
	
	

}
