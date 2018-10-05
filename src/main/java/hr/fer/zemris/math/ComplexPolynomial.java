package hr.fer.zemris.math;

/**
 * Class that represents a complex polynom in a standard format.
 * 
 * @author Dinz
 *
 */
public class ComplexPolynomial {
	/**
	 * Factors of the polynom.
	 */
	private Complex[] factors;

	public ComplexPolynomial(Complex... factors) {
		this.factors = factors;
	}

	/**
	 * Gets the order of the polynom.
	 * 
	 * @return Order of the polynom.
	 */
	public short order() {
		return (short) (factors.length - 1);
	}

	/**
	 * Multiplys the another polynom with the current one.
	 * 
	 * @param p
	 *            Another polynom.
	 * @return New calculated polynom.
	 */
	public ComplexPolynomial multiply(ComplexPolynomial p) {
		int order = this.order() + p.order();
		Complex[] newFactors = new Complex[order + 1];
		for (int i = 0; i < this.order() + 1; i++) {
			for (int j = 0; j < p.order() + 1; j++) {
				if (newFactors[i + j] == null) {
					newFactors[i + j] = this.factors[i].multiply(p.factors[j]);
				} else {
					newFactors[i + j] = newFactors[i + j].add(this.factors[i].multiply(p.factors[j]));
				}
			}
		}
		return new ComplexPolynomial(newFactors);
	}

	/**
	 * Calculates a first derivation of the polynom.
	 * 
	 * @return First derivation of the polynom.
	 */
	public ComplexPolynomial derive() {
		Complex[] newFactors = new Complex[factors.length - 1];
		for (int i = 0; i < newFactors.length; i++) {
			newFactors[i] = factors[i + 1].multiply(new Complex(i + 1, 0));
		}

		return new ComplexPolynomial(newFactors);
	}

	/**
	 * Calculates the value of the polynom for the given complex number.
	 * 
	 * @param z
	 *            Complex number.
	 * @return Value of the polynom for the given complex number.
	 */
	public Complex apply(Complex z) {
		Complex sum = new Complex(0, 0);
		for (int i = 0; i < factors.length; i++) {

			sum = sum.add(z.power(i).multiply(factors[i]));
		}

		return sum;
	}

	/**
	 * Method that forms the polynom to a string format.
	 */
	public String toString() {
		StringBuilder sb = new StringBuilder();
		for (int i = factors.length - 1; i >= 0; i--) {
			if (i == 0) {
				sb.append(factors[0]);
			} else {
				sb.append("(" + factors[i] + ")z^" + i + "+");
			}
		}

		return sb.toString();
	}
}
