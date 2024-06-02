package com.image.project.model.vo;

import com.image.imagecommon.model.entity.InterfaceInfo;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;
import java.io.Serializable;

/**
 * @author JXY
 * @version 1.0
 * @since 2024/5/28
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class InterfaceInfoVO extends InterfaceInfo implements Serializable {
    /**
     * 总调用次数
     */
    private Integer totalNum;

    @Serial
    private static final long serialVersionUID = 1L;
}
