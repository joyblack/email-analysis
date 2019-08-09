package com.sunrun.emailanalysis.mapper;

import com.sunrun.emailanalysis.data.request.attach.AttachListData;
import com.sunrun.emailanalysis.po.EmailAttach;
import org.apache.ibatis.annotations.Param;
import tk.mybatis.mapper.common.Mapper;

import java.util.HashMap;
import java.util.List;

public interface EmailAttachMapper extends Mapper<EmailAttach> {

    List<HashMap<String, Object>> getEmailAttach(@Param("emailId") Long id);

    HashMap<String, Object> getAttach(@Param("attachId") Long id);

    Long getAttachListCount(@Param("data") AttachListData attachListData, @Param("extensionList") List<String> extensionList);

    List<HashMap<String, Object>> getAttachList(@Param("data") AttachListData attachListData, @Param("extensionList") List<String> extensionList);

    List<HashMap<String, Object>> getGroupTypeCount(@Param("caseId") Long caseId);
}