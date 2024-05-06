package IESTP.FM.Entity;

import javax.persistence.*;

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

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getResultadoAuditoria() {
        return resultadoAuditoria;
    }

    public void setResultadoAuditoria(String resultadoAuditoria) {
        this.resultadoAuditoria = resultadoAuditoria;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }

    public Auditoria getAuditoria() {
        return auditoria;
    }

    public void setAuditoria(Auditoria auditoria) {
        this.auditoria = auditoria;
    }
}
