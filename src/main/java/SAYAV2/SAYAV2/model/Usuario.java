//
// Este archivo ha sido generado por la arquitectura JavaTM para la implantaci�n de la referencia de enlace (JAXB) XML v2.2.11 
// Visite <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Todas las modificaciones realizadas en este archivo se perder�n si se vuelve a compilar el esquema de origen. 
// Generado el: 2016.10.11 a las 12:15:19 AM ART 
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
 * <p>Clase Java para anonymous complex type.
 * 
 * <p>El siguiente fragmento de esquema especifica el contenido que se espera que haya en esta clase.
 * 
 * <pre>
 * &lt;complexType&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;choice&gt;
 *         &lt;sequence&gt;
 *           &lt;element name="Nombre" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *           &lt;element name="Apellido" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *           &lt;element name="Telefono" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *           &lt;element name="Email" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *           &lt;element name="Direccion" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *           &lt;element name="Subdominio" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *           &lt;element name="Contraseña" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *           &lt;element name="Salt" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *           &lt;element name="AlarmaHabilitada" type="{http://www.w3.org/2001/XMLSchema}boolean"/&gt;
 *         &lt;/sequence&gt;
 *         &lt;element name="DispositivosMoviles" type="{http://www.example.org/UsuarioXMLSchema}DispositivosType" maxOccurs="unbounded" minOccurs="0"/&gt;
 *         &lt;element name="Sensores" type="{http://www.example.org/UsuarioXMLSchema}SensoresType" maxOccurs="unbounded" minOccurs="0"/&gt;
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
    "nombre",
    "apellido",
    "telefono",
    "email",
    "direccion",
    "subdominio",
    "contrase\u00f1a",
    "salt",
    "alarmaHabilitada",
    "dispositivosMoviles",
    "sensores"
})
@XmlRootElement(name = "Usuario")
public class Usuario {

    @XmlElement(name = "Nombre")
    protected String nombre;
    @XmlElement(name = "Apellido")
    protected String apellido;
    @XmlElement(name = "Telefono")
    protected String telefono;
    @XmlElement(name = "Email")
    protected String email;
    @XmlElement(name = "Direccion")
    protected String direccion;
    @XmlElement(name = "Subdominio")
    protected String subdominio;
    @XmlElement(name = "Contraseña")
    protected String contraseña;
    @XmlElement(name = "Salt")
    protected String salt;
    @XmlElement(name = "AlarmaHabilitada")
    protected Boolean alarmaHabilitada;
    @XmlElement(name = "DispositivosMoviles")
    protected List<DispositivosType> dispositivosMoviles;
    @XmlElement(name = "Sensores")
    protected List<SensoresType> sensores;

    /**
     * Obtiene el valor de la propiedad nombre.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNombre() {
        return nombre;
    }

    /**
     * Define el valor de la propiedad nombre.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNombre(String value) {
        this.nombre = value;
    }

    /**
     * Obtiene el valor de la propiedad apellido.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getApellido() {
        return apellido;
    }

    /**
     * Define el valor de la propiedad apellido.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setApellido(String value) {
        this.apellido = value;
    }

    /**
     * Obtiene el valor de la propiedad telefono.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTelefono() {
        return telefono;
    }

    /**
     * Define el valor de la propiedad telefono.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTelefono(String value) {
        this.telefono = value;
    }

    /**
     * Obtiene el valor de la propiedad email.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getEmail() {
        return email;
    }

    /**
     * Define el valor de la propiedad email.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setEmail(String value) {
        this.email = value;
    }

    /**
     * Obtiene el valor de la propiedad direccion.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDireccion() {
        return direccion;
    }

    /**
     * Define el valor de la propiedad direccion.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDireccion(String value) {
        this.direccion = value;
    }

    /**
     * Obtiene el valor de la propiedad subdominio.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSubdominio() {
        return subdominio;
    }

    /**
     * Define el valor de la propiedad subdominio.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSubdominio(String value) {
        this.subdominio = value;
    }

    /**
     * Obtiene el valor de la propiedad contraseña.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getContraseña() {
        return contraseña;
    }

    /**
     * Define el valor de la propiedad contraseña.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setContraseña(String value) {
        this.contraseña = value;
    }

    /**
     * Obtiene el valor de la propiedad salt.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSalt() {
        return salt;
    }

    /**
     * Define el valor de la propiedad salt.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSalt(String value) {
        this.salt = value;
    }

    /**
     * Obtiene el valor de la propiedad alarmaHabilitada.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isAlarmaHabilitada() {
        return alarmaHabilitada;
    }

    /**
     * Define el valor de la propiedad alarmaHabilitada.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setAlarmaHabilitada(Boolean value) {
        this.alarmaHabilitada = value;
    }

    /**
     * Gets the value of the dispositivosMoviles property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the dispositivosMoviles property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getDispositivosMoviles().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link DispositivosType }
     * 
     * 
     */
    public List<DispositivosType> getDispositivosMoviles() {
        if (dispositivosMoviles == null) {
            dispositivosMoviles = new ArrayList<DispositivosType>();
        }
        return this.dispositivosMoviles;
    }

    /**
     * Gets the value of the sensores property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the sensores property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getSensores().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link SensoresType }
     * 
     * 
     */
    public List<SensoresType> getSensores() {
        if (sensores == null) {
            sensores = new ArrayList<SensoresType>();
        }
        return this.sensores;
    }

	@Override
	public String toString() {
		return "Usuario [nombre=" + nombre + ", apellido=" + apellido + ", telefono=" + telefono + ", email=" + email
				+ ", direccion=" + direccion + ", subdominio=" + subdominio + ", contraseña=" + contraseña + ", salt="
				+ salt + ", alarmaHabilitada=" + alarmaHabilitada + ", dispositivosMoviles=" + dispositivosMoviles
				+ ", sensores=" + sensores + "]";
	}
    
    

}
