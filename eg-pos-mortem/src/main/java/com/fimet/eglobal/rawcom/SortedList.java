package com.fimet.eglobal.rawcom;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

public class SortedList<T> implements List<T>{
	private Comparator<T> comparator;
	private int size;
	private Node<T> head = new Node<T>();
	private Node<T> tail = new Node<T>();
	public SortedList(Comparator<T> comparator) {
		this.comparator = comparator;
		head.next(tail);
		tail.prev(head);
	}
	private void checkIndex(int index) {
		if (index < 0 || index >= size) 
			throw new IndexOutOfBoundsException(""+index);
	}
	@Override
	public boolean add(T e) {
		if (isEmpty()) {
			new Node<T>(e).prev(head).next(tail);
		} else if (comparator.compare(head.next.value, e) >= 0) {
			head.next(new Node<T>(e).next(head.next));
		} else if (comparator.compare(e, tail.prev.value) >= 0) {
			new Node<T>(e).prev(tail.prev).next(tail);
		} else {
			Node<T> n = tail;
			while ((n = n.prev)!=null && comparator.compare(n.value, e) > 0) {}
			Node<T> node = new Node<T>(e); 
			n.next.prev(node);
			node.prev(n);
		}
		size++;
		return true;
	}

	@Override
	public void add(int index, T element) {
		checkIndex(index); 
		Node<T> node = head;
		while ((node = node.next)!=null && index-- >= 0) {
		}
		new Node<T>(element).next(node).prev(node.prev);
	}
	@Override
	public boolean addAll(Collection<? extends T> c) {
		for (T t : c) {
			add(t);
		}
		return true;
	}

	@Override
	public boolean addAll(int index, Collection<? extends T> c) {
		return false;
	}

	@Override
	public void clear() {
		if (!isEmpty()) {
			Node<T> first = head.next;
			Node<T> last = tail.prev;
			head.next(tail);
			tail.prev(head);
			first.prev = null;
			last.next = null;
			size = 0;
		}
	}

	@Override
	public boolean contains(Object o) {
		Node<T> node = head;
		while ((node = node.next) != null) {
			if (node.value == o) {
				return true;
			}
		}
		return false;
	}

	@Override
	public boolean containsAll(Collection<?> c) {
		return false;
	}

	@Override
	public T get(int index) {
		checkIndex(index);
		index += 1;
		Node<T> node = head;
		while ((node = node.next) != null && index-- > 0) {
		}
		return node.value;
	}

	@Override
	public int indexOf(Object o) {
		Node<T> node = head;
		int index = 0;
		while ((node = node.next) != null) {
			if (node.value == o) {
				return index;
			}
			index++;
		}
		return -1;
	}

	@Override
	public boolean isEmpty() {
		return size == 0;
	}

	@Override
	public Iterator<T> iterator() {
		return new SortedIterator();
	}
	public Iterator<T> iterator(T start) {
		return new SortedIterator(start);
	}
	@Override
	public int lastIndexOf(Object o) {
		Node<T> node = tail;
		int index = size-1;
		while ((node = node.prev) != null) {
			if (node.value == o) {
				return index;
			}
			index--;
		}
		return -1;
	}

	@Override
	public ListIterator<T> listIterator() {
		return null;
	}

	@Override
	public ListIterator<T> listIterator(int index) {
		return null;
	}

	@Override
	public boolean remove(Object o) {
		Node<T> node = head;
		while ((node = node.next) != null) {
			if (node.value == o) {
				node.next.prev(node.prev);
				node.prev = null;
				node.next = null;
				size--;
				return true;
			}
		}
		return false;
	}

	@Override
	public T remove(int index) {
		checkIndex(index);
		Node<T> node = head;
		while ((node = node.next) != null && index >= 0) {
			index--;
		}
		T value = node.value;
		node.next.prev(node.prev);
		node.prev = null;
		node.next = null;
		size--;
		return value;
	}

	@Override
	public boolean removeAll(Collection<?> c) {
		for (Object o : c) {
			remove(o);
		}
		return false;
	}

	@Override
	public boolean retainAll(Collection<?> c) {
		return false;
	}

	@Override
	public T set(int index, T element) {
		getNode(index).value = element;
		return element;
	}
	private Node<T> getNode(int index) {
		checkIndex(index);
		Node<T> n = head;
		while (index-->=0) {
			n = n.next;
		} 
		return n;
	}
	@Override
	public int size() {
		return size;
	}

	@Override
	public List<T> subList(int fromIndex, int toIndex) {
		return null;
	}

	@Override
	public Object[] toArray() {
		Object[] array = new Object[size];
		Node<T> n = head;
		for (int i = 0; i < array.length; i++) {
			n = n.next;
			array[i] = n.value;
		}
		return array;
	}

	@SuppressWarnings("unchecked")
	@Override
	public <S> S[] toArray(S[] a) {
		Node<T> n = head;
		for (int i = 0; i < a.length; i++) {
			n = n.next;
			a[i] = (S)n.value;
		}
		return a;
	}
	public class SortedIterator implements Iterator<T> {
		Node<T> next;
		public SortedIterator() {
			next = head.next;
		}
		public SortedIterator(T value) {
			next = head.next;
			while (hasNext() && next.value != value) {
				next = next.next;
			}
		}
		@Override
		public boolean hasNext() {
			return next != tail;
		}
		@Override
		public T next() {
			T value = next.value;
			next = next.next;
			return (T)value;
		}
	}
	private class Node<R> {
		R value;
		Node<R> next;
		Node<R> prev;
		public Node() {
		}
		public Node(R value) {
			this.value = value;
		}
		public Node<R> prev(Node<R> prev) {
			this.prev = prev;
			prev.next = this;
			return this;
		}
		public Node<R> next(Node<R> next) {
			this.next = next;
			next.prev = this;
			return this;
		}
	}
	public T removeFirst() {
		if (isEmpty()) {
			return null;
		}
		Node<T> removed = head.next;
		head.next.next.prev(head);
		removed.next = null;
		removed.prev = null;
		size--;
		return removed.value;
	}
	public String toString() {
		if (isEmpty()) {
			return "";
		}
		StringBuilder s = new StringBuilder();
		Node<T> node = head;
		while ((node = node.next) != tail) {
			s.append(node.value).append(",");
		}
		s.delete(s.length()-1, s.length());
		return s.toString();
	}
	public static void main(String[] args) throws ParseException {
		Comparator<Rawcom> c = (l, r)-> {
			return l.getTime().compareTo(r.getTime());
		};
		SimpleDateFormat format = new SimpleDateFormat("yyMMdd HH:mm:ss.SSS");
		Rawcom r = new Rawcom();
		Rawcom s = new Rawcom();
		Rawcom t = new Rawcom();
		SortedList<Rawcom> list = new SortedList<Rawcom>(c);
		r = new Rawcom();
		r.setTime(format.parse("20220329 12:42:40.011"));
		list.add(r);
		r = new Rawcom();
		r.setTime(format.parse("20220329 12:42:40.230"));
		list.add(r);
		r = new Rawcom();
		r.setTime(format.parse("20220329 12:42:40.590"));
		list.add(r);
		t = r = new Rawcom();
		r.setTime(format.parse("20220329 12:42:40.770"));
		list.add(r);
		r = new Rawcom();
		r.setTime(format.parse("20220329 12:42:41.075"));
		list.add(r);
		s = r = new Rawcom();
		r.setTime(format.parse("20220329 12:42:40.231"));
		list.add(r);
		r = new Rawcom();
		r.setTime(format.parse("20220329 12:42:40.388"));
		list.add(r);
		Iterator<Rawcom> i = list.iterator();
		System.out.println("all**************");
		while (i.hasNext()) {
			System.out.println(format.format(i.next().getTime()));
		}
		list.remove(s);
		list.remove(t);
		System.out.println("removed**************");
		i = list.iterator();
		while (i.hasNext()) {
			Rawcom next = i.next();
			System.out.println(format.format(next.getTime()));
			list.remove(next);
		}
		System.out.println("removed-all**************");
		i = list.iterator();
		while (i.hasNext()) {
			Rawcom next = i.next();
			System.out.println(format.format(next.getTime()));
		}		
	}
}
