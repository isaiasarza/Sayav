package SAYAV2.SAYAV2.service;

import java.util.Date;
import java.util.Iterator;
import java.util.Random;

import SAYAV2.SAYAV2.mensajeria.Mensaje;
import SAYAV2.SAYAV2.mensajeria.MensajeriaImpl;

public class MessageChecker implements Runnable {

	private Thread thread;
	private Random rand = new Random();
	private MensajeriaImpl mensajeria = MensajeriaImpl.getInstance();
	private boolean check;

	public void start() {
		if (thread == null) {
			thread = new Thread(this);
			thread.start();
		}
	}

	@Override
	public void run() {
		while (true) {
			waitRandomTime();
			verificarMensajesPendientes();
		}
	}

	/**
	 * Duerme al hilo por un determinado tiempo aleatorio
	 */
	private void waitRandomTime() {
		try {
			Thread.sleep(rand.nextInt(5000));
		} catch (InterruptedException ex) {
			System.err.println("Excepcion " + ex.getMessage());
		}
	}

	/**
	 * Duerme al hilo por un determinado tiempo
	 */
	@SuppressWarnings("unused")
	private void waitQuantum(long quantum) {
		try {
			Thread.sleep(quantum);
		} catch (InterruptedException ex) {
			System.err.println("Excepcion " + ex.getMessage());
		}
	}

	private boolean verificarMensajesPendientes() {

		Iterator<Mensaje> iterator = mensajeria.getMensajes().getMensaje().iterator();
        Date fechaActual = new Date();
		while (iterator.hasNext()) {
			System.out.println("Verificando mensajes pendientes");
			mensajeria.reenviarMensaje(iterator.next(), fechaActual); 
		}
		return check;
	}
}
