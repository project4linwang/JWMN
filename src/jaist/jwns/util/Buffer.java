package jaist.jwns.util;

public class Buffer {
	private Queue m_packets;
	private long m_maxlength; //number of packet
    private int m_curlength; 
    
	public Buffer(long maxlength){
		m_packets=new Queue(); 
		m_maxlength=maxlength;
		m_curlength=0;
	}
	public synchronized boolean enqueue(LPacket packet){
		boolean isfull=false;
		if(m_maxlength == 0 || m_curlength + 1 < m_maxlength)
        {
            m_packets.push(packet);
            m_curlength += 1;
        }
		else{
			isfull=true;
		}
		return isfull;
	}
	public synchronized LPacket dequeue(){
		if(m_packets.size() != 0)
        {

            // Dequeue a packet and decrease queue length

            LPacket packet = (LPacket) m_packets.peekFront();
            m_packets.pop();

            m_curlength -= 1;

            // Return packet

            return packet;
        }
        else
            return null;
	}
	public int size(){
		return m_packets.size(); 
	}
}
