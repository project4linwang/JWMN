package jaist.jwns.util;

import java.util.Calendar;
import java.util.LinkedList;
import java.util.Locale;

import jaist.jwns.entity.ThroughPut;

/**
 * Log the ThroughPut 
 * It is not used in this version
 * @author lin
 *
 */
public class LogThroughPut {
	private ThroughPut output=ThroughPut.getThroughPut();
	private WriteToFile wtf=new WriteToFile();
	private LinkedList<String> log_str;
	public LogThroughPut(){
		log_str=new LinkedList<String>();
	}
    public void PrintToFile(String filename){
    	//Calendar date= Calendar.getInstance();
    	double averate=0.0;
    	double total=0.0;
    	PrintTitle1();
    	LinkedList<Double> linkput= output.getLinkPut();
    	if(linkput!=null){
    		for(int i=1;i<=linkput.size();i++){
    			log_str.add(i+"  "+String.format("%.4f", linkput.get(i-1)));
    		}
    	}
    	PrintTitle2();
    	LinkedList<LPacket> lpks= output.getDiffSrcLPacket();   	
    	StringBuilder sb=new StringBuilder();
    	if(lpks!=null){
    		
    		long total_time=0;
    		float averate_time=0;
    		long length=0;
    		for(LPacket lpk:lpks){
    			double tp= ((double)lpk.getLength()*8)/(lpk.getRec_time_dst()*1000);
    			sb.append(String.format("%.4f", tp)+"  ");
    			total_time=total_time+lpk.getRec_time_dst();
    			length=lpk.getLength();
    		}
    		averate_time=total_time/lpks.size();
    		averate=(double)length*8/(averate_time*1000);
    		total=(double)length*8/(total_time*1000);
    		
    	}
    	sb.append(String.format("%.4f", averate)+"  ");
    	sb.append(String.format("%.4f", total)+"  ");
    	log_str.add(sb.toString());
    	write(filename);
    }
    private void PrintTitle1(){
    	PrintSign();
    	log_str.add("Column1: x coordinate (Time). Unit: s");
    	log_str.add("Column2: y coordinate (Link ThroughPut). Unit: Mbps");
    	PrintSign();
    }
    private void PrintTitle2(){
    	PrintSign();    	
    	int lsize=output.getDiffSrcLPacket().size();
    	int number=0;
    	for(int index=1;index<=lsize;index++){
    		LinkedList<LPacket> lpks= output.getDiffSrcLPacket();
    		if(lpks!=null){
    			LPacket lpk=lpks.get(index-1);
        		log_str.add("Column"+index+": "+"ThroughPut (From "+lpk.getSrc().toString()+" To "+lpk.getDest().toString()+". Unit: Mbps");
    		}
    		number=index;
    	}
    	number++;
    	log_str.add("Column"+number+": "+"Average ThroughPut. Unit: Mbps");
    	number++;
    	log_str.add("Column"+number+": "+"Total ThroughPut. Unit: Mbps");
    	PrintSign();
    }
    private void PrintSign(){
    	StringBuilder sb=new StringBuilder();
    	for(int i=0;i<70;i++){
    		sb.append("#");
    	}
    	log_str.add(sb.toString());
    }
    private void write(String filename){
  	  boolean res=	wtf.write(filename, log_str);
  	  if(res){
  		  System.out.println("Log Finished!");
  	  }
  	  else{
  		  System.out.println("Log failed");
  	  }
  	}
}
