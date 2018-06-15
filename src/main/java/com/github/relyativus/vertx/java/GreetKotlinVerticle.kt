package com.github.relyativus.vertx.java

import io.vertx.core.json.JsonObject
import io.vertx.reactivex.core.AbstractVerticle

class GreetKotlinVerticle : AbstractVerticle() {

    override fun start(startFuture: io.vertx.core.Future<Void>?) {
        val eventBus = vertx.eventBus()
        vertx.setPeriodic(1_500) {
            eventBus.send("gateway.greet", JsonObject().put("message", "Hello From Kotlin!"))
        }
        startFuture?.complete()
    }

}