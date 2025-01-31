package vttp.batch5.paf.day27.repositories;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import vttp.batch5.paf.day27.models.PurchaseOrder;

import static vttp.batch5.paf.day27.constants.Queries.*;

@Repository
public class PurchaseOrderRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public void insertPo(PurchaseOrder po) {

        // Use update for insert also
        jdbcTemplate.update(SQL_INSERT_PO,
            po.getPoId(),
            po.getName(),
            po.getAddress(),
            po.getDeliveryDate());
    }

}
