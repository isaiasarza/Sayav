//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.11 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2017.05.16 at 11:34:44 AM ART 
//


package SAYAV2.SAYAV2.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;


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
 *         &lt;sequence&gt;
 *           &lt;element name="Broker" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *           &lt;element name="Port" type="{http://www.w3.org/2001/XMLSchema}int"/&gt;
 *           &lt;element name="Sectores" type="{http://www.w3.org/2001/XMLSchema}int"/&gt;
 *         &lt;/sequence&gt;
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
    "broker",
    "port",
    "sectores"
})
@XmlRootElement(name = "Configurator")
public class Configurator {

    @XmlElement(name = "Broker")
    protected String broker;
    @XmlElement(name = "Port")
    protected Integer port;
    @XmlElement(name = "Sectores")
    protected Integer sectores;
    
    

    public Configurator() {
		super();
	}

	public Configurator(String broker, Integer port, Integer sectores) {
		super();
		this.broker = broker;
		this.port = port;
		this.sectores = sectores;
	}

	/**
     * Gets the value of the broker property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getBroker() {
        return broker;
    }

    /**
     * Sets the value of the broker property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setBroker(String value) {
        this.broker = value;
    }

    /**
     * Gets the value of the port property.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getPort() {
        return port;
    }

    /**
     * Sets the value of the port property.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setPort(Integer value) {
        this.port = value;
    }

    /**
     * Gets the value of the sectores property.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getSectores() {
        return sectores;
    }

    /**
     * Sets the value of the sectores property.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setSectores(Integer value) {
        this.sectores = value;
    }

}
