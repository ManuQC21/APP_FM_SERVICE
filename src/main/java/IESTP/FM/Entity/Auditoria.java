package IESTP.FM.Entity;


import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "Auditoria")
public class Auditoria {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "fecha_auditoria", nullable = false)
    private LocalDateTime fechaAuditoria;

    @Column(name = "observaciones", length = 255)
    private String observaciones;

    @Column(name = "estado_auditoria", nullable = false, length = 100)
    private String estadoAuditoria;

    @ManyToOne
    @JoinColumn(name = "equipo_id")
    private Equipo equipo;

    public Auditoria() {
    }

    public Auditoria(Long id) {
        this.id = id;
    }

    public Auditoria(Long id, LocalDateTime fechaAuditoria, String observaciones, String estadoAuditoria, Equipo equipo) {
        this.id = id;
        this.fechaAuditoria = fechaAuditoria;
        this.observaciones = observaciones;
        this.estadoAuditoria = estadoAuditoria;
        this.equipo = equipo;
    }

    public LocalDateTime getFechaAuditoria() {
        return fechaAuditoria;
    }

    public void setFechaAuditoria(LocalDateTime fechaAuditoria) {
        this.fechaAuditoria = fechaAuditoria;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }

    public String getEstadoAuditoria() {
        return estadoAuditoria;
    }

    public void setEstadoAuditoria(String estadoAuditoria) {
        this.estadoAuditoria = estadoAuditoria;
    }

    public Equipo getEquipo() {
        return equipo;
    }

    public void setEquipo(Equipo equipo) {
        this.equipo = equipo;
    }
}
