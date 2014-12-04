package jaist.jwns.entity;

import jaist.jwns.util.LPacket;

import java.util.ArrayList;

public class OutPutList {

	private ArrayList<OutPutPacket> packets;
	public OutPutList(){
		packets=new ArrayList<OutPutPacket>();
	}
	
	public void addLPacket(LPacket lp){
	   if(isExist(lp.getSrc())){
		   OutPutPacket output=getOutPutPacket(lp.getSrc());
		   if(output!=null){
			   output.addPacket(lp);
		   }
	   }
	   else{
		   OutPutPacket output=new OutPutPacket(lp.getSrc(),lp.getDest());
		   packets.add(output);
	   }
		   
	}
	public OutPutPacket getOutPutPacket(String src){
		for(OutPutPacket output:packets){
			   if(output!=null){
				   if(output.getSrc().equals(src)){
					  return output;
				   }
			   }
		 }
		return null;
	}
	private boolean isExist(String src){
		 for(OutPutPacket output:packets){
			   if(output!=null){
				   if(output.getSrc().equals(src)){
					  return true;
				   }
			   }
		   }
		 return false;
	}
	public ArrayList<LPacket> getLPackets(String src){
		for(OutPutPacket output:packets){
			   if(output!=null){
				   if(output.getSrc().equals(src)){
					   return output.getlist();
				   }
			   }
		}
		return null;
	}
	public ArrayList<String> getSrcList(){
		ArrayList<String> srclist=new ArrayList<String>();
		for(OutPutPacket output:packets){
			   if(output!=null){
				   srclist.add(output.getSrc());
			   }
		}
		return srclist;
	}
	
	public int size(){
		return packets.size();
	}
}
