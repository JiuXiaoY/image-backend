package com.image.project.controller.thirdparty;

import cn.hutool.core.util.IdUtil;
import com.alibaba.fastjson.JSONObject;
import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.internal.util.AlipaySignature;
import com.alipay.api.request.AlipayTradePagePayRequest;
import com.alipay.api.response.AlipayTradePagePayResponse;
import com.image.imagecommon.model.entity.InterfaceInfo;
import com.image.project.common.BaseResponse;
import com.image.project.common.ErrorCode;
import com.image.project.common.ResultUtils;
import com.image.project.config.AlipayConfig;
import com.image.project.exception.BusinessException;
import com.image.project.model.dto.order.OrderInfoAddRequest;
import com.image.project.service.InterfaceInfoService;
import com.image.project.service.OrderInfoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * @author JXY
 * @version 1.0
 * @since 2024/5/31
 */
@RestController
@Slf4j
@RequestMapping("/Alipay")
public class AlipayController {

    @Resource
    InterfaceInfoService interfaceInfoService;

    @Resource
    OrderInfoService orderInfoService;

    @PostMapping("/pay")
    public BaseResponse<String> getPay(@RequestBody OrderInfoAddRequest orderInfoAddRequest, HttpServletResponse response) throws AlipayApiException, IOException {
        AlipayClient alipayClient = new DefaultAlipayClient(AlipayConfig.gatewayUrl, AlipayConfig.app_id,
                AlipayConfig.merchant_private_key,
                "json",
                AlipayConfig.charset,
                AlipayConfig.alipay_public_key,
                AlipayConfig.sign_type);
        AlipayTradePagePayRequest aliPayRequest = new AlipayTradePagePayRequest();

        JSONObject bizContent = new JSONObject();
        // 随机生成一个流水号号
        String out_trade_no = IdUtil.simpleUUID();

        // todo 这里应该获取到接口单价在进行计算，先模拟为10块钱一次
        String total_amount = String.valueOf(orderInfoAddRequest.getPurchasesCount() * 10);

        // 根据接口 id 获取接口名称
        Long interfaceInfoId = orderInfoAddRequest.getInterfaceInfoId();
        InterfaceInfo interfaceInfo = interfaceInfoService.getById(interfaceInfoId);
        String interfaceInfoName = interfaceInfo.getName();

        bizContent.put("out_trade_no", out_trade_no);
        bizContent.put("total_amount", total_amount);
        bizContent.put("subject", interfaceInfoName);
        bizContent.put("product_code", "FAST_INSTANT_TRADE_PAY");

        aliPayRequest.setBizContent(bizContent.toString());
        // 获取原始地址
        String return_url = orderInfoAddRequest.getOriginalUrl();
        aliPayRequest.setReturnUrl(return_url);
        // 回调地址
        aliPayRequest.setNotifyUrl(AlipayConfig.notify_url
                + "?interfaceInfoId=" + interfaceInfoId
                + "&purchasesCount=" + orderInfoAddRequest.getPurchasesCount()
                + "&userId=" + orderInfoAddRequest.getUserId());

        AlipayTradePagePayResponse aliPayResponse = alipayClient.pageExecute(aliPayRequest);
        if (aliPayResponse.isSuccess()) {
            log.info("调用支付接口成功");
            String form = aliPayResponse.getBody();
            // response.setContentType("text/html;charset=" + AlipayConfig.charset);
            // response.getWriter().write(form);
            return ResultUtils.success(form);
        } else {
            log.info("调用支付接口失败");
            // 在失败情况下可能需要进行相应的处理
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR);
        }
    }

    /**
     * 支付成功回调
     *
     * @param request 请求
     * @throws AlipayApiException 异常
     */
    @PostMapping("/notify")
    public String payNotify(HttpServletRequest request) throws AlipayApiException {
        Map<String, String[]> parameterMap = request.getParameterMap();
        Map<String, String> tempMap = new HashMap<>();
        parameterMap.forEach((key, values) -> {
            String valueStr = "";
            for (int i = 0; i < values.length; i++) {
                valueStr = i == values.length - 1 ? valueStr + values[i] : valueStr + ",";
            }
            tempMap.put(key, valueStr);
            // 记录日志
            log.info("key: " + key + "  value: " + valueStr);
        });

        // 保存参数
        OrderInfoAddRequest orderInfoAddRequest = new OrderInfoAddRequest();
        orderInfoAddRequest.setInterfaceInfoId(Long.valueOf(tempMap.get("interfaceInfoId")));
        orderInfoAddRequest.setUserId(Long.valueOf(tempMap.get("userId")));
        orderInfoAddRequest.setPurchasesCount(Long.valueOf(tempMap.get("purchasesCount")));

        // 必须要移除，否则会失败
        tempMap.remove("sign_type");
        tempMap.remove("interfaceInfoId");
        tempMap.remove("purchasesCount");
        tempMap.remove("userId");

        // 验签
        boolean checkV2 = AlipaySignature.rsaCheckV2(tempMap, AlipayConfig.alipay_public_key, AlipayConfig.charset, AlipayConfig.sign_type);
        if (checkV2) {
            log.info("验签成功，支付完成");

            // 异步处理业务逻辑
            orderInfoService.addOrderInfo(orderInfoAddRequest,tempMap);

            return "success";
        } else {
            log.info("验签失败");
            return "failure";
        }
    }
}
