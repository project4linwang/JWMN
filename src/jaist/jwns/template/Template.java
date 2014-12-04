package jaist.jwns.template;

import java.util.ArrayList;
import java.util.LinkedList;

/**
 * Deserialize the scenario file to the template object
 * @author lin
 *
 */
public class Template {

	private long NodeNumber;
	private ArrayList<NodeData> Nodes=new ArrayList<NodeData>();	
	private ArrayList<SDPair> SDPairs=new ArrayList<SDPair>();
	private ArrayList<LinkThroughPut> OutPut=new ArrayList<LinkThroughPut>();
	
	public void setNodeNumber(long nodeNumber) {
		NodeNumber = nodeNumber;
	}
	public long getNodeNumber() {
		return NodeNumber;
	}
	public void setNodes(ArrayList<NodeData> nodes) {
		Nodes = nodes;
	}
	public ArrayList<NodeData> getNodes() {
		return Nodes;
	}

	public void setOutPut(ArrayList<LinkThroughPut> outPut) {
		OutPut = outPut;
	}
	public ArrayList<LinkThroughPut> getOutPut() {
		return OutPut;
	}

	public void setSDPairs(ArrayList<SDPair> sDPairs) {
		SDPairs = sDPairs;
	}
	public ArrayList<SDPair> getSDPairs() {
		return SDPairs;
	}
					
}
