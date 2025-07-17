package com.example.obwiki.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.obwiki.entity.Category;
import com.example.obwiki.mapper.CategoryMapper;
import com.example.obwiki.rep.CategoryQueryReq;
import com.example.obwiki.rep.CategorySaveReq;
import com.example.obwiki.resp.CategoryQueryResp;
import com.example.obwiki.resp.PageResp;
import com.example.obwiki.service.ICategoryService;
import com.example.obwiki.utils.CopyUtil;
import com.example.obwiki.utils.SnowFlake;
import org.springframework.beans.factory.annotation.Autowired;
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
public class CategoryServiceImpl extends ServiceImpl<CategoryMapper, Category> implements ICategoryService {

    @Autowired
    private SnowFlake snowFlake;

    @Override
    public PageResp<CategoryQueryResp> listByname(CategoryQueryReq req) {

        QueryWrapper<Category> queryWrapper = new QueryWrapper<Category>();
        //第一个参数：该参数是一个布尔类型，只有该参数是true时，才将like条件拼接到sql中；本例中，如果name字段不为空，则拼接name字段的like查询条件；
        queryWrapper.like(StringUtils.isNotBlank(req.getName()),"name",req.getName());
        //创建分页对象
        Page<Category> page = new Page<>(req.getPage(),req.getSize());
        page = this.baseMapper.selectPage(page, queryWrapper);

        List<CategoryQueryResp> resps = CopyUtil.copyList(page.getRecords(), CategoryQueryResp.class);
        //创建返回对象
        PageResp<CategoryQueryResp> pageResp = new PageResp<>();
        pageResp.setList(resps);
        pageResp.setTotal(page.getTotal());
        return pageResp;
    }

    @Override
    public void save(CategorySaveReq req) {
        Category category = CopyUtil.copy(req, Category.class);
        if (ObjectUtils.isEmpty(req.getId())) {
            // 新增
            this.baseMapper.insert(category);
        } else {
            // 更新
            this.baseMapper.updateById(category);
        }
    }
    @Override
    public List<CategoryQueryResp> all() {
        //使用 sort 列进行排序
        List<Category> categories = this.baseMapper.selectList(new QueryWrapper<Category>().orderByAsc("sort"));
        List<CategoryQueryResp> list = CopyUtil.copyList(categories, CategoryQueryResp.class);
        return list;
    }
    @Override
    public void delete(Long id) {
        this.baseMapper.deleteById(id);
    }
}
