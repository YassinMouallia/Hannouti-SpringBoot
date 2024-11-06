package com.yassinmouallia.gestiondestock.repository;

import com.yassinmouallia.gestiondestock.model.Client;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClientRepository extends JpaRepository<Client, Integer> {

}
