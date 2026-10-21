package org.formation.coffee.adapter.in.web;

import java.util.List;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.formation.coffee.adapter.in.web.dto.*;
import org.formation.coffee.application.port.in.*;
import org.formation.coffee.domain.model.CoffeeId;

@RestController
@RequestMapping(path = "/api/coffees", version = "1")
class CoffeeController {
    private final ListCoffeesQuery list;
    private final GetCoffeeQuery get;
    private final CreateCoffeeUseCase create;
    private final UpdateCoffeeUseCase update;
    private final CoffeeWebMapper mapper;
    CoffeeController(ListCoffeesQuery list, GetCoffeeQuery get, CreateCoffeeUseCase create,
            UpdateCoffeeUseCase update, CoffeeWebMapper mapper) {
        this.list = list; this.get = get; this.create = create; this.update = update; this.mapper = mapper;
    }
    @GetMapping
    List<CoffeeResponse> list() {
        return list.listCoffees().stream().map(mapper::toResponse).toList();
    }
    @GetMapping("/{id}")
    CoffeeResponse get(@PathVariable long id) {
        return mapper.toResponse(get.getCoffee(new CoffeeId(id)));
    }
    @PostMapping
    ResponseEntity<CoffeeResponse> create(@Valid @RequestBody CreateCoffeeRequest request) {
        CoffeeResponse response = mapper.toResponse(create.createCoffee(mapper.toCommand(request)));
        var location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
                .buildAndExpand(response.id()).toUri();
        return ResponseEntity.created(location).body(response);
    }
    @PatchMapping("/{id}")
    CoffeeResponse update(@Valid @RequestBody UpdateCoffeeRequest request, @PathVariable long id) {
        return mapper.toResponse(update.updateCoffee(mapper.toCommand(id, request)));
    }
}
