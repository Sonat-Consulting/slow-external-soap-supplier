package no.sonat.external.supplier2;

import no.sonat.external.supplier2.model.Order;
import no.sonat.external.supplier2.model.OrderLine;
import no.sonat.external.supplier2.model.OrderReference;
import no.sonat.external.supplier2.model.OrderState;
import org.junit.Test;

import java.math.BigDecimal;
import static org.junit.Assert.*;

public class SupplierServiceTest {

    private SupplierService supplierService = new SupplierService();

    @Test
    public void testPlaceOrder() throws Exception {
        final OrderReference orderStuff = supplierService.placeOrder(Order.createNew(new OrderLine(2L, 44L, "stuff", BigDecimal.valueOf(22L))));
        assertEquals(OrderState.RUNNING, orderStuff.getOrderState());
    }

    @Test
    public void testPlaceOrderAndWaitForCompletion() throws Exception {
        final OrderReference orderStuff = supplierService.placeOrder(Order.createNew(new OrderLine(2L, 44L, "stuff", BigDecimal.valueOf(22L))));

    }

    @Test
    public void testCheckOrder() throws Exception {

    }

    @Test
    public void testCancelOrder() throws Exception {

    }
}