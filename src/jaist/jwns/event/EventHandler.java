package jaist.jwns.event;

import java.util.ArrayList;


public class EventHandler<T> {
	private ArrayList<EventListener> listeners = new ArrayList<EventListener>(); 
	public synchronized void add(EventListener listener) 
	{ // �ж��ڼ������б����Ƿ���ڵ�ǰ���������� 
		if (listeners.indexOf(listener) < 0) { 
			listeners.add(listener); 
		} 
	}
	public synchronized void remove(EventListener listener){ 
		listeners.remove(listener); 
	}
	public void invoke(Object sender) { 
		// �����¼����� 
		EventArg e = new EventArg(); 
		invoke(sender, e); 
	} 
	public void invoke(Object sender, Object... args) { 
		// �����¼�����
		EventArg e = new EventArg(args); 
		invoke(sender, e); 
	}
	public void invoke(Object sender, EventArg e) { 
		synchronized (listeners) { 
			// �����¼����� 
			for (int i = 0; i < listeners.size(); i++) { 
				listeners.get(i).doEvent(sender, e); 
				} 
			} 
		}
	}

