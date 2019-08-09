package com.sunrun.emailanalysis.tool.lucene;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.highlight.*;

import java.io.IOException;
import java.io.StringReader;

public class LuceneUtil {
    private static String highlight(String content, Query query, Analyzer analyzer) throws IOException, InvalidTokenOffsetsException {
        SimpleHTMLFormatter formatter = new SimpleHTMLFormatter("<span style=\"background-color: red\"><big><big>", "</big></big></span>");
        Highlighter highlighter = new Highlighter(formatter, new QueryScorer(query));
        highlighter.setTextFragmenter(new SimpleFragmenter(25));
        String resultString = highlighter.getBestFragments(analyzer.tokenStream("", new StringReader(content)), content, 3, "...");
        return resultString + "...";
    }
}
