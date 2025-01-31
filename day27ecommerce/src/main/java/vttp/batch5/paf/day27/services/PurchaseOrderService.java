package vttp.batch5.paf.day27.services;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jakarta.json.Json;
import jakarta.json.JsonObject;
import vttp.batch5.paf.day27.models.LineItem;
import vttp.batch5.paf.day27.models.PurchaseOrder;
import vttp.batch5.paf.day27.repositories.EventRepository;
import vttp.batch5.paf.day27.repositories.LineItemRepository;
import vttp.batch5.paf.day27.repositories.MessageRepository;
import vttp.batch5.paf.day27.repositories.PurchaseOrderRepository;

import static vttp.batch5.paf.day27.Utils.*;

@Service
public class PurchaseOrderService {

    @Autowired
    private PurchaseOrderRepository poRepo;

    @Autowired
    private LineItemRepository liRepo;

    @Autowired
    private EventRepository eventRepo;

    @Autowired
    private MessageRepository messageRepo;

    public String createPurchaseOrder() {
        String poId = UUID.randomUUID().toString().substring(0, 8);

        return poId;
    }

    @Transactional
    public void insertPo(PurchaseOrder po) {
        poRepo.insertPo(po);
        liRepo.insertLineItems(po);
    }

    public void createEvents(PurchaseOrder po) {
        JsonObject poEvent = createPoEvent(po, "ADD", "purchase_orders");
        
        List<JsonObject> liEvents = po.getLineItems()
            .stream()
            .map(li -> createLiEvent(li, "ADD", "line_items"))
            .toList();     
            
        // Save events to Mongo
        eventRepo.insertEvent(poEvent);
        eventRepo.insertEventsByBatch(liEvents);

        // Publish event as message to Redis
        messageRepo.publish(poEvent);
        for (JsonObject event: liEvents)
            messageRepo.publish(event);
    }

    private JsonObject createPoEvent(PurchaseOrder po, String operation, String table) {
        String eventId = UUID.randomUUID().toString().substring(0, 8);

        return Json.createObjectBuilder()
                .add("event_id", eventId)
                .add("timestamp", getEventTime())
                .add("ops", operation)
                .add("fields", po.toJsonObject())
                .add("table", table)
                .build();
    }

    private JsonObject createLiEvent(LineItem li, String operation, String table) {
        String eventId = UUID.randomUUID().toString().substring(0, 8);

        return Json.createObjectBuilder()
                .add("event_id", eventId)
                .add("timestamp", getEventTime())
                .add("ops", operation)
                .add("fields", li.toJsonObject())
                .add("table", table)
                .build();
    }

}
