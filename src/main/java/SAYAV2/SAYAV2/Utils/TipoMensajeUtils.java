package SAYAV2.SAYAV2.Utils;

import Datos.DatoGrupo;
import SAYAV2.SAYAV2.mensajeria.Mensaje;

public class TipoMensajeUtils {
	public static final Object OK = "Ok";
	public static final String HANDSHAKE_REQUEST = "Handshake Request";
	public static final String HANDSHAKE_RESPONSE = "Handshake Response";
	public static final String SOLICITUD_BAJA_MIEMBRO = "Solicitud Baja Miembro";
	public static final String VOTO = "Voto";
	public static final String BAJA_GRUPO = "Baja Grupo";
	public static final String NUEVO_MIEMBRO = "Nuevo Miembro";
	public static final String ALERTA = "Alerta";
	public static final String BAJA_MIEMBRO = "Baja Miembro";
	public static final String NUEVO_GRUPO = "Nuevo Grupo";
	public static final String CONECTADO = "Conectado";
	public static final String DESCONECTADO = "Desconectado";
	public static final String MIEMBRO_NO_SE_ALERTO = "No se pudo alertar";
	public static final String MIEMBRO_NO_AGREGADO = "No se pudo agregar el Miembro";
	public static final String NO_BAJA_MIEMBRO = "No se pudo dar de baja el Miembro";
	public static final String NO_GRUPO_NUEVO = "No se pudo crear el grupo";
	public static final String CHECK_CONNECTIVITY = "Ver Conectividad";
	public static final String OK_CONFIRMACION = "Se recibio la confirmacion";
	public static final String NUEVO_DISPOSITIVO = "Nuevo Dispositivo";
	public static final String NOTIFICACION_MOVIL = "Notificacion Movil";


	public static String generarDetalle(DatoGrupo datoGrupo, Mensaje mensaje) {
		String detalle = null;

		if (mensaje.getTipoMensaje().equals(NUEVO_GRUPO))
			detalle = "Usted es parte del grupo: " + datoGrupo.getGrupo().getNombre();
		if (mensaje.getTipoMensaje().equals(NUEVO_MIEMBRO))	
			detalle = "El miembro " + datoGrupo.getMiembro().getDireccion() + " es parte del grupo " + datoGrupo.getGrupo().getNombre();
		if (mensaje.getTipoMensaje().equals(BAJA_GRUPO))
			detalle = "Se ha dado de baja del grupo" +"Fecha: " + mensaje.getFechaCreacion()+"Reenvio: " +mensaje.getFechaReenvio();
		if (mensaje.getTipoMensaje().equals(BAJA_MIEMBRO))
			detalle = "El miembro "+ datoGrupo.getMiembro().getDireccion()+ " abandono el grupo "+ datoGrupo.getGrupo().getNombre() +" Fecha: " + mensaje.getFechaCreacion()+"Reenvio: " +mensaje.getFechaReenvio();
		if (mensaje.getTipoMensaje().equals(SOLICITUD_BAJA_MIEMBRO))
			detalle = "Se solicito la baja del miembro: "+ datoGrupo.getMiembro().getDireccion()+ " Solicitud de Baja" + "Fecha: " + mensaje.getFechaCreacion()+"Reenvio: " +mensaje.getFechaReenvio();

		return detalle;
	};

}
