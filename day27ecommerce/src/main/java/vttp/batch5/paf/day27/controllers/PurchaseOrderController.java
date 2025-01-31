package vttp.batch5.paf.day27.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import jakarta.json.Json;
import jakarta.json.JsonObject;
import vttp.batch5.paf.day27.Utils;
import vttp.batch5.paf.day27.models.PurchaseOrder;
import vttp.batch5.paf.day27.services.PurchaseOrderService;

@Controller
@RequestMapping(path="/api", produces = MediaType.APPLICATION_JSON_VALUE)
public class PurchaseOrderController {

  @Autowired
  private PurchaseOrderService poSvc;

  @PostMapping(path="/purchaseorder", consumes = MediaType.APPLICATION_JSON_VALUE)
  @ResponseBody
  public ResponseEntity<String> postPurchaseOrder(@RequestBody String payload) {

    String poId = poSvc.createPurchaseOrder();

    PurchaseOrder po = Utils.toPurchaseOrder(payload, poId);
    System.out.printf(">> po: %s\n", po);

    // insert po and lis into SQL database
    poSvc.insertPo(po);

    // create event and store in mongodb
    poSvc.createEvents(po);

    // propagate event using Redis messaging

    // Returns the poId as JSON object
    JsonObject resp = Json.createObjectBuilder()
        .add("poId", poId)
        .build();

    return ResponseEntity.ok(resp.toString());
  }

}
