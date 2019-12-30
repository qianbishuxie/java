package com.itheima.lucene;

import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.Term;
import org.apache.lucene.store.FSDirectory;
import org.junit.Before;
import org.junit.Test;
import org.wltea.analyzer.lucene.IKAnalyzer;

import java.io.File;

public class IndexManager {

    private IndexWriter indexWriter;
    @Before
    public void init() throws Exception{
        indexWriter = new IndexWriter(
                FSDirectory.open(new File("C:\\temp_lucene\\index").toPath()),
                new IndexWriterConfig(new IKAnalyzer())
        );
    }

    @Test
    public void delDocumentByQuery() throws  Exception{
        indexWriter.deleteDocuments(new Term("name","apache"));
        indexWriter.close();
    }

    // 修改索引库
    @Test
    public void updateDocument() throws Exception {
        //创建一个新的文档对象
        Document document = new Document();
        //向文档对象中添加域
        document.add(new TextField("name", "更新之后的文档", Field.Store.YES));
        document.add(new TextField("name1", "更新之后的文档2", Field.Store.YES));
        document.add(new TextField("name2", "更新之后的文档3", Field.Store.YES));
        //更新操作
        indexWriter.updateDocument(new Term("name", "spring"), document);
        //关闭索引库
        indexWriter.close();
    }
}
