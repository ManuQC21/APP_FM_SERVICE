package IESTP.FM.Entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;
@Setter
@Getter
@Entity
@Table(name = "Movimiento")
public class Movimiento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "fecha_movimiento", nullable = false)
    private LocalDateTime fechaMovimiento;

    @Column(name = "nombre_responsable", nullable = false, length = 255)
    private String nombreResponsable;

    @Column(name = "motivo_movimiento", length = 255)
    private String motivoMovimiento;

    @ManyToOne
    @JoinColumn(name = "equipo_id")
    private Equipo equipo;

    @ManyToOne
    @JoinColumn(name = "ubicacion_origen_id")
    private Ubicacion ubicacionOrigen;

    @ManyToOne
    @JoinColumn(name = "ubicacion_destino_id")
    private Ubicacion ubicacionDestino;

    public Movimiento() {
    }

    public Movimiento(Integer id) {
        this.id = id;
    }

    public Movimiento(Integer id, LocalDateTime fechaMovimiento, String nombreResponsable, String motivoMovimiento, Equipo equipo, Ubicacion ubicacionOrigen, Ubicacion ubicacionDestino) {
        this.id = id;
        this.fechaMovimiento = fechaMovimiento;
        this.nombreResponsable = nombreResponsable;
        this.motivoMovimiento = motivoMovimiento;
        this.equipo = equipo;
        this.ubicacionOrigen = ubicacionOrigen;
        this.ubicacionDestino = ubicacionDestino;
    }
}
