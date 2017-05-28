package SAYAV2.SAYAV2.Utils;

import java.util.Date;
import java.util.concurrent.TimeUnit;

public class FechaUtils {

	/**
	 * 
	 * @param fechaInicio
	 * @param fechaFin
	 * @return la diferencia de fechas en minutos
	 */
	public static long diffDays(Date fechaInicio, Date fechaFin){
		long diff = fechaFin.getTime() - fechaInicio.getTime();
		return TimeUnit.MINUTES.convert(diff, TimeUnit.MILLISECONDS);
	}
}
