package SAYAV2.SAYAV2.mensajeria;

import java.util.List;

public interface Sender {
	
	public String send(Mensaje mensaje) throws MensajeNoEnviadoException;
	public List<Mensaje> send(List<Mensaje> mensajes);

}
