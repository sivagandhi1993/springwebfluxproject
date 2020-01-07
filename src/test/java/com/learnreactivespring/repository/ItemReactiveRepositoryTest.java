/*
package com.learnreactivespring.repository;

import com.learnreactivespring.document.Item;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.test.context.junit4.SpringRunner;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import static java.util.Objects.nonNull;

@DataMongoTest
@RunWith(SpringRunner.class)
public class ItemReactiveRepositoryTest {

    @Autowired
    ItemReactiveRepository itemReactiveRepository;
    List<Item> itemList = Arrays.asList(new Item(null, "Samsung TV", 400.0),
            new Item(null, "LG TV", 420.0),
            new Item(null, "Apple Watch", 299.0),
            new Item(null, "Beats Headphone", 149.0),
            new Item("ABC", "Bose Headphone", 1049.99));

    @Before
    public void setUp() {
        itemReactiveRepository.deleteAll().
                thenMany(Flux.fromIterable(itemList))
                .flatMap(itemReactiveRepository::save)
                .doOnNext(item -> System.out.println("Inserted Item is : " + item))
                .blockLast();
    }

    @Test
    public void getAllItems() {
        StepVerifier.create(itemReactiveRepository.findAll()).expectSubscription()
                .expectNextCount(5).verifyComplete();
    }

    @Test
    public void getItemById() {
        StepVerifier.create(itemReactiveRepository.findById("ABC")).expectSubscription()
                .expectNextMatches(item -> Objects.equals(item.getDescription(), "Bose Headphone")).verifyComplete();
    }

    @Test
    public void findItemByDescription() {
        StepVerifier.create(itemReactiveRepository.findByDescription("Bose Headphone").log()).expectSubscription()
                .expectNextCount(1).verifyComplete();
    }

    @Test
    public void saveItem() {
        Item item = new Item(null, "Google Mini", 550.0);
        Mono<Item> savedItem = itemReactiveRepository.save(item);
        StepVerifier.create(savedItem).expectSubscription()
                .expectNextMatches(item1 -> nonNull(item1.getId()) && Objects.equals(item1.getDescription(), "Google Mini"))
                .verifyComplete();
    }

    @Test
    public void updateItem() {
        double newPrice = 540.00;
        Flux<Item> updatedItem = itemReactiveRepository.findByDescription("LG TV")
                .map(item -> {
                    item.setPrice(newPrice);
                    return item;
                })//Why specifically used flatMap here \????
                .flatMap(item -> itemReactiveRepository.save(item));
        StepVerifier.create(updatedItem)
                .expectSubscription()
                .expectNextMatches(item -> item.getPrice() == 540.00)
                .verifyComplete();
    }
}
*/
