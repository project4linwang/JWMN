package jaist.jwns.util;

import jaist.jwns.entity.StoredPackets;

import java.util.ArrayList;

public class PacketList {
	private ArrayList<StoredPackets> storedlists;

	public PacketList(){
		storedlists=new ArrayList<StoredPackets>();
	}
   
	public void add(StoredPackets lps){
		storedlists.add(lps);
	}
	public StoredPackets getStoredPackets(String ipAddress){
		if(storedlists!=null){
			for(StoredPackets sp:storedlists){
				String name=sp.getIpAddress();
				if(name!=null){
					if(name.equals(ipAddress)){
						return sp;
					}
				}
			}
		}
		return null;
	}
	
	public synchronized void clearStoredPackets(String ipAddress){
		
		StoredPackets sp=getStoredPackets(ipAddress);
		storedlists.remove(sp);
		//System.out.println("Clear SP: "+sp.getIpAddress());
	}
	
}
