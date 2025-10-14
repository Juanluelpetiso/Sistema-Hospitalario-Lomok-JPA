# CLAUDE.md — Guía Técnica del Proyecto

##  Arquitectura
Estructura orientada a dominio (DDD light) con entidades: Persona, Hospital, Departamento, Paciente, Medico, Sala, Cita.

##  Entidades Principales
- Persona (abstracta)
- Medico y Paciente heredan de Persona
- Cita asocia Paciente, Medico y Sala
- Matricula es @Embeddable

##  Relaciones JPA
- Uno a muchos: Hospital → Departamento, Departamento → Medico
- Muchos a uno: Cita → Paciente, Medico, Sala
- Uno a uno: Paciente ↔ HistoriaClinica
- Embeddable: Matricula en Medico

##  Lombok + Builder
Uso de @SuperBuilder, @Builder.Default, @NoArgsConstructor(access = PROTECTED).

##  Persistencia
Base: H2 file-based, unidad: hospital-persistence-unit, `hibernate.hbm2ddl.auto=update`.

##  Pruebas
JUnit 5: creación de entidades, validaciones y persistencia.

##  Consultas JPQL
- SELECT m FROM Medico m WHERE m.especialidad = :esp
- SELECT COUNT(c) FROM Cita c WHERE c.estado = :estado

##  Buenas prácticas
Encapsulación, inmutabilidad, validaciones con Objects.requireNonNull.
