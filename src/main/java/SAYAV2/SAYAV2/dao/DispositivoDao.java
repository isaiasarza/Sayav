package SAYAV2.SAYAV2.dao;

import java.io.File;

import javax.xml.bind.Binder;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import SAYAV2.SAYAV2.model.DispositivoM;


public class DispositivoDao implements Dao{

	public static DispositivoM disp = new DispositivoM();
	
	
	@Override
	public void persist(Object obj, File file) {
		JAXBContext context;
		try {

			
			context = JAXBContext.newInstance(obj.getClass());
			Marshaller m;
			m = context.createMarshaller();
			m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
			m.marshal(obj, file);

		} catch (JAXBException e) {
			e.printStackTrace();
		}
	}

	@Override
	public Object load(Object obj, File file) {
		// TODO Auto-generated method stub
		try {
			JAXBContext context = JAXBContext.newInstance(obj.getClass());
			Unmarshaller um = context.createUnmarshaller();

			// Reading XML from the file and unmarshalling.
			obj =  um.unmarshal(file);

		} catch (Exception e) { // catches ANY exception
			e.printStackTrace();
		}
	
		return obj;
	}

	public void update(Object obj) {
		//StringWriter writer = new StringWriter();
		JAXBContext context;
		try {

			
			context = JAXBContext.newInstance(obj.getClass());
			Binder b = context.createBinder(obj.getClass());
			b.updateXML(obj);

		} catch (JAXBException e) {
			e.printStackTrace();
		}

	}
	
	
	
	
}
