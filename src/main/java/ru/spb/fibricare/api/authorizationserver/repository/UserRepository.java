package ru.spb.fibricare.api.authorizationserver.repository;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import ru.spb.fibricare.api.authorizationserver.model.User;

public interface UserRepository extends CrudRepository<User, Long> {
    Optional<User> findByLogin(String login);
}
