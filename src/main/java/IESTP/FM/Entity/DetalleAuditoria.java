package IESTP.FM.Entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
@Setter
@Getter
@Entity
@Table(name = "Detalle_Auditoria")
public class DetalleAuditoria {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "resultado_auditoria", length = 100)
    private String resultadoAuditoria;

    @Column(name = "observaciones", length = 255)
    private String observaciones;

    @ManyToOne
    @JoinColumn(name = "auditoria_id")
    private Auditoria auditoria;

    // Constructor vacío
    public DetalleAuditoria() {
    }

    public DetalleAuditoria(Integer id) {
        this.id = id;
    }

    public DetalleAuditoria(Integer id, String resultadoAuditoria, String observaciones, Auditoria auditoria) {
        this.id = id;
        this.resultadoAuditoria = resultadoAuditoria;
        this.observaciones = observaciones;
        this.auditoria = auditoria;
    }

}
