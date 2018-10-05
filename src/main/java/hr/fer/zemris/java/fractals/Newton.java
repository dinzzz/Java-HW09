package hr.fer.zemris.java.fractals;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadFactory;

import hr.fer.zemris.java.fractals.viewer.FractalViewer;
import hr.fer.zemris.java.fractals.viewer.IFractalProducer;
import hr.fer.zemris.java.fractals.viewer.IFractalResultObserver;
import hr.fer.zemris.math.Complex;
import hr.fer.zemris.math.ComplexPolynomial;
import hr.fer.zemris.math.ComplexRootedPolynomial;

/**
 * Class that represents a program running a Newton-Raphson iteration-based
 * fractal viewer. The class expects a series of roots to form a complex polynom
 * and based on that polynom generates a fractal which is presented to the user.
 * 
 * @author Dinz
 *
 */
public class Newton {
	/**
	 * Method that runs the program.
	 * 
	 * @param args
	 *            Arguments from the command line.
	 */
	public static void main(String[] args) {
		System.out.println("Welcome to Newton-Raphson iteration-based fractal viewer.");
		System.out.println("Please enter at least two roots one root per line. Enter 'done' when done.");

		Scanner sc = new Scanner(System.in);
		List<Complex> complexList = new ArrayList<>();
		int counter = 1;
		while (true) {
			System.out.print("\n>Root number " + counter + ":");
			String input = sc.nextLine();
			if (input.equals("done")) {
				if (counter < 3) {
					sc.close();
					System.out.println("Sufficient number of roots. Exiting.");
					return;
				}
				break;
			}

			try {
				Complex number = parse(input);
				complexList.add(number);
				counter++;
			} catch (IllegalArgumentException | ArrayIndexOutOfBoundsException ex) {
				System.out.println("Wrong input");
			}
		}
		sc.close();
		FractalViewer.show((new FractalProducer(complexList.toArray(new Complex[complexList.size()]))));

	}

	/**
	 * Method that parses the user input into a valid complex number format.
	 * 
	 * @param input
	 *            User's input.
	 * @return Valid complex number.
	 */
	private static Complex parse(String input) {
		if (input.trim().isEmpty()) {
			throw new IllegalArgumentException("Empty input!!!!");
		}
		if (input.contains("+")) {
			String[] split = input.trim().split("\\+");
			if (split.length != 2) {
				throw new IllegalArgumentException("Wrong input.");
			}
			double real = Double.parseDouble(split[0].trim());
			double imag;
			if (split[1].replace("i", "").trim().isEmpty()) {
				imag = 1;
			} else {
				imag = Double.parseDouble(split[1].replace("i", "").trim());
			}
			return new Complex(real, imag);
		} else if (input.contains("-")) {
			String[] split = input.split("\\-");
			if (!input.contains("i")) {
				double real = Double.parseDouble(input.trim());
				return new Complex(real, 0);
			} else if (input.indexOf("-") != input.lastIndexOf("-")) {
				double real = -1 * Double.parseDouble(split[1].trim());
				double imag;
				if (split[2].replaceAll("i", "").trim().isEmpty()) {
					imag = -1;
				} else {
					imag = -1 * Double.parseDouble(split[2].replaceAll("i", "").trim());
				}
				return new Complex(real, imag);
			} else if (split[0].isEmpty() && !split[1].isEmpty()) {
				double imag;
				if (split[1].replaceAll("i", "").trim().isEmpty()) {
					imag = -1;
				} else {
					imag = -1 * Double.parseDouble(split[1].replaceAll("i", "").trim());
				}
				return new Complex(0, imag);
			} else {
				double real = Double.parseDouble(split[0].trim());
				double imag;
				if (split[1].replaceAll("i", "").trim().isEmpty()) {
					imag = -1;
				} else {
					imag = -1 * Double.parseDouble(split[1].replaceAll("i", "").trim());
				}
				return new Complex(real, imag);
			}
		} else if (input.contains("i")) {
			double imag;
			if (input.replace("i", "").trim().isEmpty()) {
				imag = 1;
			} else {
				imag = Double.parseDouble(input.replace("i", "").trim());
			}
			return new Complex(0, imag);
		} else {
			double real = Double.parseDouble(input.trim());
			return new Complex(real, 0);
		}
	}

	/**
	 * Class that runs the main calculation of the program and generates appropriate
	 * colors of the picture.
	 * 
	 * @author Dinz
	 *
	 */
	public static class Calculation implements Callable<Void> {
		/**
		 * Convergence threshold.
		 */
		private static final double CONV_THRESHOLD = 1E-3;
		/**
		 * Minimum real component.
		 */
		double reMin;
		/**
		 * Maximum real component.
		 */
		double reMax;
		/**
		 * Minimum imaginary component.
		 */
		double imMin;
		/**
		 * Maximum imaginary component.
		 */
		double imMax;
		/**
		 * Width.
		 */
		int width;
		/**
		 * Height.
		 */
		int height;
		/**
		 * Minimum y.
		 */
		int yMin;
		/**
		 * Maximum y.
		 */
		int yMax;
		/**
		 * Maximum iterations.
		 */
		int m;
		/**
		 * Data that stores colors.
		 */
		short[] data;
		/**
		 * Complex roots.
		 */
		Complex[] roots;

		public Calculation(double reMin, double reMax, double imMin, double imMax, int width, int height, int yMin,
				int yMax, int m, short[] data, Complex[] roots) {
			this.reMin = reMin;
			this.reMax = reMax;
			this.imMin = imMin;
			this.imMax = imMax;
			this.width = width;
			this.height = height;
			this.yMin = yMin;
			this.yMax = yMax;
			this.m = m;
			this.data = data;
			this.roots = roots;

		}

		@Override
		public Void call() throws Exception {
			int offset = yMin * width;
			ComplexRootedPolynomial crp = new ComplexRootedPolynomial(roots);
			ComplexPolynomial cp = crp.toComplexPolynom();
			for (int y = yMin; y <= yMax; y++) {
				for (int x = 0; x < width; x++) {
					Complex c = mapToComplexPlain(x, y, width, height, reMin, reMax, imMin, imMax);
					Complex zn = c;
					int iter = 0;
					double module = 0;
					do {
						Complex numerator = crp.apply(zn);
						Complex denominator = cp.derive().apply(zn);
						Complex fraction = numerator.divide(denominator);
						Complex zn1 = zn.sub(fraction);
						module = zn1.sub(zn).module();
						iter++;
						zn = zn1;
					} while (iter < m && module > CONV_THRESHOLD);

					int index = crp.indexOfClosestRootFor(zn, CONV_THRESHOLD);
					if (index == -1) {
						data[offset++] = 0;
					} else {
						data[offset++] = (short) (index + 1);
					}
				}

			}
			return null;
		}

		/**
		 * Method that converts the current position on the map into a valid complex
		 * number in complex plain.
		 * 
		 * @param x
		 *            X parameter.
		 * @param y
		 *            Y parameter.
		 * @param width
		 *            Width.
		 * @param height
		 *            Height.
		 * @param reMin
		 *            Minimum real component.
		 * @param reMax
		 *            Maximum real component.
		 * @param imMin
		 *            Minimum imaginary component.
		 * @param imMax
		 *            Maximum imaginary component.
		 * @return Valid complex number.
		 */
		private Complex mapToComplexPlain(double x, double y, double width, double height, double reMin, double reMax,
				double imMin, double imMax) {
			double real = reMin + ((reMax - reMin) * x) / (width - 1);
			double imag = imMin + ((imMax - imMin) * (height - 1 - y)) / (height - 1);

			return new Complex(real, imag);
		}

	}

	/**
	 * Class that produces a fractal from the given roots. It uses a multi-thread
	 * technique and showcases a given fractal to the user.
	 * 
	 * @author Dinz
	 *
	 */
	public static class FractalProducer implements IFractalProducer {
		/**
		 * Maximum iterations.
		 */
		private static final int MAX_ITERATIONS = 16 * 16 * 16;
		/**
		 * Processor multiplier.
		 */
		private static final int PROCESSOR_MULTIPLIER = 8;
		/**
		 * Complex roots.
		 */
		private Complex[] roots;

		public FractalProducer(Complex[] roots) {
			this.roots = roots;
		}

		/**
		 * Main method of the class that does all the work.
		 */
		@Override
		public void produce(double reMin, double reMax, double imMin, double imMax, int width, int height,
				long requestNo, IFractalResultObserver observer) {

			System.out.println("Starting calculation.");

			int maxIterations = MAX_ITERATIONS;

			short[] data = new short[width * height];

			int trackNumber = PROCESSOR_MULTIPLIER * Runtime.getRuntime().availableProcessors();
			int yPerTrack = height / trackNumber;

			ExecutorService pool = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors(),
					new DaemonicThreadFactory());
			List<Future<Void>> results = new ArrayList<>();

			for (int i = 0; i < trackNumber; i++) {
				int yMin = i * yPerTrack;
				int yMax = (i + 1) * yPerTrack - 1;
				if (i == trackNumber - 1) {
					yMax = height - 1;
				}

				Calculation calculation = new Calculation(reMin, reMax, imMin, imMax, width, height, yMin, yMax,
						maxIterations, data, roots);
				results.add(pool.submit(calculation));

			}
			for (Future<Void> calculation : results) {
				try {
					calculation.get();
				} catch (InterruptedException | ExecutionException e) {
					System.out.println("Error while executing.");
				}
			}

			pool.shutdown();
			System.out.println("Calculation ended.");
			ComplexRootedPolynomial crp = new ComplexRootedPolynomial(roots);
			observer.acceptResult(data, (short) (crp.toComplexPolynom().order() + 1), requestNo);
		}

	}

	/**
	 * Class that creates a new thread used in generating an output picture and
	 * ensures that this thread is daemonic.
	 * 
	 * @author Dinz
	 *
	 */
	private static class DaemonicThreadFactory implements ThreadFactory {

		@Override
		public Thread newThread(Runnable r) {
			Thread thread = new Thread(r);
			thread.setDaemon(true);
			return thread;
		}

	}
}
