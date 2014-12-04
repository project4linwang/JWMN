package jaist.jwns.entity;

import jaist.jwns.util.Configuration;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Timer;
import java.util.TimerTask;

public class HelloTable {
    private ArrayList<HTable> tables;
	private Timer expiration_timer;
	private Configuration config=Configuration.getConfig();
	public HelloTable(){
		expiration_timer=new Timer();
		tables=new ArrayList<HTable>();
	}
	/***
	 * initial the table expiration timer (for future extending)
	 */
	public void init(){
		TimerTask tk=new TimerTask(){
     
			@Override
			public synchronized void run() {
				// TODO Auto-generated method stub
				ArrayList<HTable> tmp_tables=new ArrayList<HTable>();
				if(tables!=null){
					if(tables.size()>0){
						for(HTable table:tables){
							if(table.getCount()< config.getHello_entry_expirationtime()){								//table.setDespose(true);
								
							}
						}
					}
				}
				
				tables=tmp_tables;			    			    
			}
			
		};
		expiration_timer.scheduleAtFixedRate(tk, config.getHello_entry_expirationtime() , config.getHello_entry_expirationtime());
	}
	/***
	 * Update the content in the hello table
	 * @param dst IP Address of destination node
	 * @param next IP Address of next node
	 * @param nd   The name of the current node
	 */
	public  void updateTable(String dst,String next,String nd){
			if(tables!=null){
				if(tables.size()>0){
					try {
						Thread.sleep(50);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}					
					for(HTable table:tables){ 
						if(table.getNext().equals(next) && table.getDst().equals(dst)){
							return;
						}
					}
					
				}
					HTable ht=new HTable(dst,next);
					tables.add(ht);
			}
		
		
	}
	/***
	 * Print Hello Table
	 * @param node_name
	 */
	public synchronized void printTable(String node_name){
		if(tables!=null){
			if(tables.size()>0){
				System.out.println("-------------------------------");
				for(HTable table:tables){
					if(table!=null){
						System.out.println(node_name+" DST: "+table.getDst()+" Next: "+table.getNext());
						
					}
				}
				System.out.println("-------------------------------");
			}
			else{
				System.out.println("There are no message in the hello table!");
			}
		}
	}
	/**
	 * Depose the hello table
	 */
	public void Close(){
		if(expiration_timer!=null){
			expiration_timer.cancel();
			expiration_timer=null;
			tables.clear();
		}
	}
	public ArrayList<HTable> getTables(){
		return tables;
	}
	
	/**
	 * Struck of hello table
	 * @author lin
	 *
	 */
	public class HTable{
		private String dst;
		private String next;
		private boolean despose=false;
		private long count=0;
		private Configuration config=Configuration.getConfig();
		public HTable(String dst,String next){
			this.dst=dst;
			this.next=next;
			count= config.getHello_entry_expirationtime(); 
		}
		public void reduceCount(){
			count--;
		}
		public long getCount(){
			return count;
		}
		public void reSetCount(){
			count=config.getHello_entry_expirationtime(); 
		}
		public void setDst(String dst) {
			this.dst = dst;
		}
		public String getDst() {
			return dst;
		}
		public void setNext(String next) {
			this.next = next;
		}
		public String getNext() {
			return next;
		}
		public void setDespose(boolean despose) {
			this.despose = despose;
		}
		public boolean isDespose() {
			return despose;
		}
	}
}
