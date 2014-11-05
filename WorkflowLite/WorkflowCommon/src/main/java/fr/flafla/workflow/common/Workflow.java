package fr.flafla.workflow.common;

import java.util.List;

import fr.flafla.workflow.common.event.TransitionHandler;

/**
 * Define a workflow engine.
 * 
 * @author rflafla
 *
 * @param <T> The type of managed document
 */
public interface Workflow<T> {
	/**
	 * Check if an action is acceptable with the current state of the document
	 * @param document The document
	 * @param action The action's name
	 * @return true if the action is acceptable
	 */
	public boolean isActionAllowed(T document, String action);
	
	/**
	 * Execute the action on the document
	 * @param document The document
	 * @param action The action's name
	 * @return States after action
	 * @throws NoTransitionException Exception thrown if no transition has been done
	 */
	public List<State<T>> doAction(T document, String action) throws NoTransitionException;
	
	/**
	 * Get current states of the document
	 * @param document The document
	 * @return Current states
	 */
	public List<State<T>> getCurrent(T document);
	
	/**
	 * Add an handler that trap transition events
	 * @param handler The handler
	 */
	public void addTransitionHandler(TransitionHandler<T> handler);
}
