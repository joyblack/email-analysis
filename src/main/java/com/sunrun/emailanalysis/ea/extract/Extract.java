package com.sunrun.emailanalysis.ea.extract;

public interface Extract {
    // single email extract.
    ExtractResult extract() throws Exception;
}
