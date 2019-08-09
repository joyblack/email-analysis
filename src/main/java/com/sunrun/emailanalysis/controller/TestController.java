package com.sunrun.emailanalysis.controller;

import com.alibaba.fastjson.JSONObject;
import com.sunrun.emailanalysis.ea.recognition.AppearPosition;
import com.sunrun.emailanalysis.ea.recognition.ner.Entity;
import com.sunrun.emailanalysis.joy.exception.EAException;
import com.sunrun.emailanalysis.joy.result.EAResult;
import com.sunrun.emailanalysis.mapper.*;
import com.sunrun.emailanalysis.po.*;
import com.sunrun.emailanalysis.tool.uuid.JoyUUID;
import com.sunrun.emailanalysis.vo.RegionTest;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Collection;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("test")
public class TestController {
    @Autowired
    private EmailEntityMapper emailEntityMapper;

    @Autowired
    private EmailMapper emailMapper;

    @Autowired
    private EmailCaseMapper emailCaseMapper;

    @Autowired
    private IpAddressMapper ipAddressMapper;

    @Autowired
    private AccountMapper accountMapper;




    @Autowired
    private CustomDictionaryMapper customDictionaryMapper;

    @RequestMapping("insertKeyword")
    public String testInsert(){
        EAResult result;
        try{
            int count = 160;
            while(count -- != 0){
                CustomDictionary customDictionary = new CustomDictionary();
                customDictionary.setCreateTime(new Date());
                customDictionary.setFrequency(1024);
                customDictionary.setNature("key");
                customDictionary.setValue("黄晓婵" + count);
                customDictionary.setId(JoyUUID.getUUId());
                customDictionaryMapper.insertSelective(customDictionary);
            }
        } catch (EAException e) {
            result = EAResult.buildFailedResult(e.getNotice(),e.getMessage());
            e.printStackTrace();
        }catch (Exception e) {
            result = EAResult.buildDefaultResult();
            e.printStackTrace();
        }
        return "OK";
    }

    @RequestMapping("insertEntity")
    public String insertEntity(){
        List<EmailCase> emailCases = emailCaseMapper.selectAll();

        EmailCase emailCase = emailCases.get(0);

        if(emailCase != null){
            Long caseId = emailCase.getCaseId();

            Email c = new Email();
            c.setCaseId(caseId);
            List<Email> emails = emailMapper.select(c);

            for (Email email : emails) {
                EmailEntity entity = new EmailEntity();
                entity.setId(JoyUUID.getUUId());
                entity.setEntityType(Entity.PersonName.name());
                entity.setEmailId(email.getEmailId());
                entity.setCaseId(caseId);
                entity.setAttachId(0L);
                entity.setAppearPosition(AppearPosition.CONTENT.getId());
                entity.setEntityValue("黄晓婵");
                emailEntityMapper.insertSelective(entity);
            }
        }

        return "ok";
    }

    @RequestMapping("postal")
    public String postal() throws IOException {
        Collection<File> files = FileUtils.listFiles(new File("postal"), new String[]{"json"}, true);

        for (File file : files) {
            List<String> list = Files.readAllLines(Paths.get(file.toURI()));
            String dataStr = list.get(0);

            List<RegionTest> worldRegions = JSONObject.parseArray(dataStr, RegionTest.class);



        }
        return "OK";
    }



    @RequestMapping("ip")
    public String ip() throws IOException {
        System.out.println("~~~~~~~~~~~~~~~");
        List<String> lines = Files.readAllLines(Paths.get("ip.txt"), Charset.forName("UTF-8"));
        ipAddressMapper.selectByPrimaryKey(1);
        int error = 0;
        for (String line : lines) {
            String[] split = line.split("\\s+");
            IpAddress a = new IpAddress();
            try{
                a.setId(JoyUUID.getUUId());
                a.setStartIp(split[0]);
                a.setEndIp(split[1]);
                a.setAddress1(split[2]);
                a.setAddress2(split[3]);
                a.setStartIpValue(ipToLong(split[0]));
                a.setEndIpValue(ipToLong(split[1]));
                ipAddressMapper.insertSelective(a);
            }catch (Exception e){
                error ++ ;
                try{
                    System.out.println("出错数据: " + split);
                }catch (Exception xe){

                }
            }
        }
        System.out.println("错误数" + error);
        return "OK";
    }

    @RequestMapping("insertAccount")
    public String insertAccount(){
        for (int i = 1; i< 156; i++) {
            Account entity = new Account();
            entity.setCaseId(586137947305869312L);
            entity.setDomainName("eriez.com");
            entity.setNickName("");
            entity.setAddress("huangxiaochan" + i ++ + "@eriez.com");
            entity.setAccountId(JoyUUID.getUUId());
            accountMapper.insertSelective(entity);
        }


        return "ok";
    }

    private long ipToLong (String ip){

        String[] split = ip.split("\\.");
        long value = 0;

        value += Long.valueOf(split[0]) * 256 * 256 * 256;

        value += Long.valueOf(split[1]) * 256 * 256;

        value += Long.valueOf(split[2]) * 256;

        value += Long.valueOf(split[3]);

        return value;

    }
}
