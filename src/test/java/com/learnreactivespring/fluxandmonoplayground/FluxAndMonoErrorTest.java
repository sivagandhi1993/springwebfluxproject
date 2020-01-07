package com.learnreactivespring.fluxandmonoplayground;

import org.junit.Test;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

import java.time.Duration;

public class FluxAndMonoErrorTest {

    @Test
    public void fluxErrorHandling() {
        Flux<String> stringFlux = Flux.just("A", "B", "C")
                .concatWith(Flux.error(new RuntimeException()))
                .concatWith(Flux.just("D"))
                .onErrorResume(s -> {
                    System.out.println("Exception is : " + s);
                    return Flux.just("default", "default1");
                });
        StepVerifier.create(stringFlux.log()).expectSubscription()
                .expectNext("A", "B", "C")
                .expectNext("default", "default1")
                .verifyComplete();
    }

    @Test
    public void fluxErrorHandling_onErrorReturn() {
        Flux<String> stringFlux = Flux.just("A", "B", "C")
                .concatWith(Flux.error(new RuntimeException()))
                .concatWith(Flux.just("D"))
                .onErrorReturn("default");
        StepVerifier.create(stringFlux.log()).expectSubscription()
                .expectNext("A", "B", "C")
                .expectNext("default")
                .verifyComplete();
    }

    @Test
    public void fluxErrorHandling_onErrorMap() {
        Flux<String> stringFlux = Flux.just("A", "B", "C")
                .concatWith(Flux.error(new RuntimeException()))
                .concatWith(Flux.just("D"))
                .onErrorMap(p -> new CustomException(p));
        StepVerifier.create(stringFlux.log()).expectSubscription()
                .expectNext("A", "B", "C")
                .expectError(CustomException.class)
                .verify();
    }

    @Test
    public void fluxErrorHandling_onErrorMap_withRetry() {
        Flux<String> stringFlux = Flux.just("A", "B", "C")
                .concatWith(Flux.error(new RuntimeException()))
                .concatWith(Flux.just("D"))
                .onErrorMap(p -> new CustomException(p))
                .retry(2);
        StepVerifier.create(stringFlux.log()).expectSubscription()
                .expectNext("A", "B", "C")
                .expectNext("A", "B", "C")
                .expectNext("A", "B", "C")
                .expectError(CustomException.class)
                .verify();
    }

    @Test
    public void fluxErrorHandling_onErrorMap_withRetryBackoff() {
        System.out.println(Runtime.getRuntime().availableProcessors());
        Flux<String> stringFlux = Flux.just("A", "B", "C")
                .concatWith(Flux.error(new RuntimeException()))
                .concatWith(Flux.just("D"))
                .onErrorMap(p -> new CustomException(p))
                .retryBackoff(2, Duration.ofSeconds(2));
        StepVerifier.create(stringFlux.log()).expectSubscription()
                .expectNext("A", "B", "C")
                .expectNext("A", "B", "C")
                .expectNext("A", "B", "C")
                .expectError(IllegalStateException.class)
                .verify();
    }
}
