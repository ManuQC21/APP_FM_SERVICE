package IESTP.FM.Entity;


import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;

@Setter
@Getter
@Entity
@Table(name = "Auditoria")
public class Auditoria {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

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

    public Auditoria(Integer id) {
        this.id = id;
    }

    public Auditoria(Integer id, LocalDateTime fechaAuditoria, String observaciones, String estadoAuditoria, Equipo equipo) {
        this.id = id;
        this.fechaAuditoria = fechaAuditoria;
        this.observaciones = observaciones;
        this.estadoAuditoria = estadoAuditoria;
        this.equipo = equipo;
    }

}
