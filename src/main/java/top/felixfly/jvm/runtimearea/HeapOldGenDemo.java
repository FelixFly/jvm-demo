package top.felixfly.jvm.runtimearea;

import java.lang.management.ManagementFactory;
import java.lang.management.MemoryPoolMXBean;

/**
 * 堆老年代演示Demo
 *
 * @author FelixFly 2019/9/10
 */
public class HeapOldGenDemo {


    /**
     * -Xms120M -Xmx300M -XX:+PrintGCDetails
     * 新生代 40M(Eden 30M Survivor 5M*2) 老年代 80M
     */
    public static void main(String[] args) {
        // 1. 新生代的大对象(超过新生代大小)直接进入老年代(老年代的担保机制)
        // bigDataMoreEden();
        // 2. 新生代的对象大小进过一轮GC后,超过Survivor区的对象晋升老年代(老年代的担保机制)
        // survivorNotMemory();
        // 3. 晋升年龄  -XX:+PrintTenuringDistribution 打印GC年龄信息  -XX:MaxTenuringThreshold=6 设置最大的晋升年龄
        // 对于Parallel GC 默认值是15，最大值是15，对于CMS GC 默认值是6，最大值也是6
        for (int i = 0; i < 100; i++) {
            byte[] b1 = new byte[3 * 1024 * 1024];
            tenuringThreshold();
        }
    }

    private static void tenuringThreshold() {
        printHeap();
        byte[] b1 = new byte[27 * 1024 * 1024];
        printHeap();
    }

    private static void bigDataMoreEden() {
        printHeap();
        /*
         * [PS Eden Space]:30720K used 4917K
         * [PS Survivor Space]:5120K used 0K
         * [PS Old Gen]:81920K used 0K
         */
        // 分配30M的空间 Eden区进行分配
        byte[] b1 = new byte[30 * 1024 * 1024];
        printHeap();
        /*
         * [PS Eden Space]:30720K used 5533K
         * [PS Survivor Space]:5120K used 0K
         * [PS Old Gen]:81920K used 30720K
         */
    }

    private static void survivorNotMemory() {
        printHeap();
        /*
         * [PS Eden Space]:30720K used 4917K
         * [PS Survivor Space]:5120K used 0K
         * [PS Old Gen]:81920K used 0K
         */
        // 分配20M的空间 Eden区进行分配
        byte[] b1 = new byte[20 * 1024 * 1024];
        printHeap();
        /*
         * [PS Eden Space]:30720K used 26013K
         * [PS Survivor Space]:5120K used 0K
         * [PS Old Gen]:81920K used 0K
         */
        // 再分配一个10M的空间,Eden区放不下了，需要GC,这时候会赋值到Survivor区，但是Survivor区的大小不足以存放，所以b1晋升老年代
        byte[] b2 = new byte[10 * 1024 * 1024];
        printHeap();
        /*
         * [PS Eden Space]:30720K used 10832K
         * [PS Survivor Space]:5120K used 1176K
         * [PS Old Gen]:81920K used 20488K
         */
    }


    private static boolean isHeap(MemoryPoolMXBean mxBean) {
        // 根据观察,Heap的内存池的名称是PS打头
        return mxBean.getName().startsWith("PS");
    }


    private static void printHeap() {
        ManagementFactory.getMemoryPoolMXBeans().stream()
                         .filter(HeapOldGenDemo::isHeap)
                         .forEach(mxBean -> {
                             System.out.printf("[%s]:%dK used %dK\n", mxBean.getName(),
                                               mxBean.getUsage().getInit() / 1024,
                                               mxBean.getUsage().getUsed() / 1024);
                         });
        System.out.println();
    }


}
