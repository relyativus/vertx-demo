package com.github.relyativus.vertx.java;

import io.vertx.reactivex.core.Vertx;

/**
 * @author anatolii vakaliuk
 */
public class AppRunner {
    public static void main(String[] args) {
        final Vertx vertx = Vertx.vertx();
        vertx.deployVerticle(GatewayVerticle.class.getName());
        vertx.deployVerticle(JavaGreetVerticle.class.getName());
        vertx.deployVerticle(GreetKotlinVerticle.class.getName());
    }
}
