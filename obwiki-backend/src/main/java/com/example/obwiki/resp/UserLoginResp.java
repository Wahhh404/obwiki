package com.example.obwiki.resp;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class UserLoginResp {
    private Long id;

    private String loginName;

    private String name;

    private String token;
}