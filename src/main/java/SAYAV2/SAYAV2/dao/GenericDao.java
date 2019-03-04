package SAYAV2.SAYAV2.dao;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.transform.Result;

public class GenericDao<E> {

	protected E e;
	protected File file;

	public File getFile() {
		return file;
	}

	public void setFile(File file) {
		this.file = file;
	}

	public synchronized void guardar(Object entidad, File file) {
		// StringWriter writer = new StringWriter();
		JAXBContext context;
		try {

			context = JAXBContext.newInstance(e.getClass());
			Marshaller m;
			m = context.createMarshaller();
			m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
			m.marshal(entidad, file);

		} catch (JAXBException e) {
			e.printStackTrace();
		}

	}

	public synchronized void guardar(Object entidad, String ruta) throws IOException {
		// StringWriter writer = new StringWriter();
		JAXBContext context;
		try {

			context = JAXBContext.newInstance(e.getClass());
			Marshaller m;
			m = context.createMarshaller();
			m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
		//	PipedInputStream pp = (PipedInputStream) this.getClass().getResourceAsStream(ruta);

		//	OutputStream outputStream = new PipedOutputStream(pp);
			System.out.println("ruta " + ruta);
			OutputStream outputStream = new FileOutputStream(ruta);
			m.marshal(entidad, outputStream);
			
			outputStream.flush();
			outputStream.close();

		} catch (JAXBException e) {
			e.printStackTrace();
		}

	}

	@SuppressWarnings("unchecked")
	public E cargar(File file) throws JAXBException {
		E entidad;
		JAXBContext context = JAXBContext.newInstance(e.getClass());
		Unmarshaller um = context.createUnmarshaller();

		// Reading XML from the file and unmarshalling.

		entidad = (E) um.unmarshal(file);

		return entidad;
	}

	@SuppressWarnings("unchecked")
	public E cargar(String ruta) throws JAXBException, IOException {
		E entidad;
		JAXBContext context = JAXBContext.newInstance(e.getClass());
		Unmarshaller um = context.createUnmarshaller();

		InputStream pp = this.getClass().getResourceAsStream(ruta);

		entidad = (E) um.unmarshal(pp);
		
		pp.close();

		return entidad;
	}

	@SuppressWarnings("unused")
	private E actualizar(Object entidad, File file) {

		// TODO

		return null;
	}

}
