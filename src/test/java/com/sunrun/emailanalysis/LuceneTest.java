package com.sunrun.emailanalysis;

import com.hankcs.hanlp.HanLP;
import com.sunrun.emailanalysis.data.request.common.Pagination;
import com.sunrun.emailanalysis.data.request.email.SearchEmailData;
import com.sunrun.emailanalysis.dictionary.common.JudgeDictionary;
import com.sunrun.emailanalysis.dictionary.common.ResultDictionary;
import com.sunrun.emailanalysis.ea.recognition.dictionary.EmailIndexDictionary;
import com.sunrun.emailanalysis.joy.exception.EAException;
import com.sunrun.emailanalysis.joy.result.Notice;
import com.sunrun.emailanalysis.po.EmailCase;
import org.apache.commons.io.FileUtils;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.cn.smart.SmartChineseAnalyzer;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.apache.lucene.document.*;
import org.apache.lucene.index.*;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.*;
import org.apache.lucene.search.highlight.*;
import org.apache.lucene.store.FSDirectory;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ucar.ma2.Index;

import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class LuceneTest {
    private static Logger log = LoggerFactory.getLogger(LuceneTest.class);
    // 在索引写入的时候是否考虑存放一个字段表明该条索引是来源email还是attach
    private HashMap<String, Object> contentDoc2EmailInfo(Document doc){
        HashMap<String, Object> result = new HashMap<>();
        result.put(EmailIndexDictionary.EMAIL_ID, doc.get(EmailIndexDictionary.EMAIL_ID));
        result.put(EmailIndexDictionary.SUBJECT, doc.get(EmailIndexDictionary.SUBJECT));
        result.put(EmailIndexDictionary.CASE_ID, doc.get(EmailIndexDictionary.CASE_ID));
        result.put(EmailIndexDictionary.FROM_NAME, doc.get(EmailIndexDictionary.FROM_NAME));
        result.put(EmailIndexDictionary.FROM_ADDRESS, doc.get(EmailIndexDictionary.FROM_ADDRESS));
        result.put(EmailIndexDictionary.TO_NAME, doc.get(EmailIndexDictionary.TO_NAME));
        result.put(EmailIndexDictionary.TO_ADDRESS, doc.get(EmailIndexDictionary.TO_ADDRESS));
        result.put(EmailIndexDictionary.CREATE_TIME, doc.get(EmailIndexDictionary.CREATE_TIME));
        result.put(EmailIndexDictionary.SEND_TIME, doc.get(EmailIndexDictionary.SEND_TIME));
        result.put(EmailIndexDictionary.OLD_FILE_NAME, doc.get(EmailIndexDictionary.OLD_FILE_NAME));
        result.put(EmailIndexDictionary.NEW_FILE_NAME, doc.get(EmailIndexDictionary.NEW_FILE_NAME));
        //result.put(EmailIndexDictionary.HTML_TEXT_CONTENT, doc.get(EmailIndexDictionary.HTML_TEXT_CONTENT));
        //result.put(EmailIndexDictionary.TEXT_CONTENT, doc.get(EmailIndexDictionary.TEXT_CONTENT));
        //result.put(EmailIndexDictionary.ATTACH_CONTENT, doc.get(EmailIndexDictionary.ATTACH_CONTENT));
        //result.put(EmailIndexDictionary.ATTACH_ID, doc.get(EmailIndexDictionary.ATTACH_ID));
        return result;
    }

    // 查询 - 邮件索引
    @Test
    public void search3() throws IOException {
        SearchEmailData input = new SearchEmailData();
        Pagination pagination = new Pagination();
        pagination.setPageIndex(0);
        pagination.setPageSize(3);
        pagination.setShift(0);
        pagination.setSort(null);
        pagination.setOrder(null);
        input.setPagination(pagination);
        input.setFrom(null);
        input.setTo(null);
        input.setContent("这是三条是欧洲破碎 ，8号尾期，到时会交单到翁小姐");
        input.setCaseId(570658832556642304L);
        input.setIsExact(0);

        HashMap<String, Object> result = new HashMap<>();
        List<HashMap<String, Object>> data = new ArrayList<>();

        log.info("Full text search...");
        String content = input.getContent();
        log.info("Search content: {}", content);
        try {
            // 打开索引库：如果没提供 caseId 则检索当前的所有库
            List<EmailCase> emailCases = new ArrayList<>();
            IndexReader[] indexReaders = new IndexReader[1];
            log.info("共联合了{}个索引库", indexReaders.length);
            for (int i = 0; i < 1; i++) {
                indexReaders[i] = DirectoryReader.open(FSDirectory.open(Paths.get(new File("D:\\email3\\570658832556642304\\index").getPath())));
            }
            MultiReader multiReader = new MultiReader(indexReaders);
            // 创建Searcher.
            IndexSearcher indexSearcher = new IndexSearcher(multiReader);
            SmartChineseAnalyzer analyzer = new SmartChineseAnalyzer();
            // 精确和非精确（对输入关键字进行分词）查询
            BooleanQuery.Builder builder = new BooleanQuery.Builder();
            if (input.getIsExact().equals(JudgeDictionary.YES)) {
                if (input.getSubject() != null) {
                    builder.add(new TermQuery(new Term(EmailIndexDictionary.SUBJECT, content)), BooleanClause.Occur.SHOULD);
                }
                if (input.getContent() != null) {
                    TermQuery termQuery = new TermQuery(new Term(EmailIndexDictionary.ATTACH_CONTENT, content));
                    builder.add(termQuery, BooleanClause.Occur.SHOULD);
                    log.info("attachContentQuery is : {}", termQuery);
                    builder.add(new TermQuery(new Term(EmailIndexDictionary.TEXT_CONTENT, content)), BooleanClause.Occur.SHOULD);
                }
            } else {
                try {
                    if (input.getSubject() != null) {
                        Query parse = new QueryParser(EmailIndexDictionary.SUBJECT, analyzer).parse(content);
                        builder.add(parse, BooleanClause.Occur.SHOULD);
                    }
                    if (input.getContent() != null) {
                        Query parse = new QueryParser(EmailIndexDictionary.ATTACH_CONTENT, analyzer).parse("\""+content + "\"");
                        builder.add(parse, BooleanClause.Occur.SHOULD);
                        log.info("attachContentParser is : {}", parse);
                        builder.add(new QueryParser(EmailIndexDictionary.TEXT_CONTENT, analyzer).parse("\""+content + "\""), BooleanClause.Occur.SHOULD);

                    }
                } catch (ParseException e) {
                    e.printStackTrace();
                    throw new EAException("创建查询对象时发生错误.", Notice.EXECUTE_IS_FAILED);
                }
            }
            // 起始记录ID
            int start = input.getPagination().getPageIndex() * input.getPagination().getPageSize();

            int num = (input.getPagination().getPageIndex() + 1) * input.getPagination().getPageSize();

            // CollectionStatistics
            BooleanQuery query = builder.build();
            TopDocs topDocs = indexSearcher.search(query, num);
            // total
            long totalHits = topDocs.totalHits;
            log.info("本次查询共搜索到邮件数据: {}条.", totalHits);
            long end = Math.min(num, totalHits);
            log.info("预备获取编号{} - {} 的数据.", start + 1, end);
            ScoreDoc[] scoreDocs = topDocs.scoreDocs;

            QueryScorer queryScorer = new QueryScorer(query);
            Fragmenter fragmenter = new SimpleSpanFragmenter(queryScorer);
            //设置标签内部关键字的颜色
            //第一个参数：标签的前半部分；第二个参数：标签的后半部分。
            SimpleHTMLFormatter simpleHTMLFormatter = new SimpleHTMLFormatter("<b><font color='red'>","</font></b>");
            //第一个参数是对查到的结果进行实例化；第二个是片段得分（显示得分高的片段，即摘要）
            Highlighter highlighter=new Highlighter(simpleHTMLFormatter, queryScorer);


            //设置片段
            highlighter.setTextFragmenter(fragmenter);
            for (int i = start; i < end; i++) {
                System.out.println("=================================================");
                System.out.println("第" + (i+1) +"条数据：");
                System.out.println("<br />");
                System.out.println("<br />");

                Document doc = indexSearcher.doc(scoreDocs[i].doc);
                System.out.println("邮件的ID：" + doc.get("emailId"));
                System.out.println("<br />");
                System.out.println("<br />");


                String htmlTextContent = doc.get("htmlTextContent");
                if(htmlTextContent != null){
                    TokenStream tokenStream= analyzer.tokenStream("htmlTextContent", new StringReader(htmlTextContent));
                    //获取最高的片段
                    System.out.println("htmlTextContent：" + highlighter.getBestFragment(tokenStream, htmlTextContent));
                    System.out.println("<br />");
                    System.out.println("<br />");
                }

                String textContent = doc.get("textContent");
                if(textContent != null){
                    TokenStream tokenStream= analyzer.tokenStream("textContent", new StringReader(textContent));
                    //获取最高的片段
                    System.out.println("textContent：" + highlighter.getBestFragment(tokenStream, textContent));
                    System.out.println("<br />");
                    System.out.println("<br />");
                }

                String attachContent = doc.get("attachContent");
                if(attachContent != null){
                    TokenStream tokenStream = analyzer.tokenStream("attachContent", new StringReader(attachContent));
                    //获取最高的片段
                    System.out.println("attachContent：" + highlighter.getBestFragment(tokenStream, attachContent));
                    System.out.println("<br />");
                    System.out.println("<br />");
                }
                System.out.println("Score信息: " +scoreDocs[i]);
                System.out.println("得分: " +scoreDocs[i].score );

                data.add(contentDoc2EmailInfo(doc));
            }
            // 本次查询记录返回的条数
            result.put(ResultDictionary.TOTAL, totalHits); // 总条数
            result.put(ResultDictionary.COUNT, data.size()); //本次查询返回条数
            result.put(ResultDictionary.DATA, data); // 数据
            // 关闭reader
            multiReader.close();
        } catch (Exception e) {
            log.error(e.getMessage());
            result.put(ResultDictionary.TOTAL, 0); // 总条数
            result.put(ResultDictionary.COUNT, 0); //本次查询返回条数
            result.put(ResultDictionary.DATA, null); // 数据
            e.printStackTrace();
        }
        //System.out.println(result);
    }

    // 创建索引
    @Test
    public void test() throws IOException {
        // 指定索引存放的位置
        FSDirectory indexPath = FSDirectory.open(Paths.get("data/lucene/index"));
        System.out.println("索引存放目录为: " + indexPath.getDirectory().toString());

        // 创建分词器
        SmartChineseAnalyzer analyzer = new SmartChineseAnalyzer();
        // 创建参数分词器
        IndexWriterConfig indexWriterConfig = new IndexWriterConfig(analyzer);
        // 创建indexWrite对象（文件对象、索引配置对象）
        IndexWriter indexWriter = new IndexWriter(indexPath, indexWriterConfig);
        for (int i=0;i< 10; i++) {
            // 创建文件域名
            //域的名称 域的内容 是否存储

            Field fileNameField = new TextField("fileName", "fileName" + i, Field.Store.YES);
            Field fileContentField = new TextField("fileContent", "fileContent" + i, Field.Store.YES);
            Field filePathField = new TextField("filePath", "filePath" + i, Field.Store.YES);
            Field fileSizeField = new TextField("fileSize", "fileSize" + i, Field.Store.YES);
            //Field dateField = new TextField("sendTime", "2019-05-22 16:0" + i, Field.Store.YES);
            Field dateField = new StringField("sendTime", "2019-05-22 16:0" + i, Field.Store.YES);

            // 创建Doc，并将文件域加入
            Document doc = new Document();
            doc.add(fileNameField);
            doc.add(fileContentField);
            doc.add(filePathField);
            doc.add(fileSizeField);
            doc.add(dateField);

            // 创建索引，并写入索引库
            indexWriter.addDocument(doc);

        }
        indexWriter.close();
    }

    // 查询
    @Test
    public void search() throws Exception {
        //指定索引库存放路径
        //E:\Lucene_index
        FSDirectory directory = FSDirectory.open(Paths.get("data/lucene/index"));
        //创建indexReader对象
        IndexReader indexReader = DirectoryReader.open(directory);
        //创建indexSearcher对象
        IndexSearcher indexSearcher = new IndexSearcher(indexReader);

        log.info("共有文档数：" + indexReader.maxDoc());
        //创建查询
        //Query query = new QueryParser("fileContent", new SmartChineseAnalyzer()).parse("\"" + "fileContent" + "\"");
        // 2019-05-22 16:09

        Query query = TermRangeQuery.newStringRange("sendTime", null, "2019-05-22 16:08", true, false);
        //执行查询
        //参数一  查询对象    参数二  查询结果返回的最大值
        TopDocs topDocs = indexSearcher.search(query, 10);
        System.out.println("查询结果的总数" + topDocs.totalHits);
        //遍历查询结果
        showDocs(indexSearcher, topDocs);
        indexReader.close();
    }

    public void showDocs(IndexSearcher indexSearcher,TopDocs topDocs) throws IOException {
        for (ScoreDoc scoreDoc: topDocs.scoreDocs){
            System.out.println("===========");
            //scoreDoc.doc 属性就是documnet对象的id
            Document doc = indexSearcher.doc(scoreDoc.doc);
            System.out.println(doc.getField("fileName"));
            System.out.println(doc.getField("fileContent"));
            System.out.println(doc.getField("filePath"));
            System.out.println(doc.getField("fileSize"));
            System.out.println(doc.getField("sendTime"));
        }
    }

    // 查询 - 邮件索引
    @Test
    public void search2() throws IOException {
        //指定索引库存放路径
        //E:\Lucene_index
        FSDirectory directory = FSDirectory.open(Paths.get("D:\\email3\\570658832556642304\\index"));
        //创建indexReader对象
        IndexReader indexReader = DirectoryReader.open(directory);
        //创建indexSearcher对象
        IndexSearcher indexSearcher = new IndexSearcher(indexReader);
        //创建查询
        Query query = new TermQuery(new Term("content", "2"));
        //执行查询
        //参数一  查询对象    参数二  查询结果返回的最大值
        TopDocs topDocs = indexSearcher.search(query, 10);
        System.out.println("查询结果的总数"+topDocs.totalHits);
        //遍历查询结果
        for (ScoreDoc scoreDoc: topDocs.scoreDocs){
            //scoreDoc.doc 属性就是doucumnet对象的id
            Document doc = indexSearcher.doc(scoreDoc.doc);
//            System.out.println(doc.getField("fileName"));
//            System.out.println(doc.getField("fileContent"));
//            System.out.println(doc.getField("filePath"));
//            System.out.println(doc.getField("fileSize"));

            System.out.println(doc);
        }
        indexReader.close();
    }

    @Test
    public void test3() throws IOException {
        // 指定索引存放的位置
        FSDirectory indexPath = FSDirectory.open(Paths.get("data/lucene/index"));
        System.out.println("索引存放目录为: " + indexPath.getDirectory().toString());

        // 创建分词器
        SmartChineseAnalyzer analyzer = new SmartChineseAnalyzer();
        // 创建参数分词器
        IndexWriterConfig indexWriterConfig = new IndexWriterConfig(analyzer);
        // 创建indexWrite对象（文件对象、索引配置对象）
        IndexWriter indexWriter = new IndexWriter(indexPath, indexWriterConfig);

        for (int i = 0; i< 10; i++) {

            // 创建Doc，并将文件域加入
            Document doc = new Document();
            doc.add(new TextField("fileName","filename1", Field.Store.YES));
            doc.add(new TextField("fileContent", "filename1", Field.Store.YES));
            indexWriter.addDocument(doc);

            // 创建Doc，并将文件域加入
            Document doc2 = new Document();
            doc2.add(new TextField("attachName","attachName1", Field.Store.YES));
            doc2.add(new TextField("attachContent1", "attachContent1", Field.Store.YES));
            indexWriter.addDocument(doc2);
        }

        indexWriter.close();
    }

    // 高亮显示
    public void highLight(Query query){
        QueryScorer scorer = new QueryScorer(query);
        Fragmenter fragmenter=new SimpleSpanFragmenter(scorer);
        //设置标签内部关键字的颜色
        //第一个参数：标签的前半部分；第二个参数：标签的后半部分。
        SimpleHTMLFormatter simpleHTMLFormatter = new SimpleHTMLFormatter("<b><font color='red'>","</font></b>");
        //第一个参数是对查到的结果进行实例化；第二个是片段得分（显示得分高的片段，即摘要）
        Highlighter highlighter=new Highlighter(simpleHTMLFormatter, scorer);
        //设置片段
        highlighter.setTextFragmenter(fragmenter);

    }

    @Test
    public void analysis(){
        SmartChineseAnalyzer analyzer = new SmartChineseAnalyzer();
        String content = "中华人民共和国很辽阔";
        TokenStream tokenStream = analyzer.tokenStream("content", content);
        try {
            long start = System.nanoTime();
            System.out.println("====== Smart分词器: ");
            doToken(tokenStream);
            long end = System.nanoTime();
            System.out.println("耗时：" + (end - start));
            System.out.println("====== HanLP分词器: ");
            List<com.hankcs.hanlp.seg.common.Term> segment = HanLP.segment(content);
            System.out.println(segment.toString());
            System.out.println("耗时：" + (System.nanoTime() - end));



        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private static void doToken(TokenStream ts) throws IOException {
        ts.reset();
        CharTermAttribute cta = ts.getAttribute(CharTermAttribute.class);
        while (ts.incrementToken()) {
            System.out.print(cta.toString() + "|");
        }
        System.out.println();
        ts.end();
        ts.close();
    }


}
