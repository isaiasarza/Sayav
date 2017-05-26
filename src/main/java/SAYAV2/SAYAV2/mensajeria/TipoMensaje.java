//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.11 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2017.05.26 at 04:38:39 PM ART 
//


package SAYAV2.SAYAV2.mensajeria;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for TipoMensaje complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * 
 * 
 * <pre>
 * &lt;complexType name="TipoMensaje"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="tipo" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;element name="quantum" type="{http://www.w3.org/2001/XMLSchema}long"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "TipoMensaje", propOrder = {
    "tipo",
    "quantum"
})
public class TipoMensaje {

    @XmlElement(required = true)
    protected String tipo;
    protected long quantum;
    
    
    /**
     * 
     * @param tipo : de mensaje
     * @param quantum : tiempo en el que se repetira este tipo de mensajes en minutos
     */
    public TipoMensaje(String tipo, long quantum) {
		super();
		this.tipo = tipo;
		this.quantum = quantum;
	}

	public TipoMensaje() {
		super();
	}

	/**
     * Gets the value of the tipo property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTipo() {
        return tipo;
    }

    /**
     * Sets the value of the tipo property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTipo(String value) {
        this.tipo = value;
    }

    /**
     * Gets the value of the quantum property.
     * 
     */
    public long getQuantum() {
        return quantum;
    }

    /**
     * Sets the value of the quantum property.
     * 
     */
    public void setQuantum(long value) {
        this.quantum = value;
    }

}
