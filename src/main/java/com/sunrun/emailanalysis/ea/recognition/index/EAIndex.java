package com.sunrun.emailanalysis.ea.recognition.index;

import com.sunrun.emailanalysis.ea.extract.ExtractResult;
import com.sunrun.emailanalysis.ea.recognition.AppearPosition;
import com.sunrun.emailanalysis.po.EmailAttach;

public interface EAIndex {

    void indexAttach(ExtractResult extractResult, EmailAttach attach, String attachContent);

    void indexEmail(ExtractResult extractResult);

    void close();
}
