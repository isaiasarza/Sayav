//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.11 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2016.11.20 at 05:53:44 PM ART 
//

package SAYAV2.SAYAV2.model;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

/**
 * <p>
 * Java class for Grupo complex type.
 * 
 * <p>
 * The following schema fragment specifies the expected content contained within
 * this class.
 * 
 * <pre>
 * &lt;complexType name="Grupo"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="Id" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;element name="Nombre" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;element name="Peers" type="{http://www.example.org/UsuarioXMLSchema}Peer" maxOccurs="unbounded" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "Grupo", propOrder = { "id", "nombre", "peers" })
public class Grupo {

	@XmlElement(name = "Id")
	protected String id;
	@XmlElement(name = "Nombre", required = true)
	protected String nombre;
	@XmlElement(name = "Peers")
	protected List<Peer> peers;

	public Grupo() {
		super();
		this.id = UUID.randomUUID().toString();
		this.peers = new ArrayList<Peer>();
	}

	public Grupo(String nombre) {
		super();
		this.id = UUID.randomUUID().toString();
		this.peers = new ArrayList<Peer>();
		this.nombre = nombre;
	}

	/**
	 * Gets the value of the id property.
	 * 
	 */
	public String getId() {
		return id;
	}

	/**
	 * Sets the value of the id property.
	 * 
	 */
	public void setId(String value) {
		this.id = value;
	}

	/**
	 * Gets the value of the nombre property.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getNombre() {
		return nombre;
	}

	/**
	 * Sets the value of the nombre property.
	 * 
	 * @param value
	 *            allowed object is {@link String }
	 * 
	 */
	public void setNombre(String value) {
		this.nombre = value;
	}

	/**
	 * Gets the value of the peers property.
	 * 
	 * <p>
	 * This accessor method returns a reference to the live list, not a
	 * snapshot. Therefore any modification you make to the returned list will
	 * be present inside the JAXB object. This is why there is not a
	 * <CODE>set</CODE> method for the peers property.
	 * 
	 * <p>
	 * For example, to add a new item, do as follows:
	 * 
	 * <pre>
	 * getPeers().add(newItem);
	 * </pre>
	 * 
	 * 
	 * <p>
	 * Objects of the following type(s) are allowed in the list {@link Peer }
	 * 
	 * 
	 */
	public List<Peer> getPeers() {
		if (peers == null) {
			peers = new ArrayList<Peer>();
		}
		return this.peers;
	}

	
	public boolean addPeer(String domain){
		Peer nuevo = new Peer(domain);
		if(this.peers.contains(nuevo)){
			return false;
		}
		return this.peers.add(nuevo);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((nombre == null) ? 0 : nombre.hashCode());
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
		Grupo other = (Grupo) obj;
		if (nombre == null) {
			if (other.nombre != null)
				return false;
		} else if (!nombre.equals(other.nombre))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Grupo [id=" + id + ", nombre=" + nombre + ", peers=" + peers + "]";
	}

	public boolean removePeer(String peer) {
		// TODO Auto-generated method stub
	   Peer bajapeer = new Peer();
	   bajapeer.setDireccion(peer);
	   
	   return this.peers.remove(bajapeer);
	}

	public void addAll(List<String> listaPeers) {
		// TODO Auto-generated method stub
		for(String peer : listaPeers){
			
			Peer p = new Peer();
			p.setDireccion(peer);
			this.peers.add(p);
		}
		
	}

	public boolean add(String origen) {
		Peer peer = new Peer();
		peer.setDireccion(origen);
		return this.peers.add(peer);		
	}
	
	
}
