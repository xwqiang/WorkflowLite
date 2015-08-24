package fr.flafla.workflow.common.test;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import fr.flafla.workflow.common.State;

public class Document {
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