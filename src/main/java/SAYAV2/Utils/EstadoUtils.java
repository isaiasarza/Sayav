package SAYAV2.Utils;

import lombok.Getter;

/**
 * 
 * @author Isaías Arza
 * 
 */
public class EstadoUtils {

	public static class Estado {
		@Getter
		public static final String PENDIENTE = "Pendiente";
		@Getter
		public static final String CONFIRMADO = "Confirmado";
		@Getter
		public static final String ZOMBIE = "Zombie";
		
		public static String getPendiente() {
			return PENDIENTE;
		}
		public static String getConfirmado() {
			return CONFIRMADO;
		}
		public static String getZombie() {
			return ZOMBIE;
		}
		
		
	}

	/**
	 * {@value tiempo de vida en días}
	 */
	public static final long TIME_TO_LIVE_CONF = 1;
	
	

}
