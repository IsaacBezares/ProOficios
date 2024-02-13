package org.bessarez.prooficios.user;

import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Service
@Transactional
public class UserService {
    private final UserRepository uRepository;
    private final UserModelAssembler assembler;
    private final KafkaTemplate<String,Long> userUpdatedTemplate;

    public UserService(UserRepository uRepository, UserModelAssembler assembler, KafkaTemplate<String, Long> userUpdatedTemplate) {
        this.uRepository = uRepository;
        this.assembler = assembler;
        this.userUpdatedTemplate = userUpdatedTemplate;
    }

    CollectionModel<EntityModel<User>> all(){
        List<EntityModel<User>> users = uRepository.findAll().stream()
                .map(assembler::toModel)
                .toList();

        return CollectionModel.of(users,
                linkTo(methodOn(UserController.class).all()).withSelfRel());
    }

    EntityModel<User> one(Long id){
        User user = uRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException(id));

        return assembler.toModel(user);
    }

    User create(User newU){
        User user = uRepository.save(newU);
        userUpdatedTemplate.send("userCreatedTopic", user.getId());
        return user;
    }

    User update(Long id, User newU){
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

    void delete(Long id){
        uRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException(id));

        userUpdatedTemplate.send("userDeletedTopic", id);

        uRepository.deleteById(id);
    }
}
