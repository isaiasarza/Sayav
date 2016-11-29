package SAYAV2.SAYAV2.bussines;

import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.xml.bind.JAXBException;

import SAYAV2.SAYAV2.Utils.TipoMensaje;
import SAYAV2.SAYAV2.dao.MensajePendienteDao;
import SAYAV2.SAYAV2.model.Grupo;
import SAYAV2.SAYAV2.model.GrupoPeer;
import SAYAV2.SAYAV2.model.Mensaje;
import SAYAV2.SAYAV2.model.MensajePendienteData;
import SAYAV2.SAYAV2.model.MensajesPendientes;
import SAYAV2.SAYAV2.model.Peer;
import SAYAV2.SAYAV2.service.JsonTransformer;
import SAYAV2.SAYAV2.service.PostGrupo;

public class Notificacion {

	// private static UsuarioDao usuarioDao = UsuarioDao.getInstance();
	private static MensajePendienteDao mensajeDao = MensajePendienteDao.getInstance();
	private static File file = new File("Mensajes");
	private static JsonTransformer jsonTransformer = new JsonTransformer();

	public static boolean verConectividad(List<Peer> peers) {

		for (Peer p : peers) {
			if (!verConectividad(p)) {
				return false;
			}
		}
		return true;
	}

	public static boolean verConectividad(Peer peer) {
		Mensaje mensaje = new Mensaje();
		mensaje.setTipo(TipoMensaje.CHECK_CONNECTIVITY);
		try {
			PostGrupo.post(peer.getDireccion(), mensaje);
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

	public static void notificarGrupo(List<Grupo> grupos, Mensaje mensaje) {

		for (Grupo g : grupos)
			notificarUnGrupo(g, mensaje);
	}

	public static void notificarUnGrupo(Grupo grupo, Mensaje mensaje) {

		for (Peer p : grupo.getPeers()) {
			try {
				PostGrupo.post("http://" + p.getDireccion(), mensaje);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				MensajesPendientes m;
				try {
					m = mensajeDao.cargar(file);
				} catch (JAXBException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
					m = new MensajesPendientes();
				}
				// Guardo en data el peer al que no se pudo notificar, junto a
				// la hora del suceso
				Date date = new Date();
				DateFormat hourdateFormat = new SimpleDateFormat("HH:mm:ss dd/MM/yyyy");

				MensajePendienteData data = new MensajePendienteData();
				data.setPeer(p.getDireccion());
				data.setFecha(hourdateFormat.format(date));
				data.setTipo(mensaje.getTipo());
				Mensaje nuevo = new Mensaje();
				nuevo.setTipo(mensaje.getTipo());
				nuevo.setDescripcion(mensaje.getDescripcion());
				nuevo.setOrigen(mensaje.getOrigen());

				nuevo.setDatos(jsonTransformer.render(data));

				m.addMensaje(nuevo);
				mensajeDao.guardar(m, file);
			}

		}
	}
	
	public static boolean enviarGrupo(Peer peer, Grupo grupo, String origen){
		Mensaje mensaje = new Mensaje();
		GrupoPeer data = new GrupoPeer();
		data.setGrupo(grupo.getNombre());
		data.setListaPeersByPeer(grupo.getPeers());
		mensaje.setDescripcion("Nuevo grupo");
		mensaje.setTipo(TipoMensaje.NUEVO_GRUPO);
		mensaje.setOrigen(origen);
		mensaje.setDatos(jsonTransformer.render(data));
		try {
			PostGrupo.post(peer.getDireccion(), mensaje);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
		return true;
	}

}
