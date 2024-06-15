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
@Table(name = "Ubicacion")
public class Ubicacion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @NotNull
    @Size(min = 1, max = 255)
    @Pattern(regexp = "^[A-Za-z ]+$")
    @Column(name = "ambiente", nullable = false, length = 255)
    private String ambiente;

    @NotNull
    @Size(min = 1, max = 255)
    @Pattern(regexp = "^[A-Za-z0-9 ]+$")
    @Column(name = "ubicacion_fisica", nullable = false, length = 255)
    private String ubicacionFisica;

    public Ubicacion(Integer id) {
        this.id = id;
    }
}
