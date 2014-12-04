package jaist.jwns.entity;

import jaist.jwns.util.LPacket;

import java.util.LinkedList;

public class ThroughPut {

	private static ThroughPut output;
	private LinkedList<Double> link_put;
	private LinkedList<LPacket> diff_src_pk;
	
	public ThroughPut(){
		link_put=new LinkedList<Double>();
		diff_src_pk=new LinkedList<LPacket>();
		
	}
	public static ThroughPut getThroughPut(){
		if(output==null){
			output=new ThroughPut();
		}
		return output;
	}
	public void addLinkTraffic(double traffic){
		link_put.add(traffic);
	}
	public LinkedList<Double> getLinkPut(){
		return link_put;
	}
	public void addDiffSrcLPacket(LPacket lpk){
		if(diff_src_pk.size()>0){
			for(LPacket pk:diff_src_pk){
				if(pk.getSrc().toString().equals(lpk.getSrc().toString())){
					return;
				}
			}
		}		
		diff_src_pk.add(lpk);		
	}
	public LinkedList<LPacket> getDiffSrcLPacket(){
		return diff_src_pk;
	}
}
