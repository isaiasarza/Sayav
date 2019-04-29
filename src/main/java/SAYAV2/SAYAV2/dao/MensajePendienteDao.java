package SAYAV2.SAYAV2.dao;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.Iterator;

import javax.xml.bind.JAXBException;

import Datos.DatoGrupo;
import SAYAV2.SAYAV2.Utils.EstadoUtils;
import SAYAV2.SAYAV2.Utils.FileUtils;
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
		if(mensajes.getMensaje().remove(msg)){
			this.guardar(mensajes, file);
			return true;
		}
		return false;	
	}


	 public synchronized Mensaje eliminarMensajeId(String mensajeId) throws JAXBException { 
		   
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
	
	
	
	public synchronized boolean exist(DatoGrupo datos, TipoMensaje tipoMensaje, String tipoHandshake,String estado){
		MensajesPendientes mensajes;
		
			try {
				mensajes = this.cargar(FileUtils.MENSAJES_FILE);
			} catch (JAXBException | IOException e) {
				e.printStackTrace();
				return false;
			}
			if(mensajes == null || mensajes.getMensaje().isEmpty()){
				return false;
			}
			Iterator<Mensaje> iterator = mensajes.getMensaje().iterator();
			while(iterator.hasNext()){
				Mensaje mensaje = iterator.next();		
				if(mensaje.getTipoMensaje().getTipo().equals(tipoMensaje.getTipo()) 
						&& mensaje.getTipoHandshake().equals(tipoHandshake) && mensaje.getEstado().equals(estado)){
					DatoGrupo datoGrupo = json.getGson().fromJson(mensaje.getDatos(), DatoGrupo.class);
					if(datoGrupo.getGrupo().getNombre().equals(datos.getGrupo().getNombre()) && datoGrupo.getMiembro().equals(datos.getMiembro())){
						return true;
					}
				}
			}
		
		return false;
	}
	
	public synchronized MensajesPendientes cargar(){
		try {
			MensajesPendientes m = super.cargar(FileUtils.MENSAJES_FILE);
			return m;
		} catch (JAXBException | IOException e) {
			e.printStackTrace();
		} 
		return new MensajesPendientes();
	}
	
	@Override
	public synchronized MensajesPendientes cargar(File file)  {
		try {
			return super.cargar(file);
		} catch (JAXBException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return new MensajesPendientes();
	}
	
	@Override
	public synchronized void guardar(Object entidad, File file) {
		super.guardar(entidad, file);
	}



	public boolean cambiarEstado(String id, String estado) {
		MensajesPendientes m;
		try {
			m = cargar();
			Mensaje aux = new Mensaje();
			aux.setId(id);
			int index = m.getMensaje().indexOf(aux);
			if(index < 0){
				return false;
			}
			Mensaje mensaje = m.getMensaje().get(index);
			if(estado.equals(EstadoUtils.Estado.PENDIENTE)){
				mensaje.setFechaCreacion(new Date());
				mensaje.setFechaReenvio(new Date());	
			}
			mensaje.setEstado(estado);
			m.getMensaje().set(index, mensaje);
			this.guardar(m, FileUtils.MENSAJES_FILE);
			return true;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return false;
		
	}
	
}
