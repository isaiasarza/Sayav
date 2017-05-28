//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.11 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2017.05.26 at 04:38:39 PM ART 
//

package SAYAV2.SAYAV2.mensajeria;

import java.util.Date;
import java.util.UUID;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

/**
 * <p>
 * Java class for anonymous complex type.
 * 
 * <p>
 * The following schema fragment specifies the expected content contained within
 * this class.
 * 
 * <pre>
 * &lt;complexType&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;choice&gt;
 *         &lt;sequence&gt;
 *           &lt;element name="Id" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *           &lt;element name="origen" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *           &lt;element name="destino" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *           &lt;element name="fecha" type="{http://www.w3.org/2001/XMLSchema}date"/&gt;
 *           &lt;element name="estado" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *           &lt;element name="datos" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *           &lt;element name="descripcion" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *           &lt;element name="tipoHandshake" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;element name="TipoMensaje" type="{http://www.example.org/Mensaje}TipoMensaje"/&gt;      
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
@XmlType(name = "", propOrder = { "id", "origen", "destino", "fecha", "estado", "datos", "descripcion","tipoHandshake", "tipoMensaje" })
@XmlRootElement(name = "Mensaje")
public class Mensaje {

	@XmlElement(name = "Id")
	protected String id;
	protected String origen;
	protected String destino;
	protected Date fecha;
	protected String estado;
	protected String datos;
	protected String descripcion;
	private String tipoHandshake;
	@XmlElement(name = "TipoMensaje")
	protected TipoMensaje tipoMensaje;

	public Mensaje() {
		super();
		this.id = UUID.randomUUID().toString();
		this.tipoMensaje = new TipoMensaje();
	}

	/**
	 * Gets the value of the id property.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getId() {
		return id;
	}

	/**
	 * Sets the value of the id property.
	 * 
	 * @param value
	 *            allowed object is {@link String }
	 * 
	 */
	public void setId(String value) {
		this.id = value;
	}

	/**
	 * Gets the value of the origen property.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getOrigen() {
		return origen;
	}

	/**
	 * Sets the value of the origen property.
	 * 
	 * @param value
	 *            allowed object is {@link String }
	 * 
	 */
	public void setOrigen(String value) {
		this.origen = value;
	}

	/**
	 * Gets the value of the destino property.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getDestino() {
		return destino;
	}

	/**
	 * Sets the value of the destino property.
	 * 
	 * @param value
	 *            allowed object is {@link String }
	 * 
	 */
	public void setDestino(String value) {
		this.destino = value;
	}

	/**
	 * Gets the value of the fecha property.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public Date getFecha() {
		return fecha;
	}

	/**
	 * Sets the value of the fecha property.
	 * 
	 * @param value
	 *            allowed object is {@link String }
	 * 
	 */
	public void setFecha(Date value) {
		this.fecha = value;
	}

	/**
	 * Gets the value of the datos property.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getDatos() {
		return datos;
	}

	/**
	 * Sets the value of the datos property.
	 * 
	 * @param value
	 *            allowed object is {@link String }
	 * 
	 */
	public void setDatos(String value) {
		this.datos = value;
	}

	/**
	 * Gets the value of the descripcion property.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getDescripcion() {
		return descripcion;
	}

	/**
	 * Sets the value of the descripcion property.
	 * 
	 * @param value
	 *            allowed object is {@link String }
	 * 
	 */
	public void setDescripcion(String value) {
		this.descripcion = value;
	}

	public String getEstado() {
		return estado;
	}

	public void setEstado(String estado) {
		this.estado = estado;
	}
	
	public String getTipoHandshake() {
		return tipoHandshake;
	}

	public void setTipoHandshake(String tipoHandshake) {
		this.tipoHandshake = tipoHandshake;
	}

	public TipoMensaje getTipoMensaje() {
		return tipoMensaje;
	}

	public void setTipoMensaje(TipoMensaje tipoMensaje) {
		this.tipoMensaje = tipoMensaje;
	}

	@Override
	public String toString() {
		return "Mensaje [id=" + id + ", origen=" + origen + ", destino=" + destino + ", fecha=" + fecha + ", estado="
				+ estado + ", datos=" + datos + ", descripcion=" + descripcion + ", tipoMensaje=" + tipoMensaje
				+ ", getId()=" + getId() + ", getOrigen()=" + getOrigen() + ", getDestino()=" + getDestino()
				+ ", getFecha()=" + getFecha() + ", getDatos()=" + getDatos() + ", getDescripcion()=" + getDescripcion()
				+ ", getEstado()=" + getEstado() + ", getTipoMensaje()=" + getTipoMensaje() + ", getClass()="
				+ getClass() + ", hashCode()=" + hashCode() + ", toString()=" + super.toString() + "]";
	}



}
