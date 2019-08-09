package com.sunrun.emailanalysis.ea.recognition.ner;
import com.sunrun.emailanalysis.controller.EmailController;
import com.sunrun.emailanalysis.ea.config.AnalysisConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

public class CombineRecognition implements Recognition {
    private static Logger log = LoggerFactory.getLogger(CombineRecognition.class);
    // regex expressions
    // phone number
    private final static String phoneNumberPattern ="(?<!\\d)((?:(\\(\\+?86\\))(0[0-9]{2,3}\\-?)?([2-9][0-9]{6,7})+(\\-[0-9]{1,4})?)|(?:(86-?)?(0[0-9]{2,3}\\-?)?([2-9][0-9]{6,7})+(\\-[0-9]{1,4})?))(?!\\d)";
    // mobile phone number
    private final static String mobilePhoneNumberPattern ="(?<!\\d)((?:(\\(\\+?86\\))((13[0-9]{1})|(15[0-9]{1})|(18[0,5-9]{1}))+\\d{8})|(?:86-?((13[0-9]{1})|(15[0-9]{1})|(18[0,5-9]{1}))+\\d{8})|(?:((13[0-9]{1})|(15[0-9]{1})|(18[0,5-9]{1}))+\\d{8}))(?!\\d)";
    // car number
    private final static String carNumberPattern="(?<!\\d)([京津沪渝冀豫云辽黑湘皖鲁新苏浙赣鄂桂甘晋蒙陕吉闽贵粤青藏川宁琼使领]{1}[a-zA-Z]{1}[a-zA-Z_0-9]{4}[a-zA-Z_0-9_\u4e00-\u9fa5]?)";
    // date time A type.
    private final static String datePatternA1 =
            "(?<=[\\D[^零一二三四五六七八九十]])(((([1920]{2})?\\d{2})|([零一二三四五六七八九十]{2,4}))[年\\-/\\s])(?!((\\d{3,}|[零一二三四五六七八九十]{4,})))(((\\d{1,2})|([零一二三四五六七八九十]{1,3}))[月\\-/\\s])?(((\\d{1,2})|([零一二三四五六七八九十]{1,3}))[日号\\s]?)?(早上|凌晨|上午|中午|下午|晚上|半夜)?(((\\d{1,2})|([零一二三四五六七八九十]{1,3}))[时点\\-/:])?(((\\d{1,2})|([零一二三四五六七八九十半]{1,3}))[分\\-/:])?(((\\d{1,2})|([零一二三四五六七八九十]{1,3}))[秒]?)?(?<!(\\d{1,2}[\\s\\-:]?)|[零一二三四五六七八九十])(?=\\D)";
    private final static String datePatternA2 =
            "(?<=[\\D[^零一二三四五六七八九十]])(((([1920]{2})?\\d{2})|([零一二三四五六七八九十]{2,4}))[年\\-/\\s])?(?!((\\d{3,}|[零一二三四五六七八九十]{4,})))(((\\d{1,2})|([零一二三四五六七八九十]{1,3}))[月\\-/\\s])(((\\d{1,2})|([零一二三四五六七八九十]{1,3}))[日号\\s]?)?(早上|凌晨|上午|中午|下午|晚上|半夜)?(((\\d{1,2})|([零一二三四五六七八九十]{1,3}))[时点\\-/:])?(((\\d{1,2})|([零一二三四五六七八九十半]{1,3}))[分\\-/:])?(((\\d{1,2})|([零一二三四五六七八九十]{1,3}))[秒]?)?(?<!(\\d{1,2}[\\s\\-:]?)|[零一二三四五六七八九十])(?=\\D)";
    private final static String datePatternA3 =
            "(?<=[\\D[^零一二三四五六七八九十]])(((([1920]{2})?\\d{2})|([零一二三四五六七八九十]{2,4}))[年\\-/\\s])?(?!((\\d{3,}|[零一二三四五六七八九十]{4,})))(((\\d{1,2})|([零一二三四五六七八九十]{1,3}))[月\\-/\\s])?(((\\d{1,2})|([零一二三四五六七八九十]{1,3}))[日号\\s]?)(早上|凌晨|上午|中午|下午|晚上|半夜)?(((\\d{1,2})|([零一二三四五六七八九十]{1,3}))[时点\\-/:])?(((\\d{1,2})|([零一二三四五六七八九十半]{1,3}))[分\\-/:])?(((\\d{1,2})|([零一二三四五六七八九十]{1,3}))[秒]?)?(?<!(\\d{1,2}[\\s\\-:]?)|[零一二三四五六七八九十])(?=\\D)";
    private final static String datePatternA4 =
            "(?<=[\\D[^零一二三四五六七八九十]])(((([1920]{2})?\\d{2})|([零一二三四五六七八九十]{2,4}))[年\\-/\\s])?(?!((\\d{3,}|[零一二三四五六七八九十]{4,})))(((\\d{1,2})|([零一二三四五六七八九十]{1,3}))[月\\-/\\s])?(((\\d{1,2})|([零一二三四五六七八九十]{1,3}))[日号\\s]?)?(早上|凌晨|上午|中午|下午|晚上|半夜)?(((\\d{1,2})|([零一二三四五六七八九十]{1,3}))[时点\\-/:])(((\\d{1,2})|([零一二三四五六七八九十半]{1,3}))[分\\-/:])?(((\\d{1,2})|([零一二三四五六七八九十]{1,3}))[秒]?)?(?<!(\\d{1,2}[\\s\\-:]?)|[零一二三四五六七八九十])(?=\\D)";
    private final static String datePatternA5 =
            "(?<=[\\D[^零一二三四五六七八九十]])(((([1920]{2})?\\d{2})|([零一二三四五六七八九十]{2,4}))[年\\-/\\s])?(?!((\\d{3,}|[零一二三四五六七八九十]{4,})))(((\\d{1,2})|([零一二三四五六七八九十]{1,3}))[月\\-/\\s])?(((\\d{1,2})|([零一二三四五六七八九十]{1,3}))[日号\\s]?)?(早上|凌晨|上午|中午|下午|晚上|半夜)?(((\\d{1,2})|([零一二三四五六七八九十]{1,3}))[时点\\-/:])(((\\d{1,2})|([零一二三四五六七八九十半]{1,3}))[分\\-/:]?)?(?<!\\d{2}[\\s\\-:]?)(?=\\D)";
    // time B-F
    private final static String datePatternB = "[本这上下][个]?(周|礼拜|星期)[一二三四五六日天\\d]";
    private final static String datePatternC = "[这上下本][个]?月[初中末]?([零一二三四五六七八九十]{1,3}|\\d{1,2})[日号]";
    private final static String datePatternD = "(?<=[\\D[^零一二三四五六七八九十]])[零一二三四五六七八九十]{1,3}([年月日天周]|礼拜|星期|小时)[以之][前后]";
    private final static String datePatternE =
            "((Jan(uary)?)|(Feb(ruary)?)|(Mar(ch)?)|(Apr(il)?)|(May)|(Jun(e)?)|(Jul(y)?)|(Aug(ust)?)|(Sep(tember)?)|(Oct(ober)?)|(Nov(ember)?)|(Dec(ember)))[\\.,](\\s*)[0-9]{1,2}((st)|(nd)|(rd)|(th))?([,]*[\\s*][\\d]+)?";
//    private final static String datePatternF =
//            "((last)|(next)|(this))?(\\s)*((Mon(day)?)|(Tues(day)?)|(Wed(nesday)?)|(Thur(day)?)|(Fri(day)?)|(Sat(day)?)|(Sun(day)))";

    // email
    private final static String emailPattern =
            "[_a-zA-Z0-9-]+(\\.[_a-zA-Z0-9-]+)*@[a-zA-Z0-9-]+(\\.[a-zA-Z0-9-]+)*\\.(([0-9]{1,3})|([a-zA-Z]{2,3})|(aero|coop|info|museum|name))";


    // postal code: 551704
    private final static String postalCodePattern   = "(?<=\\D)\\d{6}(?=\\D)";

    // identify number
    private final static String identifyNumberPatternA = "(?<!\\d)(?:[1-9]\\d{7}((0\\d)|(1[0-2]))(([0|1|2]\\d)|3[0-1])\\d{3})(?!\\d)";//身份证正则表达式(15位)
    private final static String identifyNumberPatternB = "(?<!\\d)(?:[1-9]\\d{5}[1-9]\\d{3}((0\\d)|(1[0-2]))(([0|1|2]\\d)|3[0-1])\\d{4})(?!\\d)";//18位身份证号码

    // bill group number:  all bill number,like (DFSU6239125,HJCU1359873)
    private final static String bgNumberPatter = "(?<!\\d)(?:[A-Za-z]{3}(?i)u(?-i)[0-9]{7}?)(?!\\d)";
    // bill group name: 提单,bill,contract...
    private final static String bgNamePattern = "(?<!\\d)(?:(报关单|进口|出口|发票|發票|(?i)invoice)(?-i)|合同|(?i)contract(?-i)|提單|提单|(?i)bill(?-i)|裝箱單|装箱单|(?i)packing list(?-i)|通過單|通关单|委託書|委托书?)";

    private final static String urlPattern =
            "(http|ftp|https):\\/\\/[\\w\\-_]+(\\.[\\w\\-_]+)+([\\w\\-\\.,@?^=%&amp;:/~\\+#]*[\\w\\-\\@?^=%&amp;/~\\+#])?";

    // location
    private final static String locationPatternA   =
            "(?<=[在到去来])[\\w\u4e00-\u9fa5]{2,4}(路|街|(大道)|(小区)|(宾馆))([零一二三四五六七八九十\\d]{1,3}号)?";
    private final static String locationPatternB  = "(?<=[在来到去])([\\w\u4e00-\u9fa5]{2,4}(州|省|市|县|乡|村|(自治区)))+";
    private final static String locationPatternC  = "(?<=[在来到去])([^\\s在来到去\\pP\\pS]){3,20}(?=(见|碰头|会面|里))";

    // organization
    private final static String organizationPattern  =
            "(?<=[在来到去])([^\\s在来到去\\pP\\pS]){2,8}(公司|银行|处|部|局|厅|组织|会议|机构|院|办)(?=(工作|学习|上班)?)";

    // 柜号 集装箱 柜号
    private final static String transportPattern ="(?<!\\d)(?:(货柜|集装箱|柜号)?)";

    // == tools
    private final RegexRecognition regexRecognition;

    public CombineRecognition() {
        this.regexRecognition = new RegexRecognition();
        initialRegexTool();
    }

    public void initialRegexTool(){
        regexRecognition.addRegex(phoneNumberPattern,Entity.PhoneNumber);
        regexRecognition.addRegex(mobilePhoneNumberPattern,Entity.MobilePhoneNumber);

        regexRecognition.addRegex(carNumberPattern,Entity.CarNumber);

        regexRecognition.addRegex(datePatternA1,Entity.DateTime);
        regexRecognition.addRegex(datePatternA2,Entity.DateTime);
        regexRecognition.addRegex(datePatternA3,Entity.DateTime);
        regexRecognition.addRegex(datePatternA4,Entity.DateTime);
        regexRecognition.addRegex(datePatternA5,Entity.DateTime);
        regexRecognition.addRegex(datePatternB,Entity.DateTime);
        regexRecognition.addRegex(datePatternC,Entity.DateTime);
        regexRecognition.addRegex(datePatternD,Entity.DateTime);
        regexRecognition.addRegex(datePatternE,Entity.DateTime);
        // This regex will have blank, so we cancel it.
//        regexRecognition.addRegex(datePatternF,Entity.DateTime);

        regexRecognition.addRegex(emailPattern,Entity.Email);

        regexRecognition.addRegex(postalCodePattern,Entity.PostalCode);

        regexRecognition.addRegex(identifyNumberPatternA,Entity.IdentifyNumber);
        regexRecognition.addRegex(identifyNumberPatternB,Entity.IdentifyNumber);

        regexRecognition.addRegex(bgNumberPatter,Entity.BGNumber);
        regexRecognition.addRegex(bgNamePattern,Entity.BGName);

        regexRecognition.addRegex(urlPattern,Entity.URL);

        regexRecognition.addRegex(locationPatternA,Entity.Location);
        regexRecognition.addRegex(locationPatternB,Entity.Location);
        regexRecognition.addRegex(locationPatternC,Entity.Location);

        regexRecognition.addRegex(organizationPattern,Entity.Organization);

        regexRecognition.addRegex(transportPattern, Entity.TransportName);

    }

    // 处理小模块
    public HashMap<String, Entity> handler(String text){
        HashMap<String, Entity> result  = new HashMap<>();
        try{
            // used regex tool
            result = regexRecognition.ner(text);
            // used hanLP tool
            Recognition han = HanLPRecognition.getInstance();
            HashMap<String, Entity> hResult = han.ner(text);
            for (Map.Entry<String, Entity> entry : hResult.entrySet()) {
                result.put(entry.getKey(),entry.getValue());
            }
        }catch (Throwable e){
            log.error("Recognition error: " + e.getMessage());
        }

        return result;
    }


    @Override
    public HashMap<String, Entity> ner(String text){
        log.info("内容识别...");
        HashMap<String, Entity> result  = new HashMap<>();
        if(text == null){
            return result;
        }
        log.info("替换所有空格...");
        text = text.replaceAll(" ", "");
        if(text.isEmpty()){
            return result;
        }

        try{
            //分段提取要素，避免文件太大时，内存占用过大导致程序崩溃
            int len = 3 * 1024 * 1024;
            if(text.length() > len) {
                String str = text.substring(0, len);
                result.putAll(handler(str));
                while((text = text.substring(len)).length()>= len) {

                    str = text.substring(0,len);
                    result.putAll(handler(str));
                }
            }else {
                result = handler(text);
            }

        }catch (Throwable e){
            log.error("Recognition error: " + e.getMessage());
        }

        return result;
    }

    // used stanford tool
//            if(isEnableEnglish){
//                Recognition stanford = StanfordNLPRecognition.getInstance();
//                HashMap<String, Entity> sResult = stanford.ner(text);
//                for (Map.Entry<String, Entity> entry : sResult.entrySet()) {
//                    result.put(entry.getKey(),entry.getValue());
//                }
//            }


    public static void main(String[] args) {
        String  text =
                "AAAU2222222 522401199401025931我的车牌TTK2ASW当前集装箱的状态是走我有两箱货柜我的报关单号：ealu3950369两地车牌粤ZF023港我的车牌粤AR5678刘晓的身份证号码是441422199005061616,银行卡：6222083602015838599是在一九八一年一月二十三日早上5点在湖南长沙出生，我的电话：0753-5610856而我是在1980-01-23晚上10点在河南安阳出生的。三天以后的时候就来安全局看你。I was bord on Jan. 23th, 1980. 我的电话号码是15829622540。";
        Recognition extract = new CombineRecognition();
        System.out.println(extract.ner(text));
    }
}
