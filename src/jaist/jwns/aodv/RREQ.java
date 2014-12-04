package jaist.jwns.aodv;

/***
 * RREQ Packet
 * @author lin
 *
 */
public class RREQ implements Cloneable{
	private String type;
	private String src;
	private long src_seq;
	private String dst;
	private long dst_seq;
	private long hop_count;
	private int RREQ_retry_count;
	private int RREQ_hop_max;
	private int length;
	private String tx;
	/***
	 * THe life span of RREQ. (for future extending)
	 */
	private long RREQ_life_time;
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
	public void setSrc_seq(long src_seq) {
		this.src_seq = src_seq;
	}
	public long getSrc_seq() {
		return src_seq;
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
	public double getDst_seq() {
		return dst_seq;
	}
	public void setHop_count(long hop_count) {
		this.hop_count = hop_count;
	}
	public long getHop_count() {
		return hop_count;
	}
	public void setRREQ_retry_count(int rREQ_retry_count) {
		RREQ_retry_count = rREQ_retry_count;
	}
	public int getRREQ_retry_count() {
		return RREQ_retry_count;
	}
	public void setRREQ_hop_max(int rREQ_hop_max) {
		RREQ_hop_max = rREQ_hop_max;
	}
	public int getRREQ_hop_max() {
		return RREQ_hop_max;
	}
	public void setLength(int length) {
		this.length = length;
	}
	public int getLength() {
		return length;
	}
	public void setRREQ_life_time(long rREQ_life_time) {
		RREQ_life_time = rREQ_life_time;
	}
	public long getRREQ_life_time() {
		return RREQ_life_time;
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
