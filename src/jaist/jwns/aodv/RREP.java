package jaist.jwns.aodv;

/***
 * RREP Packet
 * @author lin
 *
 */
public class RREP implements Cloneable{
	private String type;
	private String src;
	private String dst;
	private long dst_seq;
	private int hop_count;
	private int RREP_retry_count;
	private int RREP_hop_max;
	private int length;
	private String tx;
	private double RREP_life_time;
	private double RREP_spend_time;
	
	public void setType(String type) {
		this.type = type;
	}
	public String getType() {
		return type;
	}
	public void setSrc(String src) {
		this.src = src;
	}
	public String getSrc() {
		return src;
	}
	public void setDst(String dst) {
		this.dst = dst;
	}
	public String getDst() {
		return dst;
	}
	public void setDst_seq(long dst_seq) {
		this.dst_seq = dst_seq;
	}
	public long getDst_seq() {
		return dst_seq;
	}
	public void setHop_count(int hop_count) {
		this.hop_count = hop_count;
	}
	public int getHop_count() {
		return hop_count;
	}
	public void setRREP_retry_count(int rREP_retry_count) {
		RREP_retry_count = rREP_retry_count;
	}
	public int getRREP_retry_count() {
		return RREP_retry_count;
	}
	public void setRREP_hop_max(int rREP_hop_max) {
		RREP_hop_max = rREP_hop_max;
	}
	public int getRREP_hop_max() {
		return RREP_hop_max;
	}
	public void setLength(int length) {
		this.length = length;
	}
	public int getLength() {
		return length;
	}
	public void setRREP_life_time(double rREP_life_time) {
		RREP_life_time = rREP_life_time;
	}
	public double getRREP_life_time() {
		return RREP_life_time;
	}
	public void setRREP_spend_time(double rREP_spend_time) {
		RREP_spend_time = rREP_spend_time;
	}
	public double getRREP_spend_time() {
		return RREP_spend_time;
	}
	public void setTx(String tx) {
		this.tx = tx;
	}
	public String getTx() {
		return tx;
	}
	public Object clone()
	{
	try
	{
	   super.clone();
	   return super.clone();
	}catch(Exception e)
	 {
	   return null;
	 }
	}
}
