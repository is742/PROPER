package seaa2021;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.Scanner;

public class DevicePerformanceSimulator {
	
	static int size = 100000;
	static File file = new File("./logs/devperf_log.txt");
	
	static int devicePerformanceClass = -1;
	static int PERFORMANCE_CLASS_LOW=0;
	static int PERFORMANCE_CLASS_AVERAGE=1;
	static int PERFORMANCE_CLASS_HIGH=2;
	static int PERFORMANCE_CLASS_VERY_HIGH=3;
	static int energy=0;
	
	static int if1=0, if2=0, if3=0, if4=0;
	
	@SuppressWarnings("resource")
	public static void main  (String[] args) throws IOException, InterruptedException {
		Scanner myReader = new Scanner(file);
		long startTime = System.nanoTime();
		
		RandomAccessFile raf = new RandomAccessFile("./logs/devperf_log.txt", "r");
		int max=size-1;
		int min=0;
		int no=0;
		
		int memory=0, androidVersion=0, cpuCount=0, maxCpuFreq=0;
		//while (myReader.hasNextLine()) {
		for (int l=0; l<size; l++) {
			//String str = myReader.nextLine();
			
			no = (int)(Math.random() * ((max - min) + 1)) + min;
			raf.seek(no);
			String str = raf.readLine();
			str = raf.readLine();
			
			String[] parts = str.split(",");
			
			memory = Integer.parseInt(parts[0].substring(parts[0].lastIndexOf("=")+1));
			androidVersion = Integer.parseInt(parts[1].substring(parts[1].lastIndexOf("=")+1));	
			cpuCount = Integer.parseInt(parts[2].substring(parts[2].lastIndexOf("=")+1));
			maxCpuFreq = Integer.parseInt(parts[3].substring(parts[3].lastIndexOf("=")+1));

			getDevicePerfomanceClass(androidVersion,cpuCount,memory,maxCpuFreq);
	      }	
		 myReader.close();
		 
		
		//if 1
		double averageIf1 = (double) if1/size;
		System.out.println("p1: "+averageIf1);

		//if 2
		double averageIf2 = (double) if2/size;
		System.out.println("p2: "+averageIf2);
		
		//if 3
		double averageIf3 = (double) if3/(size-if2);
		System.out.println("p3: "+averageIf3);
		
		//if 4
		double averageIf4 = (double) if4/(size-if2-if3);
		System.out.println("p4: "+averageIf4);
		
		//energy
		System.out.println("energy consumption: "+(double) energy/size);
		
		long endTime   = System.nanoTime();
	    long totalTime = endTime - startTime;
	    double elapsedTimeInSecond = (double) totalTime / 1_000_000_000;
	    System.out.println(elapsedTimeInSecond + " seconds");
	}
	
	public static int getDevicePerfomanceClass(int androidVersion, int cpuCount, int memoryClass, int maxCpuFreq) throws InterruptedException {
		devicePerformanceClass = -1;
		if (devicePerformanceClass == -1) {
			int performance_mode = 1;
			int default_mode = 2;
			int power_mode = 3;
			int high_power_mode = 4;
			int mode;
			if1++;
			if (androidVersion < 21 || cpuCount <= 2 || memoryClass <= 100 || cpuCount <= 4 && maxCpuFreq != -1 && maxCpuFreq <= 1250 || cpuCount <= 4 && maxCpuFreq <= 1600 && memoryClass <= 128 && androidVersion <= 21 || cpuCount <= 4 && maxCpuFreq <= 1300 && memoryClass <= 128 && androidVersion <= 24) {
				devicePerformanceClass = PERFORMANCE_CLASS_LOW;
				mode = performance_mode;
				Animations(mode); //@energy=28
				//Thread.sleep(2);
				energy=energy+33;
				if2++;
			} else {
				if (cpuCount < 8 || memoryClass <= 160 || maxCpuFreq != -1 && maxCpuFreq <= 1650 || maxCpuFreq == -1 && cpuCount == 8 && androidVersion <= 23) {
					devicePerformanceClass = PERFORMANCE_CLASS_AVERAGE;
					mode = default_mode;
					Animations(mode); //@energy=34
					energy=energy+38;
					//Thread.sleep(2);
					if3++;
				} else {
					if (cpuCount < 8 || memoryClass <= 180 || maxCpuFreq != -1 && maxCpuFreq <= 1850 || maxCpuFreq == -1 && cpuCount == 8 && androidVersion <= 27){
						devicePerformanceClass = PERFORMANCE_CLASS_HIGH; 
						mode = power_mode;
						Animations(mode); //@energy=40
						energy=energy+46;
						//Thread.sleep(2);
						if4++;
					} else {
						devicePerformanceClass = PERFORMANCE_CLASS_VERY_HIGH;
						mode = high_power_mode;
						Animations(mode);	//@energy=48
						energy=energy+54;
						//Thread.sleep(2);
					}

				}
			}
		}
		
		return devicePerformanceClass;
	}
	
	public static void Animations(int mode) {
		//empty method created for the purpose of this case study 
	}
}
