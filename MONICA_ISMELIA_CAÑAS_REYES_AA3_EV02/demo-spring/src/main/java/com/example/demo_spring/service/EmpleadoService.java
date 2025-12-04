package com.example.demo_spring.service;

import com.example.demo_spring.model.Empleado;
import com.example.demo_spring.repository.EmpleadoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class EmpleadoService {

    @Autowired
    private EmpleadoRepository repository;

    // Listar todos
    public List<Empleado> listarTodos() {
        return repository.findAll();
    }

    // Guardar (con validación de correo único)
    public Empleado guardar(Empleado empleado) {
        if (repository.existsByCorreo(empleado.getCorreo())) {
            throw new RuntimeException("El correo ya está registrado: " + empleado.getCorreo());
        }
        return repository.save(empleado);
    }

    // Buscar por ID
    public Optional<Empleado> buscarPorId(Long id) {
        return repository.findById(id);
    }

    // Eliminar
    public void eliminar(Long id) {
        repository.deleteById(id);
    }

    // Actualizar
    public Empleado actualizar(Long id, Empleado empleadoActualizado) {
        Empleado empleado = repository.findById(id)
            .orElseThrow(() -> new RuntimeException("Empleado no encontrado con ID: " + id));

        // Validar si el nuevo correo ya existe (y no es el mismo)
        if (!empleado.getCorreo().equals(empleadoActualizado.getCorreo()) &&
            repository.existsByCorreo(empleadoActualizado.getCorreo())) {
            throw new RuntimeException("El correo ya está en uso: " + empleadoActualizado.getCorreo());
        }

        empleado.setNombre(empleadoActualizado.getNombre());
        empleado.setCorreo(empleadoActualizado.getCorreo());
        empleado.setSalario(empleadoActualizado.getSalario());
        return repository.save(empleado);
    }
}