package fr.flafla.workflow.common;

import java.util.Collection;
import java.util.Iterator;

/**
 * This exception is thrown when an action has no acceptable transition.
 * 
 * @author rflafla
 * 
 */
public class NoTransitionException extends Exception {
	private static final long serialVersionUID = 1L;

	private final Collection<String> states;
	private final String action;

	public NoTransitionException(Collection<String> states, String action) {
		super(createMessage(states, action));
		this.states = states;
		this.action = action;
	}

	private static String createMessage(Collection<String> states, String action) {
		final StringBuilder sb = new StringBuilder("Error from states ");

		if (states == null) {
			sb.append("none");
		} else {
			sb.append("[");
			Iterator<String> iterator = states.iterator();
			if (iterator.hasNext()) {
				sb.append(iterator.next());
				for (; iterator.hasNext();) {
					sb.append(',').append(iterator.next());
				}
			}
			sb.append("]");
		}
		sb.append(" to ").append(action);
		return sb.toString();
	}

	/**
	 * @return Current states names
	 */
	public Collection<String> getStates() {
		return states;
	}

	/**
	 * @return The disallowed action
	 */
	public String getAction() {
		return action;
	}

}
