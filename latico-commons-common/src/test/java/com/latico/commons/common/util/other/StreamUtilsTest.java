package com.latico.commons.common.util.other;

import com.latico.commons.common.util.other.StreamUtils;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class StreamUtilsTest {

    @Test
    public void filter() {
        List<String> list = new ArrayList<>();
        list.add("abc1");
        list.add("bcd2");
        list.add("edf");
        list.add("fgh");

        List<String> newList = list.stream().filter((t) -> t.contains("b")).filter((t) -> t.contains("1")).collect(Collectors.toList());
        System.out.println(newList);

        newList = list.stream().filter((t) -> t.contains("b") && t.contains("1")).collect(Collectors.toList());
        System.out.println(newList);

        newList = list.stream().filter((t) -> {return t.contains("b") && t.contains("1");}).collect(Collectors.toList());
        System.out.println(newList);

        newList = StreamUtils.filter(list, (t) -> t.contains("b"), (t) -> t.contains("1"));
        System.out.println(newList);

        newList = StreamUtils.filter(list, ((t) -> t.contains("b") && t.contains("1")));
        System.out.println(newList);

        newList = StreamUtils.filter(list, ((t) -> {return t.contains("b") && t.contains("1");}));
        System.out.println(newList);
    }
}