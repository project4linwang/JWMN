package jaist.jwns.entity;

import java.math.BigDecimal;
import java.util.List;

import jaist.jwns.util.Configuration;
import jaist.jwns.util.LogTraffic;

/**
 * Monitor the link throughput
 * @author mac
 *
 */
public class Tracer {

	private int total_runtime;
	private ThroughPut output=new ThroughPut();
	private LogTraffic trafficput =new LogTraffic();
	private Configuration config=Configuration.getConfig();
	/**
	 * The traffic. Unit: Mbps
	 */
	private double traffic=0.0; 
	
	public void logTraffic(long traffic_num,List<String> ndpair){
		String nd1="";
		String nd2="";
		if(ndpair!=null){
			nd1=ndpair.get(0);
			nd2=ndpair.get(1);
		}
		total_runtime++;
		traffic=(double)((traffic_num*config.getLPacketLength()  << 3)/(1000.0000*1000.0000))/(config.getTraceTime()/1000.0000);
		output.addLinkTraffic(traffic);
    	System.out.println("Tracer: "+"Time: "+total_runtime+" Traffic: "+traffic+"Mbps"+" ( "+nd1+" , "+nd2+" )");
	}
	public void WriteTraffic(String n1,String n2){
		if(trafficput!=null){
			trafficput.Write("Traffic"+n1+"and"+n2+".csv",output);
		}
	}
	
}
