package com.sunrun.emailanalysis.joy.file.factory;

import com.sunrun.emailanalysis.dictionary.file.FileProtocol;
import com.sunrun.emailanalysis.joy.file.service.JoyFile;
import com.sunrun.emailanalysis.joy.file.support.JoyHDFSFile;
import com.sunrun.emailanalysis.joy.file.support.JoyLocalFile;

public class JoyFileFactory {
    public static JoyFile factory(FileProtocol protocol){
        switch (protocol) {
            case LOCAL: return new JoyLocalFile();
            case DFS: return new JoyHDFSFile();
            default:return new JoyLocalFile();
        }
    }
}
