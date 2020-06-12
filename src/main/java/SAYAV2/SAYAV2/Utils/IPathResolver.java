package SAYAV2.SAYAV2.Utils;

import java.net.URL;

public class IPathResolver implements PathResolver{
	
	private URL url;
	private static IPathResolver instance;
	
	
	private IPathResolver() {
	}
	
	public String getPath(String relativePath) {
		url = this.getClass().getResource(relativePath);
		System.out.println(this.getClass());
		System.out.println(url);
		System.out.println(url.getPath());
		return url.getPath();
	}
	
	
	public static IPathResolver getInstance() {
		if(instance == null) 
			instance = new IPathResolver();
		return instance;
	}


}
