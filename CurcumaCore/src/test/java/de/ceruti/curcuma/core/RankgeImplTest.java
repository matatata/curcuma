package de.ceruti.curcuma.core;

import static org.junit.Assert.*;

import org.junit.Test;

import de.ceruti.curcuma.api.core.Range;

public class RankgeImplTest {

	@Test
	public void test() {
		Range a = new RangeImpl(4,10);
		Range b = new RangeImpl(5,12);
		
		Range i = a.rangeByIntersectingRange(b);
		assertEquals(new RangeImpl(5, 9),i);
		
		Range u = a.rangeByUnioningRange(b);
		assertEquals(new RangeImpl(4, 13),u);
		
		Range l[] = b.rangesBySubtractingRange(a);
		assertArrayEquals(new Range[]{new RangeImpl(14, 3)},l);
		
		b = new RangeImpl(1,10);
		l= b.rangesBySubtractingRange(a);
		assertArrayEquals(new Range[]{new RangeImpl(1, 3)},l);
	}

}
