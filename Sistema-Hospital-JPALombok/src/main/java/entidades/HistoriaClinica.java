package entidades;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@Getter
@NoArgsConstructor // requerido por JPA
@Table(name = "historia_clinica")
public class HistoriaClinica implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idHistoria;

    @Column(name = "numero_historia", nullable = false, unique = true)
    private String numeroHistoria;

    @OneToOne
    @JoinColumn(name = "paciente_id", nullable = false, unique = true)
    private Paciente paciente;

    @Column(name = "fecha_creacion", nullable = false)
    private LocalDateTime fechaCreacion;

    // ---- Colecciones mapeadas con ElementCollection ----

    @ElementCollection
    @CollectionTable(
            name = "hc_diagnosticos",
            joinColumns = @JoinColumn(name = "historia_id")
    )
    @Column(name = "diagnostico", length = 500)
    private List<String> diagnosticos = new ArrayList<>();

    @ElementCollection
    @CollectionTable(
            name = "hc_tratamientos",
            joinColumns = @JoinColumn(name = "historia_id")
    )
    @Column(name = "tratamiento", length = 500)
    private List<String> tratamientos = new ArrayList<>();

    @ElementCollection
    @CollectionTable(
            name = "hc_alergias",
            joinColumns = @JoinColumn(name = "historia_id")
    )
    @Column(name = "alergia", length = 200)
    private List<String> alergias = new ArrayList<>();

    // ---- Constructor principal ----
    public HistoriaClinica(Paciente paciente) {
        this.paciente = Objects.requireNonNull(paciente, "El paciente no puede ser nulo");
        this.fechaCreacion = LocalDateTime.now();
        this.numeroHistoria = generarNumeroHistoria();
    }

    private String generarNumeroHistoria() {
        return "HC-" + paciente.getDni() + "-" + fechaCreacion.getYear();
    }

    // ---- Métodos de negocio ----
    public void agregarDiagnostico(String diagnostico) {
        if (diagnostico != null && !diagnostico.trim().isEmpty()) {
            diagnosticos.add(diagnostico);
        }
    }

    public void agregarTratamiento(String tratamiento) {
        if (tratamiento != null && !tratamiento.trim().isEmpty()) {
            tratamientos.add(tratamiento);
        }
    }

    public void agregarAlergia(String alergia) {
        if (alergia != null && !alergia.trim().isEmpty()) {
            alergias.add(alergia);
        }
    }

    @Override
    public String toString() {
        return "HistoriaClinica{" +
                "numeroHistoria='" + numeroHistoria + '\'' +
                ", paciente=" + (paciente != null ? paciente.getNombreCompleto() : "null") +
                ", fechaCreacion=" + fechaCreacion +
                '}';
    }
}

