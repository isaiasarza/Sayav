package SAYAV2.SAYAV2.notificacion;

import java.io.File;
import java.util.Date;

import javax.xml.bind.JAXBException;

import Datos.DatoGrupo;
import SAYAV2.SAYAV2.Utils.EstadoUtils;
import SAYAV2.SAYAV2.Utils.TipoMensajeUtils;
import SAYAV2.SAYAV2.dao.UsuarioDao;
import SAYAV2.SAYAV2.mensajeria.Mensaje;
import SAYAV2.SAYAV2.mensajeria.MensajeriaImpl;
import SAYAV2.SAYAV2.model.Grupo;
import SAYAV2.SAYAV2.model.Peer;
import SAYAV2.SAYAV2.model.Usuario;
import SAYAV2.SAYAV2.service.JsonTransformer;

public class GruposImpl implements Grupos {
	
	private MensajeriaImpl mensajeria;
	private UsuarioDao usuarioDao;
	private File usuarioFile;
	private JsonTransformer json;
	
	public GruposImpl() {
		super();
		mensajeria = MensajeriaImpl.getInstance();
		usuarioDao = UsuarioDao.getInstance();
		usuarioFile = new File("SAYAV");
		json = new JsonTransformer();
	}
	
	private void notificarNuevoMiembro(Grupo grupo, Peer miembro, String origen) {
		DatoGrupo datos = new DatoGrupo(miembro,grupo);

		Mensaje mensaje = new Mensaje();
		mensaje.setOrigen(origen);
		mensaje.setDestino(miembro.getDireccion());
		mensaje.setEstado(EstadoUtils.PENDIENTE);
		mensaje.setTipoMensaje(mensajeria.getTipos().getTipo(TipoMensajeUtils.NUEVO_MIEMBRO));
		mensaje.setFecha(new Date());
		mensaje.setDescripcion("Usted es parte de un nuevo grupo");
		mensaje.setDatos(json.render(datos));
		
		mensajeria.propagarMensaje(mensaje, grupo);
	}

	private void notificarNuevoGrupo(Grupo grupo, Peer miembro, String origen) {
		Mensaje mensaje = new Mensaje();
		DatoGrupo datos = new DatoGrupo(miembro,grupo);
		
		mensaje.setOrigen(origen);
		mensaje.setDestino(miembro.getDireccion());
		mensaje.setEstado(EstadoUtils.PENDIENTE);
		mensaje.setTipoMensaje(mensajeria.getTipos().getTipo(TipoMensajeUtils.NUEVO_GRUPO));
		mensaje.setFecha(new Date());
		mensaje.setDescripcion("Usted es parte de un nuevo grupo");
		mensaje.setDatos(json.render(datos));
		mensajeria.enviarSolicitud(mensaje);		
	}

	@Override
	public void añadirMiembro(Grupo grupo, Peer miembro) {
		Usuario usuario;
		try {
			usuario = usuarioDao.cargar(usuarioFile);
			notificarNuevoGrupo(grupo,miembro,usuario.getSubdominio());
			notificarNuevoMiembro(grupo,miembro,usuario.getSubdominio());
		} catch (JAXBException e) {
			e.printStackTrace();
		}


	}

	@Override
	public void verMiembros(Grupo grupo) {
		// TODO Auto-generated method stub

	}

	@Override
	public void solicitarBajaMiembro(Grupo grupo, Peer miembro) {
		System.out.println("Solicitando Baja de " + miembro);
		// TODO Auto-generated method stub

	}

	@Override
	public void abandonarGrupo(Grupo grupo, Peer miembro) {
		// TODO Auto-generated method stub

	}

	@Override
	public void aceptarBajaMiembro(Grupo grupo, Peer miembro) {
		// TODO Auto-generated method stub

	}

	@Override
	public void rechazarBajaMiembro(Grupo grupo, Peer miembro) {
		// TODO Auto-generated method stub

	}

	@Override
	public void procesarBajaMiembro(Grupo grupo, Peer miembro) {
		// TODO Auto-generated method stub

	}

}
