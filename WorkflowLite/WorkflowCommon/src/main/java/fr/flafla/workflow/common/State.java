package fr.flafla.workflow.common;

/**
 * Define a state of a workflow
 * 
 * @author rflafla
 *
 * @param <T> The type of document
 */
public interface State<T> {
	/**
	 * @return The document
	 */
	public T document();
	/**
	 * @return The name of the current state
	 */
	public String name();
	
	/**
	 * @return true if the state has already been executed 
	 */
	public boolean done();
}
