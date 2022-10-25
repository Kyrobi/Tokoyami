package me.kyrobi.Tokoyami.objects;


/*References:
https://stackoverflow.com/questions/47177/how-do-i-monitor-the-computers-cpu-memory-and-disk-usage-in-java
https://stackoverflow.com/questions/26676947/get-real-ram-usage-of-current-jvm-with-java
*/


import com.sun.management.OperatingSystemMXBean;
import org.apache.commons.math3.util.Precision;

import java.lang.management.ManagementFactory;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;

public class SystemInfo {

    // private Runtime runtime = Runtime.getRuntime();
    OperatingSystemMXBean osBean = (OperatingSystemMXBean) ManagementFactory.getOperatingSystemMXBean();

    private static final long MEGABYTE_FACTOR = 1024L * 1024L;
    private static final DecimalFormat ROUNDED_DOUBLE_DECIMALFORMAT;
    private static final String MIB = "MB";

    static {
        DecimalFormatSymbols otherSymbols = new DecimalFormatSymbols(Locale.ENGLISH);
        otherSymbols.setDecimalSeparator('.');
        otherSymbols.setGroupingSeparator(',');
        ROUNDED_DOUBLE_DECIMALFORMAT = new DecimalFormat("####0.0", otherSymbols);
        ROUNDED_DOUBLE_DECIMALFORMAT.setGroupingUsed(false);
    }

    public static long getMaxMemory() {
        return Runtime.getRuntime().maxMemory();
    }

    public static long getUsedMemory() {
        return getMaxMemory() - getFreeMemory();
    }

    public static long getTotalMemory() {
        return Runtime.getRuntime().totalMemory();
    }


    public static long getFreeMemory() {
        return Runtime.getRuntime().freeMemory();
    }


    public static String getTotalMemoryInMiB() {
        double totalMiB = bytesToMiB(getTotalMemory());
        return String.format("%s %s", ROUNDED_DOUBLE_DECIMALFORMAT.format(totalMiB), MIB);
    }

    public static String getFreeMemoryInMiB() {
        double freeMiB = bytesToMiB(getFreeMemory());
        return String.format("%s %s", ROUNDED_DOUBLE_DECIMALFORMAT.format(freeMiB), MIB);
    }

    public static String getUsedMemoryInMiB() {
        double usedMiB = bytesToMiB(getUsedMemory());
        return String.format("%s %s", ROUNDED_DOUBLE_DECIMALFORMAT.format(usedMiB), MIB);
    }


    public static String getMaxMemoryInMiB() {
        double maxMiB = bytesToMiB(getMaxMemory());
        return String.format("%s %s", ROUNDED_DOUBLE_DECIMALFORMAT.format(maxMiB), MIB);
    }

    public static double getPercentageUsed() {
        return ((double) getUsedMemory() / getMaxMemory()) * 100;
    }

    public static String getPercentageUsedFormatted() {
        double usedPercentage = getPercentageUsed();
        return ROUNDED_DOUBLE_DECIMALFORMAT.format(usedPercentage) + "%";
    }

    private static double bytesToMiB(long bytes) {
        return ((double) bytes / MEGABYTE_FACTOR);
    }

    public static String getSystemInformation() {
//        return String.format("Heap: %s\n Used: %s\n Free: %s\n Max Heap: %s\n Used: %s",
//                getTotalMemoryInMiB(),
//                getUsedMemoryInMiB(),
//                getFreeMemoryInMiB(),
//                getMaxMemoryInMiB(),
//                getPercentageUsedFormatted());

        return String.format("Heap: %s\n Used: %s\n Max Heap: %s\n Used: %s",
                getTotalMemoryInMiB(),
                getUsedMemoryInMiB(),
                getMaxMemoryInMiB(),
                getPercentageUsedFormatted());
    }



    public double getProcessCPULoad(){
        //return osBean.getProcessCpuLoad() * 100; // Returns the "recent cpu usage" for the Java Virtual Machine process.

        double d = osBean.getProcessCpuLoad() * 100; // Converts to percentage
        return Precision.round(d,2); // Rounds and limits to 2 decimal places
    }

    public double getSystemCPULoad(){
        double d = osBean.getSystemCpuLoad() * 100; // Converts to percentage
        return Precision.round(d,2); // Rounds and limits to 2 decimal places
    }

    public long getCommittedVirtualMemorySize(){
        return osBean.getCommittedVirtualMemorySize() / 1000000;
    }

    public long getFreePhysicalMemorySize(){
        return osBean.getFreePhysicalMemorySize() / 1000000;
    }

    // The amount of RAM in Bytes of the entire machine
    public long getTotalPhysicalMemorySize(){
        return osBean.getTotalPhysicalMemorySize() / 1000000;
    }

    public long getMemoryUsed(){
//        long afterUsedMem=Runtime.getRuntime().totalMemory()-Runtime.getRuntime().freeMemory();
//        long actualMemUsed=afterUsedMem-beforeUsedMem;
//
//        long totalMem = (osBean.getTotalPhysicalMemorySize() / 1024) / 1024;
//        long totalFree = (osBean.getFreePhysicalMemorySize() / 1024) / 1024;
//        long comitted = (osBean.getCommittedVirtualMemorySize() / 1024) / 1024;
//
//        System.out.println("totalmem = " + osBean.getTotalPhysicalMemorySize() + " -> " + totalMem);
//        System.out.println("totalFree = " + osBean.getFreePhysicalMemorySize() + " -> " + totalFree);
//        System.out.println("CommittedVirtualMemorySize = " + osBean.getCommittedVirtualMemorySize() + " -> " + comitted);
//
//        // Total memory available to the JVM
//        Runtime.getRuntime().totalMemory();
//        System.out.println("getRuntime().totalMemory()" + Runtime.getRuntime().totalMemory() + " -> " + (Runtime.getRuntime().totalMemory()/1024)/1024);
//
//        System.out.println("actualMemUsed" + actualMemUsed + " -> " + (actualMemUsed/1024)/1024);
//
//        return totalMem - totalFree;
        return 0;
    }

    public String osName() {
        return System.getProperty("os.name");
    }


    public String osVersion() {
        return System.getProperty("os.version");
    }

    public String osArch() {
        return System.getProperty("os.arch");
    }

    public long totalMem() {
        return Runtime.getRuntime().totalMemory();
    }

    public long usedMem() {
        return Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();
    }

}
