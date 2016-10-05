package SAYAV2.SAYAV2.model;

public class DispositivoMovilFactory implements AbstractFactory{

	 /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: SAYAV2.SAYAV2.model
     * 
     */
    public DispositivoMovilFactory() {
    	
    }

	@Override
	public Object createObject() {
		return new DispositivosType();
	}
		
}
