package jaist.jwns.entity;

import jaist.jwns.aodv.RREP;
import jaist.jwns.aodv.RREQ;
import jaist.jwns.event.ACKAction;
import jaist.jwns.event.EventHandler;
import jaist.jwns.event.NAKAction;
import jaist.jwns.event.RREPAction;
import jaist.jwns.event.RREQAction;
import jaist.jwns.event.SFinishAction;
import jaist.jwns.tcp.ACKPacket;
import jaist.jwns.tcp.NAKPacket;
import jaist.jwns.util.Configuration;
import jaist.jwns.util.LPacket;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
 * The link between two nodes in the network
 * @author lin
 *
 */
public class Link {
	private String src_node;
	private String next_node;
    private Link m_link;
	private Timer timer=new Timer();
	private long total_timer=0;
	private long transmit_time=0;
	private long traffic_num=0;
	private boolean closed=true;
	private LPacket lpk;
	private LPacket tmp_lpk;
	private long real_transmit_time=0;
	/**
	 * The bandwidth of the link. unit: bps
	 */
	private long m_bandwidth;
	private SFinishAction sfaction;
	private Tracer tracer=new Tracer();
	private boolean is_open_tracer=false;	
	private RREQAction rqaction;
	private RREPAction rpaction;
	private ACKAction ackaction;
	private NAKAction nakaction;
	private ArrayList<String> node_pair=new ArrayList<String>();
	private boolean isFinished=false;
	private int rx_status=0;
	private Configuration config=Configuration.getConfig();
	
	public Link(long bdwidth){		
		this.m_bandwidth=bdwidth*1000*1000;
		calculateDelay(0);
		sfaction=new SFinishAction();
		sfaction.setLink(this);
		rqaction=new RREQAction();
		rpaction=new RREPAction();
		ackaction=new ACKAction();
		nakaction=new NAKAction();
		m_link=this;
		closed=false;	
	}

	public void openTracer(){
		is_open_tracer=true;
	}
	
	/**
	 * Send packet through this link
	 * @param lpk  is the LPacket
	 * @param proc_time  the processing time in the start node.
	 */
	public void sendStart(LPacket lpk,long proc_time){		
		if(!closed){
			this.tmp_lpk=(LPacket)lpk.clone();
			
			
			long D_t=0;
			if(config.getReal()){
				if(this.tmp_lpk.isEmpty()){
					D_t=proc_time+lpk.getInterval();
					this.tmp_lpk.setInterval(D_t);
				}
	            else{
	            	
	            	D_t=proc_time;
	            	this.tmp_lpk.setInterval(proc_time);
	            }
			}
			else{
				if(rx_status==2){
					if(this.tmp_lpk.isEmpty()){
						D_t=proc_time+lpk.getInterval();
						this.tmp_lpk.setInterval(D_t);
					}
					else{
						D_t=proc_time;
		            	this.tmp_lpk.setInterval(proc_time);
					}
				}
				else{
					if(rx_status==0){
						D_t=proc_time+lpk.getInterval();
						this.tmp_lpk.setInterval(D_t);
					}
		            else if(rx_status==1){		            	
		            	D_t=proc_time;
		            	this.tmp_lpk.setInterval(proc_time);
		            }
				}
				
			}
            
			calculateDelay(D_t);
			this.tmp_lpk.addTPro(real_transmit_time);
			
			TimerTask tk=new TimerTask(){
				@Override
				public void run() {
					// TODO Auto-generated method stub
					if(sendEnd()){												
						setLPacket(tmp_lpk);						
						sfaction.getHandler().invoke(m_link, tmp_lpk.getTx(),tmp_lpk.getDest(),tmp_lpk.getSrc(),tmp_lpk.getRx(),real_transmit_time);
					}					
				}
				
			};
			timer.schedule(tk, real_transmit_time);
		}		
	}
	
	private synchronized void setLPacket(LPacket pk){		
		this.lpk=pk;
	}
	/**
	 * check whether the sending procedure is finished
	 * @return
	 */
	public boolean sendEnd(){

		total_timer=total_timer+transmit_time;
		if(config.getTraceTime()-total_timer<0){
			if(is_open_tracer){
				tracer.logTraffic(traffic_num-1,node_pair);				
			}
			total_timer=0;
			traffic_num=0;
		}
		else{
			traffic_num++;
		}
			return true;
		
	}
	/**
     * calculate how many times need to transfer the packet
     */
	private void calculateDelay(long proc_time){
		real_transmit_time=(long) Math.ceil((config.getLPacketLength() << 3)*1000.0 / (long) m_bandwidth);
		transmit_time =real_transmit_time+proc_time;	
	}
	/**
	 * Depose the link
	 */
	public void closeLink(){	
		if(!closed){
			closed=true;
			if(timer!=null){
				timer.cancel();
				timer=null;
			}		
			System.out.println("Link CLose");
			List<String> pair=this.getNodePair();
            String n1="";
            String n2="";
            if(pair!=null){
            	n1=pair.get(0);
            	n2=pair.get(1);
            	System.out.println("close: "+n1+"-->"+n2);
            }
            if(is_open_tracer){
            	tracer.WriteTraffic(n1, n2);
            }
			
		}
		
	}
	public EventHandler getSFAction(){
		return sfaction.getHandler();
	}
	public synchronized LPacket received(){
		return this.lpk;
	}
	public long getTrans_time(){
		
		return transmit_time;
	}
	public void setSrc_node(String src_node) {
		this.src_node = src_node;
	}
	public String getSrc_node() {
		return src_node;
	}
	public void setNext_node(String next_node) {
		this.next_node = next_node;
	}
	public String getNext_node() {
		return next_node;
	}
	public EventHandler getRREQHandler(){
		return rqaction.getRREQHandler();
	}
	public EventHandler getRREPHandler(){
		return rpaction.getRREPHandler();
	}
	public EventHandler getACKHandler(){
		return ackaction.getACKHandler();
	}
	public EventHandler getNAKHandler(){
		return nakaction.getNAKHandler();
	}
	public void sendACK(ACKPacket ack, String sender){
		double ack_transmit_time=(double)ack.getLength()*8*1000.0/this.m_bandwidth;
		double sp_time=ack.getSp_time();
		ack.setSp_time(sp_time+ack_transmit_time);
		ackaction.getACKHandler().invoke(this,ack, sender);
	}
	public void sendRREQ(RREQ rreq){
		rqaction.getRREQHandler().invoke(this, rreq);
	}
	public void sendRREP(RREP rrep,String sender){
		double rrep_transmit_time=(double)rrep.getLength()*8.0*1000.0/this.m_bandwidth;
		
		double sp_time=rrep.getRREP_spend_time();
		rrep.setRREP_spend_time(rrep_transmit_time+sp_time);
		rpaction.getRREPHandler().invoke(this, rrep,sender);
	}
	public void addNode(String nodeAddress){
		if(this.node_pair.size()<2){
			this.node_pair.add(nodeAddress);
		}		
	}
	public ArrayList<String> getNodePair(){
		return this.node_pair;
	}
	public String getAnotherNode(String nodeAddress){
		if(node_pair.size()>=2){
			for(String node:node_pair){
				if(!node.equals(nodeAddress)){
					return node;
				}
			}
		}
		return null;
	}
	public void sendFinished(boolean sender){
		
		if(!isFinished){
			isFinished=true;
			if(sender){
				if(is_open_tracer){
					tracer.logTraffic(traffic_num,node_pair);
				}
				total_timer=0;
				traffic_num=0;
			}
			else{				
				if(is_open_tracer){
					System.out.println("Last Traffic: ");
					tracer.logTraffic(traffic_num,node_pair);
					total_timer=0;
					traffic_num=0;
					closeLink();
				}								
			}
			
		}		
		
	}
	public void sendNAK(NAKPacket nak, String sender) {
		// TODO Auto-generated method stub
		double ack_transmit_time=(double)nak.getLength()*8*1000.0/this.m_bandwidth;
		double sp_time=nak.getSp_time();
		nak.setSp_time(sp_time+ack_transmit_time);
		nakaction.getNAKHandler().invoke(this,nak,sender);
		
	}

	public void setRx_status(int rx_status) {
		this.rx_status = rx_status;
	}

	public int getRx_status() {
		return rx_status;
	}
	public long getRealTransmitTime(){
		return real_transmit_time;
	}
	
	
}
