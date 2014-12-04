package jaist.jwns.entity;

/**
 * The struck of last packet message
 * @author lin
 *
 */
public class LastPacketMsg implements Cloneable{
    /**
     * The IP Address of next node when the packet dequeue from the last node.
     */
	private String next;
	/**
	 * The stamp time of the packet when the packet dequeue from the last node.
	 */
	private long end_Stamp;
	/**
	 * The transmit time of the packet from the last node to the next node.
	 */
	private long transmit_time;
	
	public LastPacketMsg(String next,long end){
		this.next=next;
		this.end_Stamp=end;
	}
	public void setNext(String next) {
		this.next = next;
	}
	public String getNext() {
		return next;
	}
	public void setEnd_Stamp(long end_Stamp) {
		this.end_Stamp = end_Stamp;
	}
	public long getEnd_Stamp() {
		return end_Stamp;
	}
	public void setTransmit_time(long transmit_time) {
		this.transmit_time = transmit_time;
	}
	public long getTransmit_time() {
		return transmit_time;
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
