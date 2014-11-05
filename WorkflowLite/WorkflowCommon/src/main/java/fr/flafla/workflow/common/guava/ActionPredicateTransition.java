package fr.flafla.workflow.common.guava;

import com.google.common.base.Predicate;

import fr.flafla.workflow.common.impl.ActionTransition;
import fr.flafla.workflow.common.utils.TransitionUtils;

/**
 * This transition use a {@link Predicate} to ensure that the transition is acceptable.
 * 
 * @author rflafla
 *
 * @param <T> The type of the document
 */
public class ActionPredicateTransition<T> extends ActionTransition<T> {

	protected final Predicate<T> condition;

	protected ActionPredicateTransition(String action, Predicate<T> condition, String[] initials, String[] destinations) {
		super(action, initials, destinations);
		this.condition = condition;
	}
	
	public static <T> ActionPredicateTransition<T> create(Object action, Predicate<T> condition, Object[] initials, Object[] destinations) {
		return new ActionPredicateTransition<T>(action.toString(), condition, TransitionUtils.extractNames(initials), TransitionUtils.extractNames(destinations));
	}

	@Override
	public boolean accept(T document) {
		return condition.apply(document);
	}


}
