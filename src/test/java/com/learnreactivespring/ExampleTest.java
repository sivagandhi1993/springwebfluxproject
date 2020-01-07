package com.learnreactivespring;

import com.google.common.collect.Lists;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

public class ExampleTest {




    public static void main(String[] args) {
        List<String> list = Lists.newArrayList("a","b","c");
        Optional<String> optional = list.stream().filter(t-> Objects.equals("a",t)).findAny();
        if(optional.isPresent()){
            optional.get();
        }
        optional.orElseThrow(RuntimeException::new);
        method1();
    }
    private static void method1() {
    }

}
