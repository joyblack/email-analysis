package com.sunrun.emailanalysis;

import org.junit.Test;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

public class IPAddressTest {
    @Test
    public void t1() throws Exception {
        List<String> lines = Files.readAllLines(Paths.get("ip.txt"), Charset.forName("UTF-8"));
        for (int i = 0; i < 10; i++) {
            System.out.println(lines.get(i));
            String line = lines.get(i);
            String[] split = line.split("\\s+");
            System.out.println(split.length);
            System.out.println(split);
        }
    }

    @Test
    public void ipToLong (){
        String ip = "111.112.113.114";
        String[] split = ip.split("\\.");
        long value = 0;

        value += Long.valueOf(split[0]) * 256 * 256 * 256;

        value += Long.valueOf(split[1]) * 256 * 256;

        value += Long.valueOf(split[2]) * 256;

        value += Long.valueOf(split[3]);

        System.out.println(value);
    }


}
