package SAYAV2.SAYAV2.dao;

import java.io.File;
import java.util.concurrent.TimeUnit;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import SAYAV2.SAYAV2.Utils.TipoMensajeUtils;
import SAYAV2.SAYAV2.mensajeria.Mensaje;
import SAYAV2.SAYAV2.mensajeria.TipoMensaje;
import SAYAV2.SAYAV2.model.MensajesPendientes;
import SAYAV2.SAYAV2.model.TiposMensajes;

public class TipoMensajeDaoTest {
	TipoMensajeDao tiposMensajesDao;
	TiposMensajes tipos;
	File file;
	@Before
	public void setUp() throws Exception {
		file = new File("tipos_mensajes");
		tiposMensajesDao = TipoMensajeDao.getInstance();
		tipos = new TiposMensajes();
	}

	@After
	public void tearDown() throws Exception {
		tipos.getTipos().clear();
		
	}

	@Test
	public void test() {
		long quantumGrupo = TimeUnit.HOURS.toMinutes(4);
		long quantumVoto = TimeUnit.HOURS.toMinutes(1);
		long quantumAlerta = 3;
		tipos.getTipos().add(new TipoMensaje(TipoMensajeUtils.ALERTA, quantumAlerta));
		tipos.getTipos().add(new TipoMensaje(TipoMensajeUtils.NUEVO_MIEMBRO, quantumGrupo));
		tipos.getTipos().add(new TipoMensaje(TipoMensajeUtils.NUEVO_GRUPO, quantumGrupo));
		tipos.getTipos().add(new TipoMensaje(TipoMensajeUtils.BAJA_MIEMBRO, quantumGrupo));
		tipos.getTipos().add(new TipoMensaje(TipoMensajeUtils.SOLICITUD_BAJA_MIEMBRO, quantumGrupo));
		tipos.getTipos().add(new TipoMensaje(TipoMensajeUtils.VOTO, TimeUnit.HOURS.toMinutes(quantumVoto)));
		tiposMensajesDao.guardar(tipos, file);



	}
}
