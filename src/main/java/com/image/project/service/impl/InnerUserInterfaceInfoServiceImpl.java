package com.image.project.service.impl;

import com.image.imagecommon.model.entity.UserInterfaceInfo;
import com.image.imagecommon.service.InnerUserInterfaceInfoService;
import org.apache.dubbo.config.annotation.DubboService;

import javax.annotation.Resource;

/**
 * @author JXY
 * @version 1.0
 * @since 2024/5/27
 */
@DubboService
public class InnerUserInterfaceInfoServiceImpl implements InnerUserInterfaceInfoService {

    @Resource
    private UserInterfaceInfoServiceImpl userInterfaceInfoService;

    @Override
    public Boolean invokeCount(long interfaceInfoId, long userId) {
        return userInterfaceInfoService.invokeCount(interfaceInfoId, userId);
    }

    @Override
    public UserInterfaceInfo getUserInterfaceInfo(long interfaceInfoId, long userId) {
        return userInterfaceInfoService.getUserInterfaceInfo(interfaceInfoId, userId);
    }
}
