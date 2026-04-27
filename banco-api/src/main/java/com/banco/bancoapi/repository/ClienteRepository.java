package com.banco.bancoapi.repository;

import com.banco.bancoapi.entity.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClienteRepository extends JpaRepository<Cliente, Long> {
}