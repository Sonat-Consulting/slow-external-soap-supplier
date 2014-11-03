package no.sonat.external.supplier2.model;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class Order implements Cloneable {

    private Long id;

    private List<OrderLine> orderLines;

    private OrderState orderState = OrderState.OPEN;

    public Order(final Long id, final List<OrderLine> orderLines, final OrderState orderState) {
        this.id = id;
        this.orderLines = orderLines;
        this.orderState = orderState;
    }

    public Order() {
    }

    public Optional<Long> getId() {
        return Optional.ofNullable(id);
    }

    public List<OrderLine> getOrderLines() {
        return orderLines;
    }

    public OrderState getOrderState() {
        return orderState;
    }

    public static Order createNew(OrderLine... orderLines) {
        return createNew(Arrays.asList(orderLines));
    }

    public static Order createNew(List<OrderLine> orderLines) {
        return new Order(null, orderLines, OrderState.OPEN);
    }

    public Order cloneWithNewState(final OrderState orderState) {
        return new Order(id, orderLines, orderState);
    }

    public Order cloneWithId(final Long id) {
        return new Order(id, orderLines, orderState);
    }

    @Override
    public Order clone() {
        return new Order(null, orderLines, orderState);
    }
}

