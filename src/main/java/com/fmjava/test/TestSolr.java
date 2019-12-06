package com.fmjava.test;

import com.fmjava.pojo.Item;
import org.apache.solr.client.solrj.SolrQuery;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.solr.core.SolrTemplate;
import org.springframework.data.solr.core.query.Criteria;
import org.springframework.data.solr.core.query.SimpleQuery;
import org.springframework.data.solr.core.query.result.ScoredPage;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:applicationContext-solr.xml"})
public class TestSolr {
    @Autowired
    private SolrTemplate solrTemplate;
    @Test
    public void testIndexCreate(){
/*        Item item = new Item();
        item.setId(1L);
        item.setBarcode("barcode");
        item.setCategory("射手");
        item.setTitle("鲁班");
        item.setImage("zsefsf");
        item.setPrice(new BigDecimal(209));*/
        //添加多条
        ArrayList<Item> itemList = new ArrayList();
        int j = 0;
        for (Long i=1L;i<=100;i++){
            j+=1;
            Item item = new Item();
            item.setId(i);
            item.setBarcode("barcode"+i);
            item.setCategory("射手"+i);
            item.setTitle("鲁班"+j);
            item.setImage("zsefsf");
            item.setPrice(new BigDecimal(209));
            itemList.add(item);
        }
        //保存
        solrTemplate.saveBeans(itemList);
        //提交
        solrTemplate.commit();
    }
    //删除
    @Test
    public void testIndexDelete(){
        SimpleQuery simpleQuery = new SimpleQuery("*:*");
        solrTemplate.delete(simpleQuery);
        solrTemplate.commit();
    }
    //分页查询
    @Test
    public void testGetItem(){
        SimpleQuery simpleQuery = new SimpleQuery("*:*");
        //从第几条开始查
        simpleQuery.setOffset(10);
        //一次查多少记录
        simpleQuery.setRows(5);
        //查询返回结果
        ScoredPage<Item> items = solrTemplate.queryForPage(simpleQuery, Item.class);
        //总页数
        int totalPages = items.getTotalPages();
        System.out.println("总页数："+totalPages);
        //总记录数
        long totalElements = items.getTotalElements();
        System.out.println("总记录数："+totalElements);
        //本次查询的记录数
        int numberOfElements = items.getNumberOfElements();
        System.out.println("本次查询记录数："+numberOfElements);
        //本次查询的数据
        List<Item> content = items.getContent();
        for (Item item : content) {
            System.out.println(item.getTitle());
        }
    }
    //条件查询
    @Test
    public void testQueryItem(){
        //创建查询对象
        SimpleQuery simpleQuery = new SimpleQuery();
        //创建查询条件
        Criteria contains = new Criteria("item_title").contains("鲁班");
        //contains.or("item_title").contains("鲁班3");
        //查询对象中放入查询条件
        simpleQuery.addCriteria(contains);
        //simpleQuery.setOffset(1);
        //simpleQuery.setRows(20);
        ScoredPage<Item> items = solrTemplate.queryForPage(simpleQuery, Item.class);
        List<Item> itemList = items.getContent();
        //遍历itemList
        for (Item item : itemList) {
            System.out.println(item.getTitle());
        }
    }

}
