package no.sonat.external.supplier2;

import no.sonat.external.supplier2.model.Order;
import no.sonat.external.supplier2.model.OrderReference;
import no.sonat.external.supplier2.model.OrderState;
import no.sonat.external.supplier2.repository.StaticRepository;
import no.sonat.external.supplier2.repository.StoreRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Optional;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * @author sondre
 */
public class OrderStateMachine {

    private static ScheduledExecutorService executorService = Executors.newScheduledThreadPool(3);

    private static Logger logger = LoggerFactory.getLogger(OrderStateMachine.class);

    private final StoreRepository storeRepository;

    public OrderStateMachine(final StoreRepository storeRepository) {
        this.storeRepository = storeRepository;
    }

    public OrderReference placeOrder(final Order order) {

        final OrderReference originalOrderReference = storeRepository.storeOrder(order);
        final long orderId = originalOrderReference.getOrderId();
        executorService.schedule(new Runnable() {
            @Override
            public void run() {
                final Optional<OrderReference> reference = storeRepository.checkOrder(orderId);
                if(reference.map(OrderReference::getOrderState).filter(s -> OrderState.RUNNING.equals(s)).isPresent()) {
                    if(randomBoolean()) {
                        logger.info("Aborting order \"{}\"", orderId);
                        storeRepository.abortRunningOrder(orderId);
                    } else {
                        logger.info("Completing order \"{}\"", orderId);
                        storeRepository.completeRunningOrder(orderId);
                    }
                } else {
                    logger.warn("Could not complete or abort order \"{}\"", orderId);
                }
            }
        }, randomInMinutes(), TimeUnit.MINUTES);
        return originalOrderReference;
    }

    static long randomInMinutes() {
        return (new Random(System.currentTimeMillis()).nextLong() % 4L) + 1L;
    }

    static boolean randomBoolean() {
        return new Random(System.currentTimeMillis()).nextBoolean();
    }
}
