package IESTP.FM.Entity;

import lombok.*;
import javax.persistence.*;
import javax.validation.constraints.*;
import java.time.LocalDateTime;
import com.fasterxml.jackson.annotation.JsonFormat;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Entity
@Table(name = "Movimiento")
public class Movimiento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @NotNull
    @Column(name = "fecha_movimiento", nullable = false)
    @JsonFormat(pattern = "dd/MM/yyyy HH:mm:ss")
    private LocalDateTime fechaMovimiento;

    @NotNull
    @Size(min = 1, max = 255)
    @Pattern(regexp = "^[A-Za-z ]+$")
    @Column(name = "nombre_responsable", nullable = false, length = 255)
    private String nombreResponsable;

    @Size(max = 255)
    @Column(name = "motivo_movimiento", length = 255)
    private String motivoMovimiento;

    @ManyToOne
    @JoinColumn(name = "equipo_id", nullable = false)
    private Equipo equipo;

    @ManyToOne
    @JoinColumn(name = "ubicacion_origen_id", nullable = false)
    private Ubicacion ubicacionOrigen;

    @ManyToOne
    @JoinColumn(name = "ubicacion_destino_id", nullable = false)
    private Ubicacion ubicacionDestino;

}
