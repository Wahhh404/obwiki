package com.example.obwiki.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.obwiki.entity.EbookSnapshot;
import com.example.obwiki.resp.StatisticResp;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author cr
 * @since 2025-06-23
 */
public interface EbookSnapshotMapper extends BaseMapper<EbookSnapshot> {

    void genSnapshot();

    List<StatisticResp> getStatistic();

    List<StatisticResp> get30Statistic();
}
