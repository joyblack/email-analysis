package com.sunrun.emailanalysis.data.handler;

import com.sunrun.emailanalysis.data.request.RequestParameter;
import com.sunrun.emailanalysis.data.request.RequestParameterValue;
import com.sunrun.emailanalysis.data.request.account.AccountListData;
import com.sunrun.emailanalysis.data.request.account.warning.SetStateData;
import com.sunrun.emailanalysis.data.request.account.warning.WarningAccountData;
import com.sunrun.emailanalysis.data.request.account.warning.WarningAccountListData;
import com.sunrun.emailanalysis.data.request.analysis.EmailAnalysisData;
import com.sunrun.emailanalysis.data.request.analysis.MonitorData;
import com.sunrun.emailanalysis.data.request.attach.AttachListData;
import com.sunrun.emailanalysis.data.request.cases.CaseData;
import com.sunrun.emailanalysis.data.request.cases.CaseListData;
import com.sunrun.emailanalysis.data.request.common.KeywordData;
import com.sunrun.emailanalysis.data.request.common.Pagination;
import com.sunrun.emailanalysis.data.request.custom.dictionary.CustomDictionaryData;
import com.sunrun.emailanalysis.data.request.domain.DomainListData;
import com.sunrun.emailanalysis.data.request.email.*;
import com.sunrun.emailanalysis.data.request.entity.EditEntityData;
import com.sunrun.emailanalysis.data.request.entity.EntityListData;
import com.sunrun.emailanalysis.data.request.query.IDData;
import com.sunrun.emailanalysis.data.request.statistic.AttachTypeData;
import com.sunrun.emailanalysis.data.request.statistic.EmailTotalData;
import com.sunrun.emailanalysis.data.request.statistic.StatisticRelationData;
import com.sunrun.emailanalysis.data.request.task.TaskInfoData;
import com.sunrun.emailanalysis.data.request.tool.AddressData;
import com.sunrun.emailanalysis.dictionary.database.StatisticRelationDictionary;
import com.sunrun.emailanalysis.dictionary.file.FileProtocol;
import com.sunrun.emailanalysis.ea.config.AnalysisConfig;
import com.sunrun.emailanalysis.joy.exception.EAException;
import com.sunrun.emailanalysis.joy.file.common.FileCombineExtension;
import com.sunrun.emailanalysis.joy.result.Notice;
import com.sunrun.emailanalysis.po.EmailEntity;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

@Component
public class EADataHandler {

    // 处理并获取分页信息
    private Pagination getPagination(HashMap<String, Object> input){
        Pagination pagination = new Pagination();
        try{
            // Page index
            pagination.setPageIndex(getIntegerValue(input, RequestParameter.PAGE_INDEX, RequestParameterValue.PAGE_INDEX_DEFAULT));
            // Page Size
            pagination.setPageSize(getIntegerValue(input, RequestParameter.PAGE_SIZE, RequestParameterValue.PAGE_SIZE_DEFAULT));
            // Shift
            pagination.setShift(getIntegerValue(input, RequestParameter.SHIFT, RequestParameterValue.SHIFT_DEFAULT));
            // Sort
            pagination.setSort(getStringValue(input, RequestParameter.SORT, RequestParameterValue.SORT_DEFAULT));
            // Order
            pagination.setOrder(getStringValue(input, RequestParameter.ORDER, RequestParameterValue.ORDER_DEFAULT));
        }catch (EAException ea){
            throw ea;
        }catch (Exception ex){
            throw new EAException(ex.getMessage(),Notice.REQUEST_PARAMETER_IS_ERROR);
        }
        return pagination;
    }

    public EmailAnalysisData handlerAnalysisData(HashMap<String, Object> input){
        try {
            EmailAnalysisData data = new EmailAnalysisData();
            // taskId
            Long taskId = getLongValue(input, RequestParameter.TASK_ID, null);
            data.setTaskId(taskId);

            // Case name
            String caseName = getStringValue(input, RequestParameter.CASE_NAME, RequestParameterValue.CASE_NAME_DEFAULT);
            if(caseName == null || caseName.isEmpty()){
                throw new EAException("项目名称(caseName)不能为空", Notice.REQUEST_PARAMETER_IS_ERROR);
            }
            data.setCaseName(caseName);

            // Case type
            Integer caseType = getIntegerValue(input, RequestParameter.CASE_TYPE, null);
            if(caseType == null){
                throw new EAException("项目类型(caseType)不能为空", Notice.REQUEST_PARAMETER_IS_ERROR);
            }
            data.setCaseType(caseType);

            // File path
            String filePath = getStringValue(input, RequestParameter.FILE_PATH, null);
            if(filePath == null){
                throw new EAException("解析路径(filePath)不能为空", Notice.REQUEST_PARAMETER_IS_ERROR);
            }
            data.setFilePath(filePath);
            // protocol
            String protocol = getStringValue(input, RequestParameter.PROTOCOL, FileProtocol.LOCAL.name());
            data.setProtocol(protocol);
            // analysis configure
            AnalysisConfig analysisConfig = new AnalysisConfig();
            // Default is true.
            analysisConfig.setEnableEntity(getBooleanValue(input, RequestParameter.ENABLE_ENTITY, Boolean.TRUE));
            analysisConfig.setEnableWarningAccount(getBooleanValue(input, RequestParameter.ENABLE_WARNING_ACCOUNT, Boolean.TRUE));
            data.setAnalysisConfig(analysisConfig);
            return data;
        }catch (EAException ea){
            throw ea;
        }catch (Exception e){
            throw new EAException(e.getMessage(),Notice.REQUEST_PARAMETER_IS_ERROR);
        }

    }

    private List<Long> getLongListValue(HashMap<String, Object> input, String key, List<Long> defaultValue){
        try{
            Object temp = input.get(key);
            if(temp == null){
                return defaultValue;
            }
            return (List<Long>)temp;
        }catch (Exception ex){
            throw new EAException("请求字段'" + key  + "'解析错误",Notice.REQUEST_PARAMETER_IS_ERROR);
        }
    }

    private String getStringValue(HashMap<String, Object> input, String key, String defaultValue){
        try{
            Object temp = input.get(key);
            if(temp == null){
                return defaultValue;
            }
            String strInput = temp.toString().trim();
            if(strInput.isEmpty()){
                return defaultValue;
            }else{
                return strInput;
            }
        }catch (Exception ex){
            throw new EAException("请求字段'" + key  + "'解析错误",Notice.REQUEST_PARAMETER_IS_ERROR);
        }
    }

    private Integer getIntegerValue(HashMap<String, Object> input, String key, Integer defaultValue){
        try{
            Object temp = input.get(key);
            if(temp == null){
                return defaultValue;
            }
            String strInput = temp.toString().trim();
            if(strInput.isEmpty()){
                return defaultValue;
            }else{
                return Integer.valueOf(strInput);
            }
        }catch (Exception ex){
            throw new EAException("请求字段'" + key  + "'解析错误",Notice.REQUEST_PARAMETER_IS_ERROR);
        }
    }

    private Boolean getBooleanValue(HashMap<String, Object> input, String key, Boolean defaultValue){
        try{
            Object temp = input.get(key);
            if(temp == null){
                return defaultValue;
            }
            String strInput = temp.toString().trim();
            if(strInput.isEmpty()){
                return defaultValue;
            }else{
                if(strInput.equals("0") || strInput.toLowerCase().equals("false")){
                    return Boolean.FALSE;
                }else{
                    return Boolean.TRUE;
                }
            }
        }catch (Exception ex){
            throw new EAException("请求字段'" + key  + "'解析错误",Notice.REQUEST_PARAMETER_IS_ERROR);
        }
    }

    private String getDateStringValue(HashMap<String, Object> input, String key, String defaultValue){
        try{
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
            Object temp = input.get(key);
            if(temp == null){
                return defaultValue;
            }
            String strInput = temp.toString().trim();
            if(strInput.isEmpty()){
                return defaultValue;
            }else{
                return format.format(format.parse(strInput));
            }
        }catch (Exception ex){
            throw new EAException("请求字段'" + key  + "'解析错误",Notice.REQUEST_PARAMETER_IS_ERROR);
        }
    }

    private Long getLongValue(HashMap<String, Object> input, String key, Long defaultValue){
        try{
            Object temp = input.get(key);
            if(temp == null){
                return defaultValue;
            }
            String strInput = temp.toString().trim();
            if(strInput.isEmpty()){
                return defaultValue;
            }else{
                return Long.valueOf(strInput);
            }
        }catch (Exception ex){
            throw new EAException("请求字段'" + key  + "'解析错误",Notice.REQUEST_PARAMETER_IS_ERROR);
        }
    }



    public CaseListData getCaseListData(HashMap<String, Object> input) {
        CaseListData data =  new CaseListData();
        // keyword
        data.setKeyword(getStringValue(input, RequestParameter.KEYWORD, null));
        // pager
        data.setPagination(getPagination(input));
        return data;
    }

    public EntityListData getEntityListData(HashMap<String, Object> input) {
        EntityListData entityListData = new EntityListData();
        // entity class: explain array list post data.
        List<HashMap<String, Object>> list = getHashMapList(input, RequestParameter.ENTITIES, null);
        List<EmailEntity> entities = new ArrayList<>();
        if(list != null){
            for (HashMap<String, Object> e : list) {
                EmailEntity entity = new EmailEntity();
                // 如果entityType为空或者ALL,那么此项代表对所有类型进行检索;
                // 如果entityValue为空或者空串，那么此项代表查询所有对应的type类型的实体，不加以筛选。
                // entityValue
                Object value = e.get(RequestParameter.ENTITY_VALUE);
                if(value == null){
                    entity.setEntityValue(null);;
                }
                String entityValue = value.toString().trim();
                if(entityValue.isEmpty()){
                    entity.setEntityValue(null);
                }else{
                    entity.setEntityValue(entityValue);
                }
                // entityType
                Object type = e.get(RequestParameter.ENTITY_TYPE);
                if(type == null){
                    entity.setEntityType(null);
                }else{
                    String entityType = type.toString().trim();
                    if(entityType.isEmpty()){
                        entity.setEntityType(null);
                    }else{
                        entity.setEntityType(entityType);
                    }
                }
                // add to list.
                entities.add(entity);
            }
        }
        // entities, if not set shows no limit condition.
        entityListData.setEntities(entities.size() > 0 ? entities : null);
        // caseId
        entityListData.setCaseId(getLongValue(input, RequestParameter.CASE_ID, null));
        // emailId
        entityListData.setEmailId(getLongValue(input, RequestParameter.EMAIL_ID, null));
        // pager
        entityListData.setPagination(getPagination(input));
        return entityListData;
    }



    public SearchEmailData getSearchEmailData(HashMap<String, Object> input) {
        SearchEmailData searchEmailData = new SearchEmailData();
        // caseId
        Long caseId = getLongValue(input, RequestParameter.CASE_ID, null);
        searchEmailData.setCaseId(caseId);
        // content
        searchEmailData.setContent(getStringValue(input, RequestParameter.CONTENT, null));
        // isExact
        searchEmailData.setIsExact(getIntegerValue(input, RequestParameter.IS_EXACT, 0));
        // isWarning
        searchEmailData.setIsWarning(getIntegerValue(input, RequestParameter.IS_WARNING, 0));
        // pager
        searchEmailData.setPagination(getPagination(input));
        // subject
        searchEmailData.setSubject(getStringValue(input, RequestParameter.SUBJECT, null));
        // from
        searchEmailData.setFrom(getStringValue(input, RequestParameter.FROM, null));
        // to
        searchEmailData.setTo(getStringValue(input, RequestParameter.TO, null));
        // sendTime
        searchEmailData.setSendTimeStart(getDateStringValue(input, RequestParameter.SEND_TIME_START,null));
        searchEmailData.setSendTimeEnd(getDateStringValue(input, RequestParameter.SEND_TIME_END,null));
        // sendUserIP
        searchEmailData.setFromIp(getStringValue(input, RequestParameter.FROM_IP, null));
        return searchEmailData;
    }

    public SearchEmailByEntityData getSearchEmailByEntityData(HashMap<String, Object> input) {
        SearchEmailByEntityData data = new SearchEmailByEntityData();
        // caseId
        Long caseId = getLongValue(input, RequestParameter.CASE_ID, null);
        // entity value
        String entityValue = getStringValue(input, RequestParameter.ENTITY_VALUE, null);
        // isExact
        Integer isExact = getIntegerValue(input, RequestParameter.IS_EXACT, 1);
        // pagination
        Pagination pagination = getPagination(input);
        return new SearchEmailByEntityData(caseId, entityValue, isExact, pagination);
    }


    public CaseData getCaseData(HashMap<String, Object> input) {
        CaseData caseData = new CaseData();

        // Case name
        String caseName = getStringValue(input, RequestParameter.CASE_NAME, "");
        caseData.setCaseName(caseName);

        // Case id
        Long caseId = getLongValue(input, RequestParameter.CASE_ID, null);
        caseData.setCaseId(caseId);

        return caseData;
    }

    public IDData handlerIDData(HashMap<String, Object> input){
        Long id = getLongValue(input, RequestParameter.ID, 0L);
        return new IDData(id);
    }


    public EmailTotalData getEmailTotalData(HashMap<String, Object> input) {
        Long caseId = getLongValue(input, RequestParameter.CASE_ID, null);
        EmailTotalData data = new EmailTotalData();
        data.setCaseId(caseId);
        return data;
    }

    public AttachListData getAttachListData(HashMap<String, Object> input) {
        Long caseId = getLongValue(input, RequestParameter.CASE_ID, null);
        Pagination pagination = getPagination(input);
        String fileName = getStringValue(input, RequestParameter.FILE_NAME, null);
        Integer include = getIntegerValue(input, RequestParameter.INCLUDE, RequestParameterValue.INCLUDE_ALL);
        String fileTypeString = getStringValue(input, RequestParameter.FILE_TYPE, FileCombineExtension.ALL);
        return new AttachListData(caseId, fileName, include, Arrays.asList(fileTypeString.split(RequestParameter.TYPE_SPLIT)), pagination);
    }

    public MonitorData handlerMonitorData(HashMap<String, Object> input) {
        Long taskId = getLongValue(input, RequestParameter.TASK_ID, 0L);
        return new MonitorData(taskId);
    }

    public TaskInfoData getTaskInfoData(HashMap<String, Object> input) {
        Long taskId = getLongValue(input, RequestParameter.TASK_ID, 0L);
        return new TaskInfoData(taskId);
    }

    public StatisticRelationData getRelationData(HashMap<String, Object> input) {
        Long caseId = getLongValue(input, RequestParameter.CASE_ID, 0L);
        String keyword = getStringValue(input, RequestParameter.KEYWORD, null);
        String sendType = getStringValue(input, RequestParameter.SEND_TYPE, StatisticRelationDictionary.SEND_TYPE_FROM);
        Integer isExact = getIntegerValue(input, RequestParameter.IS_EXACT, 0);
        Pagination pagination = getPagination(input);
        return new StatisticRelationData(caseId, keyword, sendType, isExact, pagination);
    }


    public AttachTypeData getAttachTypeData(HashMap<String, Object> input) {
        Long caseId = getLongValue(input, RequestParameter.CASE_ID, RequestParameterValue.CASE_ID_DEFAULT);
        Integer include = getIntegerValue(input, RequestParameter.INCLUDE, RequestParameterValue.INCLUDE_ALL);
        String fileTypeString = getStringValue(input, RequestParameter.FILE_TYPE, FileCombineExtension.ALL);
        Integer statisticType = getIntegerValue(input, RequestParameter.STATISTIC_TYPE, RequestParameterValue.STATISTIC_TYPE_DIVIDE);
        return new AttachTypeData(caseId, Arrays.asList(fileTypeString.split(RequestParameter.TYPE_SPLIT)), statisticType, include);
    }


    private List<HashMap<String, Object>> getHashMapList(HashMap<String, Object> input, String key, List<HashMap<String, Object>> defaultValue){
        try{
            Object temp = input.get(key);
            if(temp == null){
                return defaultValue;
            }
            return (List<HashMap<String, Object>>)temp;
        }catch (Exception ex){
            throw new EAException("请求字段'" + key  + "'解析错误",Notice.REQUEST_PARAMETER_IS_ERROR);
        }
    }


    public EditEntityData getEditEntityData(HashMap<String, Object> input) {
        // attach or email id.
        Long id = getLongValue(input, RequestParameter.ID, null);

        // entities list.
        List<HashMap<String, Object>> list = getHashMapList(input, RequestParameter.ENTITIES, null);
        // Check entities.
        List<EmailEntity> entities = new ArrayList<>();
        if(list != null){
            for (HashMap<String, Object> e : list) {
                EmailEntity entity = new EmailEntity();
                entity.setEntityType(e.get(RequestParameter.ENTITY_TYPE).toString());
                entity.setEntityValue(e.get(RequestParameter.ENTITY_VALUE).toString());
                entities.add(entity);
            }
        }
        return new EditEntityData(id, entities);
    }

    public CustomDictionaryData handlerCustomDictionaryData(HashMap<String, Object> input) {
        CustomDictionaryData data = new CustomDictionaryData();
        data.setId(getLongValue(input, RequestParameter.ID, 0L));
        data.setValue(getStringValue(input, RequestParameter.VALUE, null));
        return data;
    }

    public KeywordData handlerKeywordData(HashMap<String, Object> input) {
        KeywordData data = new KeywordData();
        data.setKeyword(getStringValue(input, RequestParameter.KEYWORD, null));
        data.setPagination(getPagination(input));
        return data;
    }

    public AddressData getAddressData(HashMap<String, Object> input) {
        AddressData data = new AddressData();

        data.setIp(getStringValue(input, RequestParameter.IP, null));

        data.setAddress(getStringValue(input, RequestParameter.ADDRESS, null));

        data.setZipCode(getStringValue(input, RequestParameter.ZIP_CODE, null));

        return data;
    }

    public DomainListData getDomainListData(HashMap<String, Object> input) {
        DomainListData data = new DomainListData();

        data.setCaseId(getLongValue(input, RequestParameter.CASE_ID, 0L));

        data.setKeyword(getStringValue(input, RequestParameter.KEYWORD, null));

        data.setPagination(getPagination(input));

        return data;


    }

    public AccountListData getAccountListData(HashMap<String, Object> input) {
        AccountListData data = new AccountListData();
        // caseId
        Long caseId = getLongValue(input, RequestParameter.CASE_ID, 0L);
        // domain name
        String domainName = getStringValue(input, RequestParameter.DOMAIN_NAME, null);
        // entity value
        String keyword = getStringValue(input, RequestParameter.KEYWORD, null);
        // isExact
        Integer isExact = getIntegerValue(input, RequestParameter.IS_EXACT, 1);
        // pagination
        Pagination pagination = getPagination(input);
        return new AccountListData(caseId, domainName, keyword, isExact, pagination);
    }

    public SearchEmailByAccountData getSearchEmailByAccountData(HashMap<String, Object> input) {
        SearchEmailByAccountData data = new SearchEmailByAccountData();
        // caseId
        Long caseId = getLongValue(input, RequestParameter.CASE_ID, null);
        data.setCaseId(caseId);
        // account
        String account = getStringValue(input, RequestParameter.ACCOUNT, null);
        if(account == null){
            throw new EAException("邮箱账号不能为空", Notice.REQUEST_PARAMETER_IS_ERROR);
        }
        data.setAccount(account);
        // isExact
        Integer isExact = getIntegerValue(input, RequestParameter.IS_EXACT, 0);
        data.setIsExact(isExact);
        // pagination
        Pagination pagination = getPagination(input);
        data.setPagination(pagination);

        return data;
    }

    public WarningAccountData getWarningAccountData(HashMap<String, Object> input) {
        WarningAccountData data = new WarningAccountData();
        data.setId(getLongValue(input, RequestParameter.ACCOUNT_ID, 0L));
        data.setAddress(getStringValue(input, RequestParameter.ACCOUNT, null));
        return data;
    }

    public WarningAccountListData handlerWarningAccountListData(HashMap<String, Object> input) {
        WarningAccountListData data = new WarningAccountListData();
        data.setKeyword(getStringValue(input, RequestParameter.KEYWORD, null));
        data.setPagination(getPagination(input));
        return data;
    }

    public SetStateData handlerSetStateData(HashMap<String, Object> input) {
        SetStateData data = new SetStateData();
        data.setState(getIntegerValue(input, RequestParameter.STATE, null));
        data.setIds(getLongListValue(input, RequestParameter.IDS, null));
        return data;
    }
}
