package jaist.jwns.entity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import jaist.jwns.aodv.RREP;
import jaist.jwns.aodv.RREQ;
import jaist.jwns.entity.HelloTable.HTable;
import jaist.jwns.event.EventArg;
import jaist.jwns.event.EventHandler;
import jaist.jwns.event.EventListener;
import jaist.jwns.event.HelloAction;
import jaist.jwns.event.QueueAction;
import jaist.jwns.tcp.ACKPacket;
import jaist.jwns.tcp.NAKPacket;
import jaist.jwns.tcp.TahoeAlgorithm;
import jaist.jwns.util.Buffer;
import jaist.jwns.util.Configuration;
import jaist.jwns.util.IPAddress;
import jaist.jwns.util.LPacket;
import jaist.jwns.util.LogPacket;
import jaist.jwns.util.LogThroughPut;
import jaist.jwns.util.LogTraffic;
import jaist.jwns.util.PacketList;
import jaist.jwns.util.Status;


/**
 * The class of node in the network environment.
 * This class includes too many functions. 
 * The author recommends other researcher divide this class to a number of classes.
 * @author lin
 *
 */
public class Node {
	/**
	 * The list of links which send the packets to the route
	 */
	private LinkedList<Link> links=new LinkedList<Link>();
	private int node_id=0;
	private long x;
	private long y;
	private double range;
	private int default_queueStatus=0;
	/**
	 * The name of the node.
	 */
	private String name;
	private IPAddress src;
	private IPAddress dst;
	private long pk_id;
	private Timer timer=new Timer();
	private int queueState=Status.Free;
	private int sendState=Status.Success;
	private Buffer bf;
	private ProcessDevice process_handler;
	private Timer queue_timer=new Timer();
	private Timer hello_timer=new Timer();
	private QueueAction qaction;
	private ThroughPut output=ThroughPut.getThroughPut();
	private HelloAction haction;
	private long hello_sequenceid=1;
	private Node my_node;
	private HelloTable hello_table;
	private long node_seq=0;
	private long send_pnumber=0;
	private long lpacket_number=0;
	private long ack_sequence=0;
	private int temp_send_pnumber=0;
	private TahoeAlgorithm tahoe;
	private boolean closed=false;
	private ArrayList<LPacket> tmp_lpacket_list=new ArrayList<LPacket>();
	private PacketList packetlist=new PacketList();
    private LastMsgList lastmsglist=new LastMsgList();
    private OutPutList outputlist=new OutPutList();
    private long delay_time;
    private long cbr_value;
    private NetWorkThroughPut nw_throughput;
    private Configuration config=Configuration.getConfig();
	
    /**
     * Construct of node
     * @param id is the id of node
     * @param name is the name of node
     * @param src is the IP Address of node
     * @param queue_size is the queue size of the node
     */
	public Node(int id, String name, IPAddress src,long queue_size){
		this.node_id=id;
		this.name=name;
		this.src=src;
		bf=new Buffer(queue_size);
		haction=new HelloAction();
		hello_table=new HelloTable();
		my_node=this;
		tahoe=new TahoeAlgorithm(config.getWNDAlgorithm());
		nw_throughput=NetWorkThroughPut.getThroughPut();
	}
	/**
	 * Initial the node, before use node to send the packet the node should be initialized
	 */
	public void init(){
		
		if(links!=null){
			for(Link link:links){
				link.getSFAction().add(linkListener);
				link.getRREQHandler().add(rreq_Listener);
				link.getRREPHandler().add(rrep_Listener);
				link.getACKHandler().add(ack_Listener);
				link.getNAKHandler().add(nak_Listener);
			}
		}
		else{
			System.out.println("The Node " +node_id+". "+name+" is the island node." );
		}	
			qaction=new QueueAction();
			qaction.getQueueEvent().add(queueListener);
			startQueueSchedule();
		//start send Hello-Message
		startHelloMessage();
	}
	private void startHelloMessage(){		
		TimerTask tk=new TimerTask(){
			@Override
			public synchronized void run() {
				// TODO Auto-generated method stub
				HelloMessage hm=new HelloMessage("Hello",my_node.src.toString(),"FF",hello_sequenceid);
				haction.getHelloEvent().invoke(my_node,hm);
				hello_sequenceid++;
			}
			
		};
		hello_timer.scheduleAtFixedRate(tk, 0, config.getHelloTimeInterval());
		//hello_table.init(); // hello table expired function (for future extend)
	}
	public void addProcessDevice(ProcessDevice pd){
		this.process_handler=pd;
	}
	public void addLinks(LinkedList<Link> links){
		this.links=links;
	}
	public void addLink(Link lk){
		if(!this.links.contains(lk)){
			this.links.add(lk);
		}
		
	}
	/**
	 * send the packet to the network
	 * @param datalength is the length of the packet
	 * @param delay  is the delay time of the send operation. unit: millisecond
	 * @param dst   is the IP address of destination
	 */
	public void sendPacket(long delay,IPAddress dst2){
		this.dst=dst2;		
		TimerTask tk=new TimerTask(){

			@Override
			public void run() {
				// TODO Auto-generated method stub
				
				sendRREQ();
			}
			
		};
		timer.schedule(tk, delay);
	}
	/**
	 * 
	 * @param Packet  The length of the packet, unit: Mbytes
	 * @param delay   send delay time, unit: millisecond
	 * @param dst2    IP address of destination
	 */
	public void sendPacket(int Packet,long delay,IPAddress dst2){
		this.dst=dst2;
		this.lpacket_number=(long)Math.ceil((double)Packet*1000*1000/config.getLPacketLength());
		System.out.println("LPacket Number "+lpacket_number);
		TimerTask tk=new TimerTask(){

			@Override
			public void run() {
				// TODO Auto-generated method stub
				System.out.println("Src "+src.toString()+" Send Data");
				sendRREQ();				
			}
			
		};
		timer.schedule(tk, delay);
	}
	
	/**
	 * Only used for the source node, generate and send the LPacket.
	 * @param next  IP Address of next node
	 * @param delay is the delay time when send the packet
	 */
	private void sendFromSimBuffer(String next,double delay){
		if(links!=null){
			if(links.size()>0){
				
				for(Link lk:links){
				  if(lk.getNext_node()!=null){											
					if(lk.getNext_node().equals(next)){
						if(temp_send_pnumber<tahoe.getWnd()){							
							pk_id++;
							temp_send_pnumber++;
							LPacket lpk=new LPacket(pk_id,src,dst);
							lpk.setHop(0);
							lpk.setSend_time_src(0);
							lpk.setRec_time_dst(0);
							lpk.setTx(src.toString());
							lpk.setWnd(tahoe.getWnd());
							lpk.setDynamic_wnd(tahoe.getWnd());
							lpk= process_handler.process(lpk);
							lpk.setInterval((long)delay);
							if(pk_id==lpacket_number){
								
								lpk.setProperty(1);
								System.out.println("Finial packet: "+pk_id);
							}
							
							lpk.setWnd_id(temp_send_pnumber);		
							lk.sendStart(lpk,process_handler.getProc_Time());
							send_pnumber++;														
						}

					}
				  }
				}					
			}
		}
	}
    /**
     * The Event of NAK, it represents the node received the nak packet
     */
	private EventListener nak_Listener=new EventListener(){

		@Override
		public void doEvent(Object sender, EventArg e) {
			// TODO Auto-generated method stub
			//receive nak
			// if node is not the sender, then re-send it
			// if the node is the sender then check the lost packet, and re-send the lost packet
			String nak_sender=(String)e.getArg(1);
			NAKPacket nak=(NAKPacket)e.getArg(0);
			if(!src.toString().equals(nak_sender)){
				if(src.toString().equals(nak.getSrc())){
					sendState=Status.Failed;
					ArrayList<Long> lost_id=nak.getLost_list();
					if(config.isPrintLostPackets()){
						System.out.println("Got NAK: "+lost_id);
					}
					
					if(lost_id!=null){
						for(Long id:lost_id){
							LPacket lpk=new LPacket(id,src,dst);
							lpk.setHop(0);
							lpk.setSend_time_src(0);
							lpk.setRec_time_dst(0);
							lpk.setTx(src.toString());
							lpk.setWnd(tahoe.getWnd());
							lpk.setDynamic_wnd(lost_id.size());
							lpk= process_handler.process(lpk);
							lpk.setWnd_id(0);  //¡ŸΩÁ÷µ
							
							tmp_lpacket_list.add(lpk);
						}
					}
					sendFailedFromSimBuffer(process_handler.searchRouteTable(dst.toString()),nak.getSp_time());
					
				}
				else{
					//re-send 
					process_handler.processNAK(nak);
					String next=findRTNext(nak.getSrc());
					if(next!=""){
						Link lk=findLink(next);
						if(lk!=null){
							lk.sendNAK(nak, src.toString());
						}
					}
				}
			}			
		}
		
	};
	/**
	 * The Event of ACK, it represents the node received the ack packet
	 */
	private EventListener ack_Listener=new EventListener(){

		@Override
		public void doEvent(Object sender, EventArg e) {
			// TODO Auto-generated method stub
			//receive ack
			//if the node is not the packet sender, then re-send it.
			//if the node is the packet sender then check it, and continue to send packet
			ACKPacket ack=(ACKPacket)e.getArg(0);
			String ack_sender=(String)e.getArg(1);
			if(!src.toString().equals(ack_sender)){
				//check it
				
				if(src.toString().equals(ack.getSrc())){
						//continue to send packet
						sendState=Status.Success;
						if(send_pnumber<lpacket_number){
							temp_send_pnumber=0;
							int wnd= tahoe.process();
							sendFromSimBuffer(process_handler.searchRouteTable(dst.toString()),ack.getSp_time());
							System.out.println(src.toString()+" Continue send data: "+send_pnumber);							
						}					
				}
				else{
					//re-send
					process_handler.processACK(ack);
					String next=findRTNext(ack.getSrc());
					if(next!=""){
						Link lk=findLink(next);
						if(lk!=null){
							lk.sendACK(ack, src.toString());
						}
					}
				}
			}
		}
		
	};
    /**
     *The Event of RREQ, it represents the node received the RREQ packet
     */
	private EventListener rreq_Listener=new EventListener(){

		@Override
		public void doEvent(Object sender, EventArg e) {
			// TODO Auto-generated method stub
			RREQ rq=(RREQ)e.getArg(0);
			if(rq!=null){
				if(!rq.getTx().equals(src.toString())){
					if(!rq.getSrc().equals(src.toString())){
						boolean isbroadcast= processRREQ(rq,src.toString());
						if(isbroadcast){
							reSendRREQ(rq);
						}
					}
					
				}
				
			}
		}
		
	};
	/**
	 * The Event of RREP, it represents the node received the RREP packet
	 */
	private EventListener rrep_Listener=new EventListener(){

		@Override
		public void doEvent(Object sender, EventArg e) {
			// TODO Auto-generated method stub
			RREP rrep=(RREP)e.getArg(0);
			RREP rp=(RREP)rrep.clone();
			String event_self=(String)e.getArg(1);
			if(!event_self.equals(src.toString())){
				
			if(!rp.getDst().equals(src.toString())){
				if(!rp.getTx().equals(src.toString())){
					//print RREP for test
					//System.out.println("Node: "+src.toString()+" RREP: "+"Src: "+rp.getSrc()+" SpendTime: "+rp.getRREP_spend_time()+"ms");
					//
					if(src.toString().equals(rp.getSrc())){
						//can send Data
						RouteTable rt=process_handler.getRouteTable();
						if(rt!=null){
							RouteItem ritem=rt.getItem(rp.getDst());
							if(ritem!=null){
								if(ritem.getDst().equals(rp.getDst()) && rp.getDst_seq()<= ritem.getSequence_no()){
									//do not update
								}
								else{
									ritem.setDst(rp.getDst());
									ritem.setNext(rp.getTx());
									ritem.setHop(rp.getHop_count()+1);
									ritem.setSequence_no(rp.getDst_seq());
									ritem.setTime_stamp(new Date());
									String next=findRTNext(dst.toString());						
									setLinkConnection(next);
									sendFromSimBuffer(next,0);
								}
								
							}
							else{
								//create routing table
								long hop_count=rp.getHop_count()+1;
								RouteItem new_ritem=new RouteItem(rp.getDst(),rp.getTx(),hop_count,rp.getDst_seq());
								new_ritem.setTime_stamp(new Date());
								rt.addItem(new_ritem);
								String next=findRTNext(dst.toString());						
								setLinkConnection(next);
								sendFromSimBuffer(next,0);
							}
						}
						
						
					}
					else{
						//update RoutingTable
						RouteTable rt=process_handler.getRouteTable();
						if(rt!=null){
							RouteItem ritem=rt.getItem(rp.getDst());
							int hop_count=rp.getHop_count()+1;
							if(ritem!=null){							
									//update routing table
									ritem.setDst(rp.getDst());
									ritem.setNext(rp.getTx());
									ritem.setHop(hop_count);
									ritem.setSequence_no(rp.getDst_seq());
									ritem.setTime_stamp(new Date());
							}
							else{
								//create routing table
								
								RouteItem new_ritem=new RouteItem(rp.getDst(),rp.getTx(),hop_count,rp.getDst_seq());
								new_ritem.setTime_stamp(new Date());
								rt.addItem(new_ritem);
							}						
							rp.setHop_count(hop_count);
							process_handler.processRREP(rp);
							sendRREP(rp);
						}
						
					}
				}
				
			}
			
		}
		}
		
	};
	/**
	 *The Event of Link Action, it represents the node received message from link. 
	 *The message can be a lpacket or a notice
	 */
	private  EventListener linkListener=new EventListener(){

		@Override
		public synchronized void doEvent(Object sender, EventArg e) {
			// TODO Auto-generated method stub
			Link link=(Link)sender;
			String tx=(String)e.getArg(0);
			String lk_src=(String)e.getArg(2);
			if(tx!=null){
							
			if(tx.equals(src.toString())){
				//out link
				if(sendState==Status.Success){
					if(lk_src.equals(src.toString())){
						
						if(send_pnumber<lpacket_number){
							sendFromSimBuffer(process_handler.searchRouteTable(dst.toString()),0);
							
						}
						else{
							link.sendFinished(true);
							Close();
						}
						
					}
					else{
						if(queueState==Status.Send){
							if(link!=null){
								if(bf.size()>0){	
									String rx=(String)e.getArg(3);
									long transmit_time=(Long)e.getArg(4);
									LastPacketMsg msg=lastmsglist.getLastPacketMsg(rx);
									if(msg!=null){
										msg.setTransmit_time(transmit_time);
									}
									sendFromBuffer();
								}
								else{
									queueState=Status.Free;
								}
							}
						}
					}
				}
				else if(sendState==Status.Failed){
					sendFailedFromSimBuffer(process_handler.searchRouteTable(dst.toString()),0);
				}
				
			}
			else{
				//inlink
				if(link!=null){	
					link.setRx_status(default_queueStatus);
					LPacket lp=link.received();
					dst=lp.getDst();
					String nextip= process_handler.searchRouteTable(lp.getDest());
					if(nextip!=null){
						setLinkConnection(nextip);
					}
					
					if(lp.getDest().toString().equals(src.toString())){
						
						StoredPackets sp= packetlist.getStoredPackets(lp.getSrc());
						if(sp==null){
							StoredPackets new_sp=new StoredPackets(lp.getSrc(),my_node);
							new_sp.addStoredPacket(lp);
							packetlist.add(new_sp);
							sp=new_sp;
						}
						else{
							
							sp.addStoredPacket(lp);
							
						}
						int WND=lp.getWnd();
						if(sp.getLostList().size()==0){
							if(lp.getWnd_id()==WND || lp.getProperty()==1){
								//check LPacket list					
								ArrayList<Long> lost_id= checkLPacketList(lp.getWnd_id(),sp);
								if(lost_id.size()>0){									
										//create NAK send to source node
									    sp.setNak_wait(true);
									    sp.start(lp);
									    sp.setLostID(lost_id);
										NAKPacket nak=new NAKPacket(lost_id);
										nak.setSp_time(lp.getPk_send_time());
										nak.setSrc(lp.getSrc());
										nak.setDst(src.toString());
										process_handler.processNAK(nak);
										String next=findRTNext(lp.getSrc());
										if(next!=""){
											Link lk=findLink(next);
											if(lk!=null){
												lk.sendNAK(nak, src.toString());
												if(config.isPrintLostPackets()){
													System.out.println("Send NAK : "+lost_id+" "+lp.getSrc());
												}
												
											}
										}									
								}
								else{
									sendACK(lp, sp);
								}
								
							}
						}
						else{
							sp.removeID(lp);
							if(sp.getLostList().size()==0){
								sendACK(lp, sp);
							}
						}
						

						long rec_time=link.getRealTransmitTime()+lp.getPk_send_time();
						lp.setRec_time_dst(rec_time);
						lp.setPk_send_time(0);
						long hop= lp.getHop()+1;
						lp.setHop(hop);
						lp=process_handler.process(lp);		
						output.addDiffSrcLPacket(lp);	
						outputlist.addLPacket(lp);
					}
					else{
						
						long pst=lp.getPk_send_time();
						lp.setPk_send_time(link.getRealTransmitTime()+pst);
						long hop= lp.getHop()+1;
						lp.setHop(hop);	
						Enqueue(lp);
					}
					
													
				}
			  }
			}
		}

				
	};
	/**
	 * Send packet from queue
	 */
	private void sendFromBuffer() {
		if(links!=null){
			if(links.size()>0){
				LPacket lpk=Dequeue();
				String next=process_handler.searchRouteTable(lpk.getDest());
				
				
				setLinkConnection(next);
				for(Link lk:links){
					if(lk.getNext_node()!=null){
						if(lk.getNext_node().equals(next)){
							if(config.getReal()){
								
								LastPacketMsg msg= lastmsglist.getLastPacketMsg(next);
								LastPacketMsg lastmsg=null;
								if(msg!=null)
								{
									lastmsg=(LastPacketMsg)msg.clone();
								}
								LastPacketMsg new_msg=new LastPacketMsg(next,lpk.getEnd_stamp());
								lastmsglist.add(new_msg);
								lk.sendStart(process_handler.process(lpk),process_handler.getReal_Proc_Time(lpk,lastmsg));
								
							}
							else{
								lk.sendStart(process_handler.process(lpk),process_handler.getProc_Time());
							}
							
						}
					}
					
				}
				queueState=Status.Send;
				
			}
		}
	}
	/**
	 * Send ACK Packet
	 * @param lp 
	 * @param sp
	 */
	private void sendACK(LPacket lp, StoredPackets sp) {
		sp.setNak_wait(false);
		sp.cancel();
		sp.setLast_id();
		packetlist.clearStoredPackets(lp.getSrc());
		ack_sequence++;
		ACKPacket ack=new ACKPacket(ack_sequence,lp.getSequence_id());
		ack.setSp_time(lp.getPk_send_time());
		ack.setSrc(lp.getSrc());
		ack.setDst(src.toString());
		process_handler.processACK(ack);
		String next=findRTNext(lp.getSrc());
		if(next!=""){
			Link lk=findLink(next);
			if(lk!=null){
				lk.sendACK(ack, src.toString());
			}
		}
	}
	
	private void shouldSendACK(StoredPackets sp){
		String send_src=sp.getIpAddress();
		double spend_time=sp.getSpendTime();
		ArrayList<Long> lplist= sp.getLplist();
		long sqid=0;
		if(lplist!=null){
			int index=lplist.size()-1;
			sqid=lplist.get(index);
		}
		sp.setNak_wait(false);
		sp.cancel();
		sp.setLast_id();
		packetlist.clearStoredPackets(send_src);
		ack_sequence++;
		
		ACKPacket ack=new ACKPacket(ack_sequence,sqid);
		ack.setSp_time(spend_time);
		ack.setSrc(send_src);
		ack.setDst(src.toString());
		process_handler.processACK(ack);
		String next=findRTNext(send_src);
		if(next!=""){
			Link lk=findLink(next);
			if(lk!=null){
				lk.sendACK(ack, src.toString());
			}
		}
	}
	/**
	 * The Event of Queue. 
	 * When the queue is idle, the queue will send the packet periodically.
	 */
	private EventListener queueListener=new EventListener(){		
		@Override
		public void doEvent(Object sender, EventArg e) {
			// TODO Auto-generated method stub
				if(queueState==Status.Free){
					if(bf.size()>0){	
						//
						sendFromBuffer();												
			        }
				}

		} 
	};
	/**
	 * Enqueue the packet to the buffer
	 * @param packet
	 */
	private boolean Enqueue(LPacket packet){
		if(bf.size()==0){
			packet.setEmpty(true);
		}
		else{
			packet.setEmpty(false);
		}
		boolean res=bf.enqueue(packet);
		packet.setStart_stamp(System.currentTimeMillis());
		return res;
	}
	/**
	 * Send the packets when the node received NAK
	 * @param next
	 * @param delay
	 */
	private void sendFailedFromSimBuffer(String next,
			double delay) {
		// TODO Auto-generated method stub
		if(tmp_lpacket_list!=null){
			if(tmp_lpacket_list.size()>0){
				if(links!=null){
					if(links.size()>0){						
						for(Link lk:links){
						  if(lk.getNext_node()!=null){															
							if(lk.getNext_node().equals(next)){
								LPacket lpk=tmp_lpacket_list.get(0);
								lpk.setInterval((long)delay);
								lk.sendStart(lpk,process_handler.getProc_Time());
								tmp_lpacket_list.remove(lpk);
							}
						  }
						}
					}
				}
			}
		}
	}
	/**
	 * Dequeue the packet from the buffer
	 * @return
	 */
	private LPacket Dequeue(){
		LPacket lp=bf.dequeue();
		lp.setEnd_stamp(System.currentTimeMillis());
		return lp;
	}
	private void startQueueSchedule(){
		queue_timer.scheduleAtFixedRate(qaction, 0, config.getQueuePeriod());
	}
	/**
	 * Depose the node
	 */
	public void Close(){		
		if(!closed){
			closed=true;
			if(queue_timer!=null){
				queue_timer.cancel();
				queue_timer=null;
			}
			if(timer!=null){
				timer.cancel();
				timer=null;
			}						
			if(hello_timer!=null){
				hello_timer.cancel();
				hello_timer=null;
			}
			if(hello_table!=null){
				hello_table.Close();
			}
			if(links!=null){
				for(Link outlink:links){
					if(outlink.getNext_node()!=null){
						if(!outlink.getNext_node().equals(src.toString())){
							outlink.closeLink();	
						}
					}						
						
				}
			}
			if(outputlist.size()>0){
				ArrayList<String> srclist=outputlist.getSrcList();
				for(String name:srclist){
					ArrayList<LPacket> plist=outputlist.getLPackets(name);
					long total_time=0;
					for(LPacket lp:plist){
						total_time=lp.getRec_time_dst()+total_time;
					}
					long total_number=plist.size();				
					OutPutPacket output=outputlist.getOutPutPacket(name);
					double e2e_Throughput=(double)total_number*output.getLength()*8.0/(total_time*1000);
					String msg=output.getSrc()+" To "+output.getDst()+" ThroughPut: "+e2e_Throughput+" Mbps";
					System.out.println(msg);
					nw_throughput.addThroughputMsg(msg);
					nw_throughput.addThroughput(e2e_Throughput);					
				}
				
			}
		}		
	}

	public EventHandler getHAction(){
		return haction.getHelloEvent();
	}
	private long hm_id=0;
	public void receivedHelloMessage(HelloMessage hm){
		if(hm.getSequence_no()>hm_id){
			hello_table.updateTable(hm.getTx(), hm.getTx(),this.name);
			hm_id=hm.getSequence_no();
		}
	}
	public IPAddress getSrc(){
		return this.src;
	}
	public String getName(){
		return this.name;
	}
	public void setX(long x) {
		this.x = x;
	}
	public long getX() {
		return x;
	}
	public void setY(long y) {
		this.y = y;
	}
	public long getY() {
		return y;
	}
	public void setRange(double range) {
		this.range = range;
	}
	public double getRange() {
		return range;
	}
	private void setLinkConnection(String next){
		for(Link lk:links){
			if(!existNext(next)){
				if(lk.getAnotherNode(this.src.toString()).equals(next)){
					lk.setNext_node(next);
					lk.setSrc_node(this.src.toString());
					break;
				}
			}
			
		}
	}
	private boolean existNext(String next){
		for(Link lk:links){
			if(lk.getNext_node()!=null){
				if(lk.getNext_node().equals(next)){
					return true;
				}
				
			}
		}
		return false;
	}
	private synchronized void reSendRREQ(RREQ rreq){
		if(hello_table!=null){
			long hop=rreq.getHop_count();
			ArrayList<HTable> tables=hello_table.getTables();
			if(tables!=null){
				if(tables.size()>0){					
					for(HTable table:tables){
						if(table!=null){							
							Link lk=findLink(table.getNext());
							if(lk!=null){							
								rreq.setTx(this.src.toString());
								rreq.setHop_count(hop);
								lk.sendRREQ(rreq);	
							}
						}
					}
					
				}
		   }
		}
	}
	private void sendRREQ(){
						
		RREQ rreq=InitRREQ();
		if(hello_table!=null){
			ArrayList<HTable> tables=hello_table.getTables();
			if(tables!=null){
				if(tables.size()>0){					
					for(HTable table:tables){
						if(table!=null){							
							Link lk=findLink(table.getNext());
							if(lk!=null){
								lk.sendRREQ(rreq);
							}
						}
					}
					
				}
		   }
		}
		
	}
	
	private String findRTNext(String dst){
		String next="";
		RouteTable rt= process_handler.getRouteTable();
		if(rt!=null){
			RouteItem item= rt.getItem(dst);
			if(item!=null){
				next=item.getNext();
			}
			
		}
		return next;
	}
	private void sendRREP(RREP rp){
		String next=findRTNext(rp.getSrc());
		if(next!=""){
			Link lk=findLink(next);
			if(lk!=null){
				rp.setTx(this.src.toString());
				lk.sendRREP(rp,this.src.toString());
			}
		}
	}
	private RREQ InitRREQ(){
		node_seq++;
		RREQ rreq=new RREQ();
		rreq.setType("RREQ");
		rreq.setSrc(this.src.toString());
		rreq.setSrc_seq(node_seq);
		rreq.setDst(this.dst.toString());
		rreq.setHop_count(0);
		rreq.setLength(config.getRREQLength());
		rreq.setRREQ_hop_max(5);
		rreq.setRREQ_retry_count(3);
		rreq.setTx(this.src.toString());
		return rreq;
	}
	private Link findLink(String next){
		if(next!=null){
			for(Link lk:links){
				String next_node=lk.getAnotherNode(this.src.toString());
				if(next_node!=null){
					if(next_node.equals(next)){
						return lk;
					}
				}
				
			}
		}		
		return null;
	}
	public synchronized boolean processRREQ(RREQ rreq, String rx){
		boolean isbroadcast=true;
		RouteTable rt=process_handler.getRouteTable();
		
		if(rt!=null){		
			if(rreq.getDst().equals(rx)){
				//create RREP, routing table and discard RREQ
				isbroadcast=false;
				RouteItem ritem=rt.getItem(rreq.getSrc());
				if(ritem!=null){					
					if(ritem.getDst().toString().equals(rreq.getSrc()) &&  rreq.getSrc_seq()<=ritem.getSequence_no()){					
						isbroadcast=false;
						rreq=null;
						return isbroadcast;
					}
				}
				
				RREQ clone_rq=(RREQ)rreq.clone();
				GreedyHopAlgorithm(clone_rq);				
			}
			else{
				RouteItem ritem=rt.getItem(rreq.getSrc());
				if(ritem!=null){
					if(ritem.getDst().toString().equals(rreq.getSrc()) &&  rreq.getSrc_seq()<=ritem.getSequence_no()){
						if(rreq.getHop_count()<ritem.getHop()-1){
							//update routing table
							ritem.setDst(rreq.getSrc());
							ritem.setNext(rreq.getTx());
							long hop_count=rreq.getHop_count()+1;
							ritem.setHop(hop_count);
							ritem.setSequence_no(rreq.getSrc_seq());
							ritem.setTime_stamp(new Date());
							rreq.setHop_count(hop_count);
							isbroadcast=true;
						}
						else{
							//discard
							rreq=null;
							isbroadcast=false;
							return isbroadcast;
						}
						
					}
					else{
						//update routing table
						ritem.setDst(rreq.getSrc());
						ritem.setNext(rreq.getTx());
						long hop_count=rreq.getHop_count()+1;
						ritem.setHop(hop_count);
						ritem.setSequence_no(rreq.getSrc_seq());
						ritem.setTime_stamp(new Date());
						rreq.setHop_count(hop_count);
						isbroadcast=true;
					}
				}
				else{
					//create routing table
					long hop_count=rreq.getHop_count()+1;
					RouteItem new_item=new RouteItem(rreq.getSrc(),rreq.getTx(),hop_count,rreq.getSrc_seq());
					new_item.setTime_stamp(new Date());
					rt.addItem(new_item);
					rreq.setHop_count(hop_count);
					isbroadcast=true;
				}
			}			
		}
		return isbroadcast;
	}
	private boolean hasSmallHop(RREQ rq, LinkedList<RREQ> rqlist){
		boolean highhop=true;
		for(RREQ rreq:rqlist){
			if(rq.getSrc().equals(rreq.getSrc())){
				if(rq.getHop_count()>rreq.getHop_count()){
					return false;
				}
			}
		}
		//System.out.println("SmallHop "+"Tx: "+rq.getTx()+ " hop: "+rq.getHop_count());
		return highhop;
	}
	private LinkedList<RREQ> rqlist=new LinkedList<RREQ>();
	private boolean isStartRP_timer=false;
	private Timer RP_timer=new Timer();;
	private void GreedyHopAlgorithm(RREQ rreq) {
		// TODO Auto-generated method stub						
		rqlist.add(rreq);
		if(!isStartRP_timer){
			TimerTask tk=new TimerTask(){

				@Override
				public void run() {
					// TODO Auto-generated method stub
					//process RREQ list, find best route, update/create routing table
					//
					RouteTable rt=process_handler.getRouteTable();
					
					if(rqlist!=null){
						for(RREQ rq:rqlist){
							if(hasSmallHop(rq,rqlist)){
								
								RouteItem ritem=rt.getItem(rq.getSrc());
								long hop_count=rq.getHop_count()+1;
								if(ritem!=null){																		
									//update routing table			
									//
									ritem.setDst(rq.getSrc());
									ritem.setNext(rq.getTx());
									ritem.setHop(hop_count);
									ritem.setSequence_no(rq.getSrc_seq());
									ritem.setTime_stamp(new Date());																
							}
							else{
								RouteItem new_item=new RouteItem(rq.getSrc(),rq.getTx(),hop_count,rq.getSrc_seq());
								new_item.setTime_stamp(new Date());
								rt.addItem(new_item);
							}
							//create RREP
							RREP rp=InitRREP(rq);
							sendRREP(rp);
							}
						}
					}
					rqlist.clear();
					isStartRP_timer=false;
				}
				
			};
			RP_timer.schedule(tk, config.getRREP_live_interval());
			isStartRP_timer=true;
		}
		
	}
	private RREP InitRREP(RREQ rreq){
	    node_seq++;
		RREP rp=new RREP();
		rp.setType("RREP");
		rp.setSrc(rreq.getSrc());
		rp.setDst(rreq.getDst());
		rp.setDst_seq(node_seq);
		rp.setHop_count(0);
		rp.setLength(config.getRREPLength());
		rp.setRREP_hop_max(5);
		rp.setRREP_life_time(10000);
		rp.setRREP_retry_count(3);
		rp.setRREP_spend_time(0);
		return rp;
	}
	/**
	 * Print Routing Table
	 */
	public void printRT(){
		RouteTable rt=process_handler.getRouteTable();
		if(rt!=null){
			List<RouteItem> items= rt.getItems();
			System.out.println("Route Table "+this.name);
			for(RouteItem item:items){
				if(item!=null){
					System.out.println(this.name+" : "+item.getDst()+" "+item.getNext()+" "+item.getHop()+" "+item.getSequence_no());
					
				}
			}
		}
	}
	public ArrayList<HTable> getHelloTable(){
		return hello_table.getTables();
	}
	private ArrayList<Long> checkLPacketList(int wnd,StoredPackets ssp) {
		// TODO Auto-generated method stub
		StoredPackets sp=ssp;
		ArrayList<Long> lost_id=new ArrayList<Long>();
		
		if(sp!=null){
			ArrayList<Long> storedid_list=sp.getLplist();
			Collections.sort(storedid_list);
			//int size=storedid_list.size();						
			long first=storedid_list.get(0);  
			long last=first-1+wnd;
			//System.out.println("First: "+first+" Last: "+last+" Real "+storedid_list.get(size-1));
			if(first!=last){
				for(long i=first;i<=last;i++){
					if(!sp.getLplist().contains(i)){				
						lost_id.add(i);
					}
				}
			}						
		}		
		return lost_id;
	}
	/**
	 * When the destination does not received the last LPacket in the last WND.
	 * The destination will send a nak packet automatically.
	 * @param sp
	 */
	public void shouldSendNAK(StoredPackets sp){
		ArrayList<Long> lost_id=new ArrayList<Long>();
		if(sp.getLostList().size()>0){
			lost_id=sp.getLostList();
		}
		else{
			lost_id= checkLPacketList(sp.getWnd(),sp);
		}		
		if(lost_id.size()==0){
//			System.out.println("WARN!! "+sp.getIpAddress()+" "+sp.getLast_id());
			shouldSendACK(sp);
		}
		else{
			
		    sp.reStart();
		    sp.setNak_wait(true);
		    sp.setLostID(lost_id);
			NAKPacket nak=new NAKPacket(lost_id);
			nak.setSp_time(sp.getSpendTime());
			nak.setSrc(sp.getIpAddress());
			nak.setDst(src.toString());
			process_handler.processNAK(nak);
			String next=findRTNext(sp.getIpAddress());
			if(next!=""){
				Link lk=findLink(next);
				if(lk!=null){
					lk.sendNAK(nak, src.toString());
					if(config.isPrintLostPackets()){
						System.out.println("Send NAK due to the expiration!");
						System.out.println("Send NAK : "+lost_id+" "+sp.getIpAddress());
					}
					
				}
			}
	    }
	}
	public void setDefault_queueStatus(int default_queueStatus) {
		this.default_queueStatus = default_queueStatus;
	}
	public int getDefault_queueStatus() {
		return default_queueStatus;
	}
	public void setDelay_time(long delay_time) {
		this.delay_time = delay_time;
	}
	public long getDelay_time() {
		return delay_time;
	}
	public void setCbr_value(long cbr_value) {
		this.cbr_value = cbr_value;
	}
	public long getCbr_value() {
		return cbr_value;
	}
	/***
	 * CBR sends packet (future extend)
	 */
	public void sendPacketbyCBR(){
		
	}
}
