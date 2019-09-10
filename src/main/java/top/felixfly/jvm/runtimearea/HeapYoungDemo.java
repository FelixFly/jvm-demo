package top.felixfly.jvm.runtimearea;

import java.lang.management.ManagementFactory;
import java.lang.management.MemoryPoolMXBean;

/**
 * 堆中新生代的演示Demo
 * <p>
 * -Xmn 新生代的初始值以及最大值
 * -XX:NewSize=size 新生代的初始大小，建议为整个堆内存的1/2-1/4
 * -XX:MaxNewSize=size 新生代的最大大小
 * -XX:NewRatio=*ratio* 新生代与老年代的比例，默认值为2,这个参数优先级在上面参数之下
 * <p>
 * 新生代包含Eden区和两个Survivor区，默认比例是8:1:1，可通过-XX:InitialSurvivorRatio=*ratio*进行调整
 * 在-XX:+UseParallelGC或者-XX:+UseParallelOldGC下的幸存区的初始化比例。
 * 根据-XX:+UseParallelGC或者-XX:+UseParallelOldGC自适应分配大小并且根据应用的行为重新分配大小，这个只表示开始的初始化值。
 * 若是关闭自适应分配大小（-XX:-UseAdaptiveSizePolicy）,需要用-XX:SurvivorRatio 幸存区的比例大小。默认值是8，幸存区大小的计算公式为S=Y/(R+2)
 * <p>
 * 任何对象首先会先Eden区进行分配，minor gc后会复制到Survivor区，若Survivor区无法进行存储，会晋升到老年代，老年代的担保策略
 * <p>
 * 疑问：设置Eden区最大大小的值，是什么时候进行动态扩容的？？？
 *
 * @author FelixFly 2019/9/10
 */
public class HeapYoungDemo {

    public static void main(String[] args) {
        // -Xms120M -Xmx300M -XX:+PrintGCDetails 默认会采用XX:NewRatio=*ratio* 新生代与老年代的比例，默认值为2
        /*
         * [PS Eden Space]:30M used 4M
         * [PS Survivor Space]:5M used 0M
         * [PS Old Gen]:80M used 0M
         */
        printHeap();
        // -Xmn30M -Xms120M -Xmx300M -XX:+PrintGCDetails
        /*
         * [PS Eden Space]:23M used 4M
         * [PS Survivor Space]:3M used 0M
         * [PS Old Gen]:90M used 0M
         */
        printHeap();
        // -XX:NewSize=30M -Xmx300M -XX:+PrintGCDetails
        /*
         * [PS Eden Space]:23M used 5M
         * [PS Survivor Space]:3M used 0M
         * [PS Old Gen]:90M used 0M
         */
        byte[] b1 = new byte[10 * 1024 * 1024];
        byte[] b2 = new byte[8 * 1024 * 1024];
        byte[] b3 = new byte[10 * 1024 * 1024];
        printHeap();
    }

    private static void printHeap() {
        ManagementFactory.getMemoryPoolMXBeans().stream()
                         .filter(HeapYoungDemo::isHeap)
                         .forEach(mxBean -> {
                             System.out.printf("[%s]:%dM[%dM] used %dM\n", mxBean.getName(),
                                               mxBean.getUsage().getInit() / 1024 / 1024,
                                               mxBean.getUsage().getMax() / 1024 / 1024,
                                               mxBean.getUsage().getUsed() / 1024 / 1024);
                         });
        System.out.println();
    }

    private static boolean isHeap(MemoryPoolMXBean mxBean) {
        // 根据观察,Heap的内存池的名称是PS打头
        return mxBean.getName().startsWith("PS");
    }
}
