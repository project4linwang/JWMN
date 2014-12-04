package jaist.jwns.event;

import jaist.jwns.entity.Link;


public class SFinishAction {
	private Link link;	
	private EventHandler handler=new EventHandler();
	public void setLink(Link lk){
		link=lk;
	}
	public EventHandler getHandler(){
		return handler;
	}
}
