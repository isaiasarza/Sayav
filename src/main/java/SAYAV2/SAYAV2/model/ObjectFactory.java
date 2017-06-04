
package SAYAV2.SAYAV2.model;

import javax.xml.bind.annotation.XmlRegistry;

import SAYAV2.SAYAV2.mensajeria.Mensaje;
import SAYAV2.SAYAV2.mensajeria.TipoMensaje;
import SAYAV2.SAYAV2.notificacion.Votacion;
import SAYAV2.SAYAV2.notificacion.Votaciones;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the SAYAV2.SAYAV2.model package. 
 * <p>An ObjectFactory allows you to programatically 
 * construct new instances of the Java representation 
 * for XML content. The Java representation of XML 
 * content can consist of schema derived interfaces 
 * and classes representing the binding of schema 
 * type definitions, element declarations and model 
 * groups.  Factory methods for each of these are 
 * provided in this class.
 * 
 */
@XmlRegistry
public class ObjectFactory {


    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: SAYAV2.SAYAV2.model
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link Votaciones }
     * 
     */
    public Votaciones createVotaciones() {
        return new Votaciones();
    }

    /**
     * Create an instance of {@link Votacion }
     * 
     */
    public Votacion createVotacion() {
        return new Votacion();
    }

    /**
     * Create an instance of {@link TiposMensajes }
     * 
     */
    public TiposMensajes createTiposMensajes() {
        return new TiposMensajes();
    }

    /**
     * Create an instance of {@link Tipos }
     * 
     */
    public Tipos createTipos() {
        return new Tipos();
    }

    /**
     * Create an instance of {@link Mensaje }
     * 
     */
    public Mensaje createMensaje() {
        return new Mensaje();
    }

    /**
     * Create an instance of {@link TipoMensaje }
     * 
     */
    public TipoMensaje createTipoMensaje() {
        return new TipoMensaje();
    }
    
    /**
     * Create an instance of {@link Configurator }
     * 
     */
    public Configurator createConfigurator(){
    	return new Configurator();
    }

}
