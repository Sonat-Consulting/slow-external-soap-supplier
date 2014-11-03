package no.sonat.external.supplier2;

import no.sonat.external.supplier2.model.Order;
import no.sonat.external.supplier2.model.OrderReference;
import no.sonat.external.supplier2.model.OrderState;
import no.sonat.external.supplier2.repository.StaticRepository;
import no.sonat.external.supplier2.repository.StoreRepository;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;
import java.util.Objects;

@WebService(name = "SlowSuppliervice")
public class SupplierService {

    // Keeping these static to avoid them being reinitialized
    private static OrderStateMachine orderStateMachine;
    private static StoreRepository storeRepository;

    static {
        storeRepository = new StaticRepository();
        orderStateMachine = new OrderStateMachine(storeRepository);
    }

    @WebMethod
    public OrderReference placeOrder(@WebParam(name = "order") final Order order) {

        Objects.requireNonNull(order);
        if(order.getOrderState() != OrderState.OPEN) {
            throw new IllegalStateException("Only OPEN orders may be placed");
        }

        return orderStateMachine.placeOrder(order);
    }

    @WebMethod
    public OrderReference placeOrderAndWaitForCompletion(@WebParam(name = "order") final Order order) {
        OrderReference orderReference = placeOrder(order);
        while(OrderState.RUNNING.equals(orderReference.getOrderState())) {
            orderReference = checkOrder(orderReference.getOrderId());
        }
        return orderReference;
    }

    @WebMethod
    public OrderReference checkOrder(@WebParam(name = "orderId") final long orderId) {
        return storeRepository.checkOrder(orderId).orElseThrow(() -> new IllegalStateException(String.format("Found no order with orderId \"%s\"", orderId)));
    }

    @WebMethod
    public OrderReference cancelOrder(@WebParam(name = "orderId") final long orderId) {
        return storeRepository.terminateRunningOrder(orderId);
    }
}
