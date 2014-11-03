package no.sonat.external.supplier2.repository;

import no.sonat.external.supplier2.model.Order;
import no.sonat.external.supplier2.model.OrderLine;
import no.sonat.external.supplier2.model.OrderReference;
import no.sonat.external.supplier2.model.OrderState;
import org.junit.Before;

import java.math.BigDecimal;
import java.util.Arrays;

import static org.junit.Assert.*;

public class StaticRepositoryTest {


    private StaticRepository staticRepository;
    private OrderReference runningOrder;
    private OrderReference openOrder;
    private OrderReference completedOrder;

    @Before
    public void setUp() throws Exception {
        staticRepository = new StaticRepository();
        runningOrder = staticRepository.storeOrder(new Order(null, Arrays.asList(new OrderLine(1L, 22L, "Foo Bar", BigDecimal.valueOf(9d))), OrderState.RUNNING));
        openOrder = staticRepository.storeOrder(new Order(null, Arrays.asList(new OrderLine(1L, 22L, "Foo Bar", BigDecimal.valueOf(9d))), OrderState.OPEN));
        completedOrder = staticRepository.storeOrder(new Order(null, Arrays.asList(new OrderLine(2L, 22L, "Foo Bar", BigDecimal.valueOf(9d))), OrderState.COMPLETED));

    }

    @org.junit.Test
    public void testStoreOrder() throws Exception {
        assertNotNull(staticRepository.storeOrder(new Order(null, Arrays.asList(new OrderLine(1L, 22L, "Foo Bar", BigDecimal.valueOf(9d))), OrderState.RUNNING)));
    }

    @org.junit.Test
    public void testFindOrder() throws Exception {
        assertTrue(staticRepository.findOrder(completedOrder.getOrderId()).isPresent());
        assertFalse(staticRepository.findOrder(Long.MAX_VALUE).isPresent());
    }

    @org.junit.Test
    public void testCheckOrder() throws Exception {
        assertEquals(runningOrder, staticRepository.checkOrder(runningOrder.getOrderId()).get());
    }

    @org.junit.Test(expected = IllegalStateException.class)
    public void testAbortRunningOrder() throws Exception {
        final OrderReference orderReference = staticRepository.storeOrder(new Order(null, Arrays.asList(new OrderLine(1L, 22L, "Foo Bar", BigDecimal.valueOf(9d))), OrderState.RUNNING));
        assertEquals(OrderState.ABORTED, staticRepository.abortRunningOrder(orderReference.getOrderId()).getOrderState());
        staticRepository.abortRunningOrder(orderReference.getOrderId());

    }

    @org.junit.Test
    public void testTerminateRunningOrder() throws Exception {
        final OrderReference orderReference = staticRepository.storeOrder(new Order(null, Arrays.asList(new OrderLine(1L, 22L, "Foo Bar", BigDecimal.valueOf(9d))), OrderState.RUNNING));
        assertEquals(OrderState.ABORTED, staticRepository.abortRunningOrder(orderReference.getOrderId()).getOrderState());

    }
}