package com.image.project.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.image.imagecommon.model.entity.UserInterfaceInfo;

import java.util.List;

/**
 * @author 86187
 * @description 针对表【user_interface_info(用户调用接口关系表)】的数据库操作Service
 * @createDate 2024-05-25 00:26:47
 */
public interface UserInterfaceInfoService extends IService<UserInterfaceInfo> {

    /**
     * 检验
     *
     * @param userInterfaceInfo 信息
     * @param add               yes or no
     */
    void validUserInterfaceInfo(UserInterfaceInfo userInterfaceInfo, boolean add);

    /**
     * 调用次数的统计
     *
     * @param interfaceInfoId id
     * @param userId          id
     * @return yes or no
     */
    Boolean invokeCount(long interfaceInfoId, long userId);

    /**
     * 获取接口信息
     *
     * @param interfaceInfoId id
     * @param userId          id
     * @return 接口信息 or null
     */
    UserInterfaceInfo getUserInterfaceInfo(long interfaceInfoId, long userId);

    /**
     * 更新剩余可调用次数
     *
     * @param interfaceInfoId id
     * @param userId          id
     * @param purchasesCount  增加次数
     * @return true or false
     */
    Boolean updateUserInterfaceInfoByIds(long userId, long interfaceInfoId, long purchasesCount);

    /**
     * 获取接口调用最多的 n 个接口
     *
     * @param limit n
     * @return InterfaceInfoVO
     */
    List<UserInterfaceInfo> listTopInvokeInterfaceInfo(int limit);

}
