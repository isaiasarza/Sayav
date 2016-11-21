package SAYAV2.SAYAV2.dao;

import java.io.File;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import SAYAV2.SAYAV2.model.Sector;
import SAYAV2.SAYAV2.model.Usuario;

public class SectorDao extends GenericDao<Sector>{
	
private static SectorDao sectorDao;
	

	public SectorDao() {
		super();
		this.e = new Sector();
	}
	
	
	
	public static SectorDao getInstance(){
		if(sectorDao == null){
			sectorDao = new SectorDao();
		}
		return sectorDao;
	}

	
	
	public void actualizar(Sector sector, File file) throws JAXBException{
		
		sectorDao.guardar(sector, file);
		
	}
	

}
