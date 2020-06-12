package SAYAV2.SAYAV2.mensajeria;

public class MensajeNoEnviadoException extends Exception {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Mensaje mensaje;
	private String descripcion;

	public MensajeNoEnviadoException() {
	}
	
	

	public MensajeNoEnviadoException(Mensaje mensaje, String descripcion) {
		super();
		this.mensaje = mensaje;
		this.descripcion = descripcion;
	}

	public MensajeNoEnviadoException(String message) {
		super(message);
	}

	public MensajeNoEnviadoException(Throwable cause) {
		super(cause);
	}

	public MensajeNoEnviadoException(String message, Throwable cause) {
		super(message, cause);
	}

	public MensajeNoEnviadoException(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}



	public Mensaje getMensaje() {
		return mensaje;
	}



	public void setMensaje(Mensaje mensaje) {
		this.mensaje = mensaje;
	}



	public String getDescripcion() {
		return descripcion;
	}



	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}



	@Override
	public String toString() {
		return "MensajeNoEnviadoException [mensaje=" + mensaje + ", descripcion=" + descripcion + "]";
	}
	
	

}
