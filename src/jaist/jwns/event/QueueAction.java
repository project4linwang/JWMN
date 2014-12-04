package jaist.jwns.event;

import java.util.TimerTask;

public class QueueAction extends TimerTask {

	private EventHandler handler=new EventHandler();
	public EventHandler getQueueEvent(){
		return handler;
	}
	@Override
	public void run() {
		// TODO Auto-generated method stub
        handler.invoke(this);
	}

}
