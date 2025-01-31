package vttp.paf.day27ecommerceBackup.services;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import vttp.paf.day27ecommerceBackup.repositories.EventRepository;

@Service
public class MessageProcessor {

    @Autowired
    @Qualifier("redis-string")
    private RedisTemplate<String, String> redisTemplate;

    @Autowired
    private EventRepository eventRepo;

    // Creating a thread
    @Async
    public void start() {

        // You would want to submit number of workers = thread (in this case 2)
        ExecutorService thrPool = Executors.newFixedThreadPool(2);
        thrPool.submit(new MessageWorker(redisTemplate, "w0", eventRepo));
        thrPool.submit(new MessageWorker(redisTemplate, "w1", eventRepo));
    }
    
}
