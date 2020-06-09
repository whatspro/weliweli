package message.listener;

import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.databind.ser.std.StringSerializer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.serializer.GenericToStringSerializer;
import org.springframework.data.redis.serializer.RedisSerializer;
import redis.clients.jedis.JedisPubSub;

@Slf4j
public class NewMessageListener extends JedisPubSub {


    StringSerializer stringSerializer = new StringSerializer();
    RedisSerializer<String> valueSeiralizer = new GenericToStringSerializer(Object.class);

    @Override
    public void onMessage(String channel, String message) {
        String jsonMsg = valueSeiralizer.deserialize(message.getBytes());
        log.info("Message Received --> channel: {}, message: {}", channel, jsonMsg);
        JSONObject jsonObject = JSONObject.parseObject(jsonMsg);
        long otherUid = jsonObject.getLong("otherUid");
        JSONObject pushObject = new JSONObject();
        pushObject.put("type", 0);
        pushObject.put("data", jsonObject);
    }
}
