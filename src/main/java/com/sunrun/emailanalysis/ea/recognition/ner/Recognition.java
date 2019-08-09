package com.sunrun.emailanalysis.ea.recognition.ner;

import java.util.HashMap;

public interface Recognition {
    // name entity recognition.
    HashMap<String,Entity> ner(String text);
}