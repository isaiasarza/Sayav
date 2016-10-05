package SAYAV2.SAYAV2.model;

public class Prueba {
	
	private String titulo;
	private String subtitulo;
	
	public Prueba(String titulo, String subtitulo) {
		super();
		this.titulo = titulo;
		this.subtitulo = subtitulo;
	}
	public Prueba() {
		super();
	}
	public String getTitulo() {
		return titulo;
	}
	public void setTitulo(String titulo) {
		this.titulo = titulo;
	}
	public String getSubtitulo() {
		return subtitulo;
	}
	public void setSubtitulo(String subtitulo) {
		this.subtitulo = subtitulo;
	}
	@Override
	public String toString() {
		return "Prueba [titulo=" + titulo + ", subtitulo=" + subtitulo + "]";
	}
	
	

}
