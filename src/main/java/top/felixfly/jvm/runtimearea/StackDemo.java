package top.felixfly.jvm.runtimearea;

/**
 * 运行数据区-java栈演示
 * -Xss 线程栈的大小
 * -XX:ThreadStackSize=size 线程栈的大小
 *
 * @author FelixFly 2019/9/10
 */
public class StackDemo {

    /**
     * 会抛出栈的异常 {@link StackOverflowError}
     */
    public static void main(String[] args) {
        print();
    }

    private static void print(){
        print();
    }
}
