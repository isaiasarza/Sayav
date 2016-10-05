package SAYAV2.SAYAV2.dao;

import java.io.File;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

public class GenericDao {
	
	

	public void guardar(Object entidad, File file) {
		//StringWriter writer = new StringWriter();
		JAXBContext context;
		try {

			
			context = JAXBContext.newInstance(entidad.getClass());
			Marshaller m;
			m = context.createMarshaller();
			m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
			m.marshal(entidad, file);

		} catch (JAXBException e) {
			e.printStackTrace();
		}

	}

	public Object cargar(Object entidad, File file) {
		try {
			JAXBContext context = JAXBContext.newInstance(entidad.getClass());
			Unmarshaller um = context.createUnmarshaller();

			// Reading XML from the file and unmarshalling.
			
			entidad =  um.unmarshal(file);

			// personData.clear();
			// personData.addAll(wrapper.getPersons());

			// Save the file path to the registry.
			// setPersonFilePath(file);

		} catch (Exception e) { // catches ANY exception
			e.printStackTrace();
		}
		return entidad;
	}
	
}
