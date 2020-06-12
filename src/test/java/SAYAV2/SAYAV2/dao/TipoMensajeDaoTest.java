package SAYAV2.SAYAV2.dao;

import java.io.File;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import SAYAV2.SAYAV2.Utils.FileUtils;
import SAYAV2.SAYAV2.Utils.TipoMensajeUtils;
import SAYAV2.SAYAV2.mensajeria.TipoMensaje;
import SAYAV2.SAYAV2.model.TiposMensajes;

public class TipoMensajeDaoTest {
	TipoMensajeDao tiposMensajesDao;
	TiposMensajes tipos;
	File file;
	@Before
	public void setUp() throws Exception {
		file = new File(FileUtils.TIPOS_MENSAJES_FILE);
		tiposMensajesDao = TipoMensajeDao.getInstance();
		tipos = new TiposMensajes();
	}

	@After
	public void tearDown() throws Exception {
		tipos.getTipos().clear();
		
	}

	@Test
	public void test() {
		long quantumGrupo = 1;
		long timeToLive = 5;

		tipos.getTipos().add(new TipoMensaje(TipoMensajeUtils.ALERTA, quantumGrupo,timeToLive));
		tipos.getTipos().add(new TipoMensaje(TipoMensajeUtils.NUEVO_MIEMBRO, quantumGrupo, timeToLive));
		tipos.getTipos().add(new TipoMensaje(TipoMensajeUtils.NUEVO_GRUPO, quantumGrupo, timeToLive));
		tipos.getTipos().add(new TipoMensaje(TipoMensajeUtils.BAJA_MIEMBRO, quantumGrupo, timeToLive));
		tipos.getTipos().add(new TipoMensaje(TipoMensajeUtils.SOLICITUD_BAJA_MIEMBRO, quantumGrupo, timeToLive));
		tipos.getTipos().add(new TipoMensaje(TipoMensajeUtils.VOTO, quantumGrupo, timeToLive));
		tipos.getTipos().add(new TipoMensaje(TipoMensajeUtils.NOTIFICACION_MOVIL, quantumGrupo, timeToLive));
		tipos.getTipos().add(new TipoMensaje(TipoMensajeUtils.BAJA_GRUPO, quantumGrupo, timeToLive));
		tipos.getTipos().add(new TipoMensaje(TipoMensajeUtils.OK_CONFIRMACION, quantumGrupo, timeToLive));

		tiposMensajesDao.guardar(tipos, file);



	}
}
