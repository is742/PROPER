package seaa2021;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.math3.exception.DimensionMismatchException;
import org.apache.commons.math3.exception.NullArgumentException;
import org.apache.commons.math3.util.FastMath;

public class DistanceSimulator {
	
	static int size = 1000000;
	static File file = new File("./logs/distance_log.txt");
	static double timeTaken=0;
	static double cost=0;
	static int countIf=0, countWhile=0, countWhileNotSat=0;
	static List<Double> listWhile = new ArrayList<>();
	static List<Double> timeList = new ArrayList<>();
	
	@SuppressWarnings("resource")
	public static void main  (String[] args) throws InterruptedException, NullArgumentException, IOException {
		Scanner myReader = new Scanner(file);	    
		RandomAccessFile raf = new RandomAccessFile("./logs/distance_log.txt", "r");
		long startTime = System.nanoTime();
		
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

			String[] result = StringUtils.substringsBetween(str, "(", ")");
	        String[] token1 = result[0].split(",");
	        String[] token2 = result[1].split(",");
			int[] p1 = new int[token1.length];
			int[] p2 = new int[token2.length];;
	        for (int i=0; i<token1.length; i++) {
	        	p1[i] =  Integer.parseInt(token1[i]);
	        }
	        for (int i=0; i<token2.length; i++) {
	        	p2[i] =  Integer.parseInt(token2[i]);
	        }
	        distance1(p1,p2);
	    }
	      myReader.close();

		// if 
		double averageIf = (double) countIf/size;
		System.out.println("p1if: "+averageIf);
		
		// while
		double sumW = 0;
		for (Double i : listWhile)
			sumW += i;
		double avgW = sumW / listWhile.size();
		System.out.println("p2while:     " + avgW / (1+ avgW));
		
		//time
		double sumT = 0;
		for (Double i : timeList)
			sumT += i;
		double avgT = sumT / size;
		System.out.println("Time: "+avgT);
		
		//cost
		double avgC = (double) cost/size;
		System.out.println("Cost: "+avgC);
		
		long endTime   = System.nanoTime();
		long totalTime = endTime - startTime;
		double elapsedTimeInSecond = (double) totalTime / 1_000_000_000;
        System.out.println(elapsedTimeInSecond + " seconds");
	}

	public static int distance1(int[] p1, int[] p2) throws DimensionMismatchException, InterruptedException {
		countWhile=0;
		timeTaken=0;
		//cost=0;
		if (checkEqualLength(p1, p2) == false) {
			//throw new DimensionMismatchException(p1.length, p2.length);
			countIf++;
			cost=cost+7; //@cost=5
			return 0;
		}
		else {
			int sum = 0;
			int i=0;
			while (i < p1.length) {
				sum += FastMath.abs(p1[i] - p2[i]);
				//Thread.sleep(2);
				countWhile++;
				timeTaken=timeTaken+2.5; //@time=2.5
				i++;
			}
			countWhileNotSat++;
			listWhile.add((double) countWhile);
			timeList.add(timeTaken);
			//costList.add(cost);
			return sum;
		}
	}

	//below is the method check equals 

	public static boolean checkEqualLength(int[] a,
			int[] b) {
		if (a.length == b.length) {
			return true;
		} else {
			return false;
		}
	}
}
