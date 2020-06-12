package SAYAV2.SAYAV2.dao;

import java.io.File;

public interface Dao {
	
	
	public void persist(Object obj, File file);
	public Object load(Object obj, File file);

}
