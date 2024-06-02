package com.image.project.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.image.imagecommon.model.entity.InterfaceInfo;
import com.image.project.common.BaseResponse;
import com.image.project.common.IdRequest;
import org.springframework.web.bind.annotation.RequestBody;

import javax.servlet.http.HttpServletRequest;

/**
 * @author 86187
 * @description 针对表【interface_info(接口信息)】的数据库操作Service
 * @createDate 2024-05-19 23:57:38
 */
public interface InterfaceInfoService extends IService<InterfaceInfo> {
    /**
     * 校验
     *
     * @param interfaceInfo 接口信息
     * @param add           是否为创建校验
     */
    void validInterfaceInfo(InterfaceInfo interfaceInfo, boolean add);

    /**
     * 发布接口
     *
     * @param idRequest 参数
     * @param request   请求
     * @return true or false
     */
    Boolean onlineInterfaceInfo(@RequestBody IdRequest idRequest, HttpServletRequest request);

    /**
     * 下线接口
     *
     * @param idRequest 参数
     * @param request   请求
     * @return true or false
     */
    Boolean offlineInterfaceInfo(@RequestBody IdRequest idRequest, HttpServletRequest request);
}
