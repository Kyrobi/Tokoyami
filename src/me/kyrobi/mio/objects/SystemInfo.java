package me.kyrobi.mio.objects;


//References: https://stackoverflow.com/questions/47177/how-do-i-monitor-the-computers-cpu-memory-and-disk-usage-in-java

import com.sun.management.OperatingSystemMXBean;
import org.apache.commons.math3.util.Precision;

import javax.management.*;
import java.io.File;
import java.lang.management.ManagementFactory;
import java.text.NumberFormat;

import static me.kyrobi.mio.Main.beforeUsedMem;

public class SystemInfo {

    // private Runtime runtime = Runtime.getRuntime();
    OperatingSystemMXBean osBean = (OperatingSystemMXBean) ManagementFactory.getOperatingSystemMXBean();


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
