package org.vaadin.smartgwt.server.core;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import com.google.common.collect.Lists;
import com.vaadin.terminal.PaintException;
import com.vaadin.terminal.PaintTarget;
import com.vaadin.ui.Component;

public class ComponentList<E extends Component> implements PaintableProperty, Iterable<E> {
	private final List<E> components = Lists.newArrayList();
	private final List<Instruction<E>> instructions = Lists.newArrayList();
	private final String tagName;

	public ComponentList(String tagName) {
		this.tagName = tagName;
	}

	public void add(E e) {
		components.add(e);
		instructions.add(new Instruction<E>("add", e));
	}

	public void add(int index, E element) {
		components.add(index, element);
		instructions.add(new Instruction<E>("add", index, element));
	}

	public void addAll(Collection<? extends E> c) {
		for (E e : c) {
			add(e);
		}
	}

	public void remove(E e) {
		final int index = components.indexOf(e);

		if (components.remove(e)) {
			instructions.add(new Instruction<E>("remove", index, e));
		}
	}

	public E get(int index) {
		return components.get(index);
	}

	public E set(int index, E element) {
		final E oldElement = components.set(index, element);
		instructions.add(new Instruction<E>("remove", index, oldElement));

		if (components.size() - 1 == index) {
			instructions.add(new Instruction<E>("add", element));
		} else {
			instructions.add(new Instruction<E>("add", index, element));
		}

		return oldElement;
	}

	public void clear() {
		for (Iterator<E> iterator = iterator(); iterator.hasNext();) {
			iterator.next();
			iterator.remove();
		}
	}

	public E[] toArray(E[] a) {
		return new ArrayList<E>(components).toArray(a);
	}

	public boolean contains(Object o) {
		return components.contains(o);
	}

	public int indexOf(Object o) {
		return components.indexOf(o);
	}

	@Override
	public void paintContent(PaintTarget target) throws PaintException {
		target.startTag(tagName);
		target.addAttribute("type", "List");

		for (Component component : components) {
			component.paint(target);
		}

		if (target.isFullRepaint()) {
			for (E component : components) {
				new Instruction<E>("add", component).paintContent(target);
			}
		} else {
			for (Instruction<E> instruction : instructions) {
				instruction.paintContent(target);
			}
		}

		instructions.clear();
		target.endTag(tagName);
	}

	@Override
	public Iterator<E> iterator() {
		return new ComponentIterator(components.iterator());
	}

	private class ComponentIterator implements Iterator<E> {
		private final Iterator<E> source;
		private E next;

		public ComponentIterator(Iterator<E> source) {
			this.source = source;
		}

		@Override
		public boolean hasNext() {
			return source.hasNext();
		}

		@Override
		public E next() {
			return next = source.next();
		}

		@Override
		public void remove() {
			source.remove();
			instructions.add(new Instruction<E>("remove", next));
		}
	}

	private static class Instruction<E extends Component> {
		private final E element;
		private final Integer index;
		private final String name;

		public Instruction(String name, E element) {
			this.name = name;
			this.index = null;
			this.element = element;
		}

		public Instruction(String name, int index, E element) {
			this.name = name;
			this.index = index;
			this.element = element;
		}

		public void paintContent(PaintTarget target) throws PaintException {
			target.startTag(name);

			if (index != null) {
				target.addAttribute("index", index);
			}

			if (element != null) {
				target.addAttribute("element", element);
				detachRemovedElement();
			}

			target.endTag(name);
		}

		private void detachRemovedElement() {
			if ("remove".equals(name)) {
				element.setParent(null);
			}
		}
	}
}