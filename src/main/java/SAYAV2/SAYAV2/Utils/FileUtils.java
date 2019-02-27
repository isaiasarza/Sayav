package SAYAV2.SAYAV2.Utils;

import java.net.URL;

public class FileUtils {
	//public static final PathResolver pathResolver = PathResolver.getInstance();
	public static final String CONFIGURATOR_FILE = "/resources/files/configurator";
	public static final String USUARIO_FILE = "/resources/files/SAYAV";
	public static final String NOTIFICACIONES_FILE = "/resources/files/notificaciones";
	public static final String VOTACIONES_FILE = "/resources/files/votaciones";
	public static final String VOTACIONES_PENDIENTES_FILE = "/resources/files/votaciones_pendientes";
	public static final String MENSAJES_FILE = "/resources/files/mensajes";
	public static final String TIPOS_MENSAJES_FILE = "/resources/files/tipos_mensajes";
	public static final String WORKSPACE_PATH = "";

	public static String getWORKSPACE_PATH() {
		return WORKSPACE_PATH;
	}

	public static String getConfiguratorFile() {
		return CONFIGURATOR_FILE;
	}

	public static String getUsuarioFile() {

		return USUARIO_FILE;

	}

	public static String getNotificacionesFile() {
		return NOTIFICACIONES_FILE;

	}

	public static String getVotacionesFile() {
		return VOTACIONES_FILE;
	}

	public static String getVotacionesPendientesFile() {
		return VOTACIONES_PENDIENTES_FILE;
	}

	public static String getMensajesFile() {
		return MENSAJES_FILE;
	}

	public static String getTiposMensajesFile() {
		return TIPOS_MENSAJES_FILE;
	}

	public static String getWorkspacePath() {
		return WORKSPACE_PATH;
	}

}
