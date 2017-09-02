/*
 * JRig Java Binding framework. Copyright (C) 2006 Paul Szego.
 * 
 * This library is free software; you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation; either version 2.1 of the License, or (at your option)
 * any later version.
 * 
 * This library is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 * 
 * You should have received a copy of the GNU Lesser General Public License
 * along with this library; if not, write to the Free Software Foundation,
 * Inc., 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA
 */
package de.ceruti.curcuma.core;

import java.util.ArrayList;
import java.util.Iterator;

import de.ceruti.curcuma.api.core.IndexSet;
import de.ceruti.curcuma.api.core.MutableIndexSet;
import de.ceruti.curcuma.api.core.Range;

/**
 * 
 * @author $Author: matteo $
 * @version $Rev: 43 $ $Date: 2010/10/20 19:06:38 $
 */
public class MutableIndexSetImpl extends IndexSetImpl implements
		MutableIndexSet {
	// ==========================================================================
	// Static Attributes
	// ==========================================================================

	/**
     * 
     */
	private static final long serialVersionUID = -1423855109441767037L;

	// ==========================================================================
	// Constructors
	// ==========================================================================
	/**
     *
     */
	public MutableIndexSetImpl() {
		super();
	}

	public MutableIndexSetImpl(Range r) {
		super(r);
	}

	public MutableIndexSetImpl(IndexSetImpl rhs) {
		super(rhs);
	}

	// ==========================================================================
	// Methods
	// ==========================================================================

	public void addIndex(int index) {
		add(Factory.range(index));
	}

	@Override
	public void addIndexes(IndexSet set) {
		Iterator<Range> i = set.rangesIterator();
		while (i.hasNext()) {
			add(i.next());
		}
	}

	@Override
	public void add(Range aRange) {
		if (aRange.getLength() == 0) {
			return;
		}

		if (ranges == null) {
			ranges = new ArrayList<Range>(1);
		}

		int pos = posForIndex(aRange.getLocation());

		if (pos >= ranges.size()) {
			/*
			 * The start of the range to add lies beyond the existing ranges, so
			 * we can simply append it.
			 */
			ranges.add(aRange);
		} else {
			if (range(pos).containsLocation(aRange.getLocation())) {
				pos++;
			}

			ranges.add(pos, aRange);
		}

		/*
		 * Combine with the preceding ranges if possible.
		 */
		while (pos > 0) {
			Range r = range(pos - 1);
			if (r.maxRange() < aRange.getLocation()) {
				break;
			}
			r = Factory.range(r.getLocation(), r.getLength()
					+ aRange.maxRange() - r.maxRange());
			ranges.remove(pos--);
			ranges.set(pos, r);
		}

		/*
		 * Combine with any following ranges where possible.
		 */
		while (pos + 1 < ranges.size()) {
			Range x = range(pos + 1);
			if (aRange.maxRange() < x.getLocation()) {
				break;
			}
			ranges.remove(pos + 1);
			if (x.maxRange() > aRange.maxRange()) {
				int offset = x.maxRange() - aRange.maxRange();
				x = range(pos);
				x = Factory.range(x.getLocation(), x.getLength() + offset);
				ranges.set(pos, x);
			}
		}
	}

	@Override
	public void clear() {
		ranges.clear();
	}

	public void removeIndex(int index) {
		remove(Factory.range(index, 1));
	}

	@Override
	public void removeIndexes(IndexSet aSet) {
		for (Iterator<Range> it = aSet.rangesIterator();it.hasNext();) {
			remove(it.next());
		}
	}

	@Override
	public void remove(Range aRange) {
		int pos;
		Range r;

		if (aRange.getLength() == 0 || ranges == null || ranges.isEmpty()) {
			return;
		}

		pos = posForIndex(aRange.getLocation());
		if (pos >= ranges.size()) {
			// Already empty.
			return;
		}

		while (pos < ranges.size()) {
			r = range(pos);
	
			Range a[] = r.rangesBySubtractingRange(aRange);
			if(a.length == 0)
				ranges.remove(pos);
			else if(a.length>=1)
				ranges.set(pos++,a[0]);
			
			if(a.length==2)
				ranges.add(pos++,a[1]);
		
		}
		

	}
	
	public void removeBUG(Range aRange) {
		int pos;
		Range r;

		if (aRange.getLength() == 0 || ranges == null || ranges.isEmpty()) {
			return;
		}

		pos = posForIndex(aRange.getLocation());
		if (pos >= ranges.size()) {
			// Already empty.
			return;
		}

		r = range(pos);

		if (r.getLocation() <= aRange.getLocation()) {
			if (r.getLocation() <= aRange.maxRange()) {
				if (r.maxRange() <= aRange.maxRange()) {
					/*
					 * Found range is entirely within range to remove, leaving
					 * next range to check at current position.
					 */
					ranges.remove(pos);
				} else {
					/*
					 * Range to remove is entirely within found range and
					 * overlaps the start of the found range ... shrink it and
					 * then we are finished.
					 */
					r = Factory.range(r.getLocation() + aRange.getLength(), r
							.getLength()
							- aRange.getLength());
					ranges.set(pos, r);
					pos++;
				}
			} else {
				if (r.maxRange() <= aRange.maxRange()) {
					/*
					 * Range to remove overlaps the end of the found range. May
					 * also overlap next range ... so shorten found range and
					 * move on.
					 */
					r = Factory.range(r.getLocation(), aRange.getLocation()
							- r.getLength());
					ranges.set(pos, r);
					pos++;
				} else {
					/*
					 * Range to remove is entirely within found range and
					 * overlaps the middle of the found range ... split it. Then
					 * we are finished.
					 */
					Range next = Factory.range(aRange.maxRange(), r.maxRange()
							- aRange.maxRange());

					r = Factory.range(r.getLocation(), aRange.getLocation()
							- r.getLocation());
					ranges.set(pos, r);
					pos++;
					ranges.add(pos, next);
					pos++;
				}
			}
		}

		/*
		 * At this point we are guaranteed that, if there is a range at pos, it
		 * does not start before aRange.location
		 */
		while (pos < ranges.size()) {
			r = range(pos);

			if (r.maxRange() <= aRange.maxRange()) {
				/*
				 * Found range is entirely within range to remove ... delete it.
				 */
				ranges.remove(pos);
			} else {
				if (r.getLocation() < aRange.maxRange()) {
					/*
					 * Range to remove overlaps start of found range ... shorten
					 * it.
					 */
					r = Factory.range(aRange.maxRange(), r.maxRange()
							- aRange.maxRange());
					ranges.set(pos, r);
				}
				/*
				 * Found range extends beyond range to remove ... finished.
				 */
				break;
			}
		}

	}

	/**
	 * Shifts a group of indexes to the left or the right within the receiver.
	 * 
	 * <p>
	 * The group of indexes shifted is made up by startIndex and the indexes
	 * that follow it in the receiver.
	 * 
	 * <p>
	 * A left shift deletes the indexes in the range (startIndex-delta,delta)
	 * from the receiver.
	 * 
	 * <p>
	 * A right shift inserts empty space in the range (indexStart,delta) in the
	 * receiver.
	 * 
	 * @param anIndex
	 *            Head of the group of indexes to shift.
	 * 
	 * @param amount
	 *            Amount and direction of the shift. Positive integers shift the
	 *            indexes to the right. Negative integers shift the indexes to
	 *            the left.
	 */
	public void shiftIndexes(int anIndex, int amount) {
		if (amount == 0 || ranges == null || ranges.isEmpty()) {
			return;
		}

		int c;
		int pos;

		if (amount > 0) {
			c = ranges.size();
			pos = posForIndex(anIndex);

			if (pos < c) {
				Range r = range(pos);

				/*
				 * If anIndex is within an existing range, we split that range
				 * so we have one starting at anIndex.
				 */
				if (r.getLocation() < anIndex) {
					Range t;

					/*
					 * Split the range.
					 */
					t = Factory.range(r.getLocation(), anIndex
							- r.getLocation());
					ranges.add(pos, t);
					c++;
					r = Factory.range(anIndex, r.maxRange() - anIndex);
					ranges.set(++pos, r);
				}

				/*
				 * Shift all higher ranges to the right.
				 */
				while (c > pos) {
					r = range(--c);

					// TODO testing for overflow!
					r = Factory.range(r.getLocation() + amount, r.getLength());
					ranges.set(c, r);
				}
			}
		} else {
			amount = -amount;

			/*
			 * Delete range which will be overwritten.
			 */
			if (amount >= anIndex) {
				remove(Factory.range(0, anIndex));
			} else {
				remove(Factory.range(anIndex - amount, amount));
			}
			pos = posForIndex(anIndex);

			/*
			 * Now shift everything left into the hole we made.
			 */
			c = ranges.size();
			while (c > pos) {
				Range r = range(--c);

				if (r.maxRange() <= amount) {
					ranges.remove(c);
				} else if (r.getLocation() <= amount) {
					r = Factory.range(0, r.getLength() + r.getLocation()
							- amount);
					ranges.set(c, r);
				} else {
					r = Factory.range(r.getLocation() - amount, r.getLength());
					ranges.set(c, r);
				}
			}
		}

	}

}
