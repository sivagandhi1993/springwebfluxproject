package com.learnreactivespring.fluxandmonoplayground;

import org.junit.Test;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

import java.time.Duration;

public class FluxAndMonoWithTimeTest {

    @Test
    public void infiniteSequence() throws InterruptedException {
       Flux<Long> infiniteFlux = Flux.interval(Duration.ofMillis(200)).log();
       infiniteFlux.subscribe((e)-> System.out.println("Value is :"+ e));

       Thread.sleep(3000);

    }

    @Test
    public void infiniteSequenceTest() throws InterruptedException {
        Flux<Long> infiniteFlux = Flux.interval(Duration.ofMillis(200)).take(3).log();
        infiniteFlux.subscribe((e)-> System.out.println("Value is :"+ e));

        Thread.sleep(3000);

        StepVerifier.create(infiniteFlux).expectSubscription().expectNext(0L,1L,2L).verifyComplete();

    }
}
