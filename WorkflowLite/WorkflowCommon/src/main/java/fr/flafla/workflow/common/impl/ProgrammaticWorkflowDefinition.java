package fr.flafla.workflow.common.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import fr.flafla.workflow.common.Transition;
import fr.flafla.workflow.common.WorkflowDefinition;

/**
 * This class is a basic workflow definition implementation.
 * 
 * @author rflafla
 *
 * @param <T> The type of documents
 */
public class ProgrammaticWorkflowDefinition<T> implements WorkflowDefinition<T> {
	private final Map<String, List<Transition<T>>> mapTransitions = new HashMap<String, List<Transition<T>>>();

	public ProgrammaticWorkflowDefinition(Transition<T> ... transitions) {
		for (final Transition<T> transition : transitions) {
			addTransition(transition);
		}
	}

	protected void addTransition(final Transition<T> transition) {
		for (final String initial : transition.initials()) {
			List<Transition<T>> list = mapTransitions.get(initial);
			if (list == null) {
				list = new ArrayList<Transition<T>>();
				mapTransitions.put(initial, list);
			}
			list.add(transition);
		}
	}

	protected void addActionTransition(Object action, Object[] initials, Object[] destinations) {
		addTransition(ActionTransition.<T> create(action, initials, destinations));
	}

	@Override
	public List<Transition<T>> getTransitionsFrom(String stateName) {
		return mapTransitions.get(stateName);
	}
}
