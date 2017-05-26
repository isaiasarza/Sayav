
package SAYAV2.SAYAV2.model;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import SAYAV2.SAYAV2.mensajeria.TipoMensaje;


/**
 * <p>Java class for anonymous complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;choice&gt;
 *         &lt;element name="tipos" type="{http://www.w3.org/2001/XMLSchema}anyType" maxOccurs="unbounded" minOccurs="0"/&gt;
 *       &lt;/choice&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "tipos"
})
@XmlRootElement(name = "TiposMensajes", namespace = "http://www.example.org/TiposMensajes")
public class TiposMensajes {

    @XmlElement(namespace = "http://www.example.org/TiposMensajes")
    protected List<TipoMensaje> tipos;
    
    

    public TiposMensajes() {
		super();
		this.tipos = new LinkedList<TipoMensaje>();
	}



	/**
     * Gets the value of the tipos property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the tipos property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getTipos().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link Object }
     * 
     * 
     */
    public List<TipoMensaje> getTipos() {
        if (tipos == null) {
            tipos = new ArrayList<TipoMensaje>();
        }
        return this.tipos;
    }
    
    /**
     * 
     * @param tipo
     * @return el tipo según el nombre ingresado
     */
    public TipoMensaje getTipo(String tipo){
    	for(TipoMensaje t: tipos){
    		if(t.getTipo().equals(tipo)){
    			return t;
    		}
    	}
    	return null;
    }

}
