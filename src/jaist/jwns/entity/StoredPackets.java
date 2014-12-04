package jaist.jwns.entity;

import jaist.jwns.util.LPacket;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Timer;
import java.util.TimerTask;

public class StoredPackets {

	private String ipAddress;
	private ArrayList<Long> lplist=new ArrayList<Long>();
	private ArrayList<Long> lostlist=new ArrayList<Long>();
	private boolean nak_wait=false;
	private long waiting_time;
	private Node parent;
	private Timer timer=new Timer();
    private TimerTask check_tk;
    private int wnd;
	private StoredPackets sp;
	private long OTT;
	private long last_id=1;
	public StoredPackets(String ip,Node node){
		setIpAddress(ip);
		this.parent=node;
		sp=this;
	}
    public void reStart(){
    	if(check_tk!=null){
			check_tk.cancel();
			check_tk=null;
		}
		TimerTask tk=new TimerTask(){
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				//print traffic in the last second
				
				parent.shouldSendNAK(sp);
				
			}
			
		};
		check_tk=tk;
		if(waiting_time<5000){
			timer.schedule(check_tk, 5000);
		}
		else{
			timer.schedule(check_tk, waiting_time); 
		}
    }
	public void start(LPacket lp){
	    OTT=lp.getPk_send_time();
		//long T_pro=OTT/lp.getHop();
	    ArrayList<Long> tpro_list=lp.getTPro();
	    double T_pro=0.0;
	    if(tpro_list!=null){
	    	for(long tpro:tpro_list){
	    		T_pro=T_pro+tpro;
	    	}
	    	T_pro=T_pro/tpro_list.size();
	    	//System.out.println("T_pro"+T_pro);
	    }
	    
		long RTT=2*OTT;
		//waiting_time=3*RTT+lp.getWnd()*T_pro;  //will be used later
		waiting_time=(long)(3*RTT+lp.getDynamic_wnd()*T_pro);
		wnd=lp.getWnd();
		//System.out.println("Waiting Time: "+waiting_time);
		if(check_tk!=null){
			check_tk.cancel();
			check_tk=null;
		}
		TimerTask tk=new TimerTask(){
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				//print traffic in the last second
				
				parent.shouldSendNAK(sp);
				
			}
			
		};
		check_tk=tk;
		if(waiting_time<5000){
			timer.schedule(check_tk, 5000);
		}
		else{
			timer.schedule(check_tk, waiting_time); 
		}
		
		//timer.schedule(check_tk, 8000);
	}
	public void cancel(){
		if(check_tk!=null){
			check_tk.cancel();
			check_tk=null;
		}
	}
	public ArrayList<Long> getLplist() {
		return lplist;
	}

	private void setIpAddress(String ipAddress) {
		this.ipAddress = ipAddress;
	}

	public String getIpAddress() {
		return ipAddress;
	}
	public void addStoredPacket(LPacket lp){
		
		if(lplist.size()==0){
			start(lp);
		}
		if(!lplist.contains(lp.getSend_time_src())){
			lplist.add(lp.getSequence_id());
		}
		
		
		//
	}
	public void removeID(LPacket lp){
		lostlist.remove(lp.getSequence_id());
		if(lostlist.size()==0){
			cancel();
			
		}
		else{
			start(lp);
		}
	}

	public void setNak_wait(boolean nak_wait) {
		this.nak_wait = nak_wait;
	}

	public boolean isNak_wait() {
		return nak_wait;
	}


	public int getWnd() {
		return wnd;
	}

	
	public long getOTT() {
		return OTT;
	}
	public void setLostID( ArrayList<Long> ids){
		this.lostlist=(ArrayList<Long>)ids.clone();
	}
	public ArrayList<Long> getLostList(){
		return this.lostlist;
	}

	public void setLast_id() {
		Collections.sort(lplist);
		this.last_id =lplist.get(lplist.size()-1);
	}

	public long getLast_id() {
		return last_id;
	}
	public double getSpendTime(){
		return OTT+3*2*OTT;
	}
}
