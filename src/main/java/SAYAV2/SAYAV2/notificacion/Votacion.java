//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.11 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2017.06.04 at 01:00:56 PM ART 
//


package SAYAV2.SAYAV2.notificacion;

import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

import SAYAV2.SAYAV2.model.Peer;


/**
 * <p>Java class for Votacion complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="Votacion"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="Id" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;        
 *         &lt;element name="Miembro" type="{http://www.example.org/UsuarioXMLSchema}Peer"/&gt;
 *         &lt;element name="GrupoId" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;element name="VotantesAFavor" type="{http://www.w3.org/2001/XMLSchema}int"/&gt;
 *         &lt;element name="VotantesEnContra" type="{http://www.w3.org/2001/XMLSchema}int"/&gt;
 *         &lt;element name="Finalizo" type="{http://www.w3.org/2001/XMLSchema}boolean"/&gt;
 *         &lt;element name="Solicitante" type="{http://www.example.org/UsuarioXMLSchema}Peer"/&gt;
 *       &lt;/sequence&gt;
 *         &lt;element name="Votantes" type="{http://www.example.org/UsuarioXMLSchema}Peer" maxOccurs="unbounded" minOccurs="0"/&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "Votacion", namespace = "http://www.example.org/Votaciones", propOrder = {
    "id",
    "miembro","grupoId",
    "votantesAFavor",
    "votantesEnContra",
    "finalizo",
    "solicitante","votantes"
})
public class Votacion {
	private String id;
    @XmlElement(name = "Miembro", namespace = "http://www.example.org/Votaciones", required = true)
    protected Peer miembro;
    @XmlElement(name = "GrupoId", namespace = "http://www.example.org/Votaciones", required = true)
    protected String grupoId;
    @XmlElement(name = "VotantesAFavor", namespace = "http://www.example.org/Votaciones")
    protected int votantesAFavor;
    @XmlElement(name = "VotantesEnContra", namespace = "http://www.example.org/Votaciones")
    protected int votantesEnContra;
    @XmlElement(name = "Finalizo", namespace = "http://www.example.org/Votaciones")
    protected boolean finalizo;
    @XmlElement(name = "Solicitante", namespace = "http://www.example.org/Votaciones", required = true)
    protected Peer solicitante;
    @XmlElement(name = "Votantes", namespace = "http://www.example.org/Votaciones")
    protected List<Peer> votantes;
    
    
    public Votacion() {
		super();
		this.setId(UUID.randomUUID().toString());
		this.votantes = new LinkedList<Peer>();

	}

	

	public Votacion(Peer miembro2, String id2) {
		this.setId(UUID.randomUUID().toString());
		this.miembro = miembro2;
		this.grupoId = id2;
		this.votantesAFavor = 0;
		this.votantesEnContra = 0;
		this.finalizo = false;
		this.solicitante = new Peer();
		this.votantes = new LinkedList<Peer>();	}

	/**
     * Gets the value of the miembro property.
     * 
     * @return
     *     possible object is
     *     {@link Peer }
     *     
     */
    public Peer getMiembro() {
        return miembro;
    }

    /**
     * Sets the value of the miembro property.
     * 
     * @param value
     *     allowed object is
     *     {@link Peer }
     *     
     */
    public void setMiembro(Peer value) {
        this.miembro = value;
    }



    public String getGrupoId() {
		return grupoId;
	}



	public void setGrupoId(String grupoId) {
		this.grupoId = grupoId;
	}



	/**
     * Gets the value of the votantesAFavor property.
     * 
     */
    public int getVotantesAFavor() {
        return votantesAFavor;
    }

    /**
     * Sets the value of the votantesAFavor property.
     * 
     */
    public void setVotantesAFavor(int value) {
        this.votantesAFavor = value;
    }

    /**
     * Gets the value of the votantesEnContra property.
     * 
     */
    public int getVotantesEnContra() {
        return votantesEnContra;
    }

    /**
     * Sets the value of the votantesEnContra property.
     * 
     */
    public void setVotantesEnContra(int value) {
        this.votantesEnContra = value;
    }

    /**
     * Gets the value of the finalizo property.
     * 
     */
    public boolean isFinalizo() {
        return finalizo;
    }

    /**
     * Sets the value of the finalizo property.
     * 
     */
    public void setFinalizo(boolean value) {
        this.finalizo = value;
    }

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}
	
//	public String getPorcentajeAFavor(){
//		Grupo grupo = usuarioDao.c
//		
//		double porcentaje = this.votantesAFavor * 100 / grupo.getPeers().size();
//		
//		return porcentaje + "%";
//	}
//	
//	public String getPorcentajeEnContra(){
//		
//		double porcentaje = this.votantesEnContra * 100 / this.grupo.getPeers().size();
//		
//		return porcentaje + "%";
//	}
//	
//	public String getPorcentajeVotantes(){
//		
//		double porcentaje = (this.votantesEnContra + this.votantesAFavor) * 100 / this.grupo.getPeers().size();
//		
//		return porcentaje + "%";
//	}
//


	public Peer getSolicitante() {
		return solicitante;
	}

	public void setSolicitante(Peer solicitante) {
		this.solicitante = solicitante;
	}

	
	public List<Peer> getVotantes() {
		return votantes;
	}

	public void setVotantes(List<Peer> votantes) {
		this.votantes = votantes;
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
		Votacion other = (Votacion) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}



	@Override
	public String toString() {
		return "Votacion [miembro=" + miembro + ", votantesAFavor=" + votantesAFavor + ", votantesEnContra="
				+ votantesEnContra + ", finalizo=" + finalizo + "]";
	}

	
	
	
}
