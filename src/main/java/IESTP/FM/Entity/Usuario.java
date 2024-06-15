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
@Table(name = "Usuario")
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @NotNull
    @Email
    @Size(max = 500)
    @Column(length = 500, nullable = false, unique = true)
    private String correo;

    @NotNull
    @Size(min = 6, max = 300)
    @Column(length = 300, nullable = false)
    private String clave;

    @NotNull
    @Column(nullable = false)
    private boolean vigencia;

    @OneToOne
    @JoinColumn(name = "empleado_id", nullable = false)
    private Empleado empleado;
}
