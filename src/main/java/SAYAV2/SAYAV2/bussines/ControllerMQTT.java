package SAYAV2.SAYAV2.bussines;

import java.io.File;

import javax.xml.bind.JAXBException;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttAsyncClient;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.MqttSecurityException;

import SAYAV2.SAYAV2.Utils.TipoMensaje;
import SAYAV2.SAYAV2.dao.ConfiguratorDao;
import SAYAV2.SAYAV2.dao.UsuarioDao;
import SAYAV2.SAYAV2.model.Configurator;
import SAYAV2.SAYAV2.model.Grupo;
import SAYAV2.SAYAV2.model.GrupoPeer;
import SAYAV2.SAYAV2.model.Peer;
import SAYAV2.SAYAV2.model.Usuario;
import SAYAV2.SAYAV2.service.JsonTransformer;

public class ControllerMQTT implements MqttCallback {

	private static ControllerMQTT controllerMqtt;
	private static JsonTransformer jsonTransformer = new JsonTransformer();
	private static UsuarioDao usuarioDao = UsuarioDao.getInstance();
	private static ConfiguratorDao configDao = ConfiguratorDao.getInstance();
	private static File file = new File("SAYAV");
	private static File configFile = new File("configurator");
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
		if (controllerMqtt == null)
			controllerMqtt = new ControllerMQTT();
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

		String nuevoGrupo = usuario.getSubdominio() + "/" + TipoMensaje.NUEVO_GRUPO;
		receive(nuevoGrupo, qos);

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
			// TODO Auto-generated catch block
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
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (MqttException e) {
			e.printStackTrace();
		}

	}

	public String notificarNuevoGrupo(Peer peer, Grupo grupo) {
		System.out.println("Enviando el grupo al miembro");

		int qos = 2;
		String topic = peer.getDireccion() + "/" + TipoMensaje.NUEVO_GRUPO;
		GrupoPeer g = new GrupoPeer();
		g.setGrupoId(grupo.getId());
		g.setGrupoNombre(grupo.getNombre());
		g.setListaPeersByPeer(grupo.getPeers());
		g.addPeer(peer.getDireccion());
		String msg = jsonTransformer.render(g);

//		send(topic, msg, qos);
		return msg;
	}

	public String notificarMiembros(Peer peer, Grupo grupo) {
		System.out.println("Enviando el miembro al grupo " + peer.getDireccion());
		int qos = 1;
		GrupoPeer g = new GrupoPeer();
		g.setGrupoId(grupo.getId());
		g.setGrupoNombre(grupo.getNombre());
		g.setPeer(peer.getDireccion());
		String topic = grupo.getId() + "/" + TipoMensaje.NUEVO_MIEMBRO;
		String msg = jsonTransformer.render(g);
//		send(topic, msg, qos);
		return msg;
	}

	public boolean arriboNuevoGrupo(String msg, Usuario usuario) {
		// Deserealizar mensaje
		GrupoPeer g = jsonTransformer.getGson().fromJson(msg.toString(), GrupoPeer.class);
		System.out.println(g);
		Grupo nuevo = new Grupo();
		nuevo.setId(g.getGrupoId());
		nuevo.setNombre(g.getGrupoNombre());
		nuevo.addAll(g.getListaPeers());
		// TODO: Agregar grupo a la lista de grupos
		if (usuario.addGrupo(nuevo)) {
			return true;
		}
		// TODO: Grupo existente
		return false;

	}

	public boolean arriboNuevoMiembro(String msg, Usuario usuario) {
		System.out.println(msg);
		GrupoPeer g = jsonTransformer.getGson().fromJson(msg.toString(), GrupoPeer.class);
		Grupo aux =usuario.getSingleGrupoById(g.getGrupoId());

		if((aux) == null){
			return false;
		}
		
		if (!aux.addPeer(g.getPeer())) {
			// TODO: Solucionar posible problema
			return false;
		}

		return true;
	}

	@Override
	public void connectionLost(Throwable arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void deliveryComplete(IMqttDeliveryToken arg0) {
		// TODO Auto-generated method stub
		try {
			System.out.println("Delivery Completed\n" + arg0.getMessage().toString());
		} catch (MqttException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void messageArrived(String topic, MqttMessage msg) throws Exception {
		Usuario usuario = usuarioDao.cargar(file);
		if (topic.equals(usuario.getSubdominio() + "/" + TipoMensaje.NUEVO_GRUPO)) {
			if(arriboNuevoGrupo(msg.toString(), usuario)){
				usuarioDao.guardar(usuario, file);
			}

		}
		if (topic.equals(usuario.getSubdominio() + "/" + TipoMensaje.NUEVO_MIEMBRO)) {
			if(arriboNuevoMiembro(msg.toString(), usuario)){
				usuarioDao.guardar(usuario, file);
			}
		}
	}

}
