package fr.flafla.workflow.common.guava;

import com.google.common.eventbus.EventBus;

import fr.flafla.workflow.common.event.TransitionEvent;
import fr.flafla.workflow.common.event.TransitionHandler;

public class BusTransitionHandler<T> implements TransitionHandler<T> {

	private final EventBus bus;

	public BusTransitionHandler(EventBus bus) {
		this.bus = bus;
	}
	
	@Override
	public void onExecute(TransitionEvent<T> event) {
		bus.post(event);
	}

}
