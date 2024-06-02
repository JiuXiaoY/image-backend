package com.image.project.common;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * @author JXY
 * @version 1.0
 * @since 2024/5/23
 */
@Data
public class IdRequest implements Serializable {

    private Long id;

    @Serial
    private static final long serialVersionUID = 1L;
}
