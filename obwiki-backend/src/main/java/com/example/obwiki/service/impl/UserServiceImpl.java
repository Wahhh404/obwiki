package com.example.obwiki.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.obwiki.entity.User;
import com.example.obwiki.exception.BusinessException;
import com.example.obwiki.exception.BusinessExceptionCode;
import com.example.obwiki.mapper.UserMapper;
import com.example.obwiki.rep.UserLoginReq;
import com.example.obwiki.rep.UserQueryReq;
import com.example.obwiki.rep.UserResetPasswordReq;
import com.example.obwiki.rep.UserSaveReq;
import com.example.obwiki.resp.PageResp;
import com.example.obwiki.resp.UserLoginResp;
import com.example.obwiki.resp.UserQueryResp;
import com.example.obwiki.service.IUserService;
import com.example.obwiki.utils.CopyUtil;
import com.example.obwiki.utils.SnowFlake;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author cr
 * @since 2025-06-23
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements IUserService {
    @Autowired
    private SnowFlake snowFlake;

    private static final Logger LOG = LoggerFactory.getLogger(UserServiceImpl.class);

    @Override
    public UserLoginResp login(UserLoginReq req) {
        //1.根据用户名查询用户信息
        User user = this.baseMapper.selectOne(new QueryWrapper<User>().eq("login_name", req.getLoginName()));
        if (ObjectUtils.isEmpty(user)) {
            // 用户名不存在
            LOG.info("用户名不存在, {}", req.getLoginName());
            throw new BusinessException(BusinessExceptionCode.LOGIN_USER_ERROR);
        } else {
            if (user.getPassword().equals(req.getPassword())) {
                // 登录成功
                UserLoginResp userLoginResp = CopyUtil.copy(user, UserLoginResp.class);
                return userLoginResp;
            } else {
                // 密码不对
                LOG.info("密码不对, 输入密码：{}, 数据库密码：{}", req.getPassword(), user.getPassword());
                throw new BusinessException(BusinessExceptionCode.LOGIN_USER_ERROR);
            }
        }
    }

    @Override
    public void resetPassword(UserResetPasswordReq req) {
        User user = CopyUtil.copy(req, User.class);
        this.baseMapper.updateById(user);
    }

    @Override
    public PageResp<UserQueryResp> listByname(UserQueryReq req) {

        QueryWrapper<User> queryWrapper = new QueryWrapper<User>();
        //第一个参数：该参数是一个布尔类型，只有该参数是true时，才将like条件拼接到sql中；本例中，如果name字段不为空，则拼接name字段的like查询条件；
        queryWrapper.like(StringUtils.isNotBlank(req.getName()), "name", req.getName());
        queryWrapper.or().like(StringUtils.isNotBlank(req.getLoginName()), "login_name", req.getLoginName());
        //创建分页对象
        Page<User> page = new Page<>(req.getPage(), req.getSize());
        page = this.baseMapper.selectPage(page, queryWrapper);


        List<UserQueryResp> resps = CopyUtil.copyList(page.getRecords(), UserQueryResp.class);
        //创建返回对象
        PageResp<UserQueryResp> pageResp = new PageResp<>();
        pageResp.setList(resps);
        pageResp.setTotal(page.getTotal());
        return pageResp;
    }

    @Override
    public void save(UserSaveReq req) {
        User user = CopyUtil.copy(req, User.class);
        if (ObjectUtils.isEmpty(req.getId())) {
            //判断用户名是否已经存在
            User one = this.baseMapper.selectOne(new QueryWrapper<User>().eq("login_name", req.getLoginName()));
            if (ObjectUtils.isEmpty(one)) {
                // 新增
                long id = snowFlake.nextId();
                user.setId(id);
                this.baseMapper.insert(user);
            } else {
                throw new BusinessException(BusinessExceptionCode.USER_LOGIN_NAME_EXIST);
            }
        } else {
            // 更新
            user.setLoginName(null);//避免绕过前端 修改登录名
            user.setPassword(null);//不修改密码
            this.baseMapper.updateById(user);
        }
    }

    @Override
    public void delete(Long id) {
        this.baseMapper.deleteById(id);
    }
}
