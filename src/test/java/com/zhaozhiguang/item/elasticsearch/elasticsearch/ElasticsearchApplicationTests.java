package com.zhaozhiguang.item.elasticsearch.elasticsearch;

import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.index.query.*;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.Aggregations;
import org.elasticsearch.search.aggregations.bucket.terms.Terms;
import org.elasticsearch.search.aggregations.metrics.tophits.InternalTopHits;
import org.elasticsearch.search.sort.ScoreSortBuilder;
import org.elasticsearch.search.sort.SortBuilders;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.query.GetQuery;
import org.springframework.data.elasticsearch.core.query.IndexQuery;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.data.elasticsearch.core.query.SearchQuery;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ElasticsearchApplicationTests {

    @Autowired
    private ElasticsearchTemplate elasticsearchTemplate;

    @Test
    public void controller(){
        /*GetQuery getQuery = new GetQuery();
        getQuery.setId("123123");
        Object o = elasticsearchTemplate.queryForObject(getQuery, Object.class);*/
        //elasticsearchTemplate.createIndex(User.class);
        elasticsearchTemplate.putMapping(User.class);
    }

    @Test
    public void contextLoads() {
        User user = new User();
        user.setId("246");
        user.setName("田七");
        user.setDesc("田七王五加王五");
        user.setSex("男");
        //elasticsearchTemplate.putMapping(User.class, user);
        IndexQuery query = new IndexQuery();
        query.setObject(user);
        String index = elasticsearchTemplate.index(query);
        System.err.println(index);
    }

    @Test
    public void contextLoad() {
        CommonTermsQueryBuilder commonTermsQueryBuilder2 = QueryBuilders.commonTermsQuery("desc", "赵柳");
        CommonTermsQueryBuilder commonTermsQueryBuilder1 = QueryBuilders.commonTermsQuery("name", "王五");
        //BoostingQueryBuilder builder = QueryBuilders.boostingQuery(commonTermsQueryBuilder1, commonTermsQueryBuilder2);

        QueryStringQueryBuilder builder = QueryBuilders.queryStringQuery("王五");
        ScoreSortBuilder sortBuilder = SortBuilders.scoreSort();
        SearchQuery query = new NativeSearchQueryBuilder().withQuery(builder).withSort(sortBuilder).build();
        List<User> users = elasticsearchTemplate.queryForList(query, User.class);
        if(users!=null){
            users.forEach(user -> {
                System.err.println(user.toString());
            });
        }
    }

    @Test
    public void con(){
        SearchRequestBuilder requestBuilder = elasticsearchTemplate.getClient().prepareSearch("user").setTypes("user").setQuery(QueryBuilders.matchAllQuery());
        //聚合分析查询出现次数最多的10个词汇
        SearchResponse actionGet = requestBuilder.addAggregation(AggregationBuilders.topHits("desc")).execute().actionGet();
        //获取分析后的数据
        Aggregations aggregations = actionGet.getAggregations();
        InternalTopHits trem = aggregations.get("desc");
        SearchHits hits = trem.getHits();

    }


}
