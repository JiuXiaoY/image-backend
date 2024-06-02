package com.image.project.service.impl;

import com.alipay.api.AlipayClient;
import com.image.project.service.UserInterfaceInfoService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;

/**
 * @author JXY
 * @version 1.0
 * @since 2024/5/25
 */
public class UserInterfaceInfoServiceImplTest {

    @Resource
    UserInterfaceInfoService userInterfaceInfoService;

    @Test
    public void invokeCount() {
        Boolean aBoolean = userInterfaceInfoService.invokeCount(1L, 1L);
        Assertions.assertTrue(aBoolean);
    }
}