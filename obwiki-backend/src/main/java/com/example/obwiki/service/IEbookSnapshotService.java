package com.example.obwiki.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.obwiki.entity.EbookSnapshot;
import com.example.obwiki.resp.StatisticResp;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author cr
 * @since 2025-06-23
 */
public interface IEbookSnapshotService extends IService<EbookSnapshot> {

    void genSnapshot();

    List<StatisticResp> getStatistic();

    List<StatisticResp> get30Statistic();
}
