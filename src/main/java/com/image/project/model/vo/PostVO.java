package com.image.project.model.vo;

import com.image.project.model.entity.Post;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;

/**
 * 帖子视图
 *
 * @author yupi
 * @TableName product
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class PostVO extends Post {

    /**
     * 是否已点赞
     */
    private Boolean hasThumb;

    @Serial
    private static final long serialVersionUID = 1L;
}