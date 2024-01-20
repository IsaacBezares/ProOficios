package com.example.prooficios.user;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@Tag(name = "Users")
public class UserController {

    private final UserRepository uRepository;
    private final UserModelAssembler assembler;

    public UserController(UserRepository uRepository, UserModelAssembler assembler) {
        this.uRepository = uRepository;
        this.assembler = assembler;
    }

    @GetMapping("/users")
    @Operation(summary = "Returns all users")
    CollectionModel<EntityModel<User>> all(){
        List<EntityModel<User>> users = uRepository.findAll().stream()
                .map(assembler::toModel)
                .toList();

        return CollectionModel.of(users,
                linkTo(methodOn(UserController.class).all()).withSelfRel());
    }
    @GetMapping("/users/{id}")
    @Operation(summary = "Returns one user by its id")
    EntityModel<User> one(@PathVariable Long id){
        User user = uRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException(id));

        return assembler.toModel(user);
    }

    @PostMapping("/users")
    @Operation(summary = "Creates an user")
    User create(@RequestBody User newU){
        return uRepository.save(newU);
    }

    @PutMapping("/users/{id}")
    @Operation(summary = "Updates an user")
    User update(@PathVariable Long id, @RequestBody User newU){
        return uRepository.findById(id)
                .map(user -> {
                    user.setName(newU.getName());
                    user.setEmail(newU.getEmail());
                    user.setPhone(newU.getPhone());
                    user.setJoinDate(newU.getJoinDate());
                    user.setPassword(newU.getPassword());
                    return uRepository.save(newU);
                })
                .orElseGet(() -> {
                    newU.setId(id);
                    return uRepository.save(newU);
                });
    }

    @DeleteMapping("/users/{id}")
    @Operation(summary = "Removes an user")
    void delete(@PathVariable Long id){
        uRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException(id));

        uRepository.deleteById(id);
    }
}
