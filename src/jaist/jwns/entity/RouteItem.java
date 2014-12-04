package jaist.jwns.entity;

import java.util.Date;

import jaist.jwns.util.IPAddress;




public class RouteItem {
	private String dst;
	private String next;
	private long hop;
	private long sequence_no;
	private Date time_stamp;
	
	public RouteItem(String dst, String next, long hop,long seq){
		this.dst=dst;
		this.next=next;
		this.hop=hop;
		this.setSequence_no(seq);
		time_stamp=new Date(); 
	}
	public RouteItem( String dst){
		this.dst=dst;
		this.next=null;
		this.hop=0;
		time_stamp=new Date(); 
	}
	public void setDst(String dst) {
		this.dst = dst;
	}
	public String getDst() {
		return dst;
	}
	public void setNext(String next) {
		this.next = next;
	}
	public String getNext() {
		return next;
	}
	public void setHop(long hop) {
		this.hop = hop;
	}
	public long getHop() {
		return hop;
	}
	public void setSequence_no(long sequence_no) {
		this.sequence_no = sequence_no;
	}
	public long getSequence_no() {
		return sequence_no;
	}
	public void setTime_stamp(Date time_stamp) {
		this.time_stamp = time_stamp;
	}
	public Date getTime_stamp() {
		return time_stamp;
	}
}
