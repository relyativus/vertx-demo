package com.github.relyativus.vertx.java;

import io.vertx.core.Future;
import io.vertx.core.http.HttpHeaders;
import io.vertx.ext.bridge.PermittedOptions;
import io.vertx.ext.web.handler.sockjs.BridgeOptions;
import io.vertx.reactivex.core.AbstractVerticle;
import io.vertx.reactivex.core.eventbus.Message;
import io.vertx.reactivex.ext.web.Router;
import io.vertx.reactivex.ext.web.handler.sockjs.SockJSHandler;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

/**
 * @author anatolii vakaliuk
 */
@Slf4j
public class GatewayVerticle extends AbstractVerticle {

    @Override
    public void start(Future<Void> startFuture) throws Exception {
        final Router router = Router.router(vertx);
        registerIndexHtmlHandler(router);
        registerGatewayConsumer();
        registerUIBridge(router);
        registerHttpServer(startFuture, router);
    }

    private void registerHttpServer(Future<Void> startFuture, Router router) {
        vertx.createHttpServer()
                .requestHandler(router::accept)
                .rxListen(8080)
                .subscribe(server -> startFuture.complete(), startFuture::fail);
    }

    private void registerIndexHtmlHandler(Router router) {
        router.get("/index").handler(ctx -> {
            final String indexHtmlPath = GatewayVerticle.class.getClassLoader().getResource("index.html").getPath();
            vertx.fileSystem().rxReadFile(indexHtmlPath)
            .subscribe(fileContent -> {
                ctx.response().setStatusCode(200);
                ctx.response().putHeader(HttpHeaders.CONTENT_TYPE.toString(), "text/html");
                ctx.response().end(fileContent);
            });
        });
    }

    private void registerUIBridge(final Router router) {
        final BridgeOptions bridgeOptions = new BridgeOptions();
        bridgeOptions.setInboundPermitted(List.of(new PermittedOptions().setAddress(AddressConstants.UI_CHANNEL)));
        bridgeOptions.setOutboundPermitted(List.of(new PermittedOptions().setAddress(AddressConstants.UI_CHANNEL)));

        final SockJSHandler sockJSHandler = SockJSHandler.create(vertx);
        router.route("/notifications/*").handler(sockJSHandler.bridge(bridgeOptions));
    }

    private void registerGatewayConsumer() {
        vertx.eventBus().consumer(AddressConstants.NOTIFICATION_CHANNEL, this::broadcastGreet);
    }

    private <T> void broadcastGreet(Message<T> message) {
        vertx.eventBus().send(AddressConstants.UI_CHANNEL, message.body());
    }
}
