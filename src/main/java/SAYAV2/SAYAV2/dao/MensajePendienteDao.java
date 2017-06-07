package SAYAV2.SAYAV2.dao;

import java.io.File;
import java.util.Iterator;

import javax.xml.bind.JAXBException;

import Datos.DatoGrupo;
import SAYAV2.SAYAV2.mensajeria.Mensaje;
import SAYAV2.SAYAV2.mensajeria.TipoMensaje;
import SAYAV2.SAYAV2.model.MensajesPendientes;
import SAYAV2.SAYAV2.service.JsonTransformer;

public class MensajePendienteDao extends GenericDao<MensajesPendientes> {

	private static MensajePendienteDao mensajePendienteDao;
	private JsonTransformer json;
	public MensajePendienteDao() {
		super();
		this.e = new MensajesPendientes();
		this.json = new JsonTransformer();
	}
	
	
	
	public static MensajePendienteDao getInstance(){
		if(mensajePendienteDao == null){
			mensajePendienteDao = new MensajePendienteDao();
		}
		return mensajePendienteDao;
	}



	public synchronized boolean  eliminarMensaje(Mensaje msg) throws JAXBException {
		MensajesPendientes mensajes = this.cargar(file);
		Iterator<Mensaje> iterator = mensajes.getMensaje().iterator();
		while(iterator.hasNext()){
			if(iterator.next().equals(msg)){
				iterator.remove();
				return true;
			}
		}
		return false;
	}


	 public Mensaje eliminarMensajeId(String mensajeId) throws JAXBException { 
		   
		    MensajesPendientes mensajes = this.cargar(file); 
		    Iterator<Mensaje> iterator = mensajes.getMensaje().iterator(); 
		    while(iterator.hasNext()){ 
		      Mensaje m = iterator.next(); 
		      if(m.getId().equals(mensajeId)){ 
		        Mensaje mensajeEliminado = (Mensaje) m.clone(); 
		        iterator.remove(); 
		        this.guardar(mensajes, file); 
		        return mensajeEliminado; 
		      } 
		    } 
		    return null; 
		  } 
	
	
	
	public boolean exist(DatoGrupo datos, TipoMensaje tipoMensaje, String tipoHandshake) throws JAXBException {
		MensajesPendientes mensajes = this.cargar(file);
		Iterator<Mensaje> iterator = mensajes.getMensaje().iterator();
		while(iterator.hasNext()){
			Mensaje mensaje = iterator.next();		
			if(mensaje.getTipoMensaje().getTipo().equals(tipoMensaje.getTipo()) && mensaje.getTipoHandshake().equals(tipoHandshake)){
				DatoGrupo datoGrupo = json.getGson().fromJson(mensaje.getDatos(), DatoGrupo.class);
				if(datoGrupo.getGrupo().getNombre().equals(datos.getGrupo().getNombre()) && datoGrupo.getMiembro().equals(datos.getMiembro())){
					return true;
				}
			}
		}
		return false;
	}
	
	@Override
	public synchronized MensajesPendientes cargar(File file) throws JAXBException {
		return super.cargar(file);
	}
	
	@Override
	public synchronized void guardar(Object entidad, File file) {
		super.guardar(entidad, file);
	}
	
}
