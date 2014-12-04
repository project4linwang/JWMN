package jaist.jwns.event;

import jaist.jwns.entity.HelloMessage;
import jaist.jwns.entity.Node;

import java.util.TimerTask;

public class HelloAction {
	
	private EventHandler handler=new EventHandler();
	public EventHandler getHelloEvent(){
		return handler;
	}
	
	

}
