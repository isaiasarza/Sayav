package SAYAV2.SAYAV2.model;

import java.util.ArrayList;
import java.util.List;

public class GrupoPeer {
	
	private String grupo;
	private String peer;
	private List<String> listaPeers;
	
	
	public GrupoPeer() {
		super();
		this.listaPeers = new ArrayList<String>();
	}
	public String getGrupo() {
		return grupo;
	}
	public void setGrupo(String grupo) {
		this.grupo = grupo;
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
	@Override
	public String toString() {
		return "GrupoPeer [grupo=" + grupo + ", peer=" + peer + "]";
	}
	public List<String> getListaPeers() {
		return listaPeers;
	}
	public void setListaPeers(List<String> listaPeers) {
		this.listaPeers = listaPeers;
	}
	
	
	
	
	
	

}
