package com.nrjh.iop.modules.util.websocket;

import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArraySet;

/**
 * @Author zzy
 * @Date 2020/04/01
 * @Description: 此注解相当于设置访问URL
 */
@Component
@Slf4j
@ServerEndpoint("/iop/websocket/{userId}") //此注解相当于设置访问URL
public class IopWebSocket {
    
    private Session session;
    
    private static CopyOnWriteArraySet<IopWebSocket> iopWebSockets =new CopyOnWriteArraySet<>();
    private static Map<String,Session> sessionPool = new HashMap<String,Session>();
    
    @OnOpen
    public void onOpen(Session session, @PathParam(value="userId")String userId) {
        try {
			this.session = session;
			iopWebSockets.add(this);
			sessionPool.put(userId, session);
			log.info("【websocketIOP消息】有新的连接，总数为:"+ iopWebSockets.size());
		} catch (Exception e) {
		}
    }
    
    @OnClose
    public void onClose() {
        try {
			iopWebSockets.remove(this);
			log.info("【websocketIOP消息】连接断开，总数为:"+ iopWebSockets.size());
		} catch (Exception e) {
		}
    }
    
    @OnMessage
    public void onMessage(String message) {
        //todo 现在有个定时任务刷，应该去掉
    	log.debug("【websocketIOP消息】收到客户端消息:"+message);
    	JSONObject obj = new JSONObject();
    	obj.put("cmd", "heartcheck");//业务类型
    	obj.put("msgTxt", "心跳响应");//消息内容
    	session.getAsyncRemote().sendText(obj.toJSONString());
    }
    
    // 此为广播消息
    public void sendAllMessage(String message) {
    	log.info("【websocketIOP消息】广播消息:"+message);
        for(IopWebSocket iopWebSocket : iopWebSockets) {
            try {
            	if(iopWebSocket.session.isOpen()) {
            		iopWebSocket.session.getAsyncRemote().sendText(message);
            	}
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    
    // 此为单点消息
    public void sendOneMessage(String userId, String message) {
        Session session = sessionPool.get(userId);
        if (session != null&&session.isOpen()) {
            try {
            	log.info("【websocketIOP消息】 单点消息:"+message);
                session.getAsyncRemote().sendText(message);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    
    // 此为单点消息(多人)
    public void sendMoreMessage(String[] userIds, String message) {
    	for(String userId:userIds) {
    		Session session = sessionPool.get(userId);
            if (session != null&&session.isOpen()) {
                try {
                	log.info("【websocketIOP消息】 单点消息:"+message);
                    session.getAsyncRemote().sendText(message);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
    	}
        
    }
    
}