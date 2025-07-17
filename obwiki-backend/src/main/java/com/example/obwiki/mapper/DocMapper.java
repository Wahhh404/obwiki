package com.example.obwiki.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.obwiki.entity.Doc;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author cr
 * @since 2025-06-23
 */
public interface DocMapper extends BaseMapper<Doc> {

    void increaseViewCount(Long id);

    void increaseVoteCount(Long id);

    void updateEbookInfo();
}
