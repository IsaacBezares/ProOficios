package org.bessarez.prooficios.user;

import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.web.bind.annotation.*;

@RestController
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/users")
    CollectionModel<EntityModel<User>> all(){
        return userService.all();
    }
    @GetMapping("/users/{id}")
    EntityModel<User> one(@PathVariable Long id){
        return userService.one(id);
    }

    @PostMapping("/users")
    User create(@RequestBody User newU){
        return userService.create(newU);
    }

    @PutMapping("/users/{id}")
    User update(@PathVariable Long id, @RequestBody User newU){
        return userService.update(id, newU);
    }

    @DeleteMapping("/users/{id}")
    void delete(@PathVariable Long id){
        userService.delete(id);
    }
}
