package SAYAV2.SAYAV2.model;

import java.util.ArrayList;
import java.util.List;

public class GrupoPeer {
	
	private String grupoId;
	private String grupoNombre;
	private String peer;
	private List<String> listaPeers;
	
	
	public GrupoPeer() {
		super();
		this.listaPeers = new ArrayList<String>();
	}
	
	
	public String getGrupoId() {
		return grupoId;
	}


	public void setGrupoId(String grupoId) {
		this.grupoId = grupoId;
	}


	public String getGrupoNombre() {
		return grupoNombre;
	}


	public void setGrupoNombre(String grupoNombre) {
		this.grupoNombre = grupoNombre;
	}

	public String getPeer() {
		return peer;
	}
	public void setPeer(String peer) {
		this.peer = peer;
	}
	public void addPeer(String peer){
		this.listaPeers.add(peer);
	}
	
	public List<String> getListaPeers() {
		return listaPeers;
	}
	public void setListaPeers(List<String> listaPeers) {
		this.listaPeers = listaPeers;
	}
	public void setListaPeersByPeer(List<Peer> peers) {
		for(Peer p: peers){
			this.listaPeers.add(p.getDireccion());
		}
		
	}


	@Override
	public String toString() {
		return "GrupoPeer [grupoId=" + grupoId + ", grupoNombre=" + grupoNombre + ", peer=" + peer + ", listaPeers="
				+ listaPeers + "]";
	}
	
	
	
	
	
	

}
