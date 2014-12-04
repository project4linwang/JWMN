package jaist.jwns.tcp;

import java.util.ArrayList;

public class NAKPacket {
	   private int length=28; //bytes
	   private String src;
	   private String dst;
	   private double sp_time;
	   private ArrayList<Long> lost_list=new ArrayList<Long>();
	   public NAKPacket(ArrayList<Long> lost_id){
		   setLost_list((ArrayList<Long>)lost_id.clone());
	   }
	public void setLength(int length) {
		this.length = length;
	}
	public int getLength() {
		return length;
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
	public void setSp_time(double sp_time) {
		this.sp_time = sp_time;
	}
	public double getSp_time() {
		return sp_time;
	}
	public void setLost_list(ArrayList<Long> lost_list) {
		this.lost_list = lost_list;
	}
	public ArrayList<Long> getLost_list() {
		return lost_list;
	}
	   
}
