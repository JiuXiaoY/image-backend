package com.image.imagegateway;

import com.image.imageclientsdk.utils.SignUtils;
import com.image.imagecommon.model.entity.InterfaceInfo;
import com.image.imagecommon.model.entity.User;
import com.image.imagecommon.model.entity.UserInterfaceInfo;
import com.image.imagecommon.service.InnerInterfaceInfoService;
import com.image.imagecommon.service.InnerUserInterfaceInfoService;
import com.image.imagecommon.service.InnerUserService;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboReference;
import org.reactivestreams.Publisher;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferFactory;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.http.server.reactive.ServerHttpResponseDecorator;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * @author JXY
 * @version 1.0
 * @since 2024/5/25
 */
@Slf4j
@Component
public class CustomGlobalFilter implements GlobalFilter, Ordered {

    @DubboReference
    private InnerUserInterfaceInfoService innerUserInterfaceInfoService;

    @DubboReference
    private InnerInterfaceInfoService innerInterfaceInfoService;

    @DubboReference
    private InnerUserService innerUserService;
    // 白名单
    public static final List<String> IP_WHITE_LIST = List.of("127.0.0.1");

    private final String INTERFACE_HOST = "http://localhost:7530";

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        // 1.用户发送请求到 API 网关
        // 2. 请求日志
        ServerHttpRequest request = exchange.getRequest();

        String path = INTERFACE_HOST + request.getPath().value();
        String method = Objects.requireNonNull(request.getMethod()).toString();

        log.info("请求唯一标识: " + request.getId());
        log.info("请求路径： " + path);
        log.info("请求方法： " + method);
        log.info("请求参数： " + request.getQueryParams());
        String hostAddress = Objects.requireNonNull(request.getLocalAddress()).getAddress().getHostAddress();
        log.info("请求来源地址： " + hostAddress);
        log.info("请求来源地址： " + request.getRemoteAddress());
        // 3. (黑白名单)

        // 获得响应对象
        ServerHttpResponse response = exchange.getResponse();
        if (!IP_WHITE_LIST.contains(hostAddress)) {
            return handelNoAuth(response);
        }

        // 4. 用户鉴权(判断 ak、sk 是否合法)
        HttpHeaders headers = request.getHeaders();
        String accessKey = headers.getFirst("accessKey");
        String nonce = headers.getFirst("nonce");
        String timestamp = headers.getFirst("timestamp");
        String sign = headers.getFirst("sign");
        String body = headers.getFirst("body");


        // 数据库查询是否已经分配给用户
        // if (!Objects.equals(accessKey, "image")) {
        //     return handelNoAuth(response);
        // }
        User invokeUser = null;
        try {
            invokeUser = innerUserService.getInvokeUser(accessKey);
        } catch (Exception e) {
            log.error("get invokeUser error" + e);
        }
        if (invokeUser == null) {
            return handelNoAuth(response);
        }

        if (Long.parseLong(nonce) > 10000) {
            return handelNoAuth(response);
        }

        Long currentTime = System.currentTimeMillis() / 1000;
        final Long FIVE_MINUTES = 60 * 5L;
        if (currentTime - Long.parseLong(timestamp) >= FIVE_MINUTES) {
            return handelNoAuth(response);
        }

        /**
         * 当传输的参数为空时，一个body为 null， 一个 body 为空字符串 "" ,导致了生成的签名不一致问题
         * 1、修改 ImageClient 代码，当 body 为 null 时，赋值为 ""
         * 2、修改此处代码， 当 body 为 "" 时，修改为 null
         * 3、但是这里又出现一个新问题，没有参数的话就算可以执行下去，下面的@RequestBody也会报错
         * 4、所以最终的解决方案是，去前端限制，参数为必填项
         */
        String secretKey = invokeUser.getSecretKey();
        String serverSign = SignUtils.genSign(body, secretKey);
        if (sign == null || !sign.equals(serverSign)) {
            return handelNoAuth(response);
        }

        // 校验方法是否合法，或者参数等
        InterfaceInfo interfaceInfo = null;
        try {
            interfaceInfo = innerInterfaceInfoService.getInterfaceInfo(path, method);
        } catch (Exception e) {
            log.error("get interfaceInfo error" + e);
        }

        if (interfaceInfo == null) {
            return handelNoAuth(response);
        }

        // todo 是否还有调用次数
        UserInterfaceInfo userInterfaceInfo = null;
        try {
            userInterfaceInfo = innerUserInterfaceInfoService.getUserInterfaceInfo(interfaceInfo.getId(), invokeUser.getId());
        } catch (Exception e) {
            log.error("get userInterfaceInfo error" + e);
        }
        if (userInterfaceInfo == null || userInterfaceInfo.getLeftNum() <= 0) {
            return handelNoAuth(response);
        }

        return handelResponse(exchange, chain, interfaceInfo.getId(), invokeUser.getId());
    }


    public Mono<Void> handelResponse(ServerWebExchange exchange, GatewayFilterChain chain, long interfaceInfoId, long invokeUserId) {
        try {
            // 获取原始的response对象
            ServerHttpResponse originalResponse = exchange.getResponse();
            // 获取数据缓冲工厂
            DataBufferFactory dataBufferFactory = originalResponse.bufferFactory();
            // 获取响应状态码
            HttpStatus statusCode = originalResponse.getStatusCode();
            // 如果状态码 == 200
            if (statusCode == HttpStatus.OK) {
                ServerHttpResponseDecorator serverHttpResponseDecorator = new ServerHttpResponseDecorator(originalResponse) {
                    // 重写 writeWith 方法
                    // 当我们的模拟接口调用完成之后，等返回结果
                    // 就会调用 writeWith 方法，我们就能根据响应结果处理一些事情
                    @Override
                    public Mono<Void> writeWith(Publisher<? extends DataBuffer> body) {
                        log.info("body instanceof Flux: {}", body instanceof Flux);
                        // 判断响应体是否是Flux类型
                        if (body instanceof Flux) {
                            // 8. 调用成功，接口调用次数 + 1.
                            Flux<? extends DataBuffer> fluxBody = Flux.from(body);
                            // 返回一个处理后的响应体
                            return super.writeWith(fluxBody.map(dataBuffer -> {
                                // 调用成功，进行次数加减操作
                                try {
                                    Boolean result = innerUserInterfaceInfoService.invokeCount(interfaceInfoId, invokeUserId);
                                    if (!result) {
                                        throw new RuntimeException("invoke error");
                                    }
                                } catch (Exception e) {
                                    log.error("invokeCount error" + e);
                                }
                                byte[] content = new byte[dataBuffer.readableByteCount()];
                                dataBuffer.read(content);
                                DataBufferUtils.release(dataBuffer);
                                // 构建日志
                                StringBuilder sb2 = new StringBuilder(200);
                                ArrayList<Object> rspArgs = new ArrayList<>();
                                rspArgs.add(originalResponse.getStatusCode());
                                String data = new String(content, StandardCharsets.UTF_8);
                                sb2.append(data);
                                // 打印日志
                                log.info(sb2.toString(), rspArgs.toArray());
                                log.info("响应结果： " + data);
                                return dataBufferFactory.wrap(content);
                            }));
                        } else {
                            log.error("<--- {} 响应code异常", originalResponse.getStatusCode());
                        }
                        return super.writeWith(body);
                    }
                };
                return chain.filter(exchange.mutate().response(serverHttpResponseDecorator).build());
            }
            return chain.filter(exchange);
        } catch (Exception e) {
            log.error("网关处理相应异常" + e);
            return chain.filter(exchange);
        }
    }

    private static Mono<Void> handelNoAuth(ServerHttpResponse response) {
        response.setStatusCode(HttpStatus.FORBIDDEN);
        return response.setComplete();
    }

    private static Mono<Void> handelInvokeError(ServerHttpResponse response) {
        response.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR);
        return response.setComplete();
    }


    @Override
    public int getOrder() {
        return -1;
    }
}
