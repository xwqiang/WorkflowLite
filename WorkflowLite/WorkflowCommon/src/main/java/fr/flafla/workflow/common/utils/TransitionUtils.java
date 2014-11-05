package fr.flafla.workflow.common.utils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import fr.flafla.workflow.common.State;

/**
 * This class provides some useful methods to deal with states and transitions
 * 
 * @author rflafla
 *
 */
public final class TransitionUtils {
	
	private TransitionUtils() {
	}
	
	public static String[] extractNames(Object[] objects) {
		final String [] names = new String[objects.length];
		for (int i = 0; i < objects.length; i++) {
			names[i] = objects[i].toString();
		}
		return names;
	}
	
	public static <T> Collection<String> extractStateNames(Iterable<State<T>> states) {
		final List<String> result = new ArrayList<String>();
		for (State<T> state : states) {
			result.add(state.name());
		}
		return result;
	}
	
	public static <T> Set<String> extractSetStateNames(Iterable<State<T>> states) {
		final Set<String> result = new HashSet<String>();
		for (State<T> state : states) {
			result.add(state.name());
		}
		return result;
	}
}
