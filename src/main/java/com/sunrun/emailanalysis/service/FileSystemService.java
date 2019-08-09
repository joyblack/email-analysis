package com.sunrun.emailanalysis.service;

import com.sunrun.emailanalysis.dictionary.file.FileProtocol;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Service
public class FileSystemService {

    private static final String FILE_SYSTEM_ID = "id";

    private static final String FILE_SYSTEM_NAME = "name";

    // 目前只是以其序号为id
    public List<HashMap<String, Object>> getSupportProtocol(){
        FileProtocol[] values = FileProtocol.values();
        List<HashMap<String, Object>> result = new ArrayList<>();
        for (FileProtocol value : values) {
            HashMap<String, Object> map = new HashMap<>();
            map.put(FILE_SYSTEM_ID, value.ordinal());
            map.put(FILE_SYSTEM_NAME, value.name());
            result.add(map);
        }
        return result;
    }

}
