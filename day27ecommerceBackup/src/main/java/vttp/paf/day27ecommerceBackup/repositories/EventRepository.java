package vttp.paf.day27ecommerceBackup.repositories;

import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import com.mongodb.client.result.DeleteResult;

import static vttp.paf.day27ecommerceBackup.constants.MongoConstants.*;

import java.util.List;

@Repository
public class EventRepository {

    @Autowired
    private MongoTemplate template;

    /*
     * db.events.insert({
       event_id: <an_event_id>,
       timestamp: <a_timestamp>,
       ops: <an_operation>,
       fields: [
        {po_id: <a_po_id>},
        {name: <a_name>},
        {address: <an_address>},
        {delivery_date: <a_delivery_date>}
       ],
       table: <a_table_name>
       })
     */
    public void insertEvent(String event) {
        template.insert(Document.parse(event), C_EVENTS);
    }

    /*
     * db.events.find({
       event_id: {$in: ["<event_id_1>", "<event_id_2>"]}
       })
     */
    public List<Document> findEventsById(List<String> eventIds) {
        Criteria criteria = Criteria.where(F_EVENT_ID).in(eventIds);
        Query query = Query.query(criteria);
        return template.find(query, Document.class, C_EVENTS);
    }

    /*
     * db.unread_events.insert({
       event_id: <an_event_id>
       })
     */
    public void insertEventUnread(String eventId) {
        Document doc = new Document(F_EVENT_ID, eventId);
        template.insert(doc, C_UNREAD_EVENTS);
    }

    /*
     * db.unread_events.find({})
     */
    public List<Document> getAllUnreadEvents() {
        return template.findAll(Document.class, C_UNREAD_EVENTS);
    }

    /*
     * db.unread_events.deleteMany({
       event_id:
        {
            $in: ["<event_id_1>", "<event_id_2>"]
        }
       })
     */
    public void deleteEventsFromUnreadById(List<String> eventIds) {
        Criteria criteria = Criteria.where(F_EVENT_ID).in(eventIds);
        Query query = Query.query(criteria);
        DeleteResult result = template.remove(query, C_UNREAD_EVENTS);
        
        System.out.printf("*** Deleted documents: %d\n", result.getDeletedCount());
    }
    
}
