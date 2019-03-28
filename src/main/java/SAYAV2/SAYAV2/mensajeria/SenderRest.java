package SAYAV2.SAYAV2.mensajeria;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.ObjectMapper;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;

public class SenderRest implements Sender {
	
	private static Sender sender;

	private SenderRest() {
		
		Unirest.setObjectMapper(new ObjectMapper() {
			private com.fasterxml.jackson.databind.ObjectMapper jacksonObjectMapper = new com.fasterxml.jackson.databind.ObjectMapper();

			public <T> T readValue(String value, Class<T> valueType) {
				try {
					return jacksonObjectMapper.readValue(value, valueType);
				} catch (IOException e) {
					throw new RuntimeException(e);
				}
			}

			public String writeValue(Object value) {
				try {
					return jacksonObjectMapper.writeValueAsString(value);
				} catch (JsonProcessingException e) {
					throw new RuntimeException(e);
				}
			}
		});
		
	}
	
	public static Sender getInstance() {
		if(sender == null)
			sender = new SenderRest();
		return sender;
	}

	@Override
	public String send(Mensaje mensaje) {
		String destino = "http://" + mensaje.getDestino().getDireccion() + ":" + mensaje.getDestino().getPuerto();
		try {
			HttpResponse<String> jsonResponse = (Unirest.post(destino)
					.header("accept", "application/json")
					.header("Content-Type", "application/json")
					.body(mensaje)
					.asString());
			return jsonResponse.getBody().toString();
		} catch (UnirestException e) {
			if(mensaje.getDestino().getPuerto() == 0) {
				System.out.println();
				System.out.println("Error:No se pudo enviar mensaje");
				System.out.println("destino: " + destino );
				System.out.println();	
			}
			
			return e.getMessage();
		}
	}

}
