package com.image.project.provider;

import java.util.concurrent.CompletableFuture;

/**
 * @author JXY
 * @version 1.0
 * @since 2024/5/26
 */
public interface DemoService {

    String sayHello(String name);

    String sayHello2(String name);

    default CompletableFuture<String> sayHelloAsync(String name) {
        return CompletableFuture.completedFuture(sayHello(name));
    }

}
