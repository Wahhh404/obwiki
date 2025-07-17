package com.example.obwiki.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.obwiki.entity.EbookSnapshot;
import com.example.obwiki.mapper.EbookSnapshotMapper;
import com.example.obwiki.resp.StatisticResp;
import com.example.obwiki.service.IEbookSnapshotService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author cr
 * @since 2025-06-23
 */
@Service
public class EbookSnapshotServiceImpl extends ServiceImpl<EbookSnapshotMapper, EbookSnapshot> implements IEbookSnapshotService {
    @Override
    public void genSnapshot() {
        this.baseMapper.genSnapshot();
    }

    @Override
    public List<StatisticResp> getStatistic() {
        return  this.baseMapper.getStatistic();
    }

    @Override
    public List<StatisticResp> get30Statistic() {
        return this.baseMapper.get30Statistic();
    }
}
