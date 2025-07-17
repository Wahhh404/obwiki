package com.example.obwiki.rep;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class CategorySaveReq {

    @TableId(type = IdType.AUTO)
    private Long id;

    //父id
    private Long parent;

    //名称
    private String name;

    //顺序
    private Integer sort;
}