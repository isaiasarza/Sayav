package SAYAV2.SAYAV2.service;

import java.io.File;
import java.util.Date;
import java.util.Iterator;
import java.util.Random;

import javax.xml.bind.JAXBException;


import SAYAV2.SAYAV2.Utils.EstadoUtils;
import SAYAV2.SAYAV2.Utils.FechaUtils;
import SAYAV2.SAYAV2.Utils.TipoMensajeUtils;
import SAYAV2.SAYAV2.dao.MensajePendienteDao;
import SAYAV2.SAYAV2.dao.TipoMensajeDao;
import SAYAV2.SAYAV2.mensajeria.Mensaje;
import SAYAV2.SAYAV2.mensajeria.Mensajeria;
import SAYAV2.SAYAV2.mensajeria.MensajeriaImpl;
import SAYAV2.SAYAV2.model.MensajeDato;
import SAYAV2.SAYAV2.model.MensajesPendientes;

public class MessageChecker implements Runnable {

	private Thread thread;
	private Random rand = new Random();
	private MensajeriaImpl mensajeria = MensajeriaImpl.getInstance();
	private TipoMensajeDao tipoMensajeDao;
	private static MensajePendienteDao mensajesDao = MensajePendienteDao.getInstance();
	private static File tiposFile;
	private static File mensajesFile = new File("Mensajes");
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
              mensajeria.reenviarMensaje(iterator.next(), fechaActual); 
		}
		return check;
	}
}
