package com.image.project.model.dto.order;

import com.image.project.common.PageRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;

/**
 * 查询请求
 *
 * @author yupi
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class OrderInfoQueryRequest extends PageRequest implements Serializable {

    /**
     * 主键
     */
    private Long id;

    /**
     * 流水号
     */
    private String serialNumber;

    /**
     * 用户id
     */
    private Long userId;

    /**
     * 接口信息id
     */
    private Long interfaceInfoId;

    /**
     * 支付金额
     */
    private Long amountPaid;

    /**
     * 支付方式
     */
    private String paymentMethod;

    /**
     * 购买次数
     */
    private Long purchasesCount;

    /**
     * 订单状态（0 - 未完成，1 - 已完成）
     */
    private Integer status;


    @Serial
    private static final long serialVersionUID = 1L;
}