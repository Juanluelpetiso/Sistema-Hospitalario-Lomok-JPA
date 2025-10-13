package entidades;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@SuperBuilder
@Getter
@Entity
@ToString(callSuper = true)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Medico extends Persona {
@Id
@GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idMedico;
    @Embedded
    private Matricula matricula;
    @Enumerated(EnumType.STRING)
    private EspecialidadMedica especialidad;
    @ManyToOne
    @JoinColumn
    private Departamento departamento;
    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL,orphanRemoval = true, mappedBy = "medico")
    private List<Cita> citas;

    protected Medico(MedicoBuilder<?, ?> b) {
        super(b);

        if (b.matricula == null) {
            throw new IllegalArgumentException("La matrícula es requerida para un médico");
        }
        this.matricula = b.matricula;
        this.especialidad = Objects.requireNonNull(b.especialidad, "La especialidad no puede ser nula");
        this.departamento = b.departamento;
        this.citas = new ArrayList<>();
    }

    public void setDepartamento(Departamento departamento) {
        this.departamento = departamento;
    }

    public void addCita(Cita cita) {
        Objects.requireNonNull(cita, "Cita no puede ser null");
        this.citas.add(cita);
    }
}
