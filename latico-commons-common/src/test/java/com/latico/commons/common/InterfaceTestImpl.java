package com.latico.commons.common;

import org.junit.Test;

import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Stream;

/**
 * <PRE>
 *
 * </PRE>
 *
 * @author: latico
 * @date: 2019-03-26 16:49
 * @version: 1.0
 */
public class InterfaceTestImpl implements InterfaceTest {

    public static void main(String[] args) {
        InterfaceTestImpl impl = new InterfaceTestImpl();
        System.out.println(impl.get("jajg"));
    }
    
    /**
     * 
     */
    @Test
    public void test(){
        Optional<Integer> optional1 = Optional.ofNullable(1);
        Optional<Integer> optional2 = Optional.ofNullable(null);
        Predicate<Integer> predicate = new Predicate(){
            @Override
            public boolean test(Object o) {
                return o == null;
            }
        };
        optional1.filter(predicate);

        Optional<Integer> filter1 = optional1.filter(predicate);
        Optional<Integer> filter2 = optional1.filter((a) -> a == 1);
        Optional<Integer> filter3 = optional2.filter((a) -> a == null);
        System.out.println(filter1.isPresent());// false
        System.out.println(filter2.isPresent());// true
        System.out.println(filter2.get().intValue() == 1);// true
        System.out.println(filter3.isPresent());// false
        
    }
    
    /**
     * 
     */
    @Test
    public void test2(){
        Stream<String> concat1 = Stream.of("熊大", "熊二", "李三", "熊四", "熊五");
        Stream<String> concat2 = Stream.of("喜洋洋","懒洋洋","灰太狼");
        //把以上两个流组合成一个流
        Stream<String> concat = Stream.concat(concat1, concat2);
        concat.forEach((name) -> System.out.println(name));
    }
}
