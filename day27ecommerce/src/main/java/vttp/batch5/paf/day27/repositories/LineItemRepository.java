package vttp.batch5.paf.day27.repositories;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import vttp.batch5.paf.day27.models.PurchaseOrder;

import static vttp.batch5.paf.day27.constants.Queries.*;

@Repository
public class LineItemRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public void insertLineItems(PurchaseOrder po) {

        List<Object[]> params = po.getLineItems()
            .stream()
            .map(li -> {
                Object[] record = new Object[4];
                record[0] = li.getName();
                record[1] = li.getQuantity();
                record[2] = li.getUnitPrice();
                record[3] = po.getPoId();
                return record;
            }).toList();

        jdbcTemplate.batchUpdate(SQL_INSERT_LI, params);            
    }
    
}
