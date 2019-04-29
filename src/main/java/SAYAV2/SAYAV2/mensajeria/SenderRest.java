package SAYAV2.SAYAV2.mensajeria;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.Future;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.ObjectMapper;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.async.Callback;
import com.mashape.unirest.http.exceptions.UnirestException;

public class SenderRest implements Sender, Runnable {

	private static Sender sender;

	private SenderRest() {
		Unirest.setTimeouts(1000, 5000);
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
		if (sender == null)
			sender = new SenderRest();
		return sender;
	}

	@Override
	public String send(Mensaje mensaje) {

		String destino = "http://" + mensaje.getDestino().getDireccion() + ":" + mensaje.getDestino().getPuerto();
		Future<HttpResponse<String>> future = Unirest.post(destino).header("accept", "application/json").body(mensaje)
				.asStringAsync(new Callback<String>() {

					@Override
					public void failed(UnirestException e) {

					}

					@Override
					public void completed(HttpResponse<String> response) {
						response.getBody().toString();
					}

					@Override
					public void cancelled() {
					}
				});
		return null;
	}

	@Override
	public void send(List<Mensaje> mensajes) {
		// TODO Auto-generated method stub
		Thread thread;

		for (Mensaje mensaje : mensajes) {

			thread = new Thread(new Runnable() {
				@Override
				public void run() {
					System.out.println(mensaje);
					sender.send(mensaje);
				}
			}, "Enviando el mensaje: " + mensaje.toString());
			thread.start();
		}

	}

	@Override
	public void run() {
		// TODO Auto-generated method stub

	}

}
