package jaist.jwns.entity;

import jaist.jwns.util.IPAddress;

/***
 * The packet of hello message
 * @author lin
 *
 */
public class HelloMessage {

	private String type;
	private String tx;
	private String rx;
	private long sequence_no;
	public HelloMessage(String ty,String ttx,String rrx, long num){
		type=ty;
		tx=ttx;
		rx=rrx;
		sequence_no=num;
	}
	
	public String getType() {
		return type;
	}
	
	public String getTx() {
		return tx;
	}
	
	public String getRx() {
		return rx;
	}
	
	public long getSequence_no() {
		return sequence_no;
	}
}
