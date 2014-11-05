package fr.flafla.workflow.common.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import fr.flafla.workflow.common.DocumentManager;
import fr.flafla.workflow.common.NoTransitionException;
import fr.flafla.workflow.common.State;
import fr.flafla.workflow.common.Transition;
import fr.flafla.workflow.common.Workflow;
import fr.flafla.workflow.common.WorkflowDefinition;
import fr.flafla.workflow.common.event.TransitionEvent;
import fr.flafla.workflow.common.event.TransitionHandler;
import fr.flafla.workflow.common.utils.TransitionUtils;

/**
 * This is the default implementation of the workflow engine.
 * 
 * @author rflafla
 * 
 * @param <T> The type of document
 */
public class WorkflowEngine<T> implements Workflow<T> {

	/**
	 * List of transition handlers
	 */
	private final List<TransitionHandler<T>> transitionsHandlers = new ArrayList<TransitionHandler<T>>();

	/**
	 * The data manager associated
	 */
	private final DocumentManager<T> dataManager;
	/**
	 * The workflow definition manager
	 */
	private final WorkflowDefinition<T> definition;

	/**
	 * Construct an engine with a data manager and a workflow definition
	 * @param dataManager The data manager
	 * @param definition The workflow definition
	 */
	public WorkflowEngine(DocumentManager<T> dataManager, WorkflowDefinition<T> definition) {
		this.dataManager = dataManager;
		this.definition = definition;
	}

	@Override
	public boolean isActionAllowed(T document, String action) {
		final List<State<T>> states = getCurrent(document);
		for (final State<T> state : states) {
			final List<Transition<T>> transitions = definition.getTransitionsFrom(state.name());
			for (final Transition<T> transition : transitions) {
				if (isAcceptableTransition(transition, document, states, action)) {
					return true;
				}
			}
		}
		return false;
	}

	/**
	 * A transition is allowed when initial states correspond to the action
	 * @param transition The transition description
	 * @param document The document
	 * @param states States of the document
	 * @param action The action
	 * @return true if this action is allowed
	 */
	protected boolean isAcceptableTransition(Transition<T> transition, T document, List<State<T>> states, String action) {
		if (!transition.action().equals(action)) {
			return false;
		}

		final Set<String> stateNames = TransitionUtils.extractSetStateNames(states);
		for (String initial : transition.initials()) {
			if (!stateNames.contains(initial))
				return false;
		}
		return transition.accept(document);
	}

	@Override
	public List<State<T>> doAction(T document, String action) throws NoTransitionException {
		final List<State<T>> states = new ArrayList<State<T>>(getCurrent(document));
		final List<State<T>> resultStates = new ArrayList<State<T>>();
		
		// Search the transition
		Transition<T> transition = null;
		for (final State<T> state : states) {
			final List<Transition<T>> transitions = definition.getTransitionsFrom(state.name());
			for (final Transition<T> trans : transitions) {
				if (isAcceptableTransition(trans, document, states, action)) {
					// Do transition
					transition = trans;
					break;
				}
			}
		}
		
		// Check if a transition had been found
		if (transition == null)
			throw new NoTransitionException(TransitionUtils.extractStateNames(states), action);
		
		// Execute events
		if (transitionsHandlers.size() > 0) {
			final TransitionEvent<T> event = new TransitionEvent<T>(document, transition, action);
			for (final TransitionHandler<T> handler : transitionsHandlers)
				handler.onExecute(event);
		}
		
		// Remove old states
		for (String initial : transition.initials()) {
			for (final State<T> state : states) {
				if (state.name().equals(initial)) {
					if (!state.done())
						dataManager.markStateDone(state);
					dataManager.deleteState(state);
				} else {
					resultStates.add(state);
				}
			}
		}
		
		// Create new states
		for (String destination : transition.destinations()) {
			final State<T> newState = dataManager.createState(document, destination);
			resultStates.add(newState);
			dataManager.saveState(newState);
		}

		return resultStates;
	}

	@Override
	public List<State<T>> getCurrent(T document) {
		return dataManager.getCurrent(document);
	}

	@Override
	public void addTransitionHandler(TransitionHandler<T> handler) {
		transitionsHandlers.add(handler);
	}

}
