package entidades;

import lombok.*;
import lombok.experimental.SuperBuilder;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.Period;
import java.util.Objects;

@SuperBuilder
@Getter
@ToString
@NoArgsConstructor(access = AccessLevel.PROTECTED) // necesario luego para JPA
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public abstract class Persona implements Serializable {

    private static final long serialVersionUID = 1L;

    @EqualsAndHashCode.Include
    protected String dni;

    protected String nombre;
    protected String apellido;
    protected LocalDate fechaNacimiento;
    protected TipoSangre tipoSangre;

    protected Persona(PersonaBuilder<?, ?> b) {
        this.nombre = validarString(b.nombre, "El nombre no puede ser nulo ni vacío");
        this.apellido = validarString(b.apellido, "El apellido no puede ser nulo ni vacío");
        this.dni = validarDni(b.dni);
        this.fechaNacimiento = Objects.requireNonNull(b.fechaNacimiento, "La fecha de nacimiento no puede ser nula");
        this.tipoSangre = Objects.requireNonNull(b.tipoSangre, "El tipo de sangre no puede ser nulo");
    }

    public String getNombreCompleto() {
        return nombre + " " + apellido;
    }

    public int getEdad() {
        if (fechaNacimiento == null) return 0;
        return Period.between(fechaNacimiento, LocalDate.now()).getYears();
    }

    // Validaciones reutilizables
    protected String validarString(String valor, String mensajeError) {
        Objects.requireNonNull(valor, mensajeError);
        if (valor.trim().isEmpty()) throw new IllegalArgumentException(mensajeError);
        return valor.trim();
    }

    protected String validarDni(String dni) {
        Objects.requireNonNull(dni, "El DNI no puede ser nulo");
        String clean = dni.trim();
        if (!clean.matches("\\d{7,8}")) {
            throw new IllegalArgumentException("El DNI debe tener 7 u 8 dígitos: " + dni);
        }
        return clean;
    }
}
