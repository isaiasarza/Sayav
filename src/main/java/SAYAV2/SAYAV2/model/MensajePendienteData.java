package SAYAV2.SAYAV2.model;

public class MensajePendienteData {

	private String peer;
	private String fecha;
	public MensajePendienteData() {
		super();
	}
	public String getPeer() {
		return peer;
	}
	public void setPeer(String peer) {
		this.peer = peer;
	}
	public String getFecha() {
		return fecha;
	}
	public void setFecha(String fecha) {
		this.fecha = fecha;
	}
	@Override
	public String toString() {
		return "MensajePendienteData [peer=" + peer + ", fecha=" + fecha + "]";
	}
	
	
}
