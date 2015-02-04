package com.jerryorr.websocketclient;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.StatusCode;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketClose;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketConnect;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;

@WebSocket(maxTextMessageSize = 64 * 1024)
public class LocalNodeWebsocket {
    private final CountDownLatch closeLatch;
    
    @SuppressWarnings("unused")
    private Session session;
 
    public LocalNodeWebsocket() {
        this.closeLatch = new CountDownLatch(1);
    }
 
    public boolean awaitClose(int duration, TimeUnit unit) throws InterruptedException {
        return this.closeLatch.await(duration, unit);
    }
 
    @OnWebSocketClose
    public void onClose(int statusCode, String reason) {
        System.out.printf("Connection closed: %d - %s%n", statusCode, reason);
        this.session = null;
        this.closeLatch.countDown();
    }
 
    @OnWebSocketConnect
    public void onConnect(Session session) {
        System.out.printf("Got connect: %s%n", session);
        this.session = session;
        try {
        	//{ data: 'hello jerry' }
            Future<Void> fut;
//            fut = session.getRemote().sendStringByFuture("Hello");
//            fut = session.getRemote().sendStringByFuture("{\"data\":\"Hello, Jerry\"}");
            //{data: { room: 'abc123', msg: 'jerry'}}
            fut = session.getRemote().sendStringByFuture("{\"room\":\"abc123\", \"msg\":\"Hello, Jerry\"}");
            fut.get(2, TimeUnit.SECONDS);
            fut = session.getRemote().sendStringByFuture("{\"room\":\"abc456\", \"msg\":\"Hello, Newman\"}");
            fut.get(2, TimeUnit.SECONDS);
//            fut = session.getRemote().sendStringByFuture("Thanks for the conversation.");
//            fut.get(2, TimeUnit.SECONDS);
            session.close(StatusCode.NORMAL, "I'm done");
        } catch (Throwable t) {
            t.printStackTrace();
        }
    }
 
    @OnWebSocketMessage
    public void onMessage(String msg) {
        System.out.printf("Got msg: %s%n", msg);
    }

}
