package com.learnreactivespring.fluxandmonoplayground;

import org.junit.Test;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

import java.util.Arrays;
import java.util.List;

public class FluxAndMonoFilterTest {

    List<String> names = Arrays.asList("adam", "anna", "jack", "jenny");

    @Test
    public void filterTest() {
        Flux<String> namesFlux = Flux.fromIterable(names).filter(t -> t.startsWith("a")).log();
        StepVerifier.create(namesFlux).expectNext("adam", "anna").verifyComplete();
    }
}
