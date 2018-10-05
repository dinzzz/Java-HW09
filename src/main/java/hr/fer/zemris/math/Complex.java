package hr.fer.zemris.math;

import static java.lang.Math.pow;
import static java.lang.Math.sqrt;
import static java.lang.Math.cos;
import static java.lang.Math.sin;
import static java.lang.Math.atan2;
import static java.lang.Math.PI;
import static java.lang.Math.abs;

import java.util.ArrayList;
import java.util.List;

/**
 * Class that represents a complex number. Complex number is defined with its
 * real and imaginary component.
 * 
 * @author Dinz
 *
 */
public class Complex {
	/**
	 * Real part of the complex number.
	 */
	 double re;
	/**
	 * Imaginary part of the complex number.
	 */
	 double im;

	public static final Complex ZERO = new Complex(0, 0);
	public static final Complex ONE = new Complex(1, 0);
	public static final Complex ONE_NEG = new Complex(-1, 0);
	public static final Complex IM = new Complex(0, 1);
	public static final Complex IM_NEG = new Complex(0, -1);

	/**
	 * Constructs a new complex number.
	 */
	public Complex() {
		this(0, 0);
	}

	/**
	 * Constructs a new complex number with given real and imaginary values.
	 * 
	 * @param re
	 *            Real value.
	 * @param im
	 *            Imaginary value.
	 */
	public Complex(double re, double im) {
		this.re = re;
		this.im = im;
	}

	/**
	 * Calculates the module of the complex number.
	 * 
	 * @return Module of the complex number.
	 */
	public double module() {
		return sqrt(pow(im, 2) + pow(re, 2));
	}

	/**
	 * Multiplies the two complex numbers.
	 * 
	 * @param c
	 *            Another complex number.
	 * @return Newly calculated complex number.
	 */
	public Complex multiply(Complex c) {
		return new Complex(this.re * c.re - this.im * c.im, this.im * c.re + this.re * c.im);
	}

	/**
	 * Divides the complex number with another one.
	 * 
	 * @param c
	 *            Another complex number.
	 * @return Newly calculated complex number.
	 */
	public Complex divide(Complex c) {
		return new Complex((this.re * c.re + this.im * c.im) / (pow(c.re, 2) + pow(c.im, 2)),
				(this.im * c.re - this.re * c.im) / (pow(c.re, 2) + pow(c.im, 2)));
	}

	/**
	 * Calculates the addition of the two complex number.
	 * 
	 * @param c
	 *            Another complex number.
	 * @return Newly calculated complex number.
	 */
	public Complex add(Complex c) {
		return new Complex(this.re + c.re, this.im + c.im);

	}

	/**
	 * Subtracts the complex number from the current one.
	 * 
	 * @param c
	 *            Another complex number.
	 * @return Newly calculated complex number.
	 */
	public Complex sub(Complex c) {
		return new Complex(this.re - c.re, this.im - c.im);

	}

	/**
	 * Negates the complex number.
	 * 
	 * @return Negated complex number.
	 */
	public Complex negate() {
		return this.multiply(ONE_NEG);
	}

	/**
	 * Calculates the complex power to the power of "n". N is a given number as an
	 * argument.
	 * 
	 * @param n
	 *            Power.
	 * @return Newly calculated complex number.
	 */
	public Complex power(int n) {
		double powerReal = pow(this.getMagnitude(), n) * cos(n * this.getAngle());
		double powerImaginary = pow(this.getMagnitude(), n) * sin(n * this.getAngle());

		return new Complex(powerReal, powerImaginary);
	}

	/**
	 * Gets the angle of the complex number.
	 * 
	 * @return Angle of the complex number.
	 */
	private double getAngle() {
		return atan2(this.im, this.re);
	}

	/**
	 * Gets the magnitude of the complex number.
	 * 
	 * @return Magnitude of the complex number.
	 */
	private double getMagnitude() {
		return sqrt(pow(this.re, 2) + pow(this.im, 2));
	}

	/**
	 * Returns the list of roots to the power of n.
	 * 
	 * @param n
	 *            Power of the root.
	 * @return List of roots.
	 */
	public List<Complex> root(int n) {
		List<Complex> list = new ArrayList<>();
		for (int k = 0; k < n; k++) {
			double rootReal = pow(this.getMagnitude(), 1 / n) * cos((this.getAngle() + 2 * k * PI) / n);
			double rootImaginary = pow(this.getMagnitude(), 1 / n) * sin((this.getAngle() + 2 * k * PI) / n);
			list.add(new Complex(rootReal, rootImaginary));
		}

		return list;
	}

	/**
	 * Transforms the complex number to the string format.
	 */
	public String toString() {
		StringBuilder sb = new StringBuilder();
		if (!(this.re == 0))
			sb.append(this.re);

		if (!(this.im == 0)) {
			if (this.im < 0)
				sb.append(("-"));
			else if (!(this.re == 0))
				sb.append("+");
			sb.append(abs(this.im) + "i");
		}

		if (this.re == 0 && this.im == 0)
			return "0";
		return sb.toString();
	}

}
