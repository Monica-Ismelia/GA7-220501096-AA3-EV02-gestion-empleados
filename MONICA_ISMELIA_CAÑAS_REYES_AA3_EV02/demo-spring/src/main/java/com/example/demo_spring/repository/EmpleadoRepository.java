package com.example.demo_spring.repository;

import com.example.demo_spring.model.Empleado;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EmpleadoRepository extends JpaRepository<Empleado, Long> {
    boolean existsByCorreo(String correo);
}
