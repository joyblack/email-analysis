package com.sunrun.emailanalysis.service;

import com.sunrun.emailanalysis.component.EACustomDictionary;
import com.sunrun.emailanalysis.data.request.common.KeywordData;
import com.sunrun.emailanalysis.data.request.custom.dictionary.CustomDictionaryData;
import com.sunrun.emailanalysis.data.request.query.IDData;
import com.sunrun.emailanalysis.dictionary.common.ResultDictionary;
import com.sunrun.emailanalysis.dictionary.database.CustomDicDictionary;
import com.sunrun.emailanalysis.joy.exception.EAException;
import com.sunrun.emailanalysis.joy.result.EAResult;
import com.sunrun.emailanalysis.joy.result.Notice;
import com.sunrun.emailanalysis.mapper.CustomDictionaryMapper;
import com.sunrun.emailanalysis.po.CustomDictionary;
import com.sunrun.emailanalysis.tool.uuid.JoyUUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.List;

@Service
public class CustomDictionaryService {
    private static Logger log = LoggerFactory.getLogger(CustomDictionaryService.class);
    @Autowired
    private CustomDictionaryMapper customDictionaryMapper;

    @Autowired
    private EACustomDictionary EACustomDictionary;

    public EAResult addCustomDictionary(CustomDictionaryData data){
        try{
            EAResult result = EAResult.buildSuccessResult();
            if(data.getValue() == null){
                throw new EAException(Notice.CUSTOM_DICTIONARY_VALUE_IS_NULL);
            }
            // 检测关键字是否存在
            CustomDictionary customDictionary = new CustomDictionary();
            customDictionary.setValue(data.getValue());
            CustomDictionary checkDic = customDictionaryMapper.selectOne(customDictionary);

            // add
            if(checkDic == null){
                // insert to database.
                customDictionary.setCreateTime(new Date());
                customDictionary.setId(JoyUUID.getUUId());
                customDictionary.setFrequency(CustomDicDictionary.FREQUENCY_DEFAULT_VALUE);
                customDictionary.setNature(CustomDicDictionary.NATURE_DEFAULT_VALUE);
                customDictionaryMapper.insertSelective(customDictionary);

                // insert to hanLP custom dictionary
                EACustomDictionary.add(customDictionary);
            }else{
                customDictionary = checkDic;
                result.setDetailMessage("关键字已经存在，没有进行更新");
            }
            result.setData(customDictionaryMapper.getCustomDictionaryById(customDictionary.getId()));
            return result;
        }catch (EAException ea){
            throw ea;
        }catch (Exception e){
            throw new EAException(e.getMessage(), Notice.EXECUTE_IS_FAILED);
        }
    }


    public EAResult updateCustomDictionary(CustomDictionaryData data){
        try{
            EAResult result = EAResult.buildSuccessResult();
            if(data.getValue() == null){
                throw new EAException(Notice.CUSTOM_DICTIONARY_VALUE_IS_NULL);
            }
            // 检测关键字是否存在
            CustomDictionary oldDic = customDictionaryMapper.selectByPrimaryKey(data.getId());
            if(oldDic == null){
                throw new EAException(Notice.CUSTOM_DICTIONARY_IS_NOT_EXIST);
            }

            // 如果没有进行改变，则无需进行多余的更新操作
            if(oldDic.getValue().equals(data.getValue())){
                result.setDetailMessage("关键字的值没发生变化");
            }else{
                // 更新新记录
                CustomDictionary newDic = new CustomDictionary();
                newDic.setId(oldDic.getId());
                newDic.setValue(data.getValue());
                newDic.setNature(oldDic.getNature());
                newDic.setFrequency(oldDic.getFrequency());
                newDic.setCreateTime(new Date());
                customDictionaryMapper.updateByPrimaryKeySelective(newDic);
                // HanLP自定义词典中更新记录
                EACustomDictionary.update(oldDic, newDic);
                result.setData(customDictionaryMapper.getCustomDictionaryById(newDic.getId()));
            }
            return result;
        }catch (EAException ea){
            throw ea;
        }catch (Exception e){
            throw new EAException(e.getMessage(), Notice.EXECUTE_IS_FAILED);
        }
    }

    public List<HashMap<String, Object>> getCustomDictionaryList(KeywordData data) {
        try{
            return customDictionaryMapper.getCustomDictionaryList(data);
        }catch (Exception e){
            log.error(e.getMessage());
            throw new EAException(e.getMessage(), Notice.EXECUTE_IS_FAILED);
        }
    }

    public HashMap<String, Object> getCustomDictionaryListCount(KeywordData data) {
        HashMap<String, Object> result = new HashMap<>();
        try{
            Long total = customDictionaryMapper.getCustomDictionaryListCount(data);
            if(total == null){
                total = 0L;
            }
            result.put(ResultDictionary.TOTAL, total);
            return result;
        }catch (Exception e){
            log.error(e.getMessage());
            throw new EAException(e.getMessage(), Notice.EXECUTE_IS_FAILED);
        }
    }

    public EAResult deleteCustomDictionary(IDData data) {
        try{
            EAResult result = EAResult.buildSuccessResult();
            // 检测关键字是否存在
            CustomDictionary deleteDic = customDictionaryMapper.selectByPrimaryKey(data.getId());
            if(deleteDic == null){
                throw new EAException(Notice.CUSTOM_DICTIONARY_IS_NOT_EXIST);
            }
            // 从数据库中删除
            customDictionaryMapper.deleteByPrimaryKey(deleteDic.getId());
            // 从HanLP中删除
            EACustomDictionary.remove(deleteDic);
            return result;
        }catch (EAException ea){
            throw ea;
        }catch (Exception e){
            throw new EAException(e.getMessage(), Notice.EXECUTE_IS_FAILED);
        }

    }

    public EAResult clearCustomDictionary() {
        try{
            EAResult result = EAResult.buildSuccessResult();
            customDictionaryMapper.clear();
            EACustomDictionary.clear();
            return result;
        }catch (EAException ea){
            throw ea;
        }catch (Exception e){
            throw new EAException(e.getMessage(), Notice.EXECUTE_IS_FAILED);
        }
    }

}
