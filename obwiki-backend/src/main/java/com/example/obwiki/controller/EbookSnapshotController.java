package com.example.obwiki.controller;

import com.example.obwiki.resp.CommonResp;
import com.example.obwiki.resp.StatisticResp;
import com.example.obwiki.service.IEbookSnapshotService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author cr
 * @since 2025-06-23
 */
@RestController
@RequestMapping("/obwiki/ebookSnapshot")
public class EbookSnapshotController {
    @Resource
    private IEbookSnapshotService ebookSnapshotService;

    @GetMapping("/get30Statistic")
    public CommonResp get30Statistic() {
        List<StatisticResp> statisticResp = ebookSnapshotService.get30Statistic();
        CommonResp<List<StatisticResp>> commonResp = new CommonResp<>();
        commonResp.setContent(statisticResp);
        return commonResp;
    }

    @GetMapping("/getStatistic")
    public CommonResp getStatistic() {
        List<StatisticResp> statisticResp = ebookSnapshotService.getStatistic();
        CommonResp<List<StatisticResp>> commonResp = new CommonResp<>();
        commonResp.setContent(statisticResp);
        System.out.println("commonResp = " + commonResp);
        return commonResp;
    }

}
