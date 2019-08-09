package com.sunrun.emailanalysis.ea.recognition.ner;

import edu.stanford.nlp.ie.crf.CRFClassifier;
import edu.stanford.nlp.util.CoreMap;

import java.util.HashMap;

/**
 * extract english Entity.
 */
public class StanfordNLPRecognition implements Recognition{

    private final static String model="nlp/edu/stanford/nlp/models/ner/english.muc.7class.distsim.crf.ser.gz";

    //private final static String model="D:/ea/edu/stanford/nlp/models/ner/english.muc.7class.distsim.crf.ser.gz";

    // the stanford entity extract answer type.
    public static enum LabelType{TIME, LOCATION, ORGANIZATION, PERSON, MONEY, PERCENT, DATE};


    private static final StanfordNLPRecognition stanfordNLPTool = new StanfordNLPRecognition();
    CRFClassifier<CoreMap> defaultClassifier;



    private StanfordNLPRecognition(){
        defaultClassifier = CRFClassifier.getClassifierNoExceptions(model);
        //defaultClassifier = CRFClassifier.getDefaultClassifier();
    }

    public static StanfordNLPRecognition getInstance(){
        return stanfordNLPTool;
    }


    @Override
    public HashMap<String, Entity> ner(String text) {
        HashMap<String, Entity> result  = new HashMap<>();
        if(text == null){
            return result;
        }
        text = text.trim();
        if(text.isEmpty()){
            return result;
        }
        // 这里可能需要同步锁
        // <LOCATION>U.S.</LOCATION> and South Korean troops began a 10-day military exe
        String analysisSentence = defaultClassifier.classifyWithInlineXML(text);

        LabelType[] types = LabelType.values();

        for (LabelType type : types) {
            String patStart="<" + type.name()+">";
            String patEnd="</"+type.name()+">";
            int offset = 0;
            int idxA, idxB;
            while((idxA = analysisSentence.indexOf(patStart, offset)) >= 0){
                idxB = analysisSentence.indexOf(patEnd, idxA);
                if(idxB>=0){
                    String exp = analysisSentence.substring(idxA+patStart.length(), idxB);
                    if((exp!=null)&&(exp.length()>0)){
                        result.put(exp, typeTransform(type));
                    }
                    offset = idxB + patEnd.length();
                }else{
                    break;
                }
            }
        }
        return result;
    }

    private static Entity typeTransform(LabelType type){
        switch(type){
            case TIME:
                return Entity.DateTime;
            case LOCATION:
                return Entity.Location;
            case ORGANIZATION:
                return Entity.Organization;
            case PERSON:
                return Entity.PersonName;
            case MONEY:
                return Entity.GeneralNumber;
            case PERCENT:
                return Entity.GeneralNumber;
            case DATE:
                return Entity.DateTime;
            default:
                return Entity.GeneralNumber;
        }
    }

    public static void main(String[] args) {
        String text = "2018/3/2 U.S. and South Korean troops began a 10-day military exercise around Seoul on Tuesday, maneuvers the U.S. commander called \"defense-oriented\" during a period of heightened tensions with the communist North."+
                "The annual \"Ulchi Freedom Guardian\" exercise involves about 530,000 troops from South Korea, the United States and seven other countries, as well as computer-aided simulations, the U.S. command in Seoul reported.\""+
                "Dr. John said it was a big event in recent years. The UN annouces support to this report.";

        StanfordNLPRecognition stanfordNLPTool =  StanfordNLPRecognition.getInstance();

        System.out.println("-----------------");
        System.out.println(stanfordNLPTool.ner(text));
        System.out.println("-----------------");
    }
}
