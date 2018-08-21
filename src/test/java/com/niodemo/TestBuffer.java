package com.niodemo;

import org.junit.Test;

import java.nio.IntBuffer;

public class TestBuffer {

    // 测试基本操作
    @Test
    public void test1() {

        IntBuffer buf = IntBuffer.allocate(10);
        buf.put(10);
        buf.put(20);
        buf.put(30);
        System.out.println("flip之前的对象 " + buf);
        buf.flip();
        System.out.println("flip之后的对象 " + buf);
        for (int i = 0; i < buf.limit(); i++) {
            System.out.println(buf.get());
        }
        System.out.println("遍历之后的对象 " + buf);

        System.out.println("获取下标为1的元素 " + buf.get(1));
        buf.put(1, 40);
        System.out.println("重新设置下标后的元素 " + buf.get(1));
    }
    
    // wrap方法使用
    @Test
    public void test2() {

        int[] arr = new int[]{1, 2, 5};
        IntBuffer buf1 = IntBuffer.wrap(arr);
        System.out.println(buf1);
        IntBuffer buf2 = IntBuffer.wrap(arr, 0, 2);
        System.out.println(buf2);
    }        

    // 其他方法
    @Test
    public void test3() {

        IntBuffer buf1 = IntBuffer.allocate(10);
        int[] arr = new int[] {1, 2, 5};
        buf1.put(arr);
        System.out.println(buf1);

        IntBuffer buf2 = buf1.duplicate();
        System.out.println(buf2);

        buf1.flip();
        System.out.println("可读数据为 " + buf1.remaining());

        int[] arr1 = new int[buf1.remaining()];
        buf1.get(arr1);
        for (int i : arr1) {
            System.out.println(i);
        }
    }
}
