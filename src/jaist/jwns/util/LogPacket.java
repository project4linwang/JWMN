package jaist.jwns.util;

import java.util.LinkedList;

/**
 * Log the information for each LPacket
 * it is not used in this version
 * @author lin
 *
 */
public class LogPacket {

	private String filename="LPackets.txt";
	private WriteToFile wtf=new WriteToFile();
	private LinkedList<String> log_packet;
	public LogPacket(){
		log_packet=new LinkedList<String>();	
	}
	public void log(LPacket lpt){
		log_packet.add("---------------------------------");
		log_packet.add("SRC: "+lpt.getSrc().toString());
		log_packet.add("DST: "+lpt.getDest().toString());
		log_packet.add("Send Time At Src: "+lpt.getSend_time_src()+"ms");
		log_packet.add("Received Time At Dst: "+lpt.getRec_time_dst()+"ms");
		log_packet.add("Packet Send Time: "+lpt.getPk_send_time()+"ms");
		log_packet.add("Tx Address: "+lpt.getTx());
		log_packet.add("Rx Address: "+lpt.getRx());
		log_packet.add("Hop: "+lpt.getHop());
		log_packet.add("Sequence ID: "+lpt.getSequence_id());						
	}
	public void write(){
	  boolean res=	wtf.write(filename, log_packet);
	  if(res){
		  System.out.println("Log Finished!");
	  }
	  else{
		  System.out.println("Log failed");
	  }
	}
}
