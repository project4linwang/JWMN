package jaist.jwns.util;

import java.util.ArrayList;



public class LPacket  implements Cloneable{

	private long sequence_id;
	private String dest;
	private String next;
	private IPAddress dst;
	private long hop;
	private String src;
    private long send_time_src;
    private long rec_time_dst;
    private long pk_send_time;
    private String tx;
    private String rx;
    private int length;
    private int wnd;
    private int wnd_id;
    private int property=0; //0: normal packet ; 1: last packet; 
    private long start_stamp=0;
    private long end_stamp=0;
    private long interval=0;
    private boolean isEmpty=true;
    private int dynamic_wnd;
    Configuration config=Configuration.getConfig();
    private ArrayList<Long> t_prolist=new ArrayList<Long>();
	public LPacket(long id, IPAddress src, IPAddress dest){
		this.sequence_id=id;
		this.dest=dest.toString();
		this.dst=dest;
		this.src=src.toString();
		this.length=config.getLPacketLength();
	}
	

	public void setSequence_id(long sequence_id) {
		this.sequence_id = sequence_id;
	}

	public long getSequence_id() {
		return sequence_id;
	}

	public String getDest() {
		return dest;
	}
    public IPAddress getDst(){
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
	
	public void setSrc(String src) {
		this.src = src;
	}

	public String getSrc() {
		return src;
	}


	public void setSend_time_src(long send_time_src) {
		this.send_time_src = send_time_src;
	}


	public long getSend_time_src() {
		return send_time_src;
	}


	public void setRec_time_dst(long rec_time_dst) {
		this.rec_time_dst = rec_time_dst;
	}


	public long getRec_time_dst() {
		return rec_time_dst;
	}


	public void setPk_send_time(long pk_send_time) {
		this.pk_send_time = pk_send_time;
	}


	public long getPk_send_time() {
		return pk_send_time;
	}


	public void setTx(String tx) {
		this.tx = tx;
	}


	public String getTx() {
		return tx;
	}


	public void setRx(String rx) {
		this.rx = rx;
	}


	public String getRx() {
		return rx;
	}

	public int getLength() {
		return length;
	}


	public void setWnd(int wnd) {
		this.wnd = wnd;
	}


	public int getWnd() {
		return wnd;
	}


	public void setWnd_id(int wnd_id) {
		this.wnd_id = wnd_id;
	}


	public int getWnd_id() {
		return wnd_id;
	}


	public void setProperty(int property) {
		this.property = property;
	}


	public int getProperty() {
		return property;
	}


	public void setStart_stamp(long start_stamp) {
		this.start_stamp = start_stamp;
	}


	public long getStart_stamp() {
		return start_stamp;
	}


	public void setEnd_stamp(long end_stamp) {
		this.end_stamp = end_stamp;
	}


	public long getEnd_stamp() {
		return end_stamp;
	}


	public void setInterval(long interval) {
		this.interval = interval;
	}


	public long getInterval() {
		return interval;
	}
	public Object clone()
	{
	try
	{
	   super.clone();
	   return super.clone();
	}catch(Exception e)
	 {
	   return null;
	 }
	}


	public void setEmpty(boolean isEmpty) {
		this.isEmpty = isEmpty;
	}


	public boolean isEmpty() {
		return isEmpty;
	}


	public void setDynamic_wnd(int dynamic_wnd) {
		this.dynamic_wnd = dynamic_wnd;
	}


	public int getDynamic_wnd() {
		return dynamic_wnd;
	}
	public void addTPro(long transmittime){
		t_prolist.add(transmittime);
	}
	public ArrayList<Long> getTPro(){
		return t_prolist;
	}
}
