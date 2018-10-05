package hr.fer.zemris.math;

import org.junit.Test;
import org.junit.Assert;

public class ComplexTest {
	
	private static final double DELTA = 1E-5;
	
	@Test
	public void creationTest() {
		Complex c = new Complex();
		
		double expected = 0;
		double actual = c.re;
		double actual2 = c.im;
		
		Assert.assertEquals(expected, actual2, DELTA);
		Assert.assertEquals(expected, actual, DELTA);
	}
	
	@Test
	public void creationComponentsTest() {
		Complex c = new Complex(3, -5.16);
		
		double expected = 3;
		double expected2 = -5.16;
		
		double actual = c.re;
		double actual2 = c.im;
		
		Assert.assertEquals(expected2, actual2, DELTA);
		Assert.assertEquals(expected, actual, DELTA);	
		
		
	}
	
	@Test
	public void moduleTest() {
		Complex c = new Complex(3, 4);
		
		double expected = 5;
		double actual = c.module();
		Assert.assertEquals(expected, actual, DELTA);
	}
	
	@Test
	public void mulitplyTest() {
		Complex c = new Complex(1, 2);
		Complex c1 = new Complex(2, 3);
		Complex c2 = c.multiply(c1);
		
		double expected = -4;
		double expected2 = 7;
		
		double actual = c2.re;
		double actual2 = c2.im;
		
		Assert.assertEquals(expected2, actual2, DELTA);
		Assert.assertEquals(expected, actual, DELTA);
	}
	
	@Test
	public void divideTest() {
		Complex c = new Complex(1, 2);
		Complex c1 = new Complex(2, 3);
		Complex c2 = c.divide(c1);
		
		double expected = 8.0/13.0;
		double expected2 = 1.0/13.0;
		
		double actual = c2.re;
		double actual2 = c2.im;
		
		Assert.assertEquals(expected2, actual2, DELTA);
		Assert.assertEquals(expected, actual, DELTA);
	}
	
	@Test
	public void addTest() {
		Complex c = new Complex(1, 2);
		Complex c1 = new Complex(2, 3);
		Complex c2 = c.add(c1);
		
		double expected = 3;
		double expected2 = 5;
		
		double actual = c2.re;
		double actual2 = c2.im;
		
		Assert.assertEquals(expected2, actual2, DELTA);
		Assert.assertEquals(expected, actual, DELTA);
	}
	
	@Test
	public void subTest() {
		Complex c = new Complex(1, 2);
		Complex c1 = new Complex(2, 3);
		Complex c2 = c.sub(c1);
		
		double expected = -1;
		double expected2 = -1;
		
		double actual = c2.re;
		double actual2 = c2.im;
		
		Assert.assertEquals(expected2, actual2, DELTA);
		Assert.assertEquals(expected, actual, DELTA);
	}
	
	@Test
	public void negateTest() {
		Complex c = new Complex(1, 2);
		Complex c2 = c.negate();
		
		double expected = -1;
		double expected2 = -2;
		
		double actual = c2.re;
		double actual2 = c2.im;
		
		Assert.assertEquals(expected2, actual2, DELTA);
		Assert.assertEquals(expected, actual, DELTA);
	}
	
	
	
}
