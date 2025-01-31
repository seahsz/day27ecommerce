package vttp.paf.day27ecommerceBackup.services;

import java.util.List;

import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import vttp.paf.day27ecommerceBackup.repositories.EventRepository;
import vttp.paf.day27ecommerceBackup.repositories.LineItemRepository;
import vttp.paf.day27ecommerceBackup.repositories.PurchaseOrderRepository;

@Service
public class EventService {

    @Autowired
    private EventRepository eventRepo;

    @Autowired
    private PurchaseOrderRepository poRepo;

    @Autowired
    private LineItemRepository liRepo;

    @Scheduled(fixedRate = 60000) // 5 minutes
    public void updateSqlDatabase() {

        // Query for ALL unread events in <unread_events> collection in MongoDb
        List<Document> results = eventRepo.getAllUnreadEvents();

        if (results.isEmpty()) {
            System.out.println("No unprocessed events found");
            return;
        }

        List<String> unreadEventIds = results.stream()
            .map(doc -> doc.getString("event_id"))
            .toList();

        // Do a batch query in <events> collection in MongoDb
        List<Document> unreadEvents = eventRepo.findEventsById(unreadEventIds);

        System.out.printf("*** Fetched %s events to update\n", unreadEvents.size());

        for (Document event: unreadEvents) {

            // Extract the relevant information
            String ops = event.getString("ops");

            if (ops.equalsIgnoreCase("ADD")) {
                switch (event.getString("table")) {
                    case "line_items":
                        liRepo.insertLineItem(event.get("fields", Document.class));
                        break;

                    case "purchase_orders":
                        poRepo.insertPo(event.get("fields", Document.class));;
                        break;
                    default:
                        System.out.println("**** Entering default case");
                        break;
                }
            }
        }

        // Perform a batch removal in <unread_events> collection in MongoDb
        eventRepo.deleteEventsFromUnreadById(unreadEventIds);

        System.out.println("Completed scheduled task, will read again in 5 minutes");
    }
    
}
