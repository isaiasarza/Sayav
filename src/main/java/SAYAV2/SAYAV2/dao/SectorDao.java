package SAYAV2.SAYAV2.dao;

import java.io.File;

import javax.xml.bind.JAXBException;

import SAYAV2.SAYAV2.model.Sector;

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
