package hr.fer.zemris.java.raytracer.model;

/**
 * Class that represents a geometrycal sphere in usage as an object for a
 * graphical representation. Each sphere is determined by its center point, its
 * radius and its color components.
 * 
 * @author Dinz
 *
 */
public class Sphere extends GraphicalObject {
	/**
	 * Threshold used in operations with doubles.
	 */
	private static final double THRESHOLD = 1E-4;
	/**
	 * Center of the sphere.
	 */
	private Point3D center;
	/**
	 * Radius of the sphere.
	 */
	private double radius;
	/**
	 * Diffuse red component.
	 */
	private double kdr;
	/**
	 * Diffuse green component.
	 */
	private double kdg;
	/**
	 * Diffuse blue component.
	 */
	private double kdb;
	/**
	 * Reflective red component.
	 */
	private double krr;
	/**
	 * Reflective green component.
	 */
	private double krg;
	/**
	 * Reflective blue component.
	 */
	private double krb;
	/**
	 * Reflective multiplier.
	 */
	private double krn;

	public Sphere(Point3D center, double radius, double kdr, double kdg, double kdb, double krr, double krg, double krb,
			double krn) {
		this.center = center;
		this.radius = radius;
		this.kdr = kdr;
		this.kdg = kdg;
		this.kdb = kdb;
		this.krr = krr;
		this.krg = krg;
		this.krb = krb;
		this.krn = krn;

	}

	@Override
	public RayIntersection findClosestRayIntersection(Ray ray) {
		Point3D rayOrigin = ray.start;
		Point3D rayDirection = ray.direction;

		// ray is expressed as rayOrigin + t * rayDirection
		double discriminantA = rayDirection.scalarProduct(rayDirection); // d^2
		double discriminantB = rayDirection.scalarMultiply(2).scalarProduct(rayOrigin.sub(center));
		double discriminantC = rayOrigin.sub(center).scalarProduct(rayOrigin.sub(center)) - Math.pow(this.radius, 2);

		double discriminant = Math.pow(discriminantB, 2) - discriminantA * discriminantC * 4;

		// no intersection
		if (discriminant < 0)
			return null;

		double intDistance = 0;
		Point3D intersection = null;
		boolean outer = true;

		double distanceOne = (-discriminantB + Math.sqrt(discriminant)) / (2 * discriminantA);
		double distanceTwo = (-discriminantB - Math.sqrt(discriminant)) / (2 * discriminantA);

		// one intersection
		if (Math.abs(discriminant) < THRESHOLD) {
			intDistance = -discriminantB / (2 * discriminantA);
			intersection = rayOrigin.add(rayDirection.scalarMultiply(intDistance));
			intDistance = intersection.sub(rayOrigin).norm();
		} else if (Math.abs(distanceOne - distanceTwo) > THRESHOLD) {
			if (distanceOne <= distanceTwo) {
				Point3D intersectionOne = rayOrigin.add(rayDirection.scalarMultiply(distanceOne));
				intersection = intersectionOne;
				intDistance = intersection.sub(rayOrigin).norm();
			} else {
				Point3D intersectionTwo = rayOrigin.add(rayDirection.scalarMultiply(distanceTwo));
				intersection = intersectionTwo;
				intDistance = intersection.sub(rayOrigin).norm();

			}
			if (intersection.sub(center).norm() < radius) {
				outer = false;
			}
		}
		return new RayIntersection(intersection, intDistance, outer) {

			@Override
			public Point3D getNormal() {

				return this.getPoint().sub(getCenter()).normalize();
			}

			@Override
			public double getKdr() {
				return kdr;
			}

			@Override
			public double getKdg() {
				return kdg;
			}

			@Override
			public double getKdb() {
				return kdb;
			}

			@Override
			public double getKrr() {
				return krr;
			}

			@Override
			public double getKrg() {
				return krg;
			}

			@Override
			public double getKrb() {
				return krb;
			}

			@Override
			public double getKrn() {
				return krn;
			}

		};
	}

	/**
	 * Gets the center of the sphere.
	 * 
	 * @return Center of the sphere.
	 */
	public Point3D getCenter() {
		return center;
	}

	/**
	 * Gets the sphere radius.
	 * 
	 * @return Sphere radius.
	 */
	public double getRadius() {
		return radius;
	}

	/**
	 * Gets the red diffusse component.
	 * 
	 * @return Red diffusse component.
	 */
	public double getKdr() {
		return kdr;
	}

	/**
	 * Gets the green diffusse component.
	 * 
	 * @return Green diffusse component.
	 */
	public double getKdg() {
		return kdg;
	}

	/**
	 * Gets the blue diffusse component.
	 * 
	 * @return Blue diffusse component.
	 */
	public double getKdb() {
		return kdb;
	}

	/**
	 * Gets the red reflective component.
	 * 
	 * @return Red reflective component.
	 */
	public double getKrr() {
		return krr;
	}

	/**
	 * Gets the green reflective component.
	 * 
	 * @return Green reflective component.
	 */
	public double getKrg() {
		return krg;
	}

	/**
	 * Gets the blue reflective component.
	 * 
	 * @return Blue reflective component.
	 */
	public double getKrb() {
		return krb;
	}

	/**
	 * Gets the reflective multiplier.
	 * 
	 * @return Reflective multiplier.
	 */
	public double getKrn() {
		return krn;
	}

}
