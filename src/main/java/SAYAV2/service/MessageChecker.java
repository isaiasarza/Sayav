package SAYAV2.service;

import java.util.Date;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import SAYAV2.mensajeria.Mensaje;
import SAYAV2.mensajeria.MensajeriaImpl;
import SAYAV2.model.MensajesPendientes;

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
		MensajesPendientes m = mensajeria.getMensajes();
		Iterator<Mensaje> iterator = m.getMensaje().iterator();
		List<Mensaje> borrados = new LinkedList<Mensaje>();
		Date fechaActual = new Date();
		while (iterator.hasNext()) {
			Mensaje aux = iterator.next();
			if (mensajeria.reenviarMensaje(aux, fechaActual)) {
				borrados.add(aux.clone());
				iterator.remove();
			}
		}
		if (!borrados.isEmpty()) {
			System.out.println("Borrando mensajes");
			mensajeria.eliminarMensajes(borrados);
		}
		return check;
	}
}
