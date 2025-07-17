package com.example.obwiki.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.obwiki.entity.Doc;
import com.example.obwiki.rep.DocQueryReq;
import com.example.obwiki.rep.DocSaveReq;
import com.example.obwiki.resp.DocQueryResp;
import com.example.obwiki.resp.PageResp;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author cr
 * @since 2025-06-23
 */
public interface IDocService extends IService<Doc> {

    List<DocQueryResp> allbyEbookId(Long ebookId);

    void updateEbookInfo();

    void vote(Long id);

    PageResp<DocQueryResp> listByname(DocQueryReq req);

    void save(DocSaveReq req);

    void delete(Long id);

    List<DocQueryResp> all();

    void delete(List<Long> ids);
}
