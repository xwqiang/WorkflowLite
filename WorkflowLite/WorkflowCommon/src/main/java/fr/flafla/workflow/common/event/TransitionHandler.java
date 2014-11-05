package fr.flafla.workflow.common.event;



public interface TransitionHandler<T> {
	public void onExecute(TransitionEvent<T> event);
}
