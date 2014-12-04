package jaist.jwns.util;

import java.util.LinkedList;

import jaist.jwns.entity.ThroughPut;

public class LogTraffic {
	private WriteToFile wtf=new WriteToFile();
	private LinkedList<String> log_str;
	public LogTraffic(){
		log_str=new LinkedList<String>();
	}
	public void Write(String filename,ThroughPut output){
		LinkedList<Double> linkput= output.getLinkPut();
		for(Double put:linkput){
			log_str.add(put+",");
		}
		write(filename);
	}
	 private void write(String filename){
	  	  boolean res=	wtf.write_csv(filename, log_str);
	  	  if(res){
	  		  System.out.println("Log Finished!");
	  	  }
	  	  else{
	  		  System.out.println("Log failed");
	  	  }
	  	}
}
