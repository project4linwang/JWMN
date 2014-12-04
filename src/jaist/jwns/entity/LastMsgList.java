package jaist.jwns.entity;

import java.util.ArrayList;

/**
 * The list of last packet's message
 * @author lin
 *
 */
public class LastMsgList {
	private ArrayList<LastPacketMsg> msglists;
	public LastMsgList(){
		msglists=new ArrayList<LastPacketMsg>();
	}
	/**
	 * Add a new message of last packet
	 * @param lpm is the message of last packet
	 */
	public void add(LastPacketMsg lpm){
		if(!isExist(lpm)){
			msglists.add(lpm);
		}
		else{
			LastPacketMsg old=getLastPacketMsg(lpm.getNext());
			old.setEnd_Stamp(lpm.getEnd_Stamp());
			old.setTransmit_time(lpm.getTransmit_time());
		}
	}
	/**
	 * get the object of last packet message
	 * @param next is the IP Address of next node
	 * @return
	 */
	public LastPacketMsg getLastPacketMsg(String next){
		for(LastPacketMsg msg:msglists){
			if(msg!=null){
				if(msg.getNext().equals(next)){
					return msg;
				}
			}
		}
		return null;
	}
	private boolean isExist(LastPacketMsg lpm){
		for(LastPacketMsg msg:msglists){
			if(msg!=null){
				if(msg.getNext().equals(lpm.getNext())){
					return true;
				}
			}
		}	
		return false;
	}
}
