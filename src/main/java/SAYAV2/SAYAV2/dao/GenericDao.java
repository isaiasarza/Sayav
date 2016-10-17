package SAYAV2.SAYAV2.dao;

import java.io.File;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

public class GenericDao<E> {
	
	protected E e;
	

	public void guardar(Object entidad, File file) {
		//StringWriter writer = new StringWriter();
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

	@SuppressWarnings("unchecked")
	public E cargar( File file) throws JAXBException{
			E entidad;
			JAXBContext context = JAXBContext.newInstance(e.getClass());
			Unmarshaller um = context.createUnmarshaller();

			// Reading XML from the file and unmarshalling.
			
			entidad =  (E) um.unmarshal(file);

			// personData.clear();
			// personData.addAll(wrapper.getPersons());

			// Save the file path to the registry.
			// setPersonFilePath(file);

	
		return entidad;
	}
	
	
}
