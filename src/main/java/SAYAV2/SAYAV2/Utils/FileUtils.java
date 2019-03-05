package SAYAV2.SAYAV2.Utils;

import java.io.File;

public class FileUtils {
	private static final String folder =  File.separator + "resources" + File.separator + "files" + File.separator;
	
	public static final String CONFIGURATOR_FILE = folder  + "configurator";
	public static final String USUARIO_FILE = folder + "SAYAV";
	public static final String NOTIFICACIONES_FILE =  folder + "notificaciones";
	public static final String VOTACIONES_FILE = folder + "votaciones";
	public static final String VOTACIONES_PENDIENTES_FILE = folder + "votaciones_pendientes";
	public static final String MENSAJES_FILE =  folder + "mensajes";
	public static final String TIPOS_MENSAJES_FILE = folder + "tipos_mensajes";
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
