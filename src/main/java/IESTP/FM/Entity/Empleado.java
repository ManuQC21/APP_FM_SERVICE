package IESTP.FM.Entity;

import lombok.*;
import javax.persistence.*;
import javax.validation.constraints.*;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Entity
@Table(name = "Empleado")
public class Empleado {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @NotNull
    @Size(min = 1, max = 255)
    @Pattern(regexp = "^[A-Za-z ]+$")
    @Column(name = "nombre", nullable = false, length = 255, unique = true)
    private String nombre;

    @NotNull
    @Size(min = 1, max = 255)
    @Pattern(regexp = "^[A-Za-z ]+$")
    @Column(name = "cargo", nullable = false, length = 255)
    private String cargo;

    public Empleado(int id) {
        this.id = id;
    }
}
