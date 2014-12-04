package jaist.jwns.event;

import java.util.ArrayList;


public class EventHandler<T> {
	private ArrayList<EventListener> listeners = new ArrayList<EventListener>(); 
	public synchronized void add(EventListener listener) 
	{ // 判断在监听器列表中是否存在当前监听器对象 
		if (listeners.indexOf(listener) < 0) { 
			listeners.add(listener); 
		} 
	}
	public synchronized void remove(EventListener listener){ 
		listeners.remove(listener); 
	}
	public void invoke(Object sender) { 
		// 构造事件对象 
		EventArg e = new EventArg(); 
		invoke(sender, e); 
	} 
	public void invoke(Object sender, Object... args) { 
		// 构造事件对象
		EventArg e = new EventArg(args); 
		invoke(sender, e); 
	}
	public void invoke(Object sender, EventArg e) { 
		synchronized (listeners) { 
			// 构造事件对象 
			for (int i = 0; i < listeners.size(); i++) { 
				listeners.get(i).doEvent(sender, e); 
				} 
			} 
		}
	}

