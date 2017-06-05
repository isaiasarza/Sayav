package SAYAV2.SAYAV2.notificacion;

import java.io.File;
import java.util.Date;
import java.util.List;

import javax.xml.bind.JAXBException;

import Datos.DatoGrupo;
import Datos.DatoVoto;
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
	private static GruposImpl grupos;
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
		this.votacionesDao.setFile(votacionesFile);
		this.tiposMensajeDao = TipoMensajeDao.getInstance();
	}
	
	public static GruposImpl getInstance(){
		if(grupos == null){
			grupos = new GruposImpl();
		}
		return grupos;
	}

	private void notificarNuevoMiembro(Grupo grupo, Peer miembro, String origen) {
		DatoGrupo datos = new DatoGrupo(miembro, grupo);

		Mensaje mensaje = new Mensaje();
		mensaje.setOrigen(origen);
		mensaje.setDestino(miembro.getDireccion());
		mensaje.setEstado(EstadoUtils.PENDIENTE);
		mensaje.setTipoMensaje(mensajeria.getTipos().getTipo(TipoMensajeUtils.NUEVO_MIEMBRO));
		mensaje.setFechaCreacion(new Date());
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
		mensaje.setFechaCreacion(new Date());
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
	public void procesarBajaMiembro(Votacion votacion) {
		double minVotantes = votacion.getGrupo().getPeers().size();
		minVotantes /= 2;
		minVotantes = Math.ceil(minVotantes);
		if (votacion.getVotantesAFavor() + votacion.getVotantesEnContra() >= minVotantes) {
			votacion.setFinalizo(true);
			Usuario usuario;
			try {
				usuario = usuarioDao.cargar(usuarioFile);

				if (votacion.getVotantesAFavor() >= minVotantes) {
					Mensaje mensaje = new Mensaje();
					mensaje.setDescripcion("Se voto dar de baja al miembro");
					mensaje.setTipoMensaje(tiposMensajeDao.cargar(tiposFile).getTipo(TipoMensajeUtils.BAJA_MIEMBRO));
					mensaje.setOrigen(usuario.getSubdominio());
					mensaje.setEstado(EstadoUtils.PENDIENTE);
					usuarioDao.eliminarMiembro(usuario.getSingleGrupoById(votacion.getGrupo().getId()), votacion.getMiembro());
					notificarGrupo(votacion.getGrupo(), mensaje);
				}
				votacionesDao.eliminarVotacion(votacion);
			} catch (JAXBException e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	public void notificarGrupo(Grupo grupo, Mensaje msg) {
		mensajeria.propagarMensaje(msg, grupo);
	}

	@Override
	public void notificarMovil(DispositivoM movil, Mensaje msg) {
		// TODO Auto-generated method stub

	}

	@Override
	public void notificarGrupos(List<Grupo> grupos, Mensaje msg) {
		for(Grupo g: grupos){
			notificarGrupo(g,msg);
		}
	}

	@Override
	public void notificarMoviles(List<DispositivoM> moviles, Mensaje msg) {
		// TODO Auto-generated method stub

	}

	public void recibirAlerta(Mensaje msg) throws JAXBException {

		Usuario usuario = usuarioDao.cargar(usuarioFile);

		notificarMoviles(usuario.getDispositivosMoviles(), msg);

	}

	public void recibirNuevoMiembro(Mensaje msg) throws JAXBException {

		Usuario usuario = usuarioDao.cargar(usuarioFile);

		DatoGrupo datos = json.getGson().fromJson(msg.getDatos(), DatoGrupo.class);

		// Cambie esto porque no se estaba guardando, entonces lo hago adentro del dao y que se guarde ahi
		usuarioDao.agregarMiembro(usuario.getSingleGrupoById(datos.getGrupo().getId()), datos.getMiembro());
	}

	public void recibirNuevoGrupo(Mensaje msg) throws JAXBException {

		DatoGrupo datos = json.getGson().fromJson(msg.getDatos(), DatoGrupo.class);	
		usuarioDao.agregarGrupo(datos.getGrupo());
	}

	public void recibirBajaMiembro(Mensaje msg) throws JAXBException {

		Usuario usuario = usuarioDao.cargar(usuarioFile);
		DatoGrupo datos = json.getGson().fromJson(msg.getDatos(), DatoGrupo.class);
		usuarioDao.eliminarMiembro(usuario.getSingleGrupoById(datos.getGrupo().getId()), datos.getMiembro());

	}

	public void recibirSolicitudBaja(Mensaje msg) throws JAXBException {

		Usuario usuario = usuarioDao.cargar(usuarioFile);

		notificarMoviles(usuario.getDispositivosMoviles(), msg);

	}

	public void recibirVoto(Mensaje msg) throws JAXBException {


		DatoVoto datos = json.getGson().fromJson(msg.getDatos(), DatoVoto.class);
		Votacion votacion = votacionesDao.cargar(votacionesFile).getVotacion(datos.getGrupo(), datos.getMiembro());

		if (datos.isVoto()) {
			votacion.setVotantesAFavor(votacion.getVotantesAFavor() + 1);
		} else {
			votacion.setVotantesEnContra(votacion.getVotantesEnContra() + 1);
		}

		procesarBajaMiembro(votacion);

	}

}
