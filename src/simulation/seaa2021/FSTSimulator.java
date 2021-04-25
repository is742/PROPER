package seaa2021;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import org.apache.commons.math3.complex.Complex;
import org.apache.commons.math3.exception.MathIllegalArgumentException;
import org.apache.commons.math3.transform.DftNormalization;
import org.apache.commons.math3.transform.FastFourierTransformer;
import org.apache.commons.math3.transform.TransformType;
import org.apache.commons.math3.util.ArithmeticUtils;
import org.apache.commons.math3.util.FastMath;

public class FSTSimulator {
	static File file = new File("./logs/fst_log.txt");
	static int size=100000;
	static double timeTaken=0;
	static double cost=0;
	static List<Double> listL1 = new ArrayList<>();
	static List<Double> listL2 = new ArrayList<>();
	static List<Double> timeList = new ArrayList<>();
	static int counter1while=0, counter2while=0, counter1if=0, counter2if=0, counter3if=0;
	
	@SuppressWarnings("resource")
	public static void main  (String[] args) throws InterruptedException, IOException {
		long startTime = System.nanoTime();
		Scanner myReader = new Scanner(file);
		
		RandomAccessFile raf = new RandomAccessFile("./logs/fst_log.txt", "r");
		
		int max=size-1;
		int min=0;
		int no=0;
		//while (myReader.hasNextLine()) {
		for (int l=0; l<size; l++) {
			//String str = myReader.nextLine();
			
			no = (int)(Math.random() * ((max - min) + 1)) + min;
			raf.seek(no);
			String str = raf.readLine();
			str = raf.readLine();
			
			String[] token = str.split(",");
			double[] data = new double[token.length];
	        for (int i=0; i<token.length; i++) {
	        	data[i] =  Double.parseDouble(token[i]);
	        }
	        fst(data);
	      }
	      myReader.close();

		//if 1
		double averageIf1 = (double) counter1if/size;
		System.out.println("p1if1: "+averageIf1);
		System.out.println("-------");
		//if 2
		double averageIf2 = (double) counter2if/(size-counter1if); 
		System.out.println("p2if2: "+averageIf2);
		System.out.println("-------");

		//if 3
		double averageIf3 = (double) counter3if/(size-counter1if-counter2if);
		System.out.println("p3if3: "+averageIf3);
		System.out.println("-------");

		//while loop 1
		double sumL1 = 0;
		for (Double i : listL1)
			sumL1 += i;
		double avgL1 = sumL1 / listL1.size();
		System.out.println("p4while1:     " + avgL1 / (1+ avgL1));
		System.out.println("-------");
		
		//while loop 2
		double sumL2 = 0;
		for (Double i : listL2)
			sumL2 += i;
		double avgL2 = sumL2 / listL2.size();
		System.out.println("p5while2:     " + avgL2 / (1+ avgL2));
		System.out.println("-------");
		
		//timeTaken
		double sumT = 0;
		for (Double i : timeList)
			sumT += i;
		double avgT = sumT / size;
		System.out.println("Time: "+avgT);
		System.out.println("-------");
		
		//cost
		System.out.println("Cost: "+(double)cost/size);
		
		long endTime   = System.nanoTime();
		long totalTime = endTime - startTime;
		double elapsedTimeInSecond = (double) totalTime / 1_000_000_000;
        System.out.println(elapsedTimeInSecond + " seconds");
	}

	public static double[] fst(double[] f) throws MathIllegalArgumentException, InterruptedException {
		counter1while=0;
		counter2while=0;
		timeTaken=0;
		double[] transformed = new double[f.length];
		
		if (!ArithmeticUtils.isPowerOfTwo(f.length)) { 
			counter1if++;
			cost=cost+5;
			return null;
			//throw new MathIllegalArgumentException(
			// LocalizedFormats.NOT_POWER_OF_TWO_CONSIDER_PADDING,
			// Integer.valueOf(f.length)); cost=cost+5;
		}
		if (f[0] != 0.0) {
			counter2if++;
			cost=cost+5;
			return null;
			//throw new MathIllegalArgumentException(
			//   LocalizedFormats.FIRST_ELEMENT_NOT_ZERO,
			// Double.valueOf(f[0])); cost=cost+5;
		}
		int n = f.length;
		if (n == 1) {      
			transformed[0] = 0.0;
			counter3if++;
			return transformed;
		}

		double[] x = new double[n];
		x[0] = 0.0;
		x[n >> 1] = 2.0 * f[n >> 1];
		int i=1;
		while (i < (n >> 1)) {
			double a = FastMath.sin(i * FastMath.PI / n) * (f[i] + f[n - i]);
			timeTaken=timeTaken+1.5;  // @time=1.5
			//Thread.sleep(2);
			double b = 0.5 * (f[i] - f[n - i]);
			x[i]     = a + b;
			x[n - i] = a - b;
			i++;
			counter1while++;
		}
		FastFourierTransformer transformer;
		transformer = new FastFourierTransformer(DftNormalization.STANDARD);
		Complex[] y = transformer.transform(x, TransformType.FORWARD);

		transformed[0] = 0.0;
		transformed[1] = 0.5 * y[0].getReal();
		int j = 1;
		while (j < (n >> 1)) {
			transformed[2 * j]     = -y[j].getImaginary();
			transformed[2 * j + 1] = y[j].getReal() + transformed[2 * j - 1];
			j++;
			counter2while++;
		}
		listL1.add((double) counter1while);
		listL2.add((double) counter2while);
		timeList.add((double) timeTaken);
		return transformed;
	}
}
