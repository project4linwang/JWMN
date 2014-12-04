package jaist.jwns.entity;

import jaist.jwns.util.IPAddress;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * The class of Routing Table
 * @author lin
 *
 */
public class RouteTable {
private ArrayList<RouteItem> items=new ArrayList<RouteItem>();
	
	public void addItem(RouteItem item){
		items.add(item);
	}
	public void removeItem(RouteItem item){
		items.remove(item);
	}
	public void clear(){
		items.clear();
	}
	public RouteItem getItem(IPAddress dst){
		for(RouteItem item: items){
			if(dst!=null){
				if(item.getDst()!=null){
					if(item.getDst().toString().equals(dst.toString())){
						return item;
					}
				}
				
			}
			
		}
		return null;
	}
	public RouteItem getItem(String dst){
		for(RouteItem item: items){
			if(dst!=null){
				if(item.getDst()!=null){
					if(item.getDst().toString().equals(dst)){
						return item;
					}
				}
				
			}
			
		}
		return null;
	}
	public int size(){
		return items.size();
	}
	public List<RouteItem> getItems(){
		return this.items;
	}
}
