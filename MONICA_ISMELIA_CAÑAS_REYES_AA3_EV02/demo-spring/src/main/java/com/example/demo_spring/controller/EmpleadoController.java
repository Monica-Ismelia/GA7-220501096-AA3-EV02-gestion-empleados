package com.example.demo_spring.controller;

import com.example.demo_spring.model.Empleado;
import com.example.demo_spring.repository.EmpleadoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDate; // <-- ¡NUEVO IMPORT NECESARIO!
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/empleados")
public class EmpleadoController {

    @Autowired
    private EmpleadoRepository repository;

    // ----------------------------------------------
    // GET → Listar
    // ----------------------------------------------
    @GetMapping
    public List<Empleado> listar() {
        return repository.findAll();
    }

    // ----------------------------------------------
    // GET → Buscar por ID
    // ----------------------------------------------
    @GetMapping("/{id}")
    public ResponseEntity<Empleado> buscarPorId(@PathVariable Long id) {
        return repository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // ----------------------------------------------
    // POST → Crear
    // Se recomienda usar 201 CREATED para nuevas entidades.
    // ----------------------------------------------
    @PostMapping
    public ResponseEntity<?> crear(@RequestBody Empleado empleado) {
        if (repository.existsByCorreo(empleado.getCorreo())) {
            // Retorna 400 Bad Request si el correo ya existe
            return ResponseEntity.badRequest().body("El correo ya está registrado: " + empleado.getCorreo());
        }
        // Retorna 201 Created para la creación exitosa
        return ResponseEntity.status(HttpStatus.CREATED).body(repository.save(empleado));
    }

    // ----------------------------------------------
    // PUT → Actualización completa
    // ----------------------------------------------
    @PutMapping("/{id}")
    public ResponseEntity<?> actualizar(@PathVariable Long id, @RequestBody Empleado emp) {
        return repository.findById(id)
                .map(empleado -> {
                    // Validación de correo único: solo verifica si el correo que viene (emp.getCorreo())
                    // es diferente al correo actual (empleado.getCorreo()) Y ya existe en BD.
                    if (!empleado.getCorreo().equals(emp.getCorreo()) &&
                            repository.existsByCorreo(emp.getCorreo())) {
                        return ResponseEntity.badRequest().body("El correo ya está en uso: " + emp.getCorreo());
                    }

                    empleado.setNombre(emp.getNombre());
                    empleado.setCorreo(emp.getCorreo());
                    empleado.setSalario(emp.getSalario()); // BigDecimal viene del JSON
                    // La fecha de ingreso también se actualiza si viene en el JSON
                    empleado.setFechaIngreso(emp.getFechaIngreso());

                    return ResponseEntity.ok(repository.save(empleado));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    // ----------------------------------------------
    // PATCH → Actualización parcial (CORREGIDO: Ahora incluye 'fechaIngreso')
    // ----------------------------------------------
    @PatchMapping("/{id}")
    public ResponseEntity<?> actualizarParcial(
            @PathVariable Long id,
            @RequestBody Map<String, Object> cambios) {

        Optional<Empleado> optionalEmp = repository.findById(id);

        if (optionalEmp.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        Empleado empleado = optionalEmp.get();

        // Variable para manejar la respuesta si hay un error de unicidad
        String correoDuplicadoError = null;

        for (Map.Entry<String, Object> entry : cambios.entrySet()) {
            String campo = entry.getKey();
            Object valor = entry.getValue();

            switch (campo) {
                case "nombre":
                    empleado.setNombre(valor.toString());
                    break;

                case "correo":
                    String nuevoCorreo = valor.toString();
                    // Chequea si el nuevo correo ya existe y es diferente al actual
                    if (repository.existsByCorreo(nuevoCorreo) && !nuevoCorreo.equals(empleado.getCorreo())) {
                        correoDuplicadoError = "El correo ya está registrado: " + nuevoCorreo;
                        break; // Salir del switch
                    }
                    empleado.setCorreo(nuevoCorreo);
                    break;

                case "salario":
                    try {
                        // Asegurarse de que el valor sea un número válido antes de la conversión
                        empleado.setSalario(new BigDecimal(valor.toString()));
                    } catch (NumberFormatException e) {
                        return ResponseEntity.badRequest().body("Formato de salario inválido: " + valor);
                    }
                    break;
                
                // ----------------------------------------------
                // NUEVA LÓGICA PARA FECHA DE INGRESO
                // ----------------------------------------------
                case "fechaIngreso":
                    try {
                        // Asume el formato ISO 8601 (AAAA-MM-DD)
                        empleado.setFechaIngreso(LocalDate.parse(valor.toString()));
                    } catch (Exception e) {
                        return ResponseEntity.badRequest().body("Formato de fecha de ingreso inválido. Use AAAA-MM-DD.");
                    }
                    break;
            }

            // Si se detectó un error de correo duplicado, retorna 400 inmediatamente.
            if (correoDuplicadoError != null) {
                return ResponseEntity.badRequest().body(correoDuplicadoError); // Retorna 400 Bad Request
            }
        }

        return ResponseEntity.ok(repository.save(empleado));
    }

    // ----------------------------------------------
    // DELETE → Eliminar
    // ----------------------------------------------
    @DeleteMapping("/{id}")
    public ResponseEntity<String> eliminar(@PathVariable Long id) {
        if (!repository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        repository.deleteById(id);
        return ResponseEntity.ok("Empleado eliminado");
    }
}