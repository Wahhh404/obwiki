package com.example.obwiki.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.obwiki.entity.Content;
import com.example.obwiki.mapper.ContentMapper;
import com.example.obwiki.mapper.DocMapper;
import com.example.obwiki.service.IContentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author cr
 * @since 2025-06-23
 */
@Service
public class ContentServiceImpl extends ServiceImpl<ContentMapper, Content> implements IContentService {
    @Autowired
    DocMapper docMapper;

    @Override
    public String findContent(Long id) {
        Content content = this.baseMapper.selectById(id);
        docMapper.increaseViewCount(id);
        if(content !=null){
            return content.getContent();
        }
        return null;
    }
}
