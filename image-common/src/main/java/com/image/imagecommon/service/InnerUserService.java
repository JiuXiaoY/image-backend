package com.image.imagecommon.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.image.imagecommon.model.entity.User;

/**
 * 用户服务
 *
 * @author yupi
 */
public interface InnerUserService {
    /**
     * 根据 accessKey secretKey 匹配用户
     *
     * @param accessKey 公钥
     * @return 用户
     */
    User getInvokeUser(String accessKey);
}
