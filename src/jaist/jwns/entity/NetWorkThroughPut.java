package jaist.jwns.entity;

import java.util.ArrayList;

public class NetWorkThroughPut {
	private static NetWorkThroughPut output;
	private ArrayList<String> throughputs=new ArrayList<String>();
	private ArrayList<Double> e2ethroughputs=new ArrayList<Double>();
	public static NetWorkThroughPut getThroughPut(){
		if(output==null){
			output=new NetWorkThroughPut();
		}
		return output;
	}
    public void addThroughputMsg(String msg){
    	throughputs.add(msg);
    }
    public void addThroughput(double tp){
    	e2ethroughputs.add(tp);
    }
    public ArrayList<String> getThroughputs(){
    	return throughputs;
    }
    public Double getNetWorkThroughput(){
    	double ntp=0.0;
    	if(e2ethroughputs.size()>0){
    		for(Double tp:e2ethroughputs){
    			ntp=ntp+tp;
    		}
    		ntp=ntp/e2ethroughputs.size();
    	}
    	
    	return ntp;
    }
}
