package message.ws.handler;

import com.alibaba.fastjson.JSONObject;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.handler.codec.http.websocketx.WebSocketFrame;
import io.netty.util.AttributeKey;
import lombok.extern.slf4j.Slf4j;
import message.service.EnhancedThreadFactory;
import message.service.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.atomic.AtomicLong;

@Slf4j
@Component
public class WebSocketRouterHandler extends SimpleChannelInboundHandler<WebSocketFrame> {
    private static final ConcurrentHashMap<Long, Channel> userChannel = new
            ConcurrentHashMap<>(15000);
    private static final ConcurrentHashMap<Channel, Long> channelUser = new
            ConcurrentHashMap<>(15000);
    private ScheduledExecutorService executorService = new ScheduledThreadPoolExecutor(50,
            new EnhancedThreadFactory("ackCheckingThreadPool"));
    private static final AttributeKey<AtomicLong> TID_GENERATOR = AttributeKey.valueOf("tid_generator");
    private static final AttributeKey<ConcurrentHashMap> NON_ACKED_MAP = AttributeKey.valueOf("non_acked_map");

    @Autowired
    private MessageService messageService;

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, WebSocketFrame frame) throws Exception {
        if (frame instanceof TextWebSocketFrame) {
            String msg = ((TextWebSocketFrame) frame).text();
            JSONObject msgJson = JSONObject.parseObject(msg);
            int type = msgJson.getIntValue("type");
            JSONObject data = msgJson.getJSONObject("data");
            switch (type) {
                case 0: //心跳
                    long uid = data.getLongValue("uid");
                    long timeout = data.getLong("timeout");
                    log.info("[heartbeat]: uid = {}, current timeout is {}ms, " +
                                    "channek = {}", uid, timeout, ctx.channel());
                    ctx.writeAndFlush(new TextWebSocketFrame("{\"type\":0,\"timeout\":"+timeout+"}"));
                    break;
                case 1:  //上线
                    long loginUid = data.getLong("uid");
                    userChannel.put(loginUid, ctx.channel());
                    channelUser.put(ctx.channel(), loginUid);
                    ctx.channel().attr(TID_GENERATOR).set(new AtomicLong(0));
                    ctx.channel().attr(NON_ACKED_MAP).set(new ConcurrentHashMap<Long, JSONObject>());
                    log.info("[user bind]: uid = {} , channel = {}", loginUid, ctx.channel());
                    ctx.writeAndFlush(new TextWebSocketFrame("{\"type\":1,\"status\":\"success\"}"));
                    break;
                case 2: //查询消息
                    Long ownerUid = data.getLong("ownerUid");
                    Long otherUid = data.getLong("otherUid");

            }
        }
    }
}
