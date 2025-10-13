package org.example;

import entidades.*;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import jakarta.persistence.TypedQuery;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Main {

    public static void main(String[] args) {
        // Crear EntityManager
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("hospital-persistence-unit");
        EntityManager em = emf.createEntityManager();

        em.getTransaction().begin();

// 1. Crear hospital
        Hospital hospital = Hospital.builder()
                .nombre("Hospital Central")
                .direccion("Av. Libertador 1234")
                .telefono("011-4567-8901")
                .build();

// 2. Crear departamento
        Departamento cardiologia = Departamento.builder()
                .nombre("Cardiología")
                .especialidad(EspecialidadMedica.CARDIOLOGIA)
                .build();

        hospital.agregarDepartamento(cardiologia);

// 3. Crear sala
        Sala consultorio = Sala.builder()
                .numero("CARD-101")
                .tipo("Consultorio")
                .departamento(cardiologia)
                .build();

// 4. Crear médico
        Medico cardiologo = Medico.builder()
                .nombre("Carlos")
                .apellido("González")
                .dni("12345678")
                .fechaNacimiento(LocalDate.of(1975, 5, 15))
                .tipoSangre(TipoSangre.A_POSITIVO)
                .matricula(new Matricula("MP-12345"))
                .especialidad(EspecialidadMedica.CARDIOLOGIA)
                .build();

        cardiologia.agregarMedico(cardiologo);

// 5. Crear paciente (historia clínica se crea automáticamente)
        Paciente paciente = Paciente.builder()
                .nombre("María")
                .apellido("López")
                .dni("11111111")
                .fechaNacimiento(LocalDate.of(1985, 12, 5))
                .tipoSangre(TipoSangre.A_POSITIVO)
                .telefono("011-1111-1111")
                .direccion("Calle Falsa 123")
                .build();

        hospital.agregarPaciente(paciente);

// 6. Agregar información a historia clínica
        HistoriaClinica historia = paciente.getHistoriaClinica();
        historia.agregarDiagnostico("Hipertensión arterial");
        historia.agregarTratamiento("Enalapril 10mg");
        historia.agregarAlergia("Penicilina");

// 7. Programar cita usando CitaManager
        CitaManager citaManager = new CitaManager();
        try {
            Cita cita = citaManager.programarCita(
                    paciente,
                    cardiologo,
                    consultorio,
                    LocalDateTime.now().plusDays(1).withHour(10).withMinute(0),
                    new BigDecimal("150000.00")
            );
            System.out.println("Cita programada: " + cita.getIdCita());
        } catch (CitaException e) {
            System.err.println("Error: " + e.getMessage());
        }

// 8. Persistir todo (cascade hace el resto)
        em.persist(hospital);
        em.persist(consultorio);
        em.persist(cardiologo);
        em.persist(historia);

        em.getTransaction().commit();

        // Consultar médicos por especialidad
        TypedQuery<Medico> query = em.createQuery(
                "SELECT m FROM Medico m WHERE m.especialidad = :esp",
                Medico.class
        );
        query.setParameter("esp", EspecialidadMedica.CARDIOLOGIA);
        List<Medico> cardiologos = query.getResultList();

// Contar citas por estado
        Long citasCompletadas = em.createQuery(
                        "SELECT COUNT(c) FROM Cita c WHERE c.estado = :estado",
                        Long.class
                )
                .setParameter("estado", EstadoCita.COMPLETADA)
                .getSingleResult();

// Obtener pacientes con alergias
        TypedQuery<Paciente> queryAlergicos = em.createQuery(
                "SELECT DISTINCT p FROM Paciente p " +
                        "JOIN p.historiaClinica h " +
                        "WHERE SIZE(h.alergias) > 0",
                Paciente.class
        );

        em.close();
        emf.close();

    }
}