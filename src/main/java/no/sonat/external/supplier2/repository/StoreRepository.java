package no.sonat.external.supplier2.repository;

import no.sonat.external.supplier2.model.Order;
import no.sonat.external.supplier2.model.OrderReference;

import java.util.Optional;

/**
 * @author sondre
 */
public interface StoreRepository {
    OrderReference storeOrder(Order order);

    Optional<Order> findOrder(long orderId);

    Optional<OrderReference> checkOrder(long orderId);

    OrderReference abortRunningOrder(long orderId);

    OrderReference terminateRunningOrder(long orderId);

    OrderReference completeRunningOrder(final long orderId);
}
