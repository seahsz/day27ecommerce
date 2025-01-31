package vttp.batch5.paf.day27.repositories;

import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Repository;

import jakarta.json.JsonObject;

import static vttp.batch5.paf.day27.constants.MongoConstants.*;

import java.util.List;

@Repository
public class EventRepository {

    @Autowired
    private MongoTemplate mongoTemplate;

    public void insertEvent(JsonObject event) {
        mongoTemplate.insert(Document.parse(event.toString()), C_EVENTS);
    }

    public void insertEventsByBatch(List<JsonObject> events) {
        List<Document> strs = events.stream()
            .map(jo -> Document.parse(jo.toString()))
            .toList();

        mongoTemplate.insert(strs, C_EVENTS);
    }
    
}
