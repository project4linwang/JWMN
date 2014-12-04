package jaist.jwns.entity;

import jaist.jwns.event.EventArg;
import jaist.jwns.event.EventListener;
import jaist.jwns.event.HelloAction;

import java.util.LinkedList;

/**
 * A chain of nodes
 * @author lin
 *
 */
public class NodeChain {

	private static NodeChain chain;
	private LinkedList<Node> chains;
	private LinkChecker checker;
	private NodeChain(){
		checker=new LinkChecker();
		chains=new LinkedList<Node>();
	}
	public static NodeChain getNodeChain(){
		if(chain==null){
			chain=new NodeChain();
		}
		return chain;
	}
	public void startRegister(){
		if(chains!=null){
			if(chains.size()>0){
				for(Node nd:chains){
					if(nd!=null){
						nd.getHAction().add(helloListener);
					}
				}
			}
		}
	}
	public void addNode(Node nd){
		if(!chains.contains(nd)){
			chains.add(nd);
		}
	}
	/**
	 * The Event of hello message. 
	 * When the node received a hello message will trigger this event.
	 */
	private EventListener helloListener=new EventListener(){

		@Override
		public void doEvent(Object sender, EventArg e) {
			// TODO Auto-generated method stub
			Node nd= (Node)sender;
			if(nd!=null){
				HelloMessage hm=(HelloMessage)e.getArg(0);
				if(chains!=null){
					if(chains.size()>0){
						for(Node node:chains){
							if(!node.equals(nd)){
								if(hm!=null){
									if(checker.checker(nd, node)){
										node.receivedHelloMessage(hm);
									}
									
								}
							}
						}
					}
				}
				
					
			}
		}
		
	};
	
}
