package hr.fer.zemris.java.raytracer;

import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveAction;

import hr.fer.zemris.java.raytracer.model.IRayTracerProducer;
import hr.fer.zemris.java.raytracer.model.IRayTracerResultObserver;
import hr.fer.zemris.java.raytracer.model.LightSource;
import hr.fer.zemris.java.raytracer.model.Point3D;
import hr.fer.zemris.java.raytracer.model.Ray;
import hr.fer.zemris.java.raytracer.model.RayIntersection;
import hr.fer.zemris.java.raytracer.model.Scene;
import hr.fer.zemris.java.raytracer.viewer.RayTracerViewer;

/**
 * Class that represents a raycaster. This class creates a 3D picture showcased
 * to the user based on the technique of Ray tracing. Basically, this technique
 * is finding out the intersections between objects and light rays and
 * calculating the color based on the depth of the object. Basic coloring
 * consist of ambient, diffuse and specular component. This class works with a
 * multi-threading concept by using "fork-join" and dividing the work into equal
 * partings for the optimum execution.
 * 
 * @author Dinz
 *
 */
public class RaycasterParallel {
	/**
	 * Treshold used for operations with doubles.
	 */
	private static final double TRESHOLD = 1E-4;

	/**
	 * Main method that executes the raycaster.
	 */
	public static void main(String[] args) {
		RayTracerViewer.show(getIRayTracerProducer(), new Point3D(10, 0, 0), new Point3D(0, 0, 0),
				new Point3D(0, 0, 10), 20, 20);
	}

	/**
	 * Method that run the ray tracer producer and generates a final 3D picture to
	 * the screen using ray tracing techniques.
	 * 
	 * @return Ray tracer producer.
	 */
	private static IRayTracerProducer getIRayTracerProducer() {

		return new IRayTracerProducer() {
			@Override
			public void produce(Point3D eye, Point3D view, Point3D viewUp, double horizontal, double vertical,
					int width, int height, long requestNo, IRayTracerResultObserver observer) {

				System.out.println("Započinjem izračune...");
				short[] red = new short[width * height];
				short[] green = new short[width * height];
				short[] blue = new short[width * height];

				Point3D eyeView = view.sub(eye).normalize();

				Point3D yAxis = viewUp.sub(eyeView.scalarMultiply(eyeView.scalarProduct(viewUp))).normalize();

				Point3D xAxis = eyeView.vectorProduct(yAxis).normalize();

				Point3D screenCorner = view.sub((xAxis.scalarMultiply(horizontal / 2.0)))
						.add(yAxis.scalarMultiply(vertical / 2.0));

				Scene scene = RayTracerViewer.createPredefinedScene();

				Coloring coloring = new Coloring(0, height - 1, scene, height, width, screenCorner, xAxis, yAxis, eye,
						horizontal, vertical, red, blue, green);
				ForkJoinPool pool = new ForkJoinPool();
				pool.invoke(coloring);
				pool.shutdown();

				System.out.println("Izračuni gotovi...");
				observer.acceptResult(red, green, blue, requestNo);
				System.out.println("Dojava gotova...");
			}
		};
	}

	/**
	 * Assistent class that runs the coloring of elements in the 3D picture by using
	 * a recursive action method. The class divides the job into appropriate equal
	 * parts for optimal execution and runs the logic recursively.
	 * 
	 * @author Dinz
	 *
	 */
	public static class Coloring extends RecursiveAction {
		private static final long serialVersionUID = 1L;
		int minimum;
		int maximum;
		Scene scene;
		int height;
		int width;
		Point3D screenCorner;
		Point3D xAxis;
		Point3D yAxis;
		Point3D eye;
		double horizontal;
		double vertical;
		short[] red;
		short[] blue;
		short[] green;
		/**
		 * Threshold for the work division.
		 */
		protected final int THRESHOLD = 32; 

		public Coloring(int minimum, int maximum, Scene scene, int height, int width, Point3D screenCorner,
				Point3D xAxis, Point3D yAxis, Point3D eye, double horizontal, double vertical, short[] red,
				short[] blue, short[] green) {
			super();
			this.minimum = minimum;
			this.maximum = maximum;
			this.scene = scene;
			this.height = height;
			this.width = width;
			this.screenCorner = screenCorner;
			this.xAxis = xAxis;
			this.yAxis = yAxis;
			this.eye = eye;
			this.horizontal = horizontal;
			this.vertical = vertical;
			this.red = red;
			this.blue = blue;
			this.green = green;
		}

		@Override
		protected void compute() {
			if (maximum - minimum < THRESHOLD) {
				computeDirectly();
				return;
			}

			int split = (maximum + minimum) / 2;

			invokeAll(
					new Coloring(minimum, split, scene, height, width, screenCorner, xAxis, yAxis, eye, horizontal, vertical,
							red, blue, green),
					new Coloring(split + 1, maximum, scene, height, width, screenCorner, xAxis, yAxis, eye,
							horizontal, vertical, red, blue, green));

		}

		/**
		 * Method that calculates the color of each pixel on a screen.
		 */
		protected void computeDirectly() {
			short[] rgb = new short[3];
			int offset = minimum * width;
			for (int y = minimum; y < maximum + 1; y++) {
				for (int x = 0; x < width; x++) {
					Point3D screenPoint = screenCorner.add(xAxis.scalarMultiply((x * horizontal) / (width - 1)))
							.sub(yAxis.scalarMultiply((vertical * y) / (height - 1)));
					Ray ray = Ray.fromPoints(eye, screenPoint);

					tracer(scene, ray, rgb);
					red[offset] = rgb[0] > 255 ? 255 : rgb[0];
					green[offset] = rgb[1] > 255 ? 255 : rgb[1];
					blue[offset] = rgb[2] > 255 ? 255 : rgb[2];
					offset++;
				}
			}
		}

	}

	/**
	 * Method that calculates the color of the intersection between the scene and
	 * the light ray using ray tracing.
	 * 
	 * @param scene
	 *            Scene.
	 * @param ray
	 *            Ray.
	 * @param rgb
	 *            RGB color container.
	 */
	protected static void tracer(Scene scene, Ray ray, short[] rgb) {
		rgb[0] = 15;
		rgb[1] = 15;
		rgb[2] = 15;
		RayIntersection closest = findClosestIntersection(scene, ray);
		if (closest == null) {
			return;
		}
		determineColor(scene, ray, rgb, closest);

	}

	/**
	 * Method that finds the closest intersection between the scene and the ray.
	 * 
	 * @param scene
	 *            Scene
	 * @param ray
	 *            Ray
	 * @return Intersection.
	 */
	private static RayIntersection findClosestIntersection(Scene scene, Ray ray) {
		int closestIntersection = -1;
		for (int i = 0, j = scene.getObjects().size(); i < j; i++) {
			RayIntersection ri = scene.getObjects().get(i).findClosestRayIntersection(ray);
			if (closestIntersection == -1 && ri != null) {
				closestIntersection = i;
			} else if (ri != null && scene.getObjects().get(closestIntersection).findClosestRayIntersection(ray)
					.getDistance() > ri.getDistance()) {
				closestIntersection = i;
			}

		}
		if (closestIntersection == -1) {
			return null;

		}
		return scene.getObjects().get(closestIntersection).findClosestRayIntersection(ray);
	}

	/**
	 * Method that determines the color at the certain point on the scene.
	 * 
	 * @param scene
	 *            Scene
	 * @param ray
	 *            Ray
	 * @param rgb
	 *            EGB color container.
	 * @param intersection
	 *            Intersection between the ray and the scene.
	 */
	private static void determineColor(Scene scene, Ray ray, short[] rgb, RayIntersection intersection) {
		for (int i = 0, j = scene.getLights().size(); i < j; i++) {

			Ray lightIntersection = Ray.fromPoints(scene.getLights().get(i).getPoint(), intersection.getPoint());
			RayIntersection ciSR = findClosestIntersection(scene, lightIntersection);

			if (ciSR != null) {
				double cisrToLightDistance = ciSR.getPoint().sub(scene.getLights().get(i).getPoint()).norm();
				double intersectionToLightDistance = intersection.getPoint().sub(scene.getLights().get(i).getPoint())
						.norm();

				if (Math.abs(cisrToLightDistance - intersectionToLightDistance) < TRESHOLD) {
					addDiffusse(scene.getLights().get(i), ciSR, rgb);
					addReflective(scene.getLights().get(i), ciSR, rgb, ray);
				}

			}
		}
	}

	/**
	 * Method that adds a diffusse component to the coloring.
	 * 
	 * @param source
	 *            Light source.
	 * @param intersection
	 *            Intersection point.
	 * @param rgb
	 *            RGB color container.
	 */
	private static void addDiffusse(LightSource source, RayIntersection intersection, short[] rgb) {
		Point3D L = source.getPoint().sub(intersection.getPoint());
		Point3D N = intersection.getNormal();
		double LN = N.normalize().scalarProduct(L.normalize());

		rgb[0] += (short) (source.getR() * LN * intersection.getKdr());
		rgb[1] += (short) (source.getG() * LN * intersection.getKdg());
		rgb[2] += (short) (source.getB() * LN * intersection.getKdb());
	}

	/**
	 * Method that adds a reflective component to the coloring.
	 * 
	 * @param source
	 *            Light source.
	 * @param intersection
	 *            Intersection point.
	 * @param rgb
	 *            RGB color container.
	 * @param ray
	 *            Ray.
	 */
	private static void addReflective(LightSource source, RayIntersection intersection, short[] rgb, Ray ray) {
		Point3D L = source.getPoint().sub(intersection.getPoint());
		Point3D N = intersection.getNormal();

		Point3D LN = N.scalarMultiply(L.scalarProduct(N));
		Point3D R = LN.scalarMultiply(2).sub(L).normalize();
		Point3D V = ray.start.sub(intersection.getPoint()).normalize();

		double RV = Math.pow(R.scalarProduct(V), intersection.getKrn());

		rgb[0] += (short) (source.getR() * RV * intersection.getKrr());
		rgb[1] += (short) (source.getG() * RV * intersection.getKrg());
		rgb[2] += (short) (source.getB() * RV * intersection.getKrb());
	}

}
