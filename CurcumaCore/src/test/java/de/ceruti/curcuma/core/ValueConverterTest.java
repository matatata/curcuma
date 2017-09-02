package de.ceruti.curcuma.core;

import junit.framework.TestCase;
import de.ceruti.curcuma.api.core.exceptions.ConversionException;
import de.ceruti.curcuma.core.ValueConverter;

public class ValueConverterTest extends TestCase {
	public void testString() throws ConversionException {
		// Simple
		assertEquals(new Integer(1), ValueConverter.convertObject("1", Integer.class));
		assertEquals(new Float(1.1f), ValueConverter.convertObject("1.1f", Float.class));
		assertEquals(new Short((short) 33), ValueConverter.convertObject("33", Short.class));
		assertEquals(new Long(2233), ValueConverter.convertObject("2233", Long.class));
		assertEquals(new Double(-2.1), ValueConverter.convertObject("-2.1", Double.class));
		assertEquals(new Byte((byte) 255), ValueConverter.convertObject("255", Byte.class));

		assertEquals(Boolean.FALSE, ValueConverter.convertObject("false", Boolean.class));
		assertEquals(Boolean.TRUE, ValueConverter.convertObject("true", Boolean.class));
		assertEquals(new Character('g'), ValueConverter.convertObject("g", Character.class));
		assertEquals('x', ValueConverter.convertObject("x", Character.class));

		assertEquals("Mikey", ValueConverter.convertObject("Mikey", String.class));

	}

	public void testString2Prim() throws ConversionException {
		// Simple
		assertEquals(1, ValueConverter.convertObject("1", int.class));
		assertEquals(1.1f, ValueConverter.convertObject("1.1f", float.class));
		assertEquals((short) 33, ValueConverter.convertObject("33", short.class));
		assertEquals(2233L, ValueConverter.convertObject("2233", long.class));
		assertEquals(-2.1, ValueConverter.convertObject("-2.1", double.class));
		assertEquals((byte) 255, ValueConverter.convertObject("255", byte.class));

		assertEquals(false, ValueConverter.convertObject("false", boolean.class));
		assertEquals(true, ValueConverter.convertObject("true", boolean.class));
		assertEquals('g', ValueConverter.convertObject("g", char.class));

	}

	public void testString3() throws ConversionException {
		// Simple
		assertEquals(1, ValueConverter.convertObject("1", Integer.class));
		assertEquals(1.1f, ValueConverter.convertObject("1.1f", Float.class));
		assertEquals((short) 33, ValueConverter.convertObject("33", Short.class));
		assertEquals(2233L, ValueConverter.convertObject("2233", Long.class));
		assertEquals(-2.1, ValueConverter.convertObject("-2.1", Double.class));
		assertEquals((byte) 255, ValueConverter.convertObject(new Double(255), Byte.class));

		assertEquals(false, ValueConverter.convertObject("false", Boolean.class));
		assertEquals(true, ValueConverter.convertObject("true", Boolean.class));
		assertEquals('g', ValueConverter.convertObject("g", Character.class));

	}

}
