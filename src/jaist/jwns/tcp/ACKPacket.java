package jaist.jwns.tcp;

public class ACKPacket {
   private long seq_id;
   private long seq_data_id;
   private int length=28; //bytes
   private String src;
   private String dst;
   private double sp_time;
   public ACKPacket(long s_id, long data_id){
	   this.seq_id=s_id;
	   this.seq_data_id=data_id;
   }
public void setSeq_id(long seq_id) {
	this.seq_id = seq_id;
}
public long getSeq_id() {
	return seq_id;
}
public void setSeq_data_id(long seq_data_id) {
	this.seq_data_id = seq_data_id;
}
public long getSeq_data_id() {
	return seq_data_id;
}
public void setLength(int length) {
	this.length = length;
}
public int getLength() {
	return length;
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
public void setSp_time(double sp_time) {
	this.sp_time = sp_time;
}
public double getSp_time() {
	return sp_time;
}
   
}
