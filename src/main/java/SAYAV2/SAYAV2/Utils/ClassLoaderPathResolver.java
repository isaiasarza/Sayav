package SAYAV2.SAYAV2.Utils;

public class ClassLoaderPathResolver implements PathResolver {
	
	private ClassLoader classLoader;
	private static PathResolver pathResolver;
	
	private ClassLoaderPathResolver() {
        this.classLoader = Thread.currentThread().getContextClassLoader();
	}

	@Override
	public String getPath(String relativePath) {
		System.out.println(ClassLoader.getSystemClassLoader().getResource(relativePath).getFile());
		return classLoader.getResource(relativePath).getFile();
	}
	
	public static PathResolver getInstance() {
		if(pathResolver == null)
			pathResolver = new ClassLoaderPathResolver();
		return pathResolver;
	}
	

}
