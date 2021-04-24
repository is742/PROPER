package seaa2021;

public class DevPerf {

	static int devicePerformanceClass = -1;
	static int PERFORMANCE_CLASS_LOW=0;
	static int PERFORMANCE_CLASS_AVERAGE=1;
	static int PERFORMANCE_CLASS_HIGH=2;
	static int PERFORMANCE_CLASS_VERY_HIGH=3;
	static int performance_mode = 1, default_mode = 2, power_mode = 3, high_power_mode = 4, mode;
	
	/**
	 * Remove the commended out Animations() method before synthesising the model
	 * Change the property value @energy as necessary and/or define new properties
	 * @param androidVersion
	 * @param cpuCount
	 * @param memoryClass
	 * @param maxCpuFreq
	 * @return
	 */

	public static int getDevicePerfomanceClass(int androidVersion, int cpuCount, int memoryClass, int maxCpuFreq) {
		if (devicePerformanceClass == -1) {
			
			if (androidVersion < 21 || cpuCount <= 2 || memoryClass <= 100 || cpuCount <= 4 && maxCpuFreq != -1 && maxCpuFreq <= 1250 || cpuCount <= 4 && maxCpuFreq <= 1600 && memoryClass <= 128 && androidVersion <= 21 || cpuCount <= 4 && maxCpuFreq <= 1300 && memoryClass <= 128 && androidVersion <= 24) {
				devicePerformanceClass = PERFORMANCE_CLASS_LOW;
				mode = performance_mode;
				//Animations(mode); //@energy=28
			} else {
				if (cpuCount < 8 || memoryClass <= 160 || maxCpuFreq != -1 && maxCpuFreq <= 1650 || maxCpuFreq == -1 && cpuCount == 8 && androidVersion <= 23) {
					devicePerformanceClass = PERFORMANCE_CLASS_AVERAGE;
					mode = default_mode;
					//Animations(mode); //@energy=34
				} else {
					if (cpuCount < 8 || memoryClass <= 200 || maxCpuFreq != -1 && maxCpuFreq <= 1950 || maxCpuFreq == -1 && cpuCount == 8 && androidVersion <= 25){
						devicePerformanceClass = PERFORMANCE_CLASS_HIGH; 
						mode = power_mode;
						//Animations(mode); //@energy=40
					} else {
						devicePerformanceClass = PERFORMANCE_CLASS_VERY_HIGH;
						mode = high_power_mode;
						//Animations(mode);	//@energy=48
					}

				}
			}
		}
		return devicePerformanceClass;
	}
}
		
