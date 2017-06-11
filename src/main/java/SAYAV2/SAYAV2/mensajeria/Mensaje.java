//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.11 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2017.05.26 at 04:38:39 PM ART 
//

package SAYAV2.SAYAV2.mensajeria;

import java.text.SimpleDateFormat;
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
 *           &lt;element name="detalle" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
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
@XmlType(name = "", propOrder = { "id", "origen", "destino", "fechaCreacion", "fechaReenvio", "estado", "datos",
		"descripcion","detalle", "tipoHandshake", "tipoMensaje" })
@XmlRootElement(name = "Mensaje")
public class Mensaje implements Cloneable,Comparable<Mensaje> {

	@XmlElement(name = "Id")
	protected String id;
	protected String origen;
	protected String destino;
	protected Date fechaCreacion;
	protected Date fechaReenvio;
	protected String estado;
	protected String datos;
	protected String descripcion;
	protected String detalle;
	private String tipoHandshake;
	@XmlElement(name = "TipoMensaje")
	protected TipoMensaje tipoMensaje;

	public Mensaje() {
		super();
		this.id = UUID.randomUUID().toString();
		this.tipoMensaje = new TipoMensaje();
		this.fechaCreacion = new Date();
		this.fechaReenvio = new Date();
		this.origen = "a";
		this.destino = "b";
		this.descripcion = "";
		this.detalle = "";
	}

	public Mensaje(String id, String origen, String destino, Date fechaCreacion, Date fechaReenvio, String estado,
			String datos, String descripcion, String tipoHandshake, TipoMensaje tipoMensaje) {
		super();
		this.id = id;
		this.origen = origen;
		this.destino = destino;
		this.fechaCreacion = fechaCreacion;
		this.fechaReenvio = fechaReenvio;
		this.estado = estado;
		this.datos = datos;
		this.descripcion = descripcion;
		this.tipoHandshake = tipoHandshake;
		this.tipoMensaje = tipoMensaje;
	}

	public Mensaje clone() {

		Mensaje mensaje = new Mensaje();

		mensaje.setId(id);
		mensaje.setDatos(datos);
		mensaje.setDescripcion(descripcion);
		mensaje.setDestino(destino);
		mensaje.setOrigen(origen);
		mensaje.setEstado(estado);
		mensaje.setTipoHandshake(tipoHandshake);
		mensaje.setTipoMensaje(tipoMensaje);

		return mensaje;

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

	public String getDetalle() {
		return detalle;
	}

	public void setDetalle(String detalle) {
		this.detalle = detalle;
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
	 * @throws Exception
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
	 * @throws Exception
	 * 
	 */
	public void setDestino(String value) {
		this.destino = value;
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

	public Date getFechaCreacion() {
		return fechaCreacion;
	}

	public void setFechaCreacion(Date fechaCreacion) {
		this.fechaCreacion = fechaCreacion;
	}

	public Date getFechaReenvio() {
		return fechaReenvio;
	}

	public void setFechaReenvio(Date fechaReenvio) {
		this.fechaReenvio = fechaReenvio;
	}


	@Override
	public String toString() {
		return "Mensaje [id=" + id + ", origen=" + origen + ", destino=" + destino + ", fechaCreacion=" + fechaCreacion
				+ ", fechaReenvio=" + fechaReenvio + ", estado=" + estado + ", datos=" + datos + ", descripcion="
				+ descripcion + ", tipoHandshake=" + tipoHandshake + ", tipoMensaje=" + tipoMensaje + "]";
	}

	public String imprimirFechaCreacion() {

		String pattern = "dd/MM/yyyy hh:mm";
		SimpleDateFormat format = new SimpleDateFormat(pattern);
		return format.format(this.fechaCreacion);

	}

	public String imprimirFechaReenvio() {

		String pattern = "dd/MM/yyyy hh:mm";
		SimpleDateFormat format = new SimpleDateFormat(pattern);
		return format.format(this.fechaReenvio);

	}

	public String generateId() {
		return this.id = UUID.randomUUID().toString();
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Mensaje other = (Mensaje) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}



	@Override
	public int compareTo(Mensaje o) {
		return this.getFechaReenvio().compareTo(o.getFechaReenvio());
	}
	
	

}
