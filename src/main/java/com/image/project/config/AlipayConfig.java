package com.image.project.config;

import lombok.Data;
import org.springframework.context.annotation.Configuration;

/**
 * @author JXY
 * @version 1.0
 * @since 2024/5/30
 */
@Configuration
@Data
public class AlipayConfig {
    // 应用ID,您的APPID，收款账号既是您的APPID对应支付宝账号
    public static String app_id = "9021000137661722";

    // 商户私钥，您的PKCS8格式RSA2私钥
    public static String merchant_private_key = "MIIEvQIBADANBgkqhkiG9w0BAQEFAASCBKcwggSjAgEAAoIBAQCzRIh5TVxuWDJHML6FwIX85jtkhXeWNq27d51pJz66u/mNzeVy5FqdZ8AgmjgqV+yUbcbQUf7DGWw8isulLo/4oLHyv0TdidwUQshz/L8ZKqEuFTZ5JO14Mj8IW6MbUW1ZjR7nG/BamCjtJBryVKOi2nrw25gfSApbvM6eytqmNL4AJs6tmlOjZi83CJP0Jhkz9+objEa5HWtZ9fGGrZxdQnVM2tv9PKqfuEJHzvZStbBVTcnCdPrNaXX1Rq4CIus4ypLR01ixgLNAezwDEmAdSEtRohsrE2nZio0uZghUurkvSQOx21rv3xUMJtk80Rm4viBKIK73t+5maM7A0w4PAgMBAAECggEAKJT3IB63MfOdTihmuPigXGPyRgtAyyyNn8awR8Guna84CzX1H1E6gi+eesLuvAczc1Z2j2pAdgInUn1JJJep1+ro3b8W49/1hlCn+DnbOONBoaEDt+D4MSGOy2GTRvfj/UXJHSBiBOA1Q5nMz9cBRDiZZDUR9uLkFWhkjeX2IXGlijA9ZVTjL4x2iN4o5UHZM3wIRip4ayjbTUpKBBFvN3YBRUlXokm7nm+/0Q6h8rpR0CpQ+O/xLinDa/NIbAQzcjWgI5yqM5Tv4n5eGmrGQUt0JdTdyU88qYRoQPf6CAKjRIbLTzVIwAVl0eUbjvUog7pyS61VF2iihKdZvszpAQKBgQDvYioInwYU/ozpAsVQdyGeUk2OlYuMsrM+F0+FU+tVKf0jJ1u57hf+uJbd9o1suUdJoWSY9LWatnndZegW2ZuuA9k/yQwa85IYQ02uMNCwhnxuOzpZjt3dJTn+A5yweQlPpjtHWO6MiTkQq+5wEd6zawgJslDXCjCCiIM+U/whYQKBgQC/th04iUJyDqoYw+JTyZVzitC8zjiFMhVOoS6LbJu1IsDs54oXb64ystK825n4j+bVghNGJOfatEDx2BI/GWbgm48RB1ChnIplTNvSjZC0JKy1ys85zsi87+4+CxoL3a7e9Hv3MB+JohuY/a0rdirv6q8DXlLVJ3oi0ex+cUe1bwKBgA7VWV9xYzIX1+l0Z3+l8ipaVT35nE0aoaq+TQoo+hMr5+iuvqtVypzZGVglt/u9u2IfoxkrSLmOhxyl/Oy1jUUKLgMa1SoyX6IrQXwCwFu/6aP2/ZWhGbRjYyWPWb1BsscBuPih1R5WPnf+/2ZoZRFfF39qUkIQ+pKMHP3WvG/hAoGADugyTXse14+cWmCg8LL0FA/vOz8t1e2XCo4t+FlxohwZtFIswrXEbdzJ1jWdO161LFT4+rNT9AWgJy4N/SoUfdKaxVA0TNzHRik7zpuDbP6ND3W9t1kE8uwVMm3akKGkP2HzYb00NpeMXdEb7g06OX2eLjzfZAO1049PPGXClbECgYEA3o56JA6qigL6KD0qBellLMZw4l2PgFgHYNZoYIgxEUFJzTYjBsZAtLfRr2G0Cmz0u8XQmHVap4FjJ4DJ1x3+N/vW3o3vyRtT0mCd+8tkb77qgNj7EwB3jDu4DCtFdzvVLKbkkrvr5+HRj1Jymms4DL3uWj7T0jy3ugR4NKBSMjU=";

    // 支付宝公钥,查看地址：https://openhome.alipay.com/platform/keyManage.htm 对应APPID下的支付宝公钥。
    public static String alipay_public_key = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAziaiHgzk5f+CVJ9LwXb3g7gQKnV+h38S41jU+iyFXxcuKENHVKYjqcRs1//eaMa/ZmQ3jyC16GkSKNoyKlMPB/UzfIJPRwvEngBsa7DpLCV4l6xgDaglnfihPl8b5KDpE7o1RxrB79/yyPj3hdxjPVu5hRhlZYGmtABxtUhJvNpmzcX6F/a/VkGrc331YmWtIsRYC2iARG4pc7RhysbpHirJO+Fsfh9NGaM+Kn53fQaN8tsgPtkTJCHrULH9ZEnBSSLKWBo2PhgeAZ0QMdHrMkIbL6KmCoJMj1pRj0dKGFbOF8WnzcitCxJVBaT3t5TiB0vOwuY4/8DosQilL2jpQwIDAQAB";

    // 签名方式
    public static String sign_type = "RSA2";

    // 字符编码格式
    public static String charset = "utf-8";

    // 支付宝网关
    public static String gatewayUrl = "https://openapi-sandbox.dl.alipaydev.com/gateway.do";

    // 支付成功后的地址
    public static String return_url = "http://bt.imagegem.cn/gem.jpg";

    // 回调函数
    public static String notify_url = "https://f0b4-202-192-80-120.ngrok-free.app/api/Alipay/notify";

}



