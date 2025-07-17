package com.example.obwiki.controller;

import com.example.obwiki.rep.DocQueryReq;
import com.example.obwiki.rep.DocSaveReq;
import com.example.obwiki.resp.CommonResp;
import com.example.obwiki.resp.DocQueryResp;
import com.example.obwiki.resp.PageResp;
import com.example.obwiki.service.IDocService;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author cr
 * @since 2025-06-23
 */
@RestController
@RequestMapping("/obwiki/doc")
@Api(value = "文档接口", tags = {"文档相关接口"})
public class DocController {

    @Autowired
    private IDocService docService;

    @ApiOperation(value = "根据电子书ID获取全部文档", notes = "根据电子书ID查询该电子书下的所有文档列表")
    @ApiImplicitParam(name = "ebookId", value = "电子书ID", required = true, dataType = "long", paramType = "path")
    @GetMapping("/all/{ebookId}")
    public CommonResp<List<DocQueryResp>> allByEbookId(@PathVariable Long ebookId) {
        CommonResp<List<DocQueryResp>> resp = new CommonResp<>();
        List<DocQueryResp> list = docService.allbyEbookId(ebookId);
        resp.setContent(list);
        return resp;
    }

    @GetMapping("/vote/{id}")
    public CommonResp vote(@PathVariable Long id) {
        CommonResp commonResp = new CommonResp();
        docService.vote(id);
        return commonResp;
    }

    @ApiOperation(value = "获取全部文档", notes = "查询所有文档列表")
    @GetMapping("/all")
    public CommonResp<List<DocQueryResp>> all() {
        CommonResp<List<DocQueryResp>> resp = new CommonResp<>(true,"查询成功",null);

        List<DocQueryResp> list = docService.all();
        resp.setContent(list);
        return resp;
    }

    @ApiOperation(value = "分页查询文档", notes = "根据查询条件分页查询文档列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "name", value = "文档名称", required = false, dataType = "string", paramType = "query"),
            @ApiImplicitParam(name = "page", value = "页码", required = true, dataType = "int", paramType = "query"),
            @ApiImplicitParam(name = "size", value = "每页大小", required = true, dataType = "int", paramType = "query")
    })
    @GetMapping("/list")
    public CommonResp<PageResp<DocQueryResp>> list(@Valid DocQueryReq req) {
        CommonResp<PageResp<DocQueryResp>> resp = new CommonResp<>(true,"查询成功",null);

        PageResp<DocQueryResp> pageResp = docService.listByname(req);
        resp.setContent(pageResp);

        return resp;
    }

    @ApiOperation(value = "保存文档", notes = "新增或更新文档信息")
    @ApiResponses({
            @ApiResponse(code = 200, message = "操作成功"),
            @ApiResponse(code = 400, message = "参数校验失败")
    })
    @PostMapping("/save")
    public CommonResp save(@Valid @RequestBody DocSaveReq req) {
        CommonResp resp = new CommonResp<>(true,"成功",null);
        docService.save(req);
        return resp;
    }

    @ApiOperation(value = "删除文档", notes = "根据ID删除单个文档")
    @ApiImplicitParam(name = "id", value = "文档ID", required = true, dataType = "long", paramType = "path")
    @DeleteMapping("/delete/{id}")
    public CommonResp delete(@PathVariable Long id) {
        CommonResp resp = new CommonResp<>(true,"删除成功",null);
        docService.delete(id);
        return resp;
    }

    @ApiOperation(value = "批量删除文档", notes = "根据文档ID列表批量删除文档")
    @ApiImplicitParam(name = "ids", value = "文档ID列表", required = true, dataType = "List<Long>", paramType = "query")
    @GetMapping("/remove")
    public CommonResp delete(@RequestParam("ids") List<Long> ids) {
        CommonResp resp = new CommonResp<>(true,"删除成功",null);

        docService.delete(ids);
        return resp;
    }
}
