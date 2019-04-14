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
						// TODO Auto-generated method stub

					}

					@Override
					public void completed(HttpResponse<String> response) {
						// TODO Auto-generated method stub

						response.getBody().toString();
					}

					@Override
					public void cancelled() {
						// TODO Auto-generated method stub

					}
				});

		/*
		 * try { HttpResponse<String> jsonResponse =
		 * (Unirest.post(destino).header("accept", "application/json")
		 * .header("Content-Type", "application/json").body(mensaje).asString()); return
		 * jsonResponse.getBody().toString(); } catch (UnirestException e) { if
		 * (mensaje.getDestino().getPuerto() == 0) { System.out.println();
		 * System.out.println("Error:No se pudo enviar mensaje");
		 * System.out.println("destino: " + destino); System.out.println(); }
		 * 
		 * return e.getMessage(); }
		 */

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
