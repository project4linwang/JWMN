package jaist.jwns.entity;

import java.util.List;

import jaist.jwns.aodv.RREP;
import jaist.jwns.aodv.RREQ;
import jaist.jwns.tcp.ACKPacket;
import jaist.jwns.tcp.NAKPacket;
import jaist.jwns.util.Configuration;
import jaist.jwns.util.IPAddress;
import jaist.jwns.util.LPacket;

/**
 * Processing the LPacket in the Queue
 * @author mac
 *
 */
public class ProcessDevice {
	private long proc_time;
	private RouteTable rt;
	private Configuration config=Configuration.getConfig();
	public ProcessDevice(long pt){
		this.rt=new RouteTable();
		this.proc_time=pt;
	}
	public ProcessDevice(){
		this.proc_time=0;
		this.rt=new RouteTable();
	}
	public ProcessDevice(RouteTable rt){
		this.rt=rt;
	}

	public LPacket process(LPacket pk){
		
		LPacket lpk=pk;
		if(config.getReal()){
			//realistic mode
            if(lpk!=null){
				
				if(rt!=null){
					String rx=searchRouteTable(lpk.getDest());
					 if(rx==null){
							lpk.setPk_send_time(0);
							lpk.setTx(null);
							lpk.setRx(null);
						}
		                else{
		                	long proc_time_real=lpk.getEnd_stamp()-lpk.getStart_stamp();		                	
		                	long pst=lpk.getPk_send_time()+proc_time_real;
		                	lpk.setPk_send_time(pst);
		    				String tx=lpk.getRx();
		    				if(tx!=null){
		    					lpk.setTx(tx);
		    				}    				   				
		    				lpk.setRx(rx);	
		                }
				}
            }
		}
		else{
			//pseudo mode
			if(lpk!=null){				
				if(rt!=null){
					String rx=searchRouteTable(lpk.getDest());
	                if(rx==null){
						lpk.setPk_send_time(0);
						lpk.setTx(null);
						lpk.setRx(null);
					}
	                else{
	                	long pst=lpk.getPk_send_time()+proc_time;
	    				lpk.setPk_send_time(pst);
	    				String tx=lpk.getRx();
	    				if(tx!=null){
	    					lpk.setTx(tx);
	    				}    				   				
	    				lpk.setRx(rx);	
	                }					
					
				}
			}
		}
		
		return lpk;
	}
	/**
	 * Find the IP Address of next node
	 * @param dst is the IP Address of destination
	 * @return IP Address of next node
	 */
	public String searchRouteTable(String dst){
		String next=null;
		if(rt!=null){
			if(rt.size()>0){
				if(dst!=null){
					RouteItem ritem=rt.getItem(dst);
					if(ritem!=null){
						next=ritem.getNext();
					}
					
				}
				
			}
		}
		
		return next;
	}
	public long getProc_Time(){
		return proc_time;
	}
	public long getReal_Proc_Time(LPacket lpk, LastPacketMsg lpk_last){
		long proc_time_real=0;
		if(lpk_last!=null){
			proc_time_real=lpk.getEnd_stamp()-lpk_last.getEnd_Stamp()-lpk_last.getTransmit_time();
			if(proc_time_real<0){
				proc_time_real=0;
			}
		} 				
		return proc_time_real;
	}
    public RouteTable getRouteTable(){
    	return rt;
    }
    public void updateRouteTable(List<RouteItem> items){
    	rt.clear();
    	for(RouteItem item:items){
    		rt.addItem(item);
    	}
    }
    public void processRREP(RREP rp){
    	//double sp_time=(double)getProc_Time()+rp.getRREP_spend_time();
    	double sp_time=rp.getRREP_spend_time();
    	rp.setRREP_spend_time(sp_time);
    }
    public void processACK(ACKPacket ack){
    	//double sp_time=(double)getProc_Time()+ack.getSp_time();
    	double sp_time=ack.getSp_time();
    	ack.setSp_time(sp_time);
    }
    public void processNAK(NAKPacket nak){
    	//double sp_time=(double)getProc_Time()+nak.getSp_time();
    	double sp_time=nak.getSp_time();
    	nak.setSp_time(sp_time);
    }
}
