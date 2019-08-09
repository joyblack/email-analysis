package com.sunrun.emailanalysis.ea.recognition.ner;

import com.hankcs.hanlp.HanLP;
import com.hankcs.hanlp.seg.Segment;
import com.hankcs.hanlp.seg.common.Term;

import java.util.HashMap;
import java.util.List;

public class HanLPRecognition implements Recognition{
    private static final HanLPRecognition hanLPTool;

    static{
        hanLPTool = new HanLPRecognition();
    }

    public static HanLPRecognition getInstance(){
        return hanLPTool;
    }

    private HanLPRecognition(){}


    @Override
    public HashMap<String, Entity> ner(String text) {
        HashMap<String, Entity> result = new HashMap<>();
        // 启用所有命名实体的识别模式
        //  Segment segment = new NShortSegment().enableAllNamedEntityRecognize(true);
        Segment segment = HanLP.newSegment().enableAllNamedEntityRecognize(true);
        List<Term> terms = segment.seg(text);
        for (Term term : terms) {
            Entity trans = trans(term.nature.toString());
            if(!trans.equals(Entity.OtherTag)){
                result.put(term.word, trans);
            }
        }
        return result;
    }


    private static Entity trans(String type) {
        // ns - 地名 nsf-音译地名
        switch (type.toLowerCase()) {
            case "ns":
            case "nsf":
                return Entity.Location;
            case "nr":
            case "nr1":
            case "nr2":
            case "nrf":
            case "nrj":  // nrj - 日语人名
                return Entity.PersonName;
            case "nt":
            case "ntc":
            case "ntcb":
            case "ntcf":
            case "ntch":
            case "nto":
            case "nth":
            case "nts":
            case "ntu":  // ntu - 大学
                return Entity.Organization;
            case "mg":  // mg - 数语素
                return Entity.GeneralNumber;
            case "key": // 自定义词典
                return Entity.Key;
            default:  // 无关语素
                return Entity.OtherTag;
        }

    }

    public static void main(String[] args) {
        String             text =
                "this is a test.";

        Recognition extract = new HanLPRecognition();
        System.out.println(extract.ner(text));
    }
}
