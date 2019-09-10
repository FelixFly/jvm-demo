package top.felixfly.jvm.runtimearea;

import java.lang.management.ManagementFactory;

/**
 * 运行数据区-堆演示
 * 堆的控制参数
 * -Xms 堆的初始化大小,必须是1024的倍数并且大于1MB
 * -Xmx 堆的最大值,必须是1024的倍数并且大于2MB
 * <p>
 * -XX:InitialHeapSize=size 堆的初始化大小,必须是1024的倍数并且大于1MB
 * -XX:MaxHeapSize=size 堆的最大值,必须是1024的倍数并且大于2MB
 *
 * 因为有老年代担保机制存在，只会在老年代发生OOM:{@link OutOfMemoryError}: Java heap space
 *
 * @author FelixFly 2019/9/10
 */
public class HeapDemo {

    /**
     * -Xms120M -Xmx300M -XX:+PrintGCDetails
     * <p>
     * [Code Cache]:2496K used 1439K
     * [Metaspace]:0K used 4768K
     * [Compressed Class Space]:0K used 537K
     * [PS Eden Space]:30720K used 5533K
     * [PS Survivor Space]:5120K used 0K
     * [PS Old Gen]:81920K used 0K
     *
     * <p>
     * Heap
     * PSYoungGen      total 2560K, used 1474K [0x00000000f9c00000, 0x00000000f9f00000, 0x0000000100000000)
     * eden space 2048K, 72% used [0x00000000f9c00000,0x00000000f9d70b40,0x00000000f9e00000)
     * from space 512K, 0% used [0x00000000f9e80000,0x00000000f9e80000,0x00000000f9f00000)
     * to   space 512K, 0% used [0x00000000f9e00000,0x00000000f9e00000,0x00000000f9e80000)
     * ParOldGen       total 7168K, used 0K [0x00000000ed400000, 0x00000000edb00000, 0x00000000f9c00000)
     * object space 7168K, 0% used [0x00000000ed400000,0x00000000ed400000,0x00000000edb00000)
     * Metaspace       used 3001K, capacity 4496K, committed 4864K, reserved 1056768K
     * class space    used 323K, capacity 388K, committed 512K, reserved 1048576K
     */
    public static void main(String[] args) {
        byte[] bytes = new byte[201 * 1024 * 1024];
        ManagementFactory.getMemoryPoolMXBeans().forEach(mxBean -> {
            System.out.printf("[%s]:%dK used %dK\n", mxBean.getName(),
                              mxBean.getUsage().getInit() / 1024,
                              mxBean.getUsage().getUsed() / 1024);
        });


    }
}
