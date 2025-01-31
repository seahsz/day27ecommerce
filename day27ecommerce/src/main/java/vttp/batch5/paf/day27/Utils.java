package vttp.batch5.paf.day27;

import vttp.batch5.paf.day27.models.PurchaseOrder;

import java.io.StringReader;
import java.text.SimpleDateFormat;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;

import jakarta.json.Json;
import jakarta.json.JsonArray;
import jakarta.json.JsonObject;
import jakarta.json.JsonReader;
import jakarta.json.JsonValue;
import vttp.batch5.paf.day27.models.LineItem;

public class Utils {

    public static PurchaseOrder toPurchaseOrder(String payload, String poId) {

        JsonReader reader = Json.createReader(new StringReader(payload));
        JsonObject jo = reader.readObject();

        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");

        PurchaseOrder po = new PurchaseOrder();
        po.setPoId(jo.getString("poId", ""));
        po.setName(jo.getString("name"));
        po.setAddress(jo.getString("address"));
        po.setPoId(poId);
        try {
            po.setDeliveryDate(df.parse(jo.getString("deliveryDate")));
        } catch (Exception ex) {
            po.setDeliveryDate(new Date());
        }
        po.setLineItems(toLineItem(jo.getJsonArray("lineItems"), poId));

        return po;
    }

    public static List<LineItem> toLineItem(JsonArray arr, String poId) {

        return  arr.stream()
            .map(JsonValue::asJsonObject)
            .map(jo -> {
                LineItem li = new LineItem();
                li.setName(jo.getString("name"));
                li.setQuantity(jo.getInt("quantity"));
                li.setUnitPrice((float)jo.getJsonNumber("unitPrice").doubleValue());
                li.setPoId(poId);
                return li;
            }).toList();

    }

    public static String getEventTime() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss z");
        ZonedDateTime currTime = ZonedDateTime.now();

        return currTime.format(formatter);
    }

}
