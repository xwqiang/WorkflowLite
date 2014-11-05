package fr.flafla.workflow.common.event;

import fr.flafla.workflow.common.Transition;

public class TransitionEvent<T> {
	public final T document;
	public final Transition<T> transition;
	public String action;

	public TransitionEvent(T document, Transition<T> transition, String action) {
		this.document = document;
		this.transition = transition;
		this.action = action;
	}

}
