package com.tms.skv.registration_platform.repository;

import com.tms.skv.registration_platform.entity.ClientEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface ClientRepository extends JpaRepository<ClientEntity, Integer> {
    Optional<ClientEntity> findByLogin(String username);
}
