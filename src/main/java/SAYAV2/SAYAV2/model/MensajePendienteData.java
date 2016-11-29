package SAYAV2.SAYAV2.model;

public class MensajePendienteData {

	private String peer;
	private String fecha;
	private String tipo;
	
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
	
	public String getTipo() {
		return tipo;
	}
	public void setTipo(String tipo) {
		this.tipo = tipo;
	}
	
	@Override
	public String toString() {
		return "MensajePendienteData [peer=" + peer + ", fecha=" + fecha + ", tipo=" + tipo + "]";
	}
	
}
