package jaist.jwns.event;

public interface EventListener<T> {
	void doEvent(Object sender, EventArg e);
}
