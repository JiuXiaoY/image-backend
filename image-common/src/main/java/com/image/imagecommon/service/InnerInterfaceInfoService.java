package com.image.imagecommon.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.image.imagecommon.model.entity.InterfaceInfo;

/**
 * @author 86187
 * @description 针对表【interface_info(接口信息)】的数据库操作Service
 * @createDate 2024-05-19 23:57:38
 */
public interface InnerInterfaceInfoService {
    /**
     * 查看模拟接口是否存在
     *
     * @param path   路径
     * @param method 方法
     * @return 接口信息
     */
    InterfaceInfo getInterfaceInfo(String path, String method);
}
