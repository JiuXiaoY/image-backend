package com.image.project.model.dto.order;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;

/**
 * @author JXY
 * @version 1.0
 * @since 2024/5/31
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderInfoAddRequest implements Serializable {
    /**
     * 用户id
     */
    private Long userId;

    /**
     * 接口信息id
     */
    private Long interfaceInfoId;

    /**
     * 购买次数
     */
    private Long purchasesCount;

    /**
     * 这个字段不需要存储，但是又是要用到的
     * 作为临时处理方式
     * todo 以后改进
     */
    private String originalUrl;

    @Serial
    private static final long serialVersionUID = 1L;
}
