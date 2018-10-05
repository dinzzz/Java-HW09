package hr.fer.zemris.math;

import static java.lang.Math.pow;
import static java.lang.Math.sqrt;

import java.util.Objects;

/**
 * Class that represents a 3D vector. Vector is consisted from its x, y and z
 * components.
 * 
 * @author Dinz
 *
 */
public class Vector3 {
	/**
	 * x component.
	 */
	private double x;
	/**
	 * y component.
	 */
	private double y;
	/**
	 * z component.
	 */
	private double z;

	public Vector3(double x, double y, double z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}

	/**
	 * Calculates the norm of the vector.
	 * 
	 * @return Norm of the vector.
	 */
	public double norm() {
		return sqrt(pow(this.x, 2) + pow(this.y, 2) + pow(this.z, 2));
	}

	/**
	 * Returns the normalized vector.
	 * 
	 * @return Normalized vector.
	 */
	public Vector3 normalized() {
		return new Vector3(this.x / this.norm(), this.y / this.norm(), this.z / this.norm());
	}

	/**
	 * Adds the other vector to the current one.
	 * 
	 * @param other
	 *            Vector to be added.
	 * @return new calculated Vector.
	 */
	public Vector3 add(Vector3 other) {
		Objects.requireNonNull(other);
		return new Vector3(other.x + this.x, other.y + this.y, other.z + this.z);
	}

	/**
	 * Subtracts the other vector from the current one.
	 * 
	 * @param other
	 *            Vector to be subtracted.
	 * @return new calculated Vector.
	 */
	public Vector3 sub(Vector3 other) {
		Objects.requireNonNull(other);
		return new Vector3(this.x - other.x, this.y - other.y, this.z - other.z);

	}

	/**
	 * Calculates the dot product between vectors.
	 * 
	 * @param other
	 *            Other vector.
	 * @return Dot product.
	 */
	public double dot(Vector3 other) {
		return this.x * other.x + this.y * other.y + this.z * other.z;
	}

	/**
	 * Calculates the cross product between vectors.
	 * 
	 * @param other
	 *            Other vector.
	 * @return Cross product vector.
	 */
	public Vector3 cross(Vector3 other) {
		double crossI = this.y * other.z - other.y * this.z;
		double crossJ = other.x * this.z - this.x * other.z;
		double crossK = this.x * other.y - other.x * this.y;

		return new Vector3(crossI, crossJ, crossK);
	}

	/**
	 * Scales the vector.
	 * 
	 * @param s
	 *            Value of vector scalation.
	 * @return Scaled vector.
	 */
	public Vector3 scale(double s) {
		return new Vector3(this.x * s, this.y * s, this.z * s);
	}

	/**
	 * Calculates the cosine of the angle between vectors.
	 * 
	 * @param other
	 *            Another vector.
	 * @return Cosine of the angle between vectors.
	 */
	public double cosAngle(Vector3 other) {
		return this.dot(other) / (this.norm() * other.norm());

	}

	/**
	 * Gets the x component.
	 * 
	 * @return X component.
	 */
	public double getX() {
		return x;
	}

	/**
	 * Gets the y component.
	 * 
	 * @return Y component.
	 */
	public double getY() {
		return y;
	}

	/**
	 * Gets the z component.
	 * 
	 * @return Z component.
	 */
	public double getZ() {
		return z;
	}

	/**
	 * Transforms the vector components to an array.
	 * 
	 * @return Transformed array of vector components.
	 */
	public double[] toArray() {
		return new double[] { this.x, this.y, this.z };
	}

	/**
	 * Transforms a vector to string format.
	 */
	public String toString() {
		return new String(String.format("(%.5f, %.5f, %.5f)", this.x, this.y, this.z));
	}
}
