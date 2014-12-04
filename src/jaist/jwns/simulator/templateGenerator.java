package jaist.jwns.simulator;

import jaist.jwns.template.LinkThroughPut;
import jaist.jwns.template.NodeData;
import jaist.jwns.template.SDPair;
import jaist.jwns.template.Template;
import jaist.jwns.util.SerialXMLUtil;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Generate the scenario file, only for testing
 * @author lin
 *
 */
public class templateGenerator {
	public static void main(String[] args) {
		Template tm=new Template();
		//tm.setLSize(1480);
		NodeData nd1=new NodeData();
		nd1.setIPAddress("192,168,0,1");
		nd1.setName("A");
		nd1.setProcessTime(1);
		nd1.setX(2);
		nd1.setY(6);
		nd1.setRange(1.5);
		nd1.setQueueSize(1000);
		
	    NodeData nd2=new NodeData();
	    nd2.setIPAddress("192,168,0,2");
		nd2.setName("B");
		nd2.setProcessTime(0);
		nd2.setX(2);
		nd2.setY(2);
		nd2.setRange(1.5);
		nd2.setQueueSize(1000);
		
		NodeData nd3=new NodeData();
		nd3.setIPAddress("192,168,0,3");
		nd3.setName("C");
		nd3.setProcessTime(0);
		nd3.setX(4);
		nd3.setY(4);
		nd3.setRange(1.5);
		nd3.setQueueSize(1000);
		
		NodeData nd4=new NodeData();
		nd4.setIPAddress("192,168,0,4");
		nd4.setName("D");
		nd4.setProcessTime(0);
		nd4.setX(6);
		nd4.setY(6);
		nd4.setRange(1.5);
		nd4.setQueueSize(1000);
		
		NodeData nd5=new NodeData();
		nd5.setIPAddress("192,168,0,5");
		nd5.setName("E");
		nd5.setProcessTime(0);
		nd5.setX(6);
		nd5.setY(2);
		nd5.setRange(1.5);
		nd5.setQueueSize(1000);
		
		ArrayList<NodeData> nodes=new ArrayList<NodeData>();
		nodes.add(nd1);
		nodes.add(nd2);
		nodes.add(nd3);
		nodes.add(nd4);
		nodes.add(nd5);
		
		tm.setNodes(nodes);
		
		tm.setNodeNumber(nodes.size());
		LinkThroughPut output=new LinkThroughPut();
		output.setStartAddress("192,168,0,3");
		output.setEndAddress("192,168,0,4");
		
		LinkThroughPut output2=new LinkThroughPut();
		output2.setStartAddress("192,168,0,3");
		output2.setEndAddress("192,168,0,5");
		
		LinkThroughPut output3=new LinkThroughPut();
		output3.setStartAddress("192,168,0,1");
		output3.setEndAddress("192,168,0,3");
		
		LinkThroughPut output4=new LinkThroughPut();
		output4.setStartAddress("192,168,0,2");
		output4.setEndAddress("192,168,0,3");
		
		
		ArrayList<LinkThroughPut> outputs=new ArrayList<LinkThroughPut>();
		outputs.add(output);
		outputs.add(output2);
		outputs.add(output3);
		outputs.add(output4);
		ArrayList<String> sender=new ArrayList<String>();
		sender.add("192,168,0,1");
		sender.add("192,168,0,2");
		SDPair p1=new SDPair();
		p1.setSrcAddress("192,168,0,1");
		p1.setDstAddress("192,168,0,4");
		SDPair p2=new SDPair();
		p2.setSrcAddress("192,168,0,2");
		p2.setDstAddress("192,168,0,5");
		ArrayList<SDPair> pairs=new ArrayList<SDPair>();
		pairs.add(p1);
		pairs.add(p2);
		
		tm.setSDPairs(pairs);
		tm.setOutPut(outputs);

//		tm.setHello_entry_expirationtime(2000);
//		tm.setHello_time_interval(1000);
//		
		try {
			SerialXMLUtil.save(tm, "result/configtest.xml");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println("Error!");
		}
		System.out.println("Finished!");
	}
}
