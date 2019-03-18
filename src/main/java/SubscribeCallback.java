import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.MqttTopic;

public class SubscribeCallback implements MqttCallback {

    @Override
    public void connectionLost(Throwable cause) {
        //This is called when the connection is lost. We could reconnect here.
    }

    public void messageArrived(MqttTopic topic, MqttMessage message) throws Exception {
        System.out.println("Message arrived. Topic: " + topic.getName() + "  Message: " + message.toString());

        if ("homeautomation/exit".equals(topic.getName())) {           
        	System.err.println("Publisher gone!");
        }
    }

    public void deliveryComplete(MqttDeliveryToken token) {
        //no-op
    }

	
	
	@Override
	public void deliveryComplete(IMqttDeliveryToken token) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void messageArrived(String topic, MqttMessage message) throws Exception {
		// TODO Auto-generated method stub
		
	}

	

	
}