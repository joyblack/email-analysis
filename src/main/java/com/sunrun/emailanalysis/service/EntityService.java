package com.sunrun.emailanalysis.service;

import com.sunrun.emailanalysis.data.request.entity.EntityListData;
import com.sunrun.emailanalysis.data.request.query.IDData;
import com.sunrun.emailanalysis.dictionary.common.ResultDictionary;
import com.sunrun.emailanalysis.ea.recognition.AppearPosition;
import com.sunrun.emailanalysis.joy.exception.EAException;
import com.sunrun.emailanalysis.mapper.EmailEntityMapper;
import com.sunrun.emailanalysis.mapper.EntityTypeMapper;
import com.sunrun.emailanalysis.po.EmailEntity;
import com.sunrun.emailanalysis.po.EntityType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

@Service
public class EntityService {

    @Autowired
    private EntityTypeMapper entityTypeMapper;

    @Autowired
    private EmailEntityMapper emailEntityMapper;

    private static Logger log = LoggerFactory.getLogger(EntityService.class);


    public List<EntityType> getEntityTypeList() {
        return entityTypeMapper.selectAll();
    }

    public List<HashMap<String, Object>> getEntityList(EntityListData inputData) {
        try {
            return emailEntityMapper.getEntityList(inputData);
        }catch (Exception e){
            throw new EAException(e.getMessage());
        }
    }

    public HashMap<String, Object> getEntityListCount(EntityListData inputData) {
        try {
            HashMap<String, Object> result = new HashMap<>();
            // Get total.
            Long total = emailEntityMapper.getEntityListCount(inputData);
            log.info("The total count is {}", total);
            result.put(ResultDictionary.TOTAL, total);
            if(total == null || total.equals(0L)){
                result.put(ResultDictionary.TOTAL, 0);
            }
            return result;
        }catch (Exception e){
            throw new EAException(e.getMessage());
        }
    }

    public EmailEntity getEntityById(IDData idData) {
        return emailEntityMapper.selectByPrimaryKey(idData.getId());
    }

    public List<HashMap<String, Object>> getEntityAppearPositionList() {
        return Arrays.asList(
                AppearPosition.SUBJECT.hashMapResult(),
                AppearPosition.CONTENT.hashMapResult(),
                AppearPosition.ATTACH.hashMapResult(),
                AppearPosition.ATTACH_REMARK.hashMapResult(),
                AppearPosition.EMAIL_REMARK.hashMapResult()
        );
    }
}
