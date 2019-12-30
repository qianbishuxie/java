package com.itheima.lucene;


import org.apache.commons.io.FileUtils;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.apache.lucene.document.*;
import org.apache.lucene.index.*;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.junit.Test;
import org.wltea.analyzer.lucene.IKAnalyzer;

import java.io.File;

public class Lucene01 {

    // 创建索引库
    @Test
    public  void createIndex() throws Exception{
        // 1.创建一个Director对象,指定索引库保存的位置
        // 把索引库保存到内存中
//        RAMDirectory directory = new RAMDirectory();
        // 把索引库保存到磁盘上
        FSDirectory directory = FSDirectory.open(new File("C:\\temp_lucene\\index").toPath());
        // 2.基于Directory对象创建一个IndexWriter对象
        IndexWriter indexWriter = new IndexWriter(directory, new IndexWriterConfig());

        // 3.读取磁盘上的文件，对应每个文件创建一个文档对象
        File dir = new File("D:\\课程\\JavaEE 57期视频教程2019\\讲义+笔记+资料+软件\\流行框架\\61.会员版(2.0)-就业课(2.0)-Lucene\\lucene\\02.参考资料\\searchsource");
        // 4.向文档对象中添加域
        File[] files = dir.listFiles();
        for (File file : files) {
            // 取文件名
            String fileName = file.getName();
            // 文件路径
            String filePath = file.getPath();
            // 文件的内容
            String fileToString = FileUtils.readFileToString(file, "utf-8");
            // 文件的大小
            long size = FileUtils.sizeOf(file);
            // 创建Filed
            // 参数1.域的名称 2.域的内容 3. 是否存储
            Field fieldName = new TextField("name", fileName,Field.Store.YES);
            Field fieldPath = new TextField("path", filePath,Field.Store.YES);
            Field fieldContext = new TextField("context", fileToString,Field.Store.YES);
            Field fieldSize = new TextField("size",size+"",Field.Store.YES);
            // 创建文档对象
            Document document = new Document();
            document.add(fieldName);
            document.add(fieldPath);
            document.add(fieldContext);
            document.add(fieldSize);
            // 5.把文档对象写入索引库
            indexWriter.addDocument(document);
        }
        // 6.关闭IndexWriter对象
        indexWriter.close();
    }

    // 查询索引库
    @Test
    public void searchIndex() throws Exception{
        // 1.创建一个Directory对象，指定索引库的位置
        Directory directory = FSDirectory.open(new File("C:\\temp_lucene\\index").toPath());

        // 2.创建一个IndexReader对象
        IndexReader indexReader = DirectoryReader.open(directory);
        // 3.创建一个IndexSearch对象，构造方法中的参数indexReader对象
        IndexSearcher indexSearcher = new IndexSearcher(indexReader);
        // 4.创建一个Query对象，TermQuery
        TermQuery query = new TermQuery(new Term("context", "spring"));
        // 5.执行查询，得到一个TopDocs对象
        // 参数1.查询对象 2.查询结果返回的最大记录数
        TopDocs topDocs = indexSearcher.search(query, 10);
        // 6.取查询结果的总记录数
        System.out.println("查询总记录数:"+topDocs.totalHits);
        // 7.取文档列表
        ScoreDoc[] scoreDocs = topDocs.scoreDocs;
        // 8.打印文档中的内容
        for (ScoreDoc scoreDoc : scoreDocs) {
            // 取文档id
            int docId = scoreDoc.doc;
            // 根据id取文档对象
            Document document = indexSearcher.doc(docId);
            System.out.println(document.get("name"));
            System.out.println(document.get("path"));
            System.out.println(document.get("size"));
//            System.out.println(document.get("content"));
            System.out.println("---------分割线-------------");
        }
        // 9.关闭IndexReader对象
        indexReader.close();
    }

    // 分析器
    @Test
    public void testTokenStream() throws Exception{
        // 1.创建一个Analyzer对象，StandardAnalyzer对象
//        Analyzer analyzer = new StandardAnalyzer();// 标准分析器
        Analyzer analyzer = new IKAnalyzer();// 标准分析器

        // 2.使用分析器对象的tokenStream方法获得一个TokenStream对象
        TokenStream tokenStream = analyzer.tokenStream("", "2017年12月14日 - 传智播客Lucene概述公安局Lucene是一款高性能的、可扩展的信息检索(IR)工具库。信息检索是指文档搜索、文档内信息搜索或者文档相关的元数据搜索等操作。");
        // 3.向TokenStream对象中设置一个引用，相当于数一个指针
        CharTermAttribute charTermAttribute = tokenStream.addAttribute(CharTermAttribute.class);
        // 4.调用TokenStream对象的reset方法，如果不调用抛异常
        tokenStream.reset();
        // 5.使用while循环遍历TokenStream对象
        while (tokenStream.incrementToken()){
            System.out.println(charTermAttribute.toString());
        }
        // 6.关闭TokenStream对象
        tokenStream.close();
    }

    // 添加索引
    @Test
    public void addDocument() throws Exception{
        // 1.索引库存放路径
        FSDirectory directory = FSDirectory.open(new File("C:\\temp_lucene\\index").toPath());
        IndexWriterConfig config = new IndexWriterConfig(new IKAnalyzer());
        // 2.创建一个indexWriter对象
        IndexWriter indexWriter = new IndexWriter(directory, config);
        // 3.读取磁盘上的文件，对应每个文件创建一个文档对象
        File dir = new File("D:\\课程\\JavaEE 57期视频教程2019\\讲义+笔记+资料+软件\\流行框架\\61.会员版(2.0)-就业课(2.0)-Lucene\\lucene\\02.参考资料\\searchsource");
        // 4.向文档对象中添加域
        File[] files = dir.listFiles();
        for (File file : files) {
            // 取文件名
            String fileName = file.getName();
            // 文件路径
            String filePath = file.getPath();
            // 文件的内容
            String fileToString = FileUtils.readFileToString(file, "utf-8");
            // 文件的大小
            long size = FileUtils.sizeOf(file);
            // 3.创建一个Document对象
            Document document = new Document();
            // 4.向document对象中添加域
            //不同的document可以有不同的域，同一个document可以有相同的域。
            document.add(new TextField("filename", fileName, Field.Store.YES));
            document.add(new TextField("context", fileToString, Field.Store.YES));
            // 5.创建索引
            document.add(new LongPoint("size", size));
            // 6.存储数据
            document.add(new StoredField("size", size));
            // 7.不需要创建索引的就使用storefield存储
            document.add(new StoredField("path", filePath));
            // 8.添加文档到索引库
            indexWriter.addDocument(document);
        }

        // 9.关闭indexWriter
        indexWriter.close();
    }
}
