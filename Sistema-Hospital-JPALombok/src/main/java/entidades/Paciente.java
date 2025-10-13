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
public class Paciente extends Persona {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long idPaciente;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER,  orphanRemoval = true)
    private HistoriaClinica historiaClinica;
    private String telefono;
    private String direccion;
    @ManyToOne
    @JoinColumn(name = "Hospital")
    private Hospital hospital;
    @OneToMany(mappedBy = "paciente", cascade = CascadeType.ALL,  orphanRemoval = true)
    private List<Cita> citas;

    /**
     * Constructor usado por @SuperBuilder.
     * Inicializa colección y crea la HistoriaClinica vinculada.
     */
    protected Paciente(PacienteBuilder<?, ?> b) {
        super(b);

        // validaciones específicas de Paciente
        this.telefono = validarString(b.telefono, "El teléfono no puede ser nulo ni vacío");
        this.direccion = validarString(b.direccion, "La dirección no puede ser nula ni vacía");

        // inicializaciones internas
        this.citas = new ArrayList<>();
        this.historiaClinica = new HistoriaClinica(this);
        this.hospital = b.hospital; // puede ser null y setearse después con setHospital()
    }

    public void setHospital(Hospital hospital) {
        this.hospital = hospital;
    }

    public void addCita(Cita cita) {
        Objects.requireNonNull(cita, "Cita no puede ser null");
        this.citas.add(cita);
    }
}
