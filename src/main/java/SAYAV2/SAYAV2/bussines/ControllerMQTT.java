package SAYAV2.SAYAV2.bussines;

import java.io.File;

import javax.xml.bind.JAXBException;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.MqttSecurityException;

import SAYAV2.SAYAV2.Utils.TipoMensaje;
import SAYAV2.SAYAV2.dao.UsuarioDao;
import SAYAV2.SAYAV2.model.Usuario;

public class ControllerMQTT implements MqttCallback {
	
	private static ControllerMQTT controllerMqtt;
	private static UsuarioDao usuarioDao = UsuarioDao.getInstance();
	private static File file = new File("SAYAV");
	private MqttClient client;
	private MqttConnectOptions options;
	
	

	private ControllerMQTT() {
		super();
		try {
			client = new MqttClient("tcp://localhost:1883", "isaiasarza.dns:29111");
			options = new MqttConnectOptions();
		} catch (MqttException e) {
			e.printStackTrace();
		}
	}
	
	public static ControllerMQTT getInstance(){
		if(controllerMqtt == null)
			controllerMqtt = new ControllerMQTT();
		return controllerMqtt;
	}
	

	public void initReceive() throws JAXBException{
		
		Usuario usuario = usuarioDao.cargar(file);
		
		String nuevoGrupo = usuario.getSubdominio() + "/" + TipoMensaje.NUEVO_GRUPO;
		receive(nuevoGrupo);
		
           
	}
		
	public void send(String topic, String msg, int qos){
		
		MqttMessage message = new MqttMessage();
		try {
			client.connect(options);
			client.setCallback(this);
			message.setQos(qos);
			message.setPayload(msg.getBytes());
			client.publish(topic, message);
			client.disconnect();
		} catch (MqttSecurityException e) {
			e.printStackTrace();
		} catch (MqttException e) {
			e.printStackTrace();
		}
		
		
	}
	
	public void receive(String topic){
		
		try {
			client.connect(options);
			client.setCallback(this);
			client.subscribe(topic);
			client.disconnect();
		} catch (MqttException e) {
			e.printStackTrace();
		}
		
	}
	

	@Override
	public void connectionLost(Throwable arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void deliveryComplete(IMqttDeliveryToken arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void messageArrived(String arg0, MqttMessage arg1) throws Exception {
        Usuario usuario = usuarioDao.cargar(file);
		if(arg0.equals(usuario.getSubdominio()+ "/" +TipoMensaje.NUEVO_GRUPO)){
			//TODO deserealizar mensaje
			//TODO Agregar grupo a la lista de grupos
			System.out.println(arg1);
		}
		if(arg0.equals(usuario.getSubdominio()+ "/" +TipoMensaje.NUEVO_MIEMBRO)){
			//TODO Agregar miembro a la lista de miembros de ese grupo
			System.out.println(arg1);

		}
	}

}
