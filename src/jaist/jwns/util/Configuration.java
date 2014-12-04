package jaist.jwns.util;

public class Configuration {

	private  int LPacket_Length; 
	private  long trace_time;  
	private  long queue_period; 
	private  long hello_time_interval;	
	private  int RREQ_len; 
	private  int RREP_len; 	
	private  int WND_threshold;
	private  int WND_max;
	private  int WND_initial;
	private  boolean wnd_algorithm;
	private  boolean real;
	private int RREQ_hop_max;
	private  long RREP_live_interval;  
	private int send_packet_size;
	private boolean printRoutingTable=false;
	private boolean printLostPackets=false;
	private int link_bandwidth;
	//for future extending
	private  int RREQ_time_interval; 
	private  int RoutingTable_expiration; 	
	private  long hello_entry_expirationtime; 
	//
	public static Configuration config=null;
	public static Configuration getConfig(){
		if(config==null){
			config=new Configuration();
		}
		return config;
	}
	public void setLPacketLength(int len){
		this.LPacket_Length=len;
	}
	public int getLPacketLength(){
		return this.LPacket_Length;
	}
	public void setTraceTime(long time){
		this.trace_time=time;
		
	}
	public long getTraceTime(){
		return this.trace_time;
	}
	public void setQueuePeriod(long time){
		this.queue_period=time;
	}
	public long getQueuePeriod(){
		return this.queue_period;
	}
	public void setHelloTimeInterval(long time){
		this.hello_time_interval=time;
	}
	public long getHelloTimeInterval(){
		return this.hello_time_interval;
	}
	public void setRREQLength(int len){
		this.RREQ_len=len;
	}
	public int getRREQLength(){
	    return this.RREQ_len;
	}
	public void setRREPLength(int len){
		this.RREP_len=len;
	}
	public int getRREPLength(){
		return this.RREP_len;
	}
	public void setWNDThreshold(int threshold){
		this.WND_threshold=threshold;
	}
	public int getWNDThreshold(){
		return this.WND_threshold;
	}
	public void setWNDMax(int max){
		this.WND_max=max;
	}
	public int getWNDMax(){
		return this.WND_max;
	}
	public void setWNDInitial(int initial){
		this.WND_initial=initial;
	}
	public int getWNDInitial(){
		return this.WND_initial;
	}
	public void setWNDAlgorithm(int para){
		if(para==0){
			this.wnd_algorithm=false;
		}
		else{
			this.wnd_algorithm=true;
		}
	}
	public boolean getWNDAlgorithm(){
		return this.wnd_algorithm;
	}
	public void setReal(int para){
		if(para==0){
			this.real=false;
		}
		else{
			this.real=true;
		}
	}
	public boolean getReal(){
		return this.real;
	}
	public void setRREQ_time_interval(int rREQ_time_interval) {
		RREQ_time_interval = rREQ_time_interval;
	}
	public int getRREQ_time_interval() {
		return RREQ_time_interval;
	}
	public void setRoutingTable_expiration(int routingTable_expiration) {
		RoutingTable_expiration = routingTable_expiration;
	}
	public int getRoutingTable_expiration() {
		return RoutingTable_expiration;
	}
	public void setHello_entry_expirationtime(long hello_entry_expirationtime) {
		this.hello_entry_expirationtime = hello_entry_expirationtime;
	}
	public long getHello_entry_expirationtime() {
		return hello_entry_expirationtime;
	}
	public void setRREP_live_interval(long rREP_live_interval) {
		RREP_live_interval = rREP_live_interval;
	}
	public long getRREP_live_interval() {
		return RREP_live_interval;
	}
	public void setRREQ_hop_max(int rREQ_hop_max) {
		RREQ_hop_max = rREQ_hop_max;
	}
	public int getRREQ_hop_max() {
		return RREQ_hop_max;
	}
	public void setSend_packet_size(int send_packet_size) {
		this.send_packet_size = send_packet_size;
	}
	public int getSend_packet_size() {
		return send_packet_size;
	}
	public void setPrintRoutingTable(boolean printRoutingTable) {
		this.printRoutingTable = printRoutingTable;
	}
	public boolean isPrintRoutingTable() {
		return printRoutingTable;
	}
	public void setPrintLostPackets(boolean printLostPackets) {
		this.printLostPackets = printLostPackets;
	}
	public boolean isPrintLostPackets() {
		return printLostPackets;
	}
	public void setLink_bandwidth(int link_bandwidth) {
		this.link_bandwidth = link_bandwidth;
	}
	public int getLink_bandwidth() {
		return link_bandwidth;
	}
}
