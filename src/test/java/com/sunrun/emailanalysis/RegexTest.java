package com.sunrun.emailanalysis;

import org.junit.Test;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegexTest {
    @Test
    public void  test(){
        String text = "This is my food food00000";
        String regex = "food[\\d]+";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(text);

        while(matcher.find()){
            String group = matcher.group();
            System.out.println(group);
        }
    }

    @Test
    public void  test01(){
        String text = "China is a county with a long history.";
        String regex = ".*China.*";
        boolean matches = Pattern.matches(regex, text);
        System.out.println("是否包含China：" + matches);
        // 是否包含China：true
    }

    @Test
    public void test02(){
        String text = "Onmyoji is a round game of public test in 2016 summer.";
        String regex = "(\\D*)(\\d+)(\\D*)";

        Pattern pattern = Pattern.compile(regex);

        Matcher matcher = pattern.matcher(text);

        if(matcher.find()){
            int count = matcher.groupCount();
            for (int i = 0; i <= count ; i++) {
                System.out.println("===============");
                System.out.println("group " + i + ":" );
                System.out.println("start  - " + matcher.start(i));
                System.out.println("end    - " + matcher.end(i));
                System.out.println("String - " + matcher.group(i));

            }
        }else{
            System.out.println("Not matched.");
        }
        /**
         * ===============
         * group 0:
         * start  - 0
         * end    - 54
         * String - Onmyoji is a round game of public test in 2016 summer.
         * ===============
         * group 1:
         * start  - 0
         * end    - 42
         * String - Onmyoji is a round game of public test in
         * ===============
         * group 2:
         * start  - 42
         * end    - 46
         * String - 2016
         * ===============
         * group 3:
         * start  - 46
         * end    - 54
         * String -  summer.
         */
    }
}
