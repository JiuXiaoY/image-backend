package com.image.imageinterface.controller;


import com.image.imageclientsdk.model.User;
import com.image.imageclientsdk.utils.SignUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Objects;

/**
 * @author JXY
 * @version 1.0
 * @since 2024/5/23
 */
@RestController
@RequestMapping("/name")
public class NameController {
    @GetMapping("/")
    public String getNameByGet(String name) {
        return "GET your name is: " + name;
    }

    @PostMapping("/")
    public String getNameByPost(@RequestParam String name) {
        return "POST your name is: " + name;
    }

    @PostMapping("/user")
    public String getUserNameByPost(@RequestBody User user, HttpServletRequest request) {
        String accessKey = request.getHeader("accessKey");
        String nonce = request.getHeader("nonce");
        String timestamp = request.getHeader("timestamp");
        String sign = request.getHeader("sign");
        String body = request.getHeader("body");


        if (!Objects.equals(accessKey, "image")) {
            throw new RuntimeException("NO AUTH");
        }

        if (Long.parseLong(nonce) > 10000) {
            throw new RuntimeException("NO AUTH");
        }

        String serverSign = SignUtils.genSign(body, "abcdefgh");
        if (!sign.equals(serverSign)) {
            throw new RuntimeException("NO AUTH");
        }

        return "POST your name is: " + user.getUsername();
    }
}
