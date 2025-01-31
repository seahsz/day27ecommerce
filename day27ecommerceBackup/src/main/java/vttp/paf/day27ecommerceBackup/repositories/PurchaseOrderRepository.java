package vttp.paf.day27ecommerceBackup.repositories;

import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import static vttp.paf.day27ecommerceBackup.constants.Queries.*;

@Repository
public class PurchaseOrderRepository {

    @Autowired
    private JdbcTemplate template;

    public void insertPo(Document poDocument) {
        System.out.printf("*** Inserting %s\n", poDocument.getString("po_id"));
        template.update(SQL_INSERT_PO,
            poDocument.getString("po_id"),
            poDocument.getString("name"),
            poDocument.getString("address"),
            poDocument.getString("delivery_date"));    
    }
    
}
