package com.example.prooficios.user;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.web.bind.annotation.*;

@RestController
@Tag(name = "Users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/users")
    @Operation(summary = "Returns all users")
    CollectionModel<EntityModel<User>> all(){
        return userService.all();
    }
    @GetMapping("/users/{id}")
    @Operation(summary = "Returns one user by its id")
    EntityModel<User> one(@PathVariable Long id){
        return userService.one(id);
    }

    @PostMapping("/users")
    @Operation(summary = "Creates an user")
    User create(@RequestBody User newU){
        return userService.create(newU);
    }

    @PutMapping("/users/{id}")
    @Operation(summary = "Updates an user")
    User update(@PathVariable Long id, @RequestBody User newU){
        return userService.update(id, newU);
    }

    @DeleteMapping("/users/{id}")
    @Operation(summary = "Removes an user")
    void delete(@PathVariable Long id){
        userService.delete(id);
    }
}
