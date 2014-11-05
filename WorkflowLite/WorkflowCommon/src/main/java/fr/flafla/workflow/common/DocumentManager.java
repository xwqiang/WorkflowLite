package fr.flafla.workflow.common;

import java.util.List;

/**
 * This interface describe a document manager.
 * The role of this manager is to manage states of a document.
 * 
 * @author rflafla
 *
 * @param <T>
 */
public interface DocumentManager<T> {
	/**
	 * Get current states of the document
	 * @param document The document
	 * @return Current states
	 */
	public List<State<T>> getCurrent(T document);
	
	/**
	 * This method will be executed when the workflow engine will consume a state
	 * @param state
	 */
	public void markStateDone(State<T> state);
	
	/**
	 * This method will be executed when the workflow engine will remove a state from states list
	 * @param state
	 */
	public void deleteState(State<T> state);

	/**
	 * This method will be executed when the workflow engine will add a state from states list
	 * @param state
	 */
	public void saveState(State<T> state);
	
	/**
	 * This method will be executed when the workflow engine has to create a new state
	 * @param document The document associated to the new state
	 * @param name The name of the state
	 * @return The new state
	 */
	public State<T> createState(T document, String name);
}
