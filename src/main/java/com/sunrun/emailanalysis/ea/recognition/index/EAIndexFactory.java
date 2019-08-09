package com.sunrun.emailanalysis.ea.recognition.index;

import com.sunrun.emailanalysis.dictionary.file.FileProtocol;
import com.sunrun.emailanalysis.po.EmailCase;

import java.io.IOException;

public class EAIndexFactory {
    public static EAIndex buildDefaultEAIndex(String indexPath, FileProtocol protocol) throws IOException {
        // EmailEntityMapper应该要去掉，之后请记得将其抹除。
        return new DefaultEAIndex(indexPath, protocol);
    }
}
