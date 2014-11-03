package no.sonat.external.supplier2.model;

import java.util.Objects;
import java.util.Optional;

public class OrderReference {
    private final long orderId;

    private final OrderState orderState;

    public OrderReference() {
        this(0L, OrderState.OPEN);
    }

    public OrderReference(final long orderId, final OrderState orderState) {
        this.orderId = orderId;
        this.orderState = orderState;
    }

    public static OrderReference create(final long id, final OrderState orderState) {
        return new OrderReference(id, orderState);
    }

    public static OrderReference createForOrder(final Order order) {
        return create(order.getId().orElse(null), order.getOrderState());
    }

    public long getOrderId() {
        return orderId;
    }

    public OrderState getOrderState() {
        return orderState;
    }

    @Override
    public int hashCode() {
        return Objects.hash(orderId, orderState);
    }

    @Override
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        final OrderReference other = (OrderReference) obj;
        return Objects.equals(this.orderId, other.orderId) && Objects.equals(this.orderState, other.orderState);
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("OrderReference{");
        sb.append("orderId=").append(orderId);
        sb.append(", orderState=").append(orderState);
        sb.append('}');
        return sb.toString();
    }
}
