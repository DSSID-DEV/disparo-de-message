package com.dssiddev.disparowhatsapp.repositories;

import com.dssiddev.disparowhatsapp.models.User;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepositorio extends JpaRepository<User, Long> {
    boolean existsByUsername(String username);

    @EntityGraph(attributePaths = "roles", type = EntityGraph.EntityGraphType.FETCH)
    Optional<User> findById(Long userId);

    @Query(value = "select user from User user where user.username = :dadoDeAcesso or user.telefone = :dadoDeAcesso or user.email = :dadoDeAcesso and user.password = :password", nativeQuery = false)
    UserDetails logar(@Param(value ="dadoDeAcesso") String dadoDeAcesso, @Param(value = "password") String password);

    User findByPassword(String provissoria);

    User findByTelefone(String telefone);

    @Query(value = "select user from User user where user.username = :contato and user.password = :contato", nativeQuery = false)
    User findbyTelefoneOrEmail(String contato);

    @EntityGraph(attributePaths = "roles", type = EntityGraph.EntityGraphType.FETCH)
    Optional<User> findByUsername(String username);

    boolean existsByEmail(String email);


    @EntityGraph(attributePaths = "roles", type = EntityGraph.EntityGraphType.FETCH)
    @Query(value = "select user from User user where user.username = :dadoDeAcesso or user.telefone = :dadoDeAcesso or user.email = :dadoDeAcesso", nativeQuery = false)
    Optional<User> findByUsernameOrTelefoneOrEmail(@Param(value ="dadoDeAcesso") String dadoDeAcesso);
}
