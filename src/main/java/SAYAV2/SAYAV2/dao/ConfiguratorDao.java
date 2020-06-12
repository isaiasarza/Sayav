package SAYAV2.SAYAV2.dao;

import SAYAV2.SAYAV2.model.Configurator;

public class ConfiguratorDao extends GenericDao<Configurator>{
	
	private static ConfiguratorDao configDao;
	

	private ConfiguratorDao() {
		super();
		this.e = new Configurator();
	}
	
	public static ConfiguratorDao getInstance(){
		if(configDao == null){
			configDao = new ConfiguratorDao();
		}
		return configDao;
	}
}
