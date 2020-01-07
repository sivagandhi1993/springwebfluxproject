package com.learnreactivespring.controller.v1;

import com.learnreactivespring.constants.ItemsConstants;
import com.learnreactivespring.document.Item;
import com.learnreactivespring.repository.ItemReactiveRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import static com.learnreactivespring.constants.ItemsConstants.ITEMS_CONSTANTS_URL_V1;
import static org.junit.Assert.assertTrue;

@SpringBootTest
@RunWith(SpringRunner.class)
@DirtiesContext
@AutoConfigureWebTestClient
@ActiveProfiles("test")
public class ItemControllerTest {

    @Autowired
    private WebTestClient webTestClient;
    @Autowired
    ItemReactiveRepository itemReactiveRepository;

    public List<Item> data() {
        return Arrays.asList(new Item(null, "Samsung TV", 400.0),
                new Item(null, "LG TV", 420.0),
                new Item(null, "Apple Watch", 299.0),
                new Item(null, "Beats Headphone", 149.0),
                new Item("ABC", "Bose Headphone", 1049.99));
    }

    @Before
    public void setup() {
        itemReactiveRepository.deleteAll()
                .thenMany(Flux.fromIterable(data()))
                .flatMap(itemReactiveRepository::save)
                .thenMany(itemReactiveRepository.findAll())
                .doOnNext(item -> System.out.println("Item inserted is : " + item))
                .blockLast();
    }

    @Test
    public void getAllItems() {
        webTestClient.get().uri(ItemsConstants.ITEMS_CONSTANTS_URL_V1)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON_UTF8)
                .expectBodyList(Item.class)
                .hasSize(5);
    }

    @Test
    public void getAllItems_approach2() {
        webTestClient.get().uri(ItemsConstants.ITEMS_CONSTANTS_URL_V1)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON_UTF8)
                .expectBodyList(Item.class)
                .hasSize(5)
                .consumeWith(response -> {
                    List<Item> items = response.getResponseBody();
                    items.forEach(item ->
                            assertTrue(Objects.nonNull(item.getId()))
                    );
                });
    }

    @Test
    public void getAllItems_approach3() {
        Flux<Item> itemList = webTestClient.get().uri(ItemsConstants.ITEMS_CONSTANTS_URL_V1)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON_UTF8)
                .returnResult(Item.class)
                .getResponseBody();
        StepVerifier.create(itemList)
                .expectSubscription()
                .expectNextCount(5)
                .verifyComplete();
    }

    @Test
    public void getOneItemTest() {
        webTestClient.get().uri(ItemsConstants.ITEMS_CONSTANTS_URL_V1.concat("/{id}"), "ABC")
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.price", 1049.99);
    }

    @Test
    public void getOneItemTest_NotFound() {
        webTestClient.get().uri(ItemsConstants.ITEMS_CONSTANTS_URL_V1.concat("/{id}"), "DEF")
                .exchange()
                .expectStatus().isNotFound();
    }

    @Test
    public void createItem() {
        Item item = new Item(null, "iPhone XS", 1299.00);
        webTestClient.post().uri(ITEMS_CONSTANTS_URL_V1)
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .body(Mono.just(item), Item.class)
                .exchange()
                .expectBody()
                .jsonPath("$.id").isNotEmpty()
                .jsonPath("$.description").isEqualTo("iPhone XS")
                .jsonPath("$.price").isEqualTo(1299.00);
    }

    @Test
    public void deleteItem() {
        webTestClient.delete().uri(ITEMS_CONSTANTS_URL_V1.concat("/{id}"), "ABC")
                .accept(MediaType.APPLICATION_JSON_UTF8)
                .exchange()
                .expectStatus().isOk()
                .expectBody(Void.class);
    }

    @Test
    public void updateItem(){
        double newprice = 1299.99;
        Item item = new Item(null,"Bose Headphone", newprice);
        webTestClient.put().uri(ITEMS_CONSTANTS_URL_V1.concat("/{id}"), "ABC")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .accept(MediaType.APPLICATION_JSON_UTF8)
                .body(Mono.just(item),Item.class)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.price").isEqualTo(newprice);
    }
}
