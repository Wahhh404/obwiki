package com.example.obwiki.controller;

import com.example.obwiki.entity.Ebook;
import com.example.obwiki.rep.EbookQueryReq;
import com.example.obwiki.rep.EbookSaveReq;
import com.example.obwiki.resp.CommonResp;
import com.example.obwiki.resp.EbookQueryResp;
import com.example.obwiki.resp.PageResp;
import com.example.obwiki.service.IEbookService;
import com.example.obwiki.utils.CopyUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@Api(tags = "电子书接口")
@RestController
@RequestMapping("/obwiki/ebook")
public class EbookController {

    @Autowired
    private IEbookService ebookService;

    @ApiOperation("分页查询电子书")
    @GetMapping("/list")
    public CommonResp<PageResp<EbookQueryResp>> list(@Valid EbookQueryReq req) {
        CommonResp<PageResp<EbookQueryResp>> resp = new CommonResp<>(true, "查询成功", null);
        PageResp<EbookQueryResp> pageResp = ebookService.listByname(req);
        resp.setContent(pageResp);
        return resp;
    }

    @ApiOperation("查询全部电子书")
    @GetMapping("/all")
    public CommonResp<List<EbookQueryResp>> listAll() {
        List<Ebook> ebookList = ebookService.list();
        List<EbookQueryResp> resps = CopyUtil.copyList(ebookList, EbookQueryResp.class);
        return new CommonResp<>(true, "查询全部成功", resps);
    }

    @ApiOperation("新增或更新电子书")
    @ApiImplicitParam(name = "req", value = "电子书参数", required = true, dataType = "EbookSaveReq")
    @PostMapping("/save")
    public CommonResp save(@Valid @RequestBody EbookSaveReq req) {
        CommonResp resp = new CommonResp<>(true, "成功", null);
        ebookService.save(req);
        return resp;
    }

    @ApiOperation("根据 ID 删除电子书")
    @ApiImplicitParam(name = "id", value = "电子书 ID", required = true, dataType = "Long", paramType = "path")
    @DeleteMapping("/delete/{id}")
    public CommonResp delete(@PathVariable Long id) {
        CommonResp resp = new CommonResp<>(true,"删除成功",null);
        ebookService.delete(id);
        return resp;
    }
}

