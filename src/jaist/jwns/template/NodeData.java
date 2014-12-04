package jaist.jwns.template;

public class NodeData {

	private String Name;
	private String IPAddress;	
	private long ProcessTime;
	private int x;
	private int y;
	private double range;
	private long QueueSize;
	private int QueueStatus; //0: always free; 1: always busy; 2: random
	private long delay;
	public void setIPAddress(String iPAddress) {
		IPAddress = iPAddress;
	}
	public String getIPAddress() {
		return IPAddress;
	}
	public void setProcessTime(long processTime) {
		ProcessTime = processTime;
	}
	public long getProcessTime() {
		return ProcessTime;
	}
	public void setX(int x) {
		this.x = x;
	}
	public int getX() {
		return x;
	}
	public void setY(int y) {
		this.y = y;
	}
	public int getY() {
		return y;
	}
	public void setName(String name) {
		Name = name;
	}
	public String getName() {
		return Name;
	}
	public void setRange(double range) {
		this.range = range;
	}
	public double getRange() {
		return range;
	}
	public void setQueueSize(long queueSize) {
		QueueSize = queueSize;
	}
	public long getQueueSize() {
		return QueueSize;
	}
	public void setQueueStatus(int queueStatus) {
		QueueStatus = queueStatus;
	}
	public int getQueueStatus() {
		return QueueStatus;
	}
	public void setDelay(long delay) {
		this.delay = delay;
	}
	public long getDelay() {
		return delay;
	}
}
