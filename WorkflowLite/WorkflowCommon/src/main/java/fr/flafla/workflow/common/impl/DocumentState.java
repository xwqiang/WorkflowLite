package fr.flafla.workflow.common.impl;

import fr.flafla.workflow.common.State;

/**
 * This is the default implementation of a state.
 * 
 * @author rflafla
 *
 * @param <T> The type of document
 */
public class DocumentState<T> implements State<T> {
	private final T document;
	private final String name;
	private boolean done;

	public DocumentState(T document, String name) {
		this(document, name, false);
	}
	
	public DocumentState(T document, String name, boolean done) {
		this.document = document;
		this.name = name;
		this.done = done;
	}

	@Override
	public T document() {
		return document;
	}

	@Override
	public String name() {
		return name;
	}
	
	@Override
	public boolean done() {
		return done;
	}

	public void markDone() {
		done = true;
	}
}
