package com.image.project.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.image.imageclientsdk.client.ImageClient;
import com.image.project.common.BaseResponse;
import com.image.project.common.ErrorCode;
import com.image.project.common.IdRequest;
import com.image.project.common.ResultUtils;
import com.image.project.exception.BusinessException;
import com.image.project.mapper.InterfaceInfoMapper;
import com.image.imagecommon.model.entity.InterfaceInfo;
import com.image.project.model.enums.InterfaceInfoStatusEnum;
import com.image.project.service.InterfaceInfoService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
* @author 86187
* @description 针对表【interface_info(接口信息)】的数据库操作Service实现
* @createDate 2024-05-19 23:57:38
*/
@Service
public class InterfaceInfoServiceImpl extends ServiceImpl<InterfaceInfoMapper, InterfaceInfo>
    implements InterfaceInfoService {

    @Resource
    private ImageClient imageClient;

    @Override
    public void validInterfaceInfo(InterfaceInfo interfaceInfo, boolean add) {
        if (interfaceInfo == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }

        String name = interfaceInfo.getName();

        // 创建时，所有参数必须非空
        if (add) {
            if (StringUtils.isAnyBlank(name)) {
                throw new BusinessException(ErrorCode.PARAMS_ERROR);
            }
        }
        if (StringUtils.isNotBlank(name) && name.length() > 50) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "名称过长");
        }
    }

    @Override
    public Boolean onlineInterfaceInfo(IdRequest idRequest, HttpServletRequest request) {
        // 如果 id 为 null 或者 id 的长度小于 0
        if (idRequest == null || idRequest.getId() <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        // 获取 id
        Long id = idRequest.getId();
        // 根据 id 查询接口信息
        InterfaceInfo oldInterfaceInfo = this.getById(id);
        if (oldInterfaceInfo == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR);
        }
        // 判断接口是否可以调用
        // 假数据
        com.image.imageclientsdk.model.User user = new com.image.imageclientsdk.model.User();
        user.setUsername("dengziqi");
        String userNameByPost = imageClient.getUserNameByPost(user);
        if (StringUtils.isBlank(userNameByPost)) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "接口验证失败");
        }
        InterfaceInfo newInterfaceInfo = new InterfaceInfo();
        newInterfaceInfo.setId(id);

        newInterfaceInfo.setStatus(InterfaceInfoStatusEnum.ONLINE.getValue());
        return this.updateById(newInterfaceInfo);
    }

    @Override
    public Boolean offlineInterfaceInfo(IdRequest idRequest, HttpServletRequest request) {
        // 如果 id 为 null 或者 id 的长度小于 0
        if (idRequest == null || idRequest.getId() <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        // 获取 id
        Long id = idRequest.getId();
        // 根据 id 查询接口信息
        InterfaceInfo oldInterfaceInfo = this.getById(id);
        if (oldInterfaceInfo == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR);
        }

        InterfaceInfo newInterfaceInfo = new InterfaceInfo();
        newInterfaceInfo.setId(id);

        newInterfaceInfo.setStatus(InterfaceInfoStatusEnum.OFFLINE.getValue());
        return this.updateById(newInterfaceInfo);
    }
}




