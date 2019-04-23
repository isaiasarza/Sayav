package SAYAV2.SAYAV2.notificacion;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.xml.bind.JAXBException;

import Datos.DatoGrupo;
import Datos.DatoVoto;
import SAYAV2.SAYAV2.Utils.EstadoUtils;
import SAYAV2.SAYAV2.Utils.FileUtils;
import SAYAV2.SAYAV2.Utils.TipoMensajeUtils;
import SAYAV2.SAYAV2.dao.ConfiguratorDao;
import SAYAV2.SAYAV2.dao.TipoMensajeDao;
import SAYAV2.SAYAV2.dao.UsuarioDao;
import SAYAV2.SAYAV2.dao.VotacionesDao;
import SAYAV2.SAYAV2.mensajeria.Mensaje;
import SAYAV2.SAYAV2.mensajeria.MensajeriaImpl;
import SAYAV2.SAYAV2.model.Configurator;
import SAYAV2.SAYAV2.model.DispositivoM;
import SAYAV2.SAYAV2.model.Grupo;
import SAYAV2.SAYAV2.model.Notificacion;
import SAYAV2.SAYAV2.model.Peer;
import SAYAV2.SAYAV2.model.TiposMensajes;
import SAYAV2.SAYAV2.model.Usuario;
import SAYAV2.SAYAV2.service.FirebaseCloudMessageController;
import SAYAV2.SAYAV2.service.JsonTransformer;


public class GruposImpl implements Grupos, NotificacionesApi {
	private static GruposImpl grupos;
	private boolean init;
	private MensajeriaImpl mensajeria;
	private UsuarioDao usuarioDao;
	private ConfiguratorDao configuratorDao;
	private Configurator configurator;
	private JsonTransformer json;
	@SuppressWarnings("unused")
	private Votaciones votaciones;
	private Votaciones votacionesPendientes;
	private VotacionesDao votacionesDao;
	private TipoMensajeDao tiposMensajeDao;
	private TiposMensajes tipos;
 

	public GruposImpl() {
		super();
		this.json = new JsonTransformer();
		this.mensajeria = MensajeriaImpl.getInstance();
		this.configuratorDao = ConfiguratorDao.getInstance();
		this.usuarioDao = UsuarioDao.getInstance();
		this.tiposMensajeDao = TipoMensajeDao.getInstance();
		this.votacionesDao = VotacionesDao.getInstance();
		this.init = false;
		try {
			this.configurator = configuratorDao.cargar(FileUtils.CONFIGURATOR_FILE);
			this.votaciones = votacionesDao.cargar(FileUtils.VOTACIONES_FILE);
			this.votacionesPendientes = votacionesDao.cargar(FileUtils.VOTACIONES_PENDIENTES_FILE);
			this.tipos = tiposMensajeDao.cargar(FileUtils.TIPOS_MENSAJES_FILE);
		} catch (JAXBException e) {
			e.printStackTrace();
			this.votaciones = new Votaciones();
			this.votacionesPendientes = new Votaciones();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static GruposImpl getInstance() {
		if (grupos == null) {
			grupos = new GruposImpl();
		}
		return grupos;
	}

	public void init() {
		this.init = true;
		this.mensajeria.init();
	}

	private void notificarNuevoMiembro(Grupo grupo, Peer miembro, Peer origen) throws Exception {
	
		DatoGrupo datos = new DatoGrupo(miembro, grupo);
		
		System.out.println();
		System.out.println("4. Notificar Nuevo Miembro, " + datos.getMiembro().getDireccion()+ ":" + datos.getMiembro().getPuerto());
		System.out.println();

		Mensaje mensaje = new Mensaje();
		mensaje.setOrigen(origen);
		mensaje.setEstado(EstadoUtils.Estado.PENDIENTE);
		mensaje.setTipoMensaje(tipos.getTipo(TipoMensajeUtils.NUEVO_MIEMBRO));

		//mensaje.setTipoMensaje(tiposMensajeDao.getTipo(TipoMensajeUtils.NUEVO_MIEMBRO, FileUtils.TIPOS_MENSAJES_FILE));
		mensaje.setTipoHandshake(TipoMensajeUtils.HANDSHAKE_REQUEST);
		mensaje.setDescripcion("El miembro " + miembro.getDireccion() + " es parte del grupo " + grupo.getNombre() + ":");
		mensaje.setDatos(json.render(datos));
		//mensajeria.propagarMensaje(mensaje, grupo);
		List<Mensaje> mensajes = generarMensajes(grupo, mensaje);
		mensajeria.propagarMensaje(mensajes);
	}

	private boolean notificarNuevoGrupo(Grupo grupo, Peer miembro, Peer origen) throws Exception {
		//grupo.add(origen);
		Mensaje mensaje = new Mensaje();
		DatoGrupo datos = new DatoGrupo(miembro, grupo);
		mensaje.setOrigen(origen);
		mensaje.setDestino(miembro);
		mensaje.setEstado(EstadoUtils.Estado.PENDIENTE);
		mensaje.setTipoHandshake(TipoMensajeUtils.HANDSHAKE_REQUEST);
		mensaje.setTipoMensaje(tipos.getTipo(TipoMensajeUtils.NUEVO_GRUPO));
		mensaje.setFechaCreacion(new Date());
		mensaje.setDescripcion("Usted es parte del grupo " + grupo.getNombre() + ":");
		mensaje.setDatos(json.render(datos));

		if (mensajeria.exist(datos, mensaje.getTipoMensaje(), mensaje.getTipoHandshake(), mensaje.getEstado())) {
			//grupo.removePeer(origen);
			return false;
		}
		mensajeria.enviarSolicitud(mensaje);
		//grupo.removePeer(origen);
		return true;
	}

	@Override
	public boolean añadirMiembro(Grupo grupo, Peer miembro) throws Exception {

		Usuario usuario;
		try {
			usuario = usuarioDao.cargar();
			Peer origen = new Peer(usuario.getSubdominio(),configurator.getPort());
		    if (notificarNuevoGrupo(grupo, miembro, origen)) {
				return true;
			}
		} catch (JAXBException e) {
			e.printStackTrace();
		}
		return false;
	}

	@Override
	public void verMiembros(Grupo grupo) {

	}

	@Override
	public void solicitarBajaMiembro(Grupo grupo, Peer miembro) throws Exception {
		System.out.println("Solicitando Baja de " + miembro);
		Usuario usuario;
		try {
			usuario = usuarioDao.cargar();
			Peer solicitante = new Peer(usuario.getSubdominio(), configurator.getPort());
			Votacion votacion = new Votacion(miembro, grupo.getId());
			votacion.setVotantesAFavor(1);
			this.votaciones = this.votacionesDao.agregarVotacion(votacion, FileUtils.VOTACIONES_FILE);
			votacion.setSolicitante(solicitante);
			votacion.setVotantesAFavor(1);
			Mensaje msg = new Mensaje();
			grupo.removePeer(miembro);
			msg.setDatos(json.render(votacion));
			msg.setTipoMensaje(tipos.getTipo(TipoMensajeUtils.SOLICITUD_BAJA_MIEMBRO));

			//msg.setTipoMensaje(tiposMensajeDao.getTipo(TipoMensajeUtils.SOLICITUD_BAJA_MIEMBRO,FileUtils.TIPOS_MENSAJES_FILE));
			msg.setDescripcion("Se ha solicitado la baja de un miembro");
			msg.setEstado(EstadoUtils.Estado.PENDIENTE);
			msg.setTipoHandshake(TipoMensajeUtils.HANDSHAKE_REQUEST);
			msg.setOrigen(solicitante);
			notificarGrupo(grupo, msg);
			grupo.add(miembro);
			notificarMoviles(usuario.getDispositivosMoviles(), msg);

		} catch (JAXBException e1) {
			e1.printStackTrace();
		}

	}

	@Override
	public void abandonarGrupo(Grupo grupo) throws Exception {

		Usuario usuario = usuarioDao.cargar();

		// Creo un peer para mandarlo como datos
		Peer miembro = new Peer(usuario.getSubdominio(),configurator.getPort());
		DatoGrupo datos = new DatoGrupo(miembro, grupo);

		// Creo el mensaje con los datos correspondientes a un abandonar grupo
		Mensaje mensaje = new Mensaje();
		mensaje.setOrigen(miembro);
		mensaje.setTipoMensaje(tipos.getTipo(TipoMensajeUtils.BAJA_MIEMBRO));

		mensaje.setTipoHandshake(TipoMensajeUtils.HANDSHAKE_REQUEST);
		mensaje.setDescripcion("El miembro abandono el grupo el miembro");
		mensaje.setDatos(json.render(datos));

		// Propago el mensaje para informar a todos los miembros sobre la baja
		// de este miembro
		List<Mensaje> mensajes = generarMensajes(grupo, mensaje);
		mensajeria.propagarMensaje(mensajes);

		// Elimino el grupo de mi lista de grupos
		usuarioDao.eliminarGrupo(grupo);
	}

	@Override
	public void aceptarBajaMiembro(Votacion votacion) throws Exception {
		// Voto positivo
		votacion.setVotantesAFavor(votacion.getVotantesAFavor() + 1);

		Usuario usuario = usuarioDao.cargar();
		Peer votante = new Peer(usuario.getSubdominio(),configurator.getPort());

		// Creo un peer para mandarlo como datos
		DatoVoto datos = new DatoVoto(votacion.getId(), true);

		// Creo el mensaje con los datos correspondientes a un voto a favor
		//System.out.println(datos);
		Mensaje mensaje = new Mensaje();
		mensaje.setOrigen(votante);
		mensaje.setTipoMensaje(tipos.getTipo(TipoMensajeUtils.VOTO));

		//mensaje.setTipoMensaje(tiposMensajeDao.getTipo(TipoMensajeUtils.VOTO,FileUtils.TIPOS_MENSAJES_FILE));
		mensaje.setDescripcion("Voto el miembro");
		//mensaje.setDestino(votacion.getSolicitante());
		mensaje.setDatos(json.render(datos));
		mensaje.setTipoHandshake(TipoMensajeUtils.HANDSHAKE_REQUEST);
		mensaje.setDestino(votacion.getSolicitante());
		// Envio mi voto
		mensajeria.enviarSolicitud(mensaje);
	}

	@Override
	public void rechazarBajaMiembro(Votacion votacion) throws Exception {
		// Voto negativo
		votacion.setVotantesEnContra(votacion.getVotantesEnContra() + 1);

		Usuario usuario = usuarioDao.cargar();		
		Peer votante = new Peer(usuario.getSubdominio(),configurator.getPort());
	

		// Creo un peer para mandarlo como datos
		DatoVoto datos = new DatoVoto(votacion.getId(), false);

		// Creo el mensaje con los datos correspondientes a un voto a favor
		Mensaje mensaje = new Mensaje();
		mensaje.setOrigen(votante);
		mensaje.setTipoMensaje(tipos.getTipo(TipoMensajeUtils.VOTO));

		//mensaje.setTipoMensaje(tiposMensajeDao.getTipo(TipoMensajeUtils.VOTO,FileUtils.TIPOS_MENSAJES_FILE));
		mensaje.setDescripcion("Voto el miembro");
		//mensaje.setDestino(votacion.getSolicitante());
		mensaje.setDatos(json.render(datos));
		mensaje.setTipoHandshake(TipoMensajeUtils.HANDSHAKE_REQUEST);
		mensaje.setDestino(votacion.getSolicitante());

		// Envio mi voto
		mensajeria.enviarSolicitud(mensaje);

	}

	@Override
	public void procesarBajaMiembro(Votacion votacion) throws Exception {
		Grupo grupo = usuarioDao.cargar().getSingleGrupoById(votacion.grupoId);
		double minVotantes = grupo.getPeers().size();
		minVotantes /= 2;
		minVotantes = Math.ceil(minVotantes);
		if (votacion.getVotantesEnContra() >= minVotantes) {
			votacion.setFinalizo(true);
			return;
		}
		try {
			if (votacion.getVotantesAFavor() >= minVotantes) {
				votacion.setFinalizo(true);
				bajaMiembro(grupo, votacion.getMiembro());
			}
		} catch (JAXBException e) {
			e.printStackTrace();
		}

	}

	private void bajaMiembro(Grupo grupo, Peer miembro) throws Exception {
		Usuario usuario = usuarioDao.cargar();
		Peer votante = new Peer(usuario.getSubdominio(), configurator.getPort());
		//Peer eliminado;
		Mensaje mensaje = new Mensaje();
		Grupo g = usuario.getSingleGrupoById(grupo.getId());
		DatoGrupo datos = new DatoGrupo(miembro, g);
		mensaje.setDatos(json.render(datos));
		mensaje.setDescripcion("Se voto dar de baja al miembro");
		//mensaje.setTipoMensaje(tipos.getTipo(TipoMensajeUtils.BAJA_MIEMBRO));

		//mensaje.setTipoMensaje(tiposMensajeDao.getTipo(TipoMensajeUtils.BAJA_MIEMBRO,FileUtils.TIPOS_MENSAJES_FILE));
		mensaje.setOrigen(votante);
		mensaje.setTipoHandshake(TipoMensajeUtils.HANDSHAKE_REQUEST);
		mensaje.setEstado(EstadoUtils.Estado.PENDIENTE);
		notificarBajaGrupo(mensaje, miembro);
		notificarBajaMiembro(g, mensaje, miembro);
		
	}

	private void notificarBajaGrupo(Mensaje mensaje, Peer eliminado) throws Exception {
		System.out.println("Avisando al miembro que fue eliminado del grupo " + eliminado);
		System.out.println("Datos" + mensaje.getDatos());
		mensaje.setDestino(eliminado);
		mensaje.setDescripcion("Usted ha sido dado de baja");
		mensaje.setTipoMensaje(tipos.getTipo(TipoMensajeUtils.BAJA_GRUPO));
		mensajeria.enviarSolicitud(mensaje);
	}

	private Peer notificarBajaMiembro(Grupo g, Mensaje mensaje, Peer miembro) throws Exception {
		System.out.println("Avisando a los miembros que eliminen al eliminado" + miembro);
		System.out.println("Datos" + mensaje.getDatos());
		Peer eliminado = usuarioDao.eliminarMiembro(g, miembro);
		//g.removePeer(miembro);
		mensaje.setTipoMensaje(tipos.getTipo(TipoMensajeUtils.BAJA_MIEMBRO));
		notificarGrupo(g, mensaje);
		return eliminado;
	}

	@Override
	public void notificarGrupo(Grupo grupo, Mensaje msg) throws Exception {
		//mensajeria.propagarMensaje(msg, grupo);
		List<Mensaje> mensajes = generarMensajes(grupo, msg);
		mensajeria.propagarMensaje(mensajes);
	}

	@Override
	public void notificarMovil(DispositivoM movil, Mensaje msg) {
		// TODO Auto-generated method stub

	}

	@Override
	public void notificarGrupos(List<Grupo> grupos, Mensaje msg) throws Exception {

		for (Grupo g : grupos) {
			notificarGrupo(g, msg);
		}
	}

	@Override
	public void notificarMoviles(List<DispositivoM> moviles, Mensaje msg) throws JAXBException {
	
		String fecha = msg.imprimirFechaCreacion()+ " " + SimpleDateFormat.AM_PM_FIELD;
		msg.setDescripcion(msg.getDescripcion() + " " + fecha);
		if(!FirebaseCloudMessageController.post(msg.getTipoMensaje().getTipo(), msg.getDescripcion())){
			System.out.println("Guardando Notificacion Push...");
			Mensaje m = msg.clone();
			m.setId(m.generateId());
			m.setEstado(EstadoUtils.Estado.PENDIENTE);
			m.setTipoMensaje(tipos.getTipo(TipoMensajeUtils.NOTIFICACION_MOVIL));

			//m.setTipoMensaje(tiposMensajeDao.getTipo(TipoMensajeUtils.NOTIFICACION_MOVIL,FileUtils.TIPOS_MENSAJES_FILE));
			mensajeria.guardarMensaje(m);
		}

	}

	public Notificacion recibirAlerta(Mensaje msg) throws JAXBException, IOException {
		Notificacion notificacion = new Notificacion();
		Usuario usuario = usuarioDao.cargar();

		notificarMoviles(usuario.getDispositivosMoviles(), msg);
		
		notificacion.setTipo(msg.getTipoMensaje().getTipo());
		notificacion.setDescripcion("Se ha activado la alarma al miembro " + msg.getOrigen());
		notificacion.setDetalle(msg.getDescripcion());
		return notificacion;

	}

	public Notificacion recibirNuevoMiembro(Mensaje msg) throws Exception {
		Notificacion notificacion = new Notificacion();
		Usuario usuario = usuarioDao.cargar();
		DatoGrupo datos = json.getGson().fromJson(msg.getDatos(), DatoGrupo.class);
		System.out.println();
		System.out.println("8. Recibir Nuevo Miembro, " + datos.getMiembro().getDireccion()+ ":" + datos.getMiembro().getPuerto());
		System.out.println();
		if(datos.getMiembro().getDireccion().equals(usuario.getSubdominio())) {
			throw new Exception();
		}
		if(!usuarioDao.agregarMiembro(usuario.getSingleGrupoById(datos.getGrupo().getId()), datos.getMiembro())) {
			return null;
		}
		notificacion.setTipo(msg.getTipoMensaje().getTipo());
		notificacion.setDescripcion("El miembro " + datos.getMiembro().getDireccion()
				+ " es parte del grupo " + datos.getGrupo().getNombre());
		notificacion.setDetalle("Fue agregado por el miembro " + msg.getOrigen());
		return notificacion;
	}

	public Notificacion recibirNuevoGrupo(Mensaje msg) throws JAXBException {
		//System.out.println("Recibir Nuevo Grupo");
		Notificacion notificacion = new Notificacion();
		//System.out.println(msg.getDatos());
		DatoGrupo datos = json.getGson().fromJson(msg.getDatos(), DatoGrupo.class);
		//System.out.println("Datos Grupo: " + datos);
		//System.out.println("Tipo Mensaje: " + msg.getTipoMensaje().getTipo());
		notificacion.setTipo(msg.getTipoMensaje().getTipo());
		notificacion.setDescripcion("Fue agregado al grupo " + datos.getGrupo().getNombre());
		notificacion.setDetalle("Fue agregado por el miembro " + msg.getOrigen().getDireccion());
		datos.getGrupo().add(msg.getOrigen());
		if(!usuarioDao.agregarGrupo(datos.getGrupo()))
			return null;
		return notificacion;
	}

	public Notificacion recibirBajaMiembro(Mensaje msg) throws JAXBException {
		
		Notificacion notificacion = new Notificacion();
		DatoGrupo datos = json.getGson().fromJson(msg.getDatos(), DatoGrupo.class);
		if(usuarioDao.eliminarMiembro(datos.getGrupo(), datos.getMiembro()) == null)
			return null;
		notificacion.setTipo(msg.getTipoMensaje().getTipo());
		notificacion.setDescripcion("El miembro " + datos.getMiembro().getDireccion()
				+ " dejo de ser parte del grupo " + datos.getGrupo().getNombre());
		if (msg.getOrigen().equals(datos.getMiembro())) {
			notificacion.setDetalle("El miembro abandono el grupo por propia voluntad.");
		} else {
			notificacion.setDetalle("El miembro fue dado de baja por acuerdo común en el grupo.");
		}
		return notificacion;
	}

	public Notificacion recibirSolicitudBaja(Mensaje msg) throws JAXBException, IOException {
		Notificacion notificacion = new Notificacion();
		Usuario usuario = usuarioDao.cargar();
		System.out.println("Datos solicitud baja");
		Votacion votacion = json.getGson().fromJson(msg.getDatos(), Votacion.class);
		System.out.println(votacionesPendientes);
		System.out.println(votacion);
		votacionesPendientes = votacionesDao.agregarVotacion(votacion, FileUtils.VOTACIONES_PENDIENTES_FILE);

		notificacion.setTipo(msg.getTipoMensaje().getTipo());
		notificacion.setDescripcion("Se ha solicitado la baja del miembro " + votacion.getMiembro().getDireccion());
		notificacion.setDetalle(
				"Para determinar si el miembro sera o no dado de baja, usted debera dirigirse al menu de votacion para votar:"
						+ "Si la mitad + 1 de los votantes estan de acuerdo, el miembro sera dado de baja");
		
		System.out.println(usuario.getDispositivosMoviles());
	
		notificarMoviles(usuario.getDispositivosMoviles(), msg);
		return notificacion;
	}

	public Notificacion recibirVoto(Mensaje msg) throws Exception {

		DatoVoto datos = json.getGson().fromJson(msg.getDatos(), DatoVoto.class);
		Votacion votacion = votacionesDao.getVotacion(datos.getIdVotacion(), FileUtils.VOTACIONES_FILE);
		Peer votante = new Peer(msg.getOrigen().getDireccion(),msg.getOrigen().getPuerto());
		if(votacion.getVotantes().contains(votante)){
			return null;
		}
		Notificacion notificacion = new Notificacion();
		notificacion.setTipo(TipoMensajeUtils.VOTO);
		notificacion.setDescripcion("Ha recibido un voto del miembro " + msg.getOrigen());
		
		votacion.getVotantes().add(votante);
		if (datos.isVoto()) {
			notificacion.setDetalle("El voto fue positivo");
			votacion.setVotantesAFavor(votacion.getVotantesAFavor() + 1);
		} else {
			notificacion.setDetalle("El voto fue negativo");
			votacion.setVotantesEnContra(votacion.getVotantesEnContra() + 1);
		}
		if(!votacionesDao.actualizarVotacion(votacion,FileUtils.VOTACIONES_FILE)) {
			return null;
		}
		procesarBajaMiembro(votacion);
		return notificacion;

	}

	public boolean isInit() {
		return init;
	}

	public void setInit(boolean init) {
		this.init = init;
	}

	public Notificacion confirmarAñadirMiembro(Mensaje msg) throws JAXBException {
		Notificacion notificacion = new Notificacion();
		DatoGrupo datos = json.getGson().fromJson(msg.getDatos(), DatoGrupo.class);
		System.out.println();
		System.out.println("3. Confirmar Añadir Miembro, " + datos.getMiembro().getDireccion()+ ":" + datos.getMiembro().getPuerto());
		System.out.println();
		Peer origen = new Peer(usuarioDao.getSubdominio(),configurator.getPort());
		Grupo grupo = usuarioDao.getGrupo(datos.getGrupo().getId());
		try {
			notificarNuevoMiembro(grupo, datos.getMiembro(), origen);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		
		this.usuarioDao.agregarMiembro(grupo, datos.getMiembro());
		notificacion.setTipo(msg.getTipoMensaje().getTipo());
		notificacion.setDescripcion("El miembro " + datos.getMiembro().getDireccion() + " es parte del grupo "
				+ datos.getGrupo().getNombre());
		notificacion.setDetalle("El grupo ahora contiene " + datos.getGrupo().getPeers().size() + " miembros");
		return notificacion;
	}

	private List<Mensaje> generarMensajes(Grupo g, Mensaje msg){
		List<Mensaje> mensajes = new ArrayList<Mensaje>();
		DatoGrupo datos = json.getGson().fromJson(msg.getDatos(), DatoGrupo.class);
		Mensaje mensaje;

		for(Peer destino: g.getPeers()) {
			if(msg.getTipoMensaje().getTipo().equals(TipoMensajeUtils.NUEVO_MIEMBRO) && datos.getMiembro().getDireccion().equals(destino.getDireccion()))
				continue;
			mensaje = msg.clone();
			mensaje.setDestino(destino);
			mensajes.add(mensaje);
		}
		
		return mensajes;
	}

	public Notificacion recibirBajaGrupo(Mensaje msg) throws Exception {
		DatoGrupo datos = json.getGson().fromJson(msg.getDatos(), DatoGrupo.class);
		System.out.println("Eliminando el grupo " + datos.getGrupo().getNombre() +" donde usted fue dado de baja");
		if(!usuarioDao.eliminarGrupo(datos.getGrupo()))
			return null;
		Notificacion notificacion = new Notificacion();
		
		notificacion.setTipo(msg.getTipoMensaje().getTipo());
		notificacion.setDescripcion("Eliminando el grupo " + datos.getGrupo().getNombre());
		notificacion.setDetalle("Fue dado de baja");
		return notificacion;
	}
}
