package fr.flafla.workflow.common.impl;

import fr.flafla.workflow.common.Transition;
import fr.flafla.workflow.common.utils.TransitionUtils;

/**
 * This is a default implementation of a transition.
 * 
 * @author rflafla
 *
 * @param <T>
 */
public class ActionTransition<T> implements Transition<T> {

	protected final String action;
	private final String[] initials;
	private final String[] destinations;

	protected ActionTransition(String action, String[] initials, String[] destinations) {
		this.action = action;
		this.initials = initials;
		this.destinations = destinations;
	}

	public static <T> ActionTransition<T> create(Object action, Object[] initials, Object... destinations) {
		return new ActionTransition<T>(action.toString(), TransitionUtils.extractNames(initials), TransitionUtils.extractNames(destinations));
	}

	@Override
	public String[] initials() {
		return initials;
	}

	@Override
	public String[] destinations() {
		return destinations;
	}

	public String action() {
		return action;
	}
	
	@Override
	public boolean accept(T document) {
		return true;
	}

}
