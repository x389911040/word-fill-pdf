package com.rd.modules.common.utils;

import com.sun.scenario.effect.impl.sw.sse.SSEBlend_SRC_OUTPeer;
import org.springframework.context.ApplicationContext;
import org.springframework.util.CollectionUtils;

import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

/**
 * @author xuejh
 * @description 移交单记录单号生成
 * @create 2020-06-18 10:37
 **/
public class MoveRecordBillCode {

    public static String getMoveRecordBillCode(Date date) {
        SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmssSSS");
        String billCode = format.format(date);
        return "YJ" + billCode;
    }

    public static void main(String[] args) throws UnsupportedEncodingException {

//        ArrayList arrayList1 = new ArrayList(0);
////
////        arrayList1.add(1);

//         LinkedList<Integer> integerList = new LinkedList<>();
//         integerList.add(1);
//         integerList.add(2);
//         integerList.add(3);
//        integerList.poll();
//        System.out.println(integerList);


//         integerList.addFirst();
//        int i = integerList.lastIndexOf(3);
//        System.out.println(integerList.offer(2));
//        System.out.println(integerList.peek());
//        System.out.println(integerList.poll());
//        System.out.println(i);


//        Set set = new HashSet();
//        for (Object o : set) {
//
//        }

//        System.out.println(arrayList1.size());
//        arrayList1.forEach(i -> System.out.println(i));
//
//        arrayList1.trimToSize();
//        System.out.println(arrayList1.size());
//        arrayList1.forEach(i -> System.out.println(i));

        String s = "薛君弘";
        String utf_8 = new String(s.getBytes(), "gbk");
        System.out.println(utf_8);


    }
}
