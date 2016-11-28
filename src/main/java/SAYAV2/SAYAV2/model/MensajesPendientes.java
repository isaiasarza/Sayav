//
// Este archivo ha sido generado por la arquitectura JavaTM para la implantación de la referencia de enlace (JAXB) XML v2.2.8-b130911.1802 
// Visite <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Todas las modificaciones realizadas en este archivo se perderán si se vuelve a compilar el esquema de origen. 
// Generado el: 2016.11.27 a las 10:13:29 AM ART 
//


package SAYAV2.SAYAV2.model;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Clase Java para NewComplexType complex type.
 * 
 * <p>El siguiente fragmento de esquema especifica el contenido que se espera que haya en esta clase.
 * 
 * <pre>
 * &lt;complexType name="NewComplexType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="ListaMensajes" type="{http://www.example.org/UsuarioXMLSchema}Mensaje" maxOccurs="unbounded" minOccurs="0"/>
 *          &lt;element name="Direccion" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *          &lt;element name="Fechahora" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "NewComplexType", namespace = "http://www.example.org/MensajesPendientes", propOrder = {
    "listaMensajes",
    "direccion",
    "fechahora"
})
@XmlRootElement(name = "MensajesPendientes")
public class MensajesPendientes {

    @XmlElement(name = "ListaMensajes")
    protected List<Mensaje> listaMensajes;
    @XmlElement(name = "Direccion")
    protected String direccion;
    @XmlElement(name = "Fechahora")
    protected String fechahora;
    
    
    
    public MensajesPendientes(){
		super();
		this.listaMensajes = new ArrayList<Mensaje>();
		
	}

	/**
     * Gets the value of the listaMensajes property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the listaMensajes property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getListaMensajes().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link Mensaje }
     * 
     * 
     */
    public List<Mensaje> getListaMensajes() {
        if (listaMensajes == null) {
            listaMensajes = new ArrayList<Mensaje>();
        }
        return this.listaMensajes;
    }

	public void addMensaje(Mensaje mensaje) {
		// TODO Auto-generated method stub
		this.listaMensajes.add(mensaje);
	}

	public String getDireccion() {
		return direccion;
	}

	public void setDireccion(String direccion) {
		this.direccion = direccion;
	}

	public String getFechahora() {
		return fechahora;
	}

	public void setFechahora(String fechahora) {
		this.fechahora = fechahora;
	}

	public void setListaMensajes(List<Mensaje> listaMensajes) {
		this.listaMensajes = listaMensajes;
	}

    
    
    
}
