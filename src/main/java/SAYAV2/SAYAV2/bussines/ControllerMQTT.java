package SAYAV2.SAYAV2.bussines;

import java.io.File;
import java.util.List;

import javax.xml.bind.JAXBException;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttAsyncClient;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.MqttSecurityException;

import SAYAV2.SAYAV2.Utils.TipoMensajeUtils;
import SAYAV2.SAYAV2.dao.ConfiguratorDao;
import SAYAV2.SAYAV2.dao.UsuarioDao;
import SAYAV2.SAYAV2.mensajeria.Mensaje;
import SAYAV2.SAYAV2.mensajeria.MensajeriaImpl;
import SAYAV2.SAYAV2.model.Configurator;
import SAYAV2.SAYAV2.model.Grupo;
import SAYAV2.SAYAV2.model.Usuario;
import SAYAV2.SAYAV2.notificacion.NotificacionesImpl;
import SAYAV2.SAYAV2.service.JsonTransformer;

public class ControllerMQTT implements MqttCallback {

	private static ControllerMQTT controllerMqtt;
	private static JsonTransformer jsonTransformer = new JsonTransformer();
	private static UsuarioDao usuarioDao = UsuarioDao.getInstance();
	private static ConfiguratorDao configDao = ConfiguratorDao.getInstance();
	private static File file = new File("SAYAV");
	private static File configFile = new File("configurator");
	private static MensajeriaImpl mensajeria;
	private MqttAsyncClient client;
	private MqttConnectOptions options;

	private ControllerMQTT() {
		super();
		try {
			Configurator config = configDao.cargar(configFile);
			System.out.println(config);
			client = new MqttAsyncClient(config.getBroker(), MqttAsyncClient.generateClientId());
			options = new MqttConnectOptions();
		} catch (MqttException e) {
			e.printStackTrace();
		} catch (JAXBException e) {
			e.printStackTrace();
		}
	}

	public static ControllerMQTT getInstance() {
		if (controllerMqtt == null) {
			controllerMqtt = new ControllerMQTT();
			mensajeria = MensajeriaImpl.getInstance();
		}
		return controllerMqtt;
	}

	public void start() {
		try {
			System.out.println("Connecting...");
			client.connect(options);
			client.setCallback(this);
			System.out.println("Connected!");
		} catch (MqttSecurityException e) {
			e.printStackTrace();
		} catch (MqttException e) {
			e.printStackTrace();
		}
	}

	public void end() {
		try {
			client.disconnect();
		} catch (MqttException e) {
			e.printStackTrace();
		}
	}

	public void initReceive() throws JAXBException {
		int qos = 1;

		Usuario usuario = usuarioDao.cargar(file);

		String handshakeRequest = usuario.getSubdominio() + "/" + TipoMensajeUtils.HANDSHAKE_REQUEST;
		receive(handshakeRequest, qos);

		String handshakeResponse = usuario.getSubdominio() + "/" + TipoMensajeUtils.HANDSHAKE_RESPONSE;
		receive(handshakeResponse, qos);

		String nuevoGrupo = usuario.getSubdominio() + "/" + TipoMensajeUtils.NUEVO_GRUPO;
		receive(nuevoGrupo, qos);
		
		String nuevoMiembro = usuario.getSubdominio() + "/" + TipoMensajeUtils.NUEVO_MIEMBRO;
		receive(nuevoMiembro,qos);


	}

	public void suscribeAllGroups(List<Grupo> grupos) {

		int n = grupos.size(), i = 0;
		String[] grupoTopic = new String[n];
		int[] qoss = new int[n];
		Usuario usuario;
		try {
			usuario = usuarioDao.cargar(file);
			for (Grupo g : grupos) {
				grupoTopic[i] = (g.getId() + usuario.getSubdominio() + "/" + TipoMensajeUtils.NUEVO_MIEMBRO);
				qoss[i] = 2;
				i++;
			}
		} catch (JAXBException e) {
			e.printStackTrace();
		}

		this.receive(grupoTopic, qoss);
	}

	public void send(String topic, String msg, int qos) {

		MqttMessage message = new MqttMessage();
		try {
			Thread.sleep(300);
			message.setQos(qos);
			message.setPayload(msg.getBytes());
			System.out.println("Topic :");
			System.out.println(topic);
			client.publish(topic, message);
			// client.disconnect();
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (MqttSecurityException e) {
			e.printStackTrace();
		} catch (MqttException e) {
			e.printStackTrace();
		}

	}

	public void receive(String topic, int qos) {

		try {

			Thread.sleep(300);
			client.subscribe(topic, qos);
			// client.disconnect();
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (MqttException e) {
			e.printStackTrace();
		}

	}

	public void receive(String[] topic, int[] qos) {
		try {
			client.subscribe(topic, qos);
		} catch (MqttException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void connectionLost(Throwable arg0) {

	}

	@Override
	public void deliveryComplete(IMqttDeliveryToken arg0) {
		System.out.println("Delivery Completed\n" + arg0);
	}

	@Override
	public void messageArrived(String topic, MqttMessage msg) throws Exception {
		System.out.println("Message Arrived...");
		Mensaje mensaje;
		String tipo;

		mensaje = jsonTransformer.getGson().fromJson(msg.toString(), Mensaje.class);

		tipo = mensaje.getTipoHandshake();

		if (tipo.equals(TipoMensajeUtils.HANDSHAKE_REQUEST)) {
			mensajeria.recibirSolicitud(mensaje);
		}
		
		if(topic.equals("prueba/notificacion")){
			
		}

	}

}
