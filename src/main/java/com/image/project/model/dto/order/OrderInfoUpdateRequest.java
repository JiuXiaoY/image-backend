package com.image.project.model.dto.order;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * 更新请求
 *
 * @TableName product
 */
@Data
public class OrderInfoUpdateRequest implements Serializable {
    /**
     * 主键
     */
    private Long id;

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