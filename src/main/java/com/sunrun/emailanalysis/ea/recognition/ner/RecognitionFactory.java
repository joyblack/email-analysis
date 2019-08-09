package com.sunrun.emailanalysis.ea.recognition.ner;

import com.sunrun.emailanalysis.ea.config.AnalysisConfig;

public class RecognitionFactory {
    public static Recognition buildDefaultRecognition(){
        return new CombineRecognition();
    }

    public static Recognition buildRecognition(RecognitionType type){
        switch (type) {
            case HANLP: return HanLPRecognition.getInstance();
            case REGEX: return new RegexRecognition();
            case COMBINE: return new CombineRecognition();
            case STANFORDNLP: return StanfordNLPRecognition.getInstance();
            default: return new CombineRecognition();
        }
    }
}
