//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.11 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2016.11.13 at 04:50:10 PM ART 
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
 *         &lt;element name="Grupo" type="{http://www.example.org/UsuarioXMLSchema}Grupo" maxOccurs="unbounded" minOccurs="0"/&gt;
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
    "contraseña",
    "salt",
    "alarmaHabilitada",
    "dispositivosMoviles",
    "sensores",
    "grupo"
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
    @XmlElement(name = "Grupo")
    protected List<Grupo> grupo;

    
    
    public Usuario() {
		super();
		this.alarmaHabilitada = true;
		this.dispositivosMoviles = new ArrayList<DispositivosType>();
		this.sensores = new ArrayList<SensoresType>();
		this.grupo = new ArrayList<Grupo>();
	}

	/**
     * Gets the value of the nombre property.
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
     * Sets the value of the nombre property.
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
     * Gets the value of the apellido property.
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
     * Sets the value of the apellido property.
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
     * Gets the value of the telefono property.
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
     * Sets the value of the telefono property.
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
     * Gets the value of the email property.
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
     * Sets the value of the email property.
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
     * Gets the value of the direccion property.
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
     * Sets the value of the direccion property.
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
     * Gets the value of the subdominio property.
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
     * Sets the value of the subdominio property.
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
     * Gets the value of the contraseña property.
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
     * Sets the value of the contraseña property.
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
     * Gets the value of the salt property.
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
     * Sets the value of the salt property.
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
     * Gets the value of the alarmaHabilitada property.
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
     * Sets the value of the alarmaHabilitada property.
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

    /**
     * Gets the value of the grupo property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the grupo property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getGrupo().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link Grupo }
     * 
     * 
     */
    public List<Grupo> getGrupo() {
        if (grupo == null) {
            grupo = new ArrayList<Grupo>();
        }
        return this.grupo;
    }

	public Boolean getAlarmaHabilitada() {
		return alarmaHabilitada;
	}

	public void setDispositivosMoviles(List<DispositivosType> dispositivosMoviles) {
		this.dispositivosMoviles = dispositivosMoviles;
	}

	public void setSensores(List<SensoresType> sensores) {
		this.sensores = sensores;
	}

	public void setGrupo(List<Grupo> grupo) {
		this.grupo = grupo;
	}
    
    public boolean addGrupo(Grupo grupo){
    	if(this.grupo.contains(grupo))
    		return false;
    	this.grupo.add(grupo);
    	return true;
    }
    
    public Grupo getSingleGrupo(String name){
    	int index;
    	Grupo g = new Grupo(name);
    	index = this.grupo.indexOf(g);
    	if(index == -1)
    		return null;
    	return this.grupo.get(index);
    }
    public boolean removeGrupo(Grupo grupo){
    	return this.grupo.remove(grupo);
    }
    
    public boolean addDispositivoMovil(DispositivosType dispositivo){
    	if(this.dispositivosMoviles.contains(dispositivo))
    		return false;
    	this.dispositivosMoviles.add(dispositivo);
    	return true;
    }
    
    public boolean removeDispositivoMovil(DispositivosType dispositivo){
    	return this.dispositivosMoviles.remove(dispositivo);
    }

	@Override
	public String toString() {
		return "Usuario [nombre=" + nombre + ", apellido=" + apellido + ", telefono=" + telefono + "]";
	}

}
