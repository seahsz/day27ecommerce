package vttp.paf.day27ecommerceBackup.services;

import java.io.StringReader;
import java.time.Duration;
import java.util.Optional;

import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.RedisTemplate;

import jakarta.json.Json;
import jakarta.json.JsonObject;
import jakarta.json.JsonReader;
import vttp.paf.day27ecommerceBackup.repositories.EventRepository;

public class MessageWorker implements Runnable {

    private final RedisTemplate<String, String> template;
    private final String name;
    private final EventRepository eventRepo;

    public MessageWorker(RedisTemplate<String, String> template, String name, EventRepository eventRepo) {
        this.template = template;
        this.name = name;
        this.eventRepo = eventRepo;
    }

    private static final String REDIS_LIST_KEY = "message:queue";

    public void run() {
        System.out.println("*** Starting worker thread");

        ListOperations<String, String> listOps = template.opsForList();

        while (true) {
            try {
                System.out.println("*** Queuing");

                // The duration parameter is saying that "if list is empty, wait for 30s, if still nothing
                // return null"
                Optional<String> opt = Optional.ofNullable(listOps.rightPop(REDIS_LIST_KEY, Duration.ofSeconds(30)));
                if (opt.isEmpty())
                    continue;

                String payload = opt.get();
                System.out.printf("*** %s Processing data: %s\n", name, payload);

                // Add the event to the event store
                eventRepo.insertEvent(payload);

                JsonReader reader = Json.createReader(new StringReader(payload));
                JsonObject obj = reader.readObject();
                eventRepo.insertEventUnread(obj.getString("event_id"));

                System.out.printf("*** %s Finished processing data\n", name);

                
            } catch (Exception ex) {
                System.err.printf(">>>> exception: %s\n", ex.getMessage());
                ex.printStackTrace();
            }
        }
    }
    
}
