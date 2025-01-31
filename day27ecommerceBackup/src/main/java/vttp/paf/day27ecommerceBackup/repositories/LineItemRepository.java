package vttp.paf.day27ecommerceBackup.repositories;

import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import static vttp.paf.day27ecommerceBackup.constants.Queries.*;

@Repository
public class LineItemRepository {

    @Autowired
    private JdbcTemplate template;

    public void insertLineItem(Document liDocument) {
        System.out.printf("*** Inserting %s of %s\n", 
            liDocument.getString("name"), 
            liDocument.getString("po_id"));

        template.update(SQL_INSERT_LI, 
            liDocument.getString("name"),
            liDocument.getInteger("quantity"),
            liDocument.getDouble("unit_price"),
            liDocument.getString("po_id"));
    }
    
}
