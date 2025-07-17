package com.example.obwiki.rep;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class UserQueryReq extends PageReq {
    //主键
    private Long id;
    //登录名
    private String loginName;
    //昵称
    private String  name;
}