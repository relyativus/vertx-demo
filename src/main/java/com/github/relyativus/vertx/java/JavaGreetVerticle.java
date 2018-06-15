package com.github.relyativus.vertx.java;

import io.vertx.core.Future;
import io.vertx.core.json.JsonObject;
import io.vertx.reactivex.core.AbstractVerticle;
import lombok.extern.slf4j.Slf4j;

/**
 * @author anatolii vakaliuk
 */
@Slf4j
public class JavaGreetVerticle extends AbstractVerticle {

    @Override
    public void start(Future<Void> startFuture) throws Exception {
        vertx.setPeriodic(1_300, this::sayHelloFromJava);
    }

    private void sayHelloFromJava(Long delay) {
        vertx.eventBus().send(AddressConstants.NOTIFICATION_CHANNEL, new JsonObject().put("message", "Hello From Java 10 =)"));
    }
}
