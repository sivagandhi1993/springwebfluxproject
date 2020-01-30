package com.learnreactivespring.fluxandmonoplayground;

import org.junit.Test;
import reactor.core.publisher.BaseSubscriber;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

import java.util.Objects;

public class FluxAndMonoBackpressureTest {

    @Test
    public void backPressueTest(){
        Flux<Integer> integerFlux = Flux.range(1,10);
        StepVerifier.create(integerFlux.log()).expectSubscription().
                thenRequest(1).
                expectNext(1).thenRequest(1).expectNext(2).thenCancel().verify();
    }

    @Test
    public void backPressure(){
        Flux<Integer> integerFlux = Flux.range(1,10);
        integerFlux.subscribe((element)-> System.out.println("Element is :" + element)
                ,(e)-> System.err.println("Error is :"+e)
                ,() -> System.out.println("Completed")
                ,(subscription->subscription.request(2)));
    }

    @Test
    public void backPressure_cancel(){
        Flux<Integer> integerFlux = Flux.range(1,10).log();
        integerFlux.subscribe((element)-> System.out.println("Element is :" + element)
                ,(e)-> System.err.println("Error is :"+e)
                ,() -> System.out.println("Completed")
                ,(subscription->subscription.cancel()));
    }

    @Test
    public void customized_backPressure(){
        Flux<Integer> integerFlux = Flux.range(1,10).log();
        integerFlux.subscribe(new BaseSubscriber<Integer>() {
            @Override
            protected void hookOnNext(Integer value) {
                request(1);
                System.out.println("Element is " + value);
                if(Objects.equals(value,4)){
                    cancel();;
                }
            }
        });
    }
}
