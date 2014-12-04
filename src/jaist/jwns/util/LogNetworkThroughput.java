package jaist.jwns.util;

import java.util.ArrayList;
import java.util.LinkedList;

import jaist.jwns.entity.NetWorkThroughPut;

public class LogNetworkThroughput {
  private NetWorkThroughPut ntp;
  private WriteToFile wtf=new WriteToFile();
	private LinkedList<String> log_str;
  public LogNetworkThroughput(){
	  ntp=NetWorkThroughPut.getThroughPut();
	  log_str=new LinkedList<String>();
  }
  public void writeToFile(String filename){
	  ArrayList<String> msglist=ntp.getThroughputs();
	  if(msglist!=null){
		  for(String msg:msglist){
			  log_str.add(msg);
		  }
	  }
	  double nw_throughput=ntp.getNetWorkThroughput();
	  log_str.add("Network Throughput is "+nw_throughput+" Mbps");
	  write(filename);
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
