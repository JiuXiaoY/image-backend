package com.image.project.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.image.imagecommon.model.entity.User;
import com.image.imagecommon.service.InnerUserService;
import com.image.project.common.ErrorCode;
import com.image.project.exception.BusinessException;
import com.image.project.mapper.UserMapper;
import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.config.annotation.DubboService;

import javax.annotation.Resource;

/**
 * @author JXY
 * @version 1.0
 * @since 2024/5/27
 */
@DubboService
public class InnerUserServiceImpl implements InnerUserService {

    @Resource
    private UserMapper userMapper;

    @Override
    public User getInvokeUser(String accessKey) {
        if (StringUtils.isBlank(accessKey)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }

        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("accessKey",accessKey);
        return userMapper.selectOne(queryWrapper);
    }
}
