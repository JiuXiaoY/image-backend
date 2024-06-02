package com.image.imagecommon.service;

import com.image.imagecommon.model.entity.UserInterfaceInfo;

/**
 * @author JXY
 * @version 1.0
 * @since 2024/5/27
 */
public interface InnerUserInterfaceInfoService {

    /**
     * 调用接口统计
     *
     * @param interfaceInfoId 接口id
     * @param userId          用户id
     * @return true or false
     */
    Boolean invokeCount(long interfaceInfoId, long userId);

    /**
     * 通过接口 id 和 用户 id 获取接口信息
     * @param interfaceInfoId 接口id
     * @param userId          用户id
     * @return 接口信息
     */
    UserInterfaceInfo getUserInterfaceInfo(long interfaceInfoId, long userId);
}
