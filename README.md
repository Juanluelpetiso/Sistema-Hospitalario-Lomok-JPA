# Sistema-Hospitalario-Lomok-JPA
Retamales lazo Juan Lucas
51053 3K9

# Sistema de Gestión Hospitalaria

## Descripción general

El **Sistema de Gestión Hospitalaria** es una aplicación desarrollada en **Java** utilizando **Jakarta Persistence API (JPA)** e **Hibernate** como proveedor de persistencia. Su objetivo es administrar de forma integral la información de un hospital, incluyendo médicos, pacientes, departamentos, salas, citas médicas e historiales clínicos.  

El sistema permite almacenar, consultar y mantener los datos de las entidades mencionadas, garantizando integridad referencial y persistencia en una base de datos **H2** configurada para operar mediante **archivo local**.

---

## Objetivos

- Gestionar de forma estructurada la información hospitalaria.
- Facilitar la relación entre médicos, pacientes y sus citas.
- Permitir el registro histórico de diagnósticos y alergias.
- Proveer consultas JPQL para obtener información específica (por ejemplo, médicos por especialidad o conteo de citas completadas).

---

## Arquitectura y tecnologías

| Componente | Descripción |
|-------------|--------------|
| **Lenguaje** | Java 17 |
| **Persistencia** | Jakarta Persistence API (JPA) 3.0 |
| **ORM** | Hibernate ORM 6.x |
| **Base de datos** | H2 (modo archivo) |
| **Transacciones** | RESOURCE_LOCAL |
| **Proveedor JPA** | `org.hibernate.jpa.HibernatePersistenceProvider` |

El sistema sigue una arquitectura orientada a objetos con relaciones bien definidas entre las entidades, aprovechando anotaciones JPA como `@Entity`, `@OneToMany`, `@ManyToOne`, `@Embedded`, y `@Enumerated`.

---

## Modelo de datos

Las principales entidades del sistema son:

- **Persona**: clase base abstracta que representa a cualquier individuo dentro del sistema.
- **Paciente**: hereda de `Persona`; almacena la historia clínica y puede tener múltiples citas.
- **Medico**: hereda de `Persona`; posee una matrícula y una especialidad médica.
- **Hospital**: entidad principal que agrupa departamentos y salas.
- **Departamento**: pertenece a un hospital y contiene un conjunto de salas o médicos.
- **Sala**: representa una unidad física dentro del hospital.
- **Cita**: relaciona un paciente con un médico en una fecha y hora determinadas.
- **HistoriaClinica**: contiene registros de diagnósticos, alergias y tratamientos.
- **Matricula**: entidad asociada a un médico, representa su número profesional y datos de registro.

---

## Configuración de persistencia

El archivo `persistence.xml` define la unidad de persistencia:

```xml
<persistence-unit name="hospital-persistence-unit" transaction-type="RESOURCE_LOCAL">
    <provider>org.hibernate.jpa.HibernatePersistenceProvider</provider>

    <class>entidades.Persona</class>
    <class>entidades.Paciente</class>
    <class>entidades.Medico</class>
    <class>entidades.Hospital</class>
    <class>entidades.Departamento</class>
    <class>entidades.Sala</class>
    <class>entidades.Cita</class>
    <class>entidades.HistoriaClinica</class>
    <class>entidades.Matricula</class>

    <properties>
        <property name="jakarta.persistence.jdbc.url" value="jdbc:h2:./data/hospidb;AUTO_SERVER=TRUE"/>
        <property name="jakarta.persistence.jdbc.driver" value="org.h2.Driver"/>
        <property name="jakarta.persistence.jdbc.user" value="sa"/>
        <property name="jakarta.persistence.jdbc.password" value=""/>
        <property name="hibernate.dialect" value="org.hibernate.dialect.H2Dialect"/>
        <property name="hibernate.hbm2ddl.auto" value="update"/>
        <property name="hibernate.show_sql" value="true"/>
        <property name="hibernate.format_sql" value="true"/>
    </properties>
</persistence-unit>

```






Ejemplo de uso básico
Creación y persistencia de entidades
EntityManagerFactory emf = Persistence.createEntityManagerFactory("hospital-persistence-unit");
EntityManager em = emf.createEntityManager();

em.getTransaction().begin();

// Crear un médico
Medico medico = new Medico();
medico.setNombre("Dr. Juan Pérez");
medico.setEspecialidad(EspecialidadMedica.CARDIOLOGIA);
medico.setMatricula(new Matricula("M-1234", LocalDate.of(2020, 5, 1)));

// Crear un paciente
Paciente paciente = new Paciente();
paciente.setNombre("Ana Gómez");

// Crear una cita
Cita cita = new Cita();
cita.setMedico(medico);
cita.setPaciente(paciente);
cita.setFecha(LocalDateTime.now());
cita.setEstado(EstadoCita.PENDIENTE);

em.persist(medico);
em.persist(paciente);
em.persist(cita);

em.getTransaction().commit();
em.close();
emf.close();
