package hr.fer.zemris.math;

import org.junit.Test;
import org.junit.Assert;

public class Vector3Test {
	public static final double DELTA = 1E-4;
	
	@Test
	public void creationTest() {
		Vector3 vec = new Vector3(1, 2, 3);
		
		double expected = 1;
		double expected2 = 2;
		double expected3 = 3;
		
		double actual = vec.getX();
		double actual2 = vec.getY();
		double actual3 = vec.getZ();
		
		Assert.assertEquals(expected3, actual3, DELTA);
		Assert.assertEquals(expected2, actual2, DELTA);
		Assert.assertEquals(expected, actual, DELTA);
		
		
	}
	
	@Test
	public void normTest() {
		Vector3 vec = new Vector3(1, 2, 3);
		
		double expected = Math.sqrt(14);
		double actual = vec.norm();
		
		Assert.assertEquals(expected, actual, DELTA);
	}
	
	@Test
	public void normalizedTest() {
		Vector3 vec = new Vector3(1, 2, 3);
		Vector3 vecNormalized = vec.normalized();
		
		double expected = 1/Math.sqrt(14);
		double expected2 = 2/Math.sqrt(14);
		double expected3 = 3/Math.sqrt(14);
		
		double actual = vecNormalized.getX();
		double actual2 = vecNormalized.getY();
		double actual3 = vecNormalized.getZ();
		
		Assert.assertEquals(expected3, actual3, DELTA);
		Assert.assertEquals(expected2, actual2, DELTA);
		Assert.assertEquals(expected, actual, DELTA);
		
	}
	
	@Test
	public void scaleTest() {
		Vector3 vec = new Vector3(1, 2, 3);
		vec = vec.scale(2);
		
		double expected = 2;
		double expected2 = 4;
		double expected3 = 6;
		
		double actual = vec.getX();
		double actual2 = vec.getY();
		double actual3 = vec.getZ();
		
		Assert.assertEquals(expected3, actual3, DELTA);
		Assert.assertEquals(expected2, actual2, DELTA);
		Assert.assertEquals(expected, actual, DELTA);
	}
	
	@Test
	public void addTest() {
		Vector3 vec = new Vector3(1, 2, 3);
		vec = vec.add(new Vector3(2, 3, 4));
		
		double expected = 3;
		double expected2 = 5;
		double expected3 = 7;
		
		double actual = vec.getX();
		double actual2 = vec.getY();
		double actual3 = vec.getZ();
		
		Assert.assertEquals(expected3, actual3, DELTA);
		Assert.assertEquals(expected2, actual2, DELTA);
		Assert.assertEquals(expected, actual, DELTA);
	}
	
	@Test
	public void subTest() {
		Vector3 vec = new Vector3(1, 2, 3);
		vec = vec.sub(new Vector3(3, 2, 2));
		
		double expected = -2;
		double expected2 = 0;
		double expected3 = 1;
		
		double actual = vec.getX();
		double actual2 = vec.getY();
		double actual3 = vec.getZ();
		
		Assert.assertEquals(expected3, actual3, DELTA);
		Assert.assertEquals(expected2, actual2, DELTA);
		Assert.assertEquals(expected, actual, DELTA);
	}
	
	@Test
	public void dotTest() {
		Vector3 vec = new Vector3(1, 2, 3);
		Vector3 vec2 = new Vector3(3, 2, 2);
		
		double expected = 13;
		double actual = vec.dot(vec2);
		
		Assert.assertEquals(expected, actual, DELTA);
		
	}
	
	@Test
	public void crossTest() {
		Vector3 i = new Vector3(1, 0, 0);
		Vector3 j = new Vector3(0, 1, 0);
		Vector3 vec = i.cross(j);
		
		double expected = 0;
		double expected2 = 0;
		double expected3 = 1;
		
		double actual = vec.getX();
		double actual2 = vec.getY();
		double actual3 = vec.getZ();
		
		Assert.assertEquals(expected3, actual3, DELTA);
		Assert.assertEquals(expected2, actual2, DELTA);
		Assert.assertEquals(expected, actual, DELTA);
	}
	

}
