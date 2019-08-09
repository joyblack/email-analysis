package com.sunrun.emailanalysis;

import org.htmlparser.beans.StringBean;
import org.htmlparser.util.ParserException;
import org.junit.Test;

public class HtmlParserTest {
    @Test
    /**
     * 根据提供的URL，获取此URL对应网页的纯文本信息
     * @param url 提供的URL链接
     * @return RL对应网页的纯文本信息
     * @throws ParserException
     */
    public void getText()throws ParserException {
        String url = "C:/Users/10160/Desktop/%E5%B7%A5%E4%BD%9C%E6%96%87%E4%BB%B6/DFS/%E6%96%B0%E4%BA%91%E7%9B%98%E5%BC%80%E5%8F%91/%E6%8F%92%E4%BB%B6/zTree_v3-master/demo/cn/core/click.html";
        StringBean sb = new StringBean();
        sb.setURL(url);
        // not include page links.
        sb.setLinks(false);
        // replace space.
        sb.setReplaceNonBreakingSpaces(true);
        // 设置将一序列空格由一个单一空格所代替
        sb.setCollapse(true);


        //返回解析后的网页纯文本信息
        System.out.println(sb.getStrings());
    }
}
