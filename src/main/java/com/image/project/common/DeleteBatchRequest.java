package com.image.project.common;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * 批量删除请求
 *
 * @author yupi
 */
@Data
public class DeleteBatchRequest implements Serializable {
    /**
     * id
     */
    private Long[] ids;

    @Serial
    private static final long serialVersionUID = 1L;
}