package jaist.jwns.entity;

import jaist.jwns.util.LPacket;

import java.util.ArrayList;

public class OutPutPacket {

	private String src;
	private String dst;
	private ArrayList<LPacket> packetlist;
	private int length;
	public OutPutPacket(String src,String dst){
		this.src=src;
		this.dst=dst;
		packetlist=new ArrayList<LPacket>();
	}
	public void addPacket(LPacket lp){
		if(lp.getSrc().equals(src)){
			length=lp.getLength();
			packetlist.add(lp);
		}
	}
	public ArrayList<LPacket> getlist(){
		return packetlist;
	}
	public void setSrc(String src) {
		this.src = src;
	}
	public String getSrc() {
		return src;
	}
	public void setDst(String dst) {
		this.dst = dst;
	}
	public String getDst() {
		return dst;
	}
	public void setLength(int length) {
		this.length = length;
	}
	public int getLength() {
		return length;
	}
}
