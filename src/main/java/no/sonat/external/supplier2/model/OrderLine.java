package no.sonat.external.supplier2.model;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.util.Objects;

public class OrderLine implements Cloneable {

    private long quanta;

    private long itemId;

    private String description;

    private BigDecimal itemPrice;

    public OrderLine() {
    }

    public OrderLine(final long quanta, final long itemId, final String description, final BigDecimal itemPrice) {
        this.quanta = quanta;
        this.itemId = itemId;
        Objects.requireNonNull(description);
        this.description = description;
        Objects.requireNonNull(itemPrice);
        this.itemPrice = itemPrice;
    }

    public long getQuanta() {
        return quanta;
    }

    public long getItemId() {
        return itemId;
    }

    public String getDescription() {
        return description;
    }

    public double getItemPrice() {
        return itemPrice.doubleValue();
    }


    public double subTotal() {
        return itemPrice.multiply(BigDecimal.valueOf(quanta)).round(new MathContext(2, RoundingMode.CEILING)).doubleValue();
    }

    @Override
    protected OrderLine clone() throws CloneNotSupportedException {
        return new OrderLine(quanta, itemId, description, itemPrice);
    }
}
