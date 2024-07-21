package com.chanson.f2f.once;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.read.listener.PageReadListener;

import java.util.List;

public class ImportExcel {

    public static void main(String[] args) {

        String fileName = "D:\\workspace\\friends2friends\\f2f_backend\\src\\main\\resources\\prodExcel.xlsx";
//        readByListener(fileName);


        readBySynchronous(fileName);
    }



    /**
     * 最简单的读
     * 监听器读取数据
     * 读取性能好 需要实现监听器
     */

    public static void readByListener(String fileName) {

        // 写法1：JDK8+ ,不用额外写一个DemoDataListener
        // since: 3.0.0-beta1

        // 这里默认每次会读取100条数据 然后返回过来 直接调用使用数据就行
        // 具体需要返回多少行可以在`PageReadListener`的构造函数设置
        EasyExcel.read(fileName, UserInfoExcel.class, new PageReadListener<UserInfoExcel>(dataList -> {
            for (UserInfoExcel date : dataList) {
                System.out.println(date);
            }
        })).sheet().doRead();
        System.out.println("解析完成");
    }


    /**
     * 同步读取
     * 优点；简单 缺点：数据量大 获取数据容易卡
     * @param fileName
     */
    private static void readBySynchronous(String fileName) {

        // 这里 需要指定读用哪个class去读，然后读取第一个sheet 同步读取会自动finish
        List<UserInfoExcel> list = EasyExcel.read(fileName).head(UserInfoExcel.class).sheet().doReadSync();
        for (UserInfoExcel userInfo : list) {
            System.out.println(userInfo);
        }
        System.out.println("解析完成");
    }
}




