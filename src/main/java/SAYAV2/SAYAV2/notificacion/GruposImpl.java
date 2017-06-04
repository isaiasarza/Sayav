package SAYAV2.SAYAV2.notificacion;

import java.io.File;
import java.util.Date;
import java.util.List;

import javax.xml.bind.JAXBException;

import Datos.DatoGrupo;
import SAYAV2.SAYAV2.Utils.EstadoUtils;
import SAYAV2.SAYAV2.Utils.TipoMensajeUtils;
import SAYAV2.SAYAV2.dao.TipoMensajeDao;
import SAYAV2.SAYAV2.dao.UsuarioDao;
import SAYAV2.SAYAV2.dao.VotacionesDao;
import SAYAV2.SAYAV2.mensajeria.Mensaje;
import SAYAV2.SAYAV2.mensajeria.MensajeriaImpl;
import SAYAV2.SAYAV2.model.DispositivoM;
import SAYAV2.SAYAV2.model.Grupo;
import SAYAV2.SAYAV2.model.Peer;
import SAYAV2.SAYAV2.model.Usuario;
import SAYAV2.SAYAV2.service.JsonTransformer;

public class GruposImpl implements Grupos, Notificaciones {

	private MensajeriaImpl mensajeria;
	private UsuarioDao usuarioDao;
	private File usuarioFile;
	private File votacionesFile;
	private File tiposFile;
	private JsonTransformer json;
	private Votaciones votaciones;
	private VotacionesDao votacionesDao;
	private TipoMensajeDao tiposMensajeDao;

	public GruposImpl() {
		super();
		this.mensajeria = MensajeriaImpl.getInstance();
		this.usuarioDao = UsuarioDao.getInstance();
		this.usuarioFile = new File("SAYAV");
		this.json = new JsonTransformer();
		this.votaciones = new Votaciones();
		this.votacionesFile = new File("votaciones");
		this.votacionesDao = VotacionesDao.getInstance();
		this.tiposMensajeDao = TipoMensajeDao.getInstance();
	}

	private void notificarNuevoMiembro(Grupo grupo, Peer miembro, String origen) {
		DatoGrupo datos = new DatoGrupo(miembro, grupo);

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
		DatoGrupo datos = new DatoGrupo(miembro, grupo);

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
			notificarNuevoGrupo(grupo, miembro, usuario.getSubdominio());
			notificarNuevoMiembro(grupo, miembro, usuario.getSubdominio());
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
		Usuario usuario;
		try {
			usuario = usuarioDao.cargar(usuarioFile);
			Votacion votacion = new Votacion(miembro, grupo);
			this.votaciones.addVotacion(votacion);
			votacionesDao.guardar(votaciones, votacionesFile);
			Mensaje msg = new Mensaje();

			msg.setTipoMensaje(tiposMensajeDao.cargar(tiposFile).getTipo(TipoMensajeUtils.VOTO));
			msg.setDescripcion("Se ha solicitado la baja de un miembro");
			msg.setEstado(EstadoUtils.PENDIENTE);
			msg.setOrigen(usuario.getSubdominio());
			notificarGrupo(grupo, msg);
			notificarMoviles(usuario.getDispositivosMoviles(), msg);

		} catch (JAXBException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
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

	public Votaciones getVotaciones() {
		return votaciones;
	}

	public void setVotaciones(Votaciones votaciones) {
		this.votaciones = votaciones;
	}

	@Override
	public void notificarGrupo(Grupo grupo, Mensaje msg) {
		// TODO Auto-generated method stub
		this.mensajeria.propagarMensaje(msg, grupo);
	}

	@Override
	public void notificarMovil(DispositivoM movil, Mensaje msg) {
		// TODO Auto-generated method stub

	}

	@Override
	public void notificarGrupos(List<Grupo> grupos, Mensaje msg) {
		// TODO Auto-generated method stub

	}

	@Override
	public void notificarMoviles(List<DispositivoM> moviles, Mensaje msg) {
		// TODO Auto-generated method stub

	}

}
