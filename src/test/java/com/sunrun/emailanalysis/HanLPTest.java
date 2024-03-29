package com.sunrun.emailanalysis;

import com.hankcs.hanlp.HanLP;
import com.hankcs.hanlp.collection.AhoCorasick.AhoCorasickDoubleArrayTrie;
import com.hankcs.hanlp.dictionary.CoreDictionary;
import com.hankcs.hanlp.dictionary.CustomDictionary;
import org.junit.Test;

public class HanLPTest {

    @Test
    public void t1(){
        // 强行插入
        CustomDictionary.insert("攻城狮", "key 1024");
        System.out.println(CustomDictionary.insert("攻城狮", "key 1024"));
        CustomDictionary.insert("攻城狮", "key 1024");

        System.out.println(CustomDictionary.add("攻城狮", "key 1024"));
        // 删除词语（注释掉试试）
//        CustomDictionary.remove("攻城狮");
//        System.out.println(CustomDictionary.add("单身狗", "nz 1024 n 1"));
//        System.out.println(CustomDictionary.get("单身狗"));

        String text = "攻城狮逆袭单身狗的，迎娶白富美，走上人生巅峰";  // 怎么可能噗哈哈！




        // AhoCorasickDoubleArrayTrie自动机扫描文本中出现的自定义词语
        final char[] charArray = text.toCharArray();
//        CustomDictionary.parseText(charArray, new AhoCorasickDoubleArrayTrie.IHit<CoreDictionary.Attribute>()
//        {
//            @Override
//            public void hit(int begin, int end, CoreDictionary.Attribute value)
//            {
//                System.out.printf("[%d:%d]=%s %s\n", begin, end, new String(charArray, begin, end - begin), value);
//            }
//        });

        // 自定义词典在所有分词器中都有效
        System.out.println(HanLP.segment(text));
    }
}
