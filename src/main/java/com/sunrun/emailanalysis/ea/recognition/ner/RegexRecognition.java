package com.sunrun.emailanalysis.ea.recognition.ner;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Use java regex to extract Entity.
 */
public class RegexRecognition implements Recognition {
    // key is regex expression, value is the regex replace Entity(Entity enum).
    private HashMap<String, Entity> regexMap;

    public RegexRecognition(){
        this.regexMap = new HashMap<>();
    }

    // Add new regex item(K-Entity).
    public void addRegex(String regex, Entity Entity){
        regexMap.put(regex, Entity);
    }

    @Override
    public HashMap<String, Entity> ner(String text) {
        HashMap<String, Entity> result = new HashMap<>();

        // check null.
        if(text == null || text.isEmpty()){
            return result;
        }

        // compute every regex,the result key is the matcher info and value is Entity, do this is avoid the text appear more than once.
        for (Map.Entry<String, Entity> EntityEntry : regexMap.entrySet()) {
            String regex = EntityEntry.getKey();
            Entity Entity = EntityEntry.getValue();

            // matcher
            Pattern pattern = Pattern.compile(regex);
            Matcher matcher = pattern.matcher(text);
            while(matcher.find()){
                String group = matcher.group();
                // The old version used further judge when regex replace the bank number.
                if(group != null && group.length() > 0){
                    result.put(group, Entity);
                }
            }

        }
        return result;
    }

    public HashMap<String, Entity> getRegexMap() {
        return regexMap;
    }
}
