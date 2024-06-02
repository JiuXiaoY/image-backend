package com.image.project.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.image.imagecommon.model.entity.UserInterfaceInfo;
import com.image.project.common.ErrorCode;
import com.image.project.exception.BusinessException;
import com.image.project.mapper.OrderInfoMapper;
import com.image.project.model.dto.order.OrderInfoAddRequest;
import com.image.project.model.entity.OrderInfo;
import com.image.project.service.OrderInfoService;
import com.image.project.service.UserInterfaceInfoService;
import com.image.project.service.UserService;
import com.image.project.utils.String2LongUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Map;

/**
 * @author 86187
 * @description 针对表【order_info(订单信息)】的数据库操作Service实现
 * @createDate 2024-05-31 14:57:09
 */
@Service
public class OrderInfoServiceImpl extends ServiceImpl<OrderInfoMapper, OrderInfo>
        implements OrderInfoService {

    @Resource
    UserService userService;

    @Resource
    UserInterfaceInfoService userInterfaceInfoService;

    @Override
    public void validOrderInfo(OrderInfo orderInfo, boolean add) {
        if (orderInfo == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }

        String serialNumber = orderInfo.getSerialNumber();

        // 创建时，所有参数必须非空
        if (add) {
            if (StringUtils.isAnyBlank(serialNumber)) {
                throw new BusinessException(ErrorCode.PARAMS_ERROR);
            }
        }
        if (StringUtils.isNotBlank(serialNumber) && serialNumber.length() > 100) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "名称过长");
        }
    }

    @Async
    @Override
    public void addOrderInfo(OrderInfoAddRequest orderInfoAddRequest, Map<String, String> params) {
        if (orderInfoAddRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }

        // 将订单写入数据库
        OrderInfo orderInfo = new OrderInfo();
        // Copy properties from request to orderInfo
        BeanUtils.copyProperties(orderInfoAddRequest, orderInfo);

        // Set additional properties
        Long buyerPayAmount = String2LongUtil.string2Long(params.get("buyer_pay_amount"));
        orderInfo.setAmountPaid(buyerPayAmount);
        orderInfo.setSerialNumber(params.get("out_trade_no"));
        orderInfo.setPaymentMethod("ALIPAYACCOUNT");

        String tradeStatus = params.get("trade_status");
        if ("TRADE_SUCCESS".equals(tradeStatus)) {
            orderInfo.setStatus(1);
        }

        // Validate order info
        this.validOrderInfo(orderInfo, true);

        // Save order info to the database
        if (!this.save(orderInfo)) {
            throw new BusinessException(ErrorCode.OPERATION_ERROR);
        }

        // Handle user interface info
        handleUserInterfaceInfo(orderInfo);
    }

    private void handleUserInterfaceInfo(OrderInfo orderInfo) {
        Long interfaceInfoId = orderInfo.getInterfaceInfoId();
        Long userId = orderInfo.getUserId();
        Long purchasesCount = orderInfo.getPurchasesCount();

        UserInterfaceInfo userInterfaceInfo = userInterfaceInfoService.getUserInterfaceInfo(interfaceInfoId, userId);

        if (userInterfaceInfo == null) {
            // If not exists, create new UserInterfaceInfo
            userInterfaceInfo = new UserInterfaceInfo();
            userInterfaceInfo.setInterfaceInfoId(interfaceInfoId);
            userInterfaceInfo.setUserId(userId);
            userInterfaceInfo.setTotalNum(0);
            userInterfaceInfo.setLeftNum(Math.toIntExact(purchasesCount));
            userInterfaceInfo.setStatus(1);

            if (!userInterfaceInfoService.save(userInterfaceInfo)) {
                throw new BusinessException(ErrorCode.OPERATION_ERROR);
            }
        } else {
            // If exists, update the interface info
            if (!userInterfaceInfoService.updateUserInterfaceInfoByIds(userId, interfaceInfoId, purchasesCount)) {
                throw new BusinessException(ErrorCode.OPERATION_ERROR);
            }
        }
    }

}




