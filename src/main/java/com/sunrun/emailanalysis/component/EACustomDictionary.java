package com.sunrun.emailanalysis.component;

import com.sunrun.emailanalysis.controller.AttachController;
import com.sunrun.emailanalysis.mapper.CustomDictionaryMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class EACustomDictionary {

    private static Logger log = LoggerFactory.getLogger(AttachController.class);

    private CustomDictionaryMapper customDictionaryMapper;

    public EACustomDictionary(CustomDictionaryMapper customDictionaryMapper){
        this.customDictionaryMapper = customDictionaryMapper;
        initializer();
    }

    public void initializer(){
        log.info("Initializing SEA custom dictionary...");
        List<com.sunrun.emailanalysis.po.CustomDictionary> words = customDictionaryMapper.selectAll();

        for (com.sunrun.emailanalysis.po.CustomDictionary word : words) {
            add(word);
        }
        log.info("End initializer...");
    }

    public void clear(){
        log.info("Clear word from custom dictionary...");
        List<com.sunrun.emailanalysis.po.CustomDictionary> words = customDictionaryMapper.selectAll();

        for (com.sunrun.emailanalysis.po.CustomDictionary word : words) {
            remove(word);
        }
        log.info("End clear...");
    }

    public void update(com.sunrun.emailanalysis.po.CustomDictionary oldWord, com.sunrun.emailanalysis.po.CustomDictionary newWord){
        log.info("Update word from custom dictionary...");
        log.info("Old word: {}", oldWord.getValue());
        log.info("Old frequency: {}", oldWord.getFrequency());
        com.hankcs.hanlp.dictionary.CustomDictionary.remove(oldWord.getValue());

        log.info("New word: {}", newWord.getValue());
        log.info("New frequency: {}", newWord.getFrequency());
        com.hankcs.hanlp.dictionary.CustomDictionary.add(newWord.getValue());
    }


    public void add(com.sunrun.emailanalysis.po.CustomDictionary word){
        log.info("Add word to custom dictionary...");
        log.info("word：{}", word.getValue());
        log.info("frequency: {}", word.getFrequency());
        com.hankcs.hanlp.dictionary.CustomDictionary.add(word.getValue(), word.getNature() + " " + word.getFrequency());
    }

    public void remove(com.sunrun.emailanalysis.po.CustomDictionary word){
        log.info("Remove word from custom dictionary...");
        log.info("word：{}", word.getValue());
        com.hankcs.hanlp.dictionary.CustomDictionary.remove(word.getValue());
    }
}
