package hr.fer.zemris.math;

/**
 * Class that demonstrates the use of 3D vectors.
 * 
 * @author Dinz
 *
 */
public class Vector3Demo {
	/**
	 * Main method that executes the class.
	 * 
	 * @param args
	 *            Arguments from the command line.
	 */
	public static void main(String[] args) {
		Vector3 i = new Vector3(1, 0, 0);
		Vector3 j = new Vector3(0, 1, 0);
		Vector3 k = i.cross(j);
		Vector3 l = k.add(j).scale(5);
		Vector3 m = l.normalized();
		System.out.println(i);
		System.out.println(j);
		System.out.println(k);
		System.out.println(l);
		System.out.println(l.norm());
		System.out.println(m);
		System.out.println(l.dot(j));
		System.out.println(i.add(new Vector3(0, 1, 0)).cosAngle(l));

		//// Testovi
		// Complex first = new Complex(1, 0);
		// Complex second = new Complex(-1, 0);
		// Complex third = new Complex(0, 1);
		// Complex fourth = new Complex(0, -1);
		//
		// ComplexRootedPolynomial crp = new ComplexRootedPolynomial(first, second,
		//// third, fourth);
		// System.out.println(crp);
		//
		// ComplexPolynomial cp = crp.toComplexPolynom();
		// System.out.println(cp);
		//
		// System.out.println(cp.derive());
		// System.out.println(crp.apply(new Complex(31, 2)));
		////
		//// ComplexPolynomial cpp = new ComplexPolynomial(first, first.negate());
		//// ComplexPolynomial cppp = new ComplexPolynomial(first, second.negate());
		////
		//// System.out.println(cpp.multiply(cppp));

	}

}
