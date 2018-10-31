package com.zhaozhiguang.item.elasticsearch.elasticsearch;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.*;

import java.util.Date;

@Data
@Document(indexName = "user", type = "user")
@Mapping
public class User {

    @Id
    private String id;

    @Field(type = FieldType.Keyword)
    private String name;

    @Field(type = FieldType.Text, fielddata = true)
    private String desc;

    @Field(type = FieldType.Keyword, index = false, store = true)
    private String sex;

    @Field(type = FieldType.Date, format = DateFormat.basic_date_time, index = false)
    private Date birthday;

}
