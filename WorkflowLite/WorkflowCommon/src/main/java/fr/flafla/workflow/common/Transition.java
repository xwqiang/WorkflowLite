package fr.flafla.workflow.common;


/**
 * Define a transition
 * 
 * @author rflafla
 *
 * @param <T> The type of document
 */
public interface Transition<T> {
	/**
	 * This method has to provide the initial step's name
	 * @return The initial step's name
	 */
	public String[] initials();
	/**
	 * This method has to provide destinations names
	 * @return Destinations names
	 */
	public String [] destinations();
	/**
	 * This method has to provide action name
	 * @return The action name
	 */
	public String action();
	/**
	 * This method checks if the action is acceptable for this document.
	 * This method is executed only if the initial state is checked
	 * @param document The document
	 * @return true if the transition is acceptable
	 */
	public boolean accept(T document);
}
