package fr.flafla.workflow.common;

import java.util.List;

/**
 * This interface describe a workflow definition 
 * 
 * @author rflafla
 *
 * @param <T>
 */
public interface WorkflowDefinition<T> {

	/**
	 * This method has to return all transitions associated to one transition.
	 * @param stateName The state name
	 * @return The list of transitions
	 */
	public List<Transition<T>> getTransitionsFrom(String stateName);
	
}
