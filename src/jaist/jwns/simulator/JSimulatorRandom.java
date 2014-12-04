package jaist.jwns.simulator;

import java.util.LinkedList;
import java.util.Random;

import jaist.jwns.entity.Node;
import jaist.jwns.entity.NodeChain;
import jaist.jwns.util.IPAddress;

/**
 * Generate the network randomly (future extending)
 * @author lin
 *
 */
public class JSimulatorRandom {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
//		NodeChain chain=NodeChain.getNodeChain();
//		LinkedList<Node> nodelist=new LinkedList<Node>();
//		int node_number=10;
//		int X=1;
//		int Y=1;
//		Random rd=new Random();
//		
//		for(int i=0;i<node_number;i++){
//			int sequence=i+1;
//			String name="Node"+sequence;
//			IPAddress ip=new IPAddress(192,168,0,sequence);
//			Node node=new Node(sequence,name,ip);
//			node.setRange(5);
//			long xp=rd.nextInt(50)*X;
//			long yp=rd.nextInt(50)*Y;
//			while(exist(nodelist,xp,yp)){
//				 xp=rd.nextInt(50)*X;
//				 yp=rd.nextInt(50)*Y;
//			}
//			node.setX(xp);
//			node.setY(yp);
//			nodelist.add(node);
//			node.init();
//			chain.addNode(node);
//		}
//		printNodes(nodelist);
//		chain.startRegister();
	}
	private static void printNodes(LinkedList<Node> nodes){
		if(nodes!=null){
			if(nodes.size()>0){
				for(Node nd:nodes){
					System.out.println("Node: "+nd.getName()+" X: "+nd.getX()+" Y: "+nd.getY());
				}
			}
		}
	}
	private static boolean exist(LinkedList<Node> nds,long x,long y){
		if(nds!=null){
			for(Node nd:nds){
				if(nd.getX()==x && nd.getY()==y){
					return true;
				}
			}
		}
		return false;
	}

}
