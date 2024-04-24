package IESTP.FM.Entity;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "Movimiento")
public class Movimiento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

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

    public Movimiento(Long id) {
        this.id = id;
    }

    public Movimiento(Long id, LocalDateTime fechaMovimiento, String nombreResponsable, String motivoMovimiento, Equipo equipo, Ubicacion ubicacionOrigen, Ubicacion ubicacionDestino) {
        this.id = id;
        this.fechaMovimiento = fechaMovimiento;
        this.nombreResponsable = nombreResponsable;
        this.motivoMovimiento = motivoMovimiento;
        this.equipo = equipo;
        this.ubicacionOrigen = ubicacionOrigen;
        this.ubicacionDestino = ubicacionDestino;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDateTime getFechaMovimiento() {
        return fechaMovimiento;
    }

    public void setFechaMovimiento(LocalDateTime fechaMovimiento) {
        this.fechaMovimiento = fechaMovimiento;
    }

    public String getNombreResponsable() {
        return nombreResponsable;
    }

    public void setNombreResponsable(String nombreResponsable) {
        this.nombreResponsable = nombreResponsable;
    }

    public String getMotivoMovimiento() {
        return motivoMovimiento;
    }

    public void setMotivoMovimiento(String motivoMovimiento) {
        this.motivoMovimiento = motivoMovimiento;
    }

    public Equipo getEquipo() {
        return equipo;
    }

    public void setEquipo(Equipo equipo) {
        this.equipo = equipo;
    }

    public Ubicacion getUbicacionOrigen() {
        return ubicacionOrigen;
    }

    public void setUbicacionOrigen(Ubicacion ubicacionOrigen) {
        this.ubicacionOrigen = ubicacionOrigen;
    }

    public Ubicacion getUbicacionDestino() {
        return ubicacionDestino;
    }

    public void setUbicacionDestino(Ubicacion ubicacionDestino) {
        this.ubicacionDestino = ubicacionDestino;
    }
}
