package de.ceruti.curcuma.core;

/*
 * Portions inspired by JRig Java Binding framework. Copyright (C) 2006 Paul Szego.
 */
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;

import de.ceruti.curcuma.api.core.IndexSet;
import de.ceruti.curcuma.api.core.MutableIndexSet;
import de.ceruti.curcuma.api.core.Range;

class IndexSetImpl implements Iterable<Integer>, Serializable, IndexSet {
	private static final long serialVersionUID = 7993570471553454454L;


	public static final IndexSetImpl EMPTY_SET = new IndexSetImpl();


	protected List<Range> ranges = new ArrayList<Range>(0);


	public IndexSetImpl() {
		// do nothing.
	}

	public IndexSetImpl(Range r) {
		ranges = new ArrayList<Range>(1);
		ranges.add(r);
	}

	public IndexSetImpl(IndexSetImpl rhs) {
		if (rhs.ranges != null) {
			ranges = new ArrayList<Range>(rhs.ranges);
		}
	}

	
	@Override
	public boolean isMutable() {
		return false;
	}

	@Override
	public MutableIndexSet mutableIndexSet() {
		return new MutableIndexSetImpl(this);
	}
	
	@Override
	public IndexSet immutableIndexSet() {
		return new IndexSetImpl(this);
	}

	/**
	 * Returns an iterator over a set of elements of type <code>Integer</code>
	 * in order to satisfy the <code>Iterable</code> interface.
	 * 
	 * <p>
	 * Implementing that interface allows an object to be the target of the
	 * "foreach" statement.
	 * 
	 * @return an Iterator.
	 */
	@Override
	public Iterator<Integer> iterator() {
		return new Iterator<Integer>() {
			private int index = firstIndex();

			@Override
			public boolean hasNext() {
				return index != NotFound;
			}

			@Override
			public Integer next() {
				if (index == NotFound) {
					throw new NoSuchElementException();
				}
				Integer result = Integer.valueOf(index);
				index = IndexSetImpl.this.indexGreaterThan(index);
				return result;
			}

			@Override
			public void remove() {
				throw new UnsupportedOperationException();
			}
		};
	}


	@Override
	public Iterator<Range> rangesIterator() {
		return new ArrayList<Range>(ranges).listIterator();
	}

	protected Range range(int index) {
		return ranges.get(index);
	}

	protected int posForIndex(int index) {
		int upper = ranges.size();
		int lower = 0;
		int pos = 0;

		/*
		 * Binary search for an item equal to the one to be inserted.
		 */
		for (pos = upper / 2; upper != lower; pos = (upper + lower) / 2) {
			Range r = range(pos);

			if (index < r.getLocation()) {
				upper = pos;
			} else if (index > r.maxRange()) {
				lower = pos + 1;
			} else {
				break;
			}
		}

		/*
		 * Now skip past any item containing no values as high as the index.
		 */
		while (pos < ranges.size() && index >= range(pos).maxRange()) {
			pos++;
		}
		return pos;

	}

	public boolean containsIndex(int index) {
		if (ranges == null || ranges.isEmpty()) {
			return false;
		}
		int pos = posForIndex(index);
		if (pos >= ranges.size()) {
			return false;
		}
		return range(pos).containsLocation(index);
	}

	public boolean containsIndexes(IndexSetImpl set) {
		Iterator i = set.ranges.iterator();
		while (i.hasNext()) {
			Range r = (Range) i.next();
			if (!this.containsIndexes(r)) {
				return false;
			}
		}

		return true;
	}

	public boolean containsIndexes(Range r) {
		if (ranges == null || ranges.isEmpty()) {
			return false;
		}
		int pos = posForIndex(r.getLocation());
		if (pos >= ranges.size()) {
			return false;
		}

		if (r.getLength() == 0) {
			return true;
		}

		Range x = range(pos);
		return x.containsLocation(r.getLocation())
				&& x.containsLocation(r.maxRange() - 1);
	}

	public int size() {
		int total = 0;
		if (ranges != null) {
			Iterator i = ranges.iterator();
			while (i.hasNext()) {
				Range r = (Range) i.next();
				total += r.getLength();
			}
		}
		return total;
	}

	@Override
	public boolean isEmpty() {
		return ranges == null || ranges.isEmpty() || size() == 0;
	}

	/**
	 * Not empty, and ( first + size - 1 ) == last.
	 * 
	 * @return <code>true</code> if this index set represents a contiguous set
	 *         of indexes; <code>false</code> otherwise.
	 */
	@Override
	public boolean isContiguous() {
		return ranges != null && ranges.size() == 1;
	}

	@Override
	public int firstIndex() {
		if (ranges == null || ranges.isEmpty()) {
			return NotFound;
		}
		return range(0).getLocation();
	}

	@Override
	public int lastIndex() {
		if (ranges == null || ranges.isEmpty()) {
			return NotFound;
		}
		return range(ranges.size() - 1).maxRange() - 1;
	}


	@Override
	public int indexGreaterOrEqualThan(int index) {
		if (index == Integer.MAX_VALUE) {
			return NotFound;
		}

		if (ranges == null || ranges.isEmpty()) {
			return NotFound;
		}
		int pos = posForIndex(index);
		if (pos >= ranges.size()) {
			return NotFound;
		}

		Range r = range(pos);
		if (r.containsLocation(index)) {
			return index;
		}
		return r.getLocation();
	}

	@Override
	public int indexLessOrEqualThan(int index) {
		if (index == Integer.MIN_VALUE) {
			return NotFound;
		}

		if (ranges == null || ranges.isEmpty()) {
			return NotFound;
		}
		int pos = posForIndex(index);
		if (pos < 0) {
			return NotFound;
		}

		Range r = range(pos);
		if (r.containsLocation(index)) {
			return index;
		}

		if (pos == 0)
			return NotFound;

		return range(pos - 1).maxRange() - 1;
	}


	/**
	 * 
	 * @param index
	 * @return {@link #NotFound} on failure
	 */
	@Override
	public int indexGreaterThan(int index) {
		return indexGreaterOrEqualThan(index + 1);
	}

	/**
	 * 
	 * @param index
	 * @return {@link #NotFound} on failure
	 */
	@Override
	public int indexLessThan(int index) {
		return indexLessOrEqualThan(index - 1);
	}

	@Override
	public int hashCode() {
		return 31 + ((ranges == null) ? 0 : ranges.hashCode());
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}

		if (!getClass().isAssignableFrom(obj.getClass())) {
			return false;
		}

		final IndexSetImpl other = (IndexSetImpl) obj;
		if (this.ranges == null || this.ranges.isEmpty()) {
			return (other.ranges == null || other.ranges.isEmpty());
		} else if (other.ranges == null || other.ranges.isEmpty()) {
			return false;
		}
		return this.ranges.equals(other.ranges);
	}

	@Override
	public String toString() {
		StringBuffer buf = new StringBuffer("(");

		if (ranges != null && ranges.size() > 0) {
			buf.append(range(0));
			for (int i = 1; i < ranges.size(); i++) {
				buf.append(',');
				buf.append(range(i));
			}
		}
		buf.append(')');
		return buf.toString();
	}

}
