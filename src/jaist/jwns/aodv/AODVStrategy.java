package jaist.jwns.aodv;

public class AODVStrategy {
    /***
     * check whether can broadcast RREQ
     * @param rreq is the RREQ packet
     * @return 
     */
	public boolean isBroadCastRREQ(RREQ rreq){
		boolean broadcast=true;
		if(rreq.getHop_count()>=rreq.getRREQ_hop_max()){
			broadcast=false;
		}
		return broadcast;
	}
	/***
	 * Check whether can send RREP
	 * @param rrep is the RREP packet
	 * @return
	 */
	public boolean isSendRREP(RREP rrep){
		boolean isSend=true;
		if(rrep.getHop_count()>=rrep.getRREP_hop_max() || rrep.getRREP_spend_time()>=rrep.getRREP_life_time()){
			isSend=false;
		}
		return isSend;
	}
}
