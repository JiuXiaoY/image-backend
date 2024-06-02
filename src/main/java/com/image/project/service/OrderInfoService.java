package com.image.project.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.image.project.model.dto.order.OrderInfoAddRequest;
import com.image.project.model.entity.OrderInfo;

import java.util.Map;

/**
 * @author 86187
 * @description 针对表【order_info(订单信息)】的数据库操作Service
 * @createDate 2024-05-31 14:57:09
 */
public interface OrderInfoService extends IService<OrderInfo> {

    /**
     * 校验
     *
     * @param orderInfo 订单信息
     * @param add       true or false
     */
    void validOrderInfo(OrderInfo orderInfo, boolean add);

    /**
     * 添加订单信息（支付宝回调）
     *
     * @param orderInfoAddRequest 订单信息
     * @param params 回调参数
     */
    void addOrderInfo(OrderInfoAddRequest orderInfoAddRequest, Map<String, String> params);
}
