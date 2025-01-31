package vttp.batch5.paf.day27.repositories;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import jakarta.json.JsonObject;

@Repository
public class MessageRepository {

    private static final String REDIS_LIST_KEY = "message:queue";

    @Autowired
    @Qualifier("redis-string")
    private RedisTemplate<String, String> redisTemplate;

    public void publish(JsonObject message) {
        System.out.printf("*** Publishing event with event_id: %s\n", message.getString("event_id"));
        redisTemplate.opsForList().leftPush(REDIS_LIST_KEY, message.toString());
    }    
}
