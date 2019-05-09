package SAYAV2.SAYAV2.mensajeria;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.mashape.unirest.http.ObjectMapper;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;

public class SenderRest implements Sender, Runnable {

	private static Sender sender;
	private List<Mensaje> enviados;

	private SenderRest() {
		enviados = new ArrayList<Mensaje>();
		Unirest.setTimeouts(10000, 60000);
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
	public String send(Mensaje mensaje) throws MensajeNoEnviadoException{

		String destino = "http://" + mensaje.getDestino().getDireccion() + ":" + mensaje.getDestino().getPuerto();
		try {
			Unirest.post(destino).header("accept", "application/json").body(mensaje).asString();
		} catch (UnirestException e) {
			e.printStackTrace();
			throw new MensajeNoEnviadoException(mensaje,"");
		}
//		Future<HttpResponse<String>> future = Unirest.post(destino).header("accept", "application/json").body(mensaje)
//				.asStringAsync(new Callback<String>() {
//
//					@Override
//					public void failed(UnirestException e){
//					//	throw new MensajeNoEnviadoException(mensaje,"asdf");
//					}
//
//					@Override
//					public void completed(HttpResponse<String> response) {
//						response.getBody().toString();
//					}
//
//					@Override
//					public void cancelled() {
//					}
//				});
		
		return "Mensaje Enviado";
	}

	@Override
	public List<Mensaje> send(List<Mensaje> mensajes) {
		List<Mensaje> enviados = new ArrayList<Mensaje>();
		Thread thread;
		if(mensajes.size() == 0)
			return enviados;
        ExecutorService exec = Executors.newFixedThreadPool(mensajes.size());
		List<Callable<Object>> calls = new ArrayList<Callable<Object>>();
		for (Mensaje mensaje : mensajes) {
			thread = new Thread(new Runnable() {
				@Override
				public void run() {
					try {
						sender.send(mensaje);
						agregarMensajeEnviado(mensaje);
					} catch (MensajeNoEnviadoException e) {
						e.printStackTrace();
					}
				}
			}, "Enviando el mensaje: " + mensaje.toString());
			calls.add(Executors.callable(thread));
		}
		try {
			exec.invokeAll(calls);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		enviados.addAll(this.enviados);
		this.enviados.clear();
		return enviados;

	}
	
	public synchronized void agregarMensajeEnviado(Mensaje mensaje) {
		enviados.add(mensaje);
		System.out.println("Mensajes Enviados:" + this.enviados.size());

	}

	@Override
	public void run() {
		// TODO Auto-generated method stub

	}

}
