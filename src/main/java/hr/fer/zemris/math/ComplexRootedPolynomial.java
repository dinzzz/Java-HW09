package hr.fer.zemris.math;

/**
 * Class that represents a complex polynom when formed with complex roots.
 * 
 * @author Dinz
 *
 */
public class ComplexRootedPolynomial {
	/**
	 * Roots of the polynom.
	 */
	private Complex[] roots;

	public ComplexRootedPolynomial(Complex... roots) {
		this.roots = roots;
	}

	/**
	 * Calculates the value of the polynom with the given complex number.
	 * 
	 * @param z
	 *            Complex number.
	 * @returns Calculated value of the polynom.
	 */
	public Complex apply(Complex z) {
		Complex total = Complex.ONE;
		for (int i = 0; i < roots.length; i++) {
			total = total.multiply(z.sub(roots[i]));
		}
		return total;
	}

	/**
	 * Transforms a rooted polynom to a generic polynom format.
	 * 
	 * @return Transformed standard polynom format.
	 */
	public ComplexPolynomial toComplexPolynom() {
		ComplexPolynomial newP = new ComplexPolynomial(new Complex(1, 0));
		for (int i = 0; i < roots.length; i++) {
			newP = newP.multiply(new ComplexPolynomial(roots[i].negate(), new Complex(1, 0)));
		}

		return newP;
	}

	/**
	 * Transforms a polynom to a string format.
	 */
	public String toString() {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < roots.length; i++) {
			if (i == roots.length - 1) {
				sb.append("(z-(" + roots[i] + "))");
			} else {
				sb.append("(z-(" + roots[i] + "))*");
			}
		}

		return sb.toString();
	}

	/**
	 * Calculates the closest root for the complex value based on the treshold.
	 * 
	 * @param z
	 *            Complex number.
	 * @param treshold
	 *            Threshold.
	 * @return Index of the closest root.
	 */
	public int indexOfClosestRootFor(Complex z, double treshold) {
		int minimum = -1;
		double currentMinimum = 0;

		for (int i = 0; i < roots.length; i++) {
			if (i == 0) {
				currentMinimum = distance(z, roots[i]);
				minimum = i;
			} else {
				if (distance(z, roots[i]) < currentMinimum && distance(z, roots[i]) < treshold) {
					currentMinimum = distance(z, roots[i]);
					minimum = i;
				}
			}
		}

		return minimum;
	}

	/**
	 * Calculates the distance between complex numbers.
	 * 
	 * @param z
	 *            First complex number.
	 * @param y
	 *            Second complex number.
	 * @return Distance between complex numbers.
	 */
	private double distance(Complex z, Complex y) {
		return z.sub(y).module();
	}

}
