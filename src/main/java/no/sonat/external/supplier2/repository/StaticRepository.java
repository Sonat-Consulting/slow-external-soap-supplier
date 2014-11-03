package no.sonat.external.supplier2.repository;

import no.sonat.external.supplier2.model.Order;
import no.sonat.external.supplier2.model.OrderReference;
import no.sonat.external.supplier2.model.OrderState;

import java.util.Collections;
import java.util.Map;
import java.util.Optional;
import java.util.TreeMap;
import java.util.concurrent.atomic.AtomicLong;

public class StaticRepository implements StoreRepository {

    private static AtomicLong sequence = new AtomicLong(0L);
    private static Map<Long, Order> orders = Collections.synchronizedMap(new TreeMap<>());

    @Override
    public OrderReference storeOrder(final Order order) {
        final long id = sequence.getAndAdd(1L);
        final Order runningOrder = order.cloneWithId(id).cloneWithNewState(OrderState.RUNNING);
        orders.put(id, runningOrder);
        return new OrderReference(id, runningOrder.getOrderState());
    }

    @Override
    public Optional<Order> findOrder(final long orderId) {
        return Optional.ofNullable(orders.get(orderId));
    }

    @Override
    public Optional<OrderReference> checkOrder(final long orderId) {
        return findOrder(orderId).map(o -> OrderReference.create(o.getId().map(Long::longValue).orElseThrow(() -> new IllegalStateException("Unknown ID for order")), o.getOrderState()));
    }

    @Override
    public OrderReference abortRunningOrder(final long orderId) {
        return findOrder(orderId).filter(o -> OrderState.RUNNING.equals(o.getOrderState())).map(o -> {
            orders.put(orderId, o.cloneWithNewState(OrderState.ABORTED));
            return new OrderReference(orderId, OrderState.ABORTED);

        }).orElseThrow(() -> new IllegalStateException(String.format("Can not abort order \"%s\" ", orderId)));
    }

    @Override
    public OrderReference terminateRunningOrder(final long orderId) {
        return findOrder(orderId).filter(o -> OrderState.RUNNING.equals(o.getOrderState())).map(o -> {
            orders.put(orderId, o.cloneWithNewState(OrderState.ABORTED));
            return new OrderReference(orderId, OrderState.ABORTED);
        }).orElseThrow(() -> new IllegalStateException(String.format("Can not terminate unknown order \"%s\"", orderId)));
    }

    @Override
    public OrderReference completeRunningOrder(final long orderId) {
        return findOrder(orderId).filter(o -> o.getOrderState() == OrderState.RUNNING).map(o ->  {
            orders.put(orderId, o.cloneWithNewState(OrderState.COMPLETED));
            return new OrderReference(orderId, OrderState.COMPLETED);
        }).orElseThrow(() -> new IllegalStateException(String.format("Can not complete unknown order \"%s\"")));
    }
}
