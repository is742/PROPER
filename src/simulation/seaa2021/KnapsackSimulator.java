package seaa2021;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import org.apache.commons.lang3.StringUtils;

public class KnapsackSimulator {
	
	static int size = 100000;
	static File file = new File("./logs/knapsack_log.txt");
	
	static int energy=0;
	static double time=0;
	static List<Double> listWhile1 = new ArrayList<>();
	static List<Double> listWhile2 = new ArrayList<>();
	static List<Double> listWhile3 = new ArrayList<>();
	static List<Double> listIf = new ArrayList<>();
	static List<Double> energyList = new ArrayList<>();
	static List<Double> timeList = new ArrayList<>();
	static int counter1while=0, counter2while=0, counter3while=0, counter1if=0, counter2if=0;
	
	@SuppressWarnings("resource")
	public static void main  (String[] args) throws IOException, InterruptedException {
		Scanner myReader = new Scanner(file);
		long startTime = System.nanoTime();
		int W=0, n=0;
		
		RandomAccessFile raf = new RandomAccessFile("./logs/knapsack_log.txt", "r");
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
			
			String[] parts = str.split(",(?![^()]*\\))");
			W = Integer.parseInt(parts[0].substring(parts[0].lastIndexOf("=")+1));
			n = Integer.parseInt(parts[1].substring(parts[1].lastIndexOf("=")+1));

			String[] result = StringUtils.substringsBetween(parts[2], "(", ")");
			String[] tokenV = result[0].split(",");
			
			result = StringUtils.substringsBetween(parts[3], "(", ")");
			String[] tokenW = result[0].split(",");
			
			int[] v = new int[tokenV.length];
			int[] w = new int[tokenW.length];;
	        for (int i=0; i<tokenV.length; i++) {
	        	if (n==0) {
	        		v[i] = 0;
	        	}
	        	else {
	        		v[i] =  Integer.parseInt(tokenV[i]);
	        	}
	        }
	        for (int i=0; i<tokenW.length; i++) {
	        	if (n==0) {
	        		w[i] = 0;
	        	}
	        	else {
	        		w[i] =  Integer.parseInt(tokenW[i]);
	        	}
	        }
				        
	        knapsackDP(w,v,n,W);
	      }
	      myReader.close();
	      
	    //if 1
		double averageIf1 = (double) counter1if/size;
		System.out.println("p1if1: "+averageIf1);
		System.out.println("-------");
		
		//while 1
		double sumL1 = 0;
		for (Double i : listWhile1)
			sumL1 += i;
		double avgL1 = sumL1 / listWhile1.size();
		System.out.println("p2while1:     " + avgL1 / (1+ avgL1));
		System.out.println("-------");
		
		//while 2
		double sumL2 = 0;
		for (Double i : listWhile2)
			sumL2 += i;
		double avgL2 = sumL2 / listWhile2.size();
		System.out.println("p3while2:     " + avgL2 / (1+ avgL2));
		System.out.println("-------");
		
		//while 3
		double sumL3 = 0;
		for (Double i : listWhile3)
			sumL3 += i;
		double avgL3 = sumL3 / listWhile3.size();
		double avgL3B =  avgL3 /avgL2;
		System.out.println("p4while3:     " + avgL3B / (1+avgL3B));
		System.out.println("-------");
		
		//if 2
		double sumL4 = 0;
		for (Double i : listIf)
			sumL4 += i;
		double avgL4 = sumL4 / sumL3;
		System.out.println("p5if2:     " + avgL4);
		System.out.println("-------");

		//energy
		double sumE = 0;
		for (Double i : energyList)
			sumE += i;
		double avgE = sumE / size;
		System.out.println("Energy: "+avgE);
		
		//time
		double sumT = 0;
		for (Double i : timeList)
			sumT += i;
		double avgT = sumT / size;
		System.out.println("Time: "+avgT);

	      
	    long endTime   = System.nanoTime();
	    long totalTime = endTime - startTime;
	    double elapsedTimeInSecond = (double) totalTime / 1_000_000_000;
	    System.out.println(elapsedTimeInSecond + " seconds");
	}
	
	public static int knapsackDP(int[] w, int[] v, int n, int W) throws InterruptedException {
		counter1while=0;
		counter2while=0;
		counter3while=0;
		counter2if=0;
		energy=0;
		time=0;
		
        if (n <= 0 || W <= 0) {
        	counter1if++;
            return 0;
        }
        int[][] m = new int[n + 1][W + 1];
        int j = 0;
        while (j <= W) {
            m[0][j] = 0;
            j++;
            counter1while++;
        }
        int i = 1;
        while (i <= n) {
        	int k = 1;
        	counter2while++;
            while (k <= W) {
            	counter3while++;
                if (w[i - 1] > k) {
                    m[i][k] = m[i - 1][k];
                    counter2if++;
                } else {
                    m[i][k] = Math.max(m[i - 1][k], m[i - 1][k - w[i - 1]] + v[i - 1]); //@time=2
                    time=time+2;
                    display(); //@energy=67
                    //Thread.sleep(2);
                    energy=energy+67;
                }
                k++;
            }
            i++;
        }
        energyList.add((double) energy);
        timeList.add(time);
		listWhile1.add((double) counter1while);
		listWhile2.add((double) counter2while);
		listWhile3.add((double) counter3while);
		listIf.add((double) counter2if);
        return m[n][W];
    }
	
	public static void display() {
		//empty method created for the purpose of this case study 
	}
}
