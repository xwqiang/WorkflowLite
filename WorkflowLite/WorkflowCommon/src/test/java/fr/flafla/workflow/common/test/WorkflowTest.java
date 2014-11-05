package fr.flafla.workflow.common.test;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import org.junit.Assert;
import org.junit.Test;

import fr.flafla.workflow.common.DocumentManager;
import fr.flafla.workflow.common.NoTransitionException;
import fr.flafla.workflow.common.State;
import fr.flafla.workflow.common.Transition;
import fr.flafla.workflow.common.event.TransitionEvent;
import fr.flafla.workflow.common.event.TransitionHandler;
import fr.flafla.workflow.common.impl.ActionTransition;
import fr.flafla.workflow.common.impl.DocumentState;
import fr.flafla.workflow.common.impl.ProgrammaticWorkflowDefinition;
import fr.flafla.workflow.common.impl.WorkflowEngine;

/**
 * Test the workflow engine
 * 
 * @author rflafla
 *
 */
public class WorkflowTest {
	public static class Document {
		public List<State<Document>> steps = new ArrayList<State<Document>>();

		public AtomicInteger deletion = new AtomicInteger();
		public AtomicInteger insertion = new AtomicInteger();

		public boolean hasStep(String name) {
			for (State<Document> state : steps) {
				if (state.name().equals(name))
					return true;
			}
			return false;
		}

		@Override
		public String toString() {
			final StringBuilder sb = new StringBuilder();
			sb.append("[");
			for (State<Document> state : steps) {
				if (sb.length() > 1)
					sb.append(", ");
				sb.append(state.name());
			}
			sb.append("]");
			return sb.toString();
		}
	}

	private static Transition<Document> transition(Object action, Object initials, Object destinations) {
		return ActionTransition.<Document> create(action, new Object[] { initials }, destinations);
	}

	private static Transition<Document> fork(Object action, Object initials, Object... destinations) {
		return ActionTransition.<Document> create(action, new Object[] { initials }, destinations);
	}

	private static Transition<Document> join(Object action, Object[] initials, Object destinations) {
		return ActionTransition.<Document> create(action, initials, destinations);
	}

	private final ProgrammaticWorkflowDefinition<Document> workflowDefinition = new ProgrammaticWorkflowDefinition<Document>(
			transition("goto1", "initial", "1"),

			transition("goto2", "1", "2"),

			// Fork case
			fork("fork1", "2", "3a", "3b"),

			transition("goto4a", "3a", "4a"),

			// Join case
			join("join1", new Object[] { "4a", "3b" }, "5"),

			transition("goto6", "5", "6")
			);

	private final DocumentManager<Document> documentManager = new DocumentManager<WorkflowTest.Document>() {
		@Override
		public void saveState(State<Document> state) {
			final Document document = state.document();
			document.insertion.incrementAndGet();
			document.steps.add(state);
		}

		@Override
		public void markStateDone(State<Document> state) {
			((DocumentState<Document>) state).markDone();
		}

		@Override
		public List<State<Document>> getCurrent(Document document) {
			return document.steps;
		}

		@Override
		public void deleteState(State<Document> state) {
			final Document document = state.document();
			document.deletion.incrementAndGet();
			for (Iterator<State<Document>> iterator = document.steps.iterator(); iterator.hasNext();) {
				final State<Document> step = iterator.next();
				if (step.name().equals(state.name()))
					iterator.remove();
			}
		}

		@Override
		public State<Document> createState(Document document, String name) {
			return new DocumentState<Document>(document, name);
		}
	};

	private final WorkflowEngine<Document> workflow = new WorkflowEngine<Document>(documentManager, workflowDefinition);

	@Test
	public void testTransitions() throws NoTransitionException {
		workflow.addTransitionHandler(new TransitionHandler<WorkflowTest.Document>() {
			@Override
			public void onExecute(TransitionEvent<Document> event) {
				System.out.println("Transition : " + event.transition.action());
			}
		});

		final Document document = new Document();
		document.steps.add(new DocumentState<Document>(document, "initial"));

		Assert.assertTrue(workflow.isActionAllowed(document, "goto1"));
		Assert.assertFalse(workflow.isActionAllowed(document, "goto2"));
		Assert.assertFalse(workflow.isActionAllowed(document, "toto"));

		workflow.doAction(document, "goto1");
		assertStates(document, "1");

		try {
			workflow.doAction(document, "goto5");
			Assert.fail();
		} catch (final NoTransitionException e) {
		}

		workflow.doAction(document, "goto2");
		assertStates(document, "2");

		// Test fork
		workflow.doAction(document, "fork1");
		assertStates(document, "3a", "3b");
		Assert.assertFalse(workflow.isActionAllowed(document, "join1"));

		workflow.doAction(document, "goto4a");
		assertStates(document, "4a", "3b");
		Assert.assertTrue(workflow.isActionAllowed(document, "join1"));

		// Test join
		workflow.doAction(document, "join1");
		assertStates(document, "5");

		workflow.doAction(document, "goto6");
		assertStates(document, "6");

		Assert.assertEquals(7, document.insertion.get());
		Assert.assertEquals(7, document.deletion.get());
	}

	private void assertStates(final Document document, String... steps) {
		final List<State<Document>> current = workflow.getCurrent(document);
		Assert.assertEquals(steps.length, current.size());

		for (final String step : steps) {
			Assert.assertTrue(document.hasStep(step));
		}
	}
}
