package IESTP.FM.Entity;

import javax.persistence.*;

@Entity
@Table(name = "Detalle_Movimiento")
public class DetalleMovimiento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "cantidad")
    private int cantidad;

    @Column(name = "tipo_movimiento", length = 100)
    private String tipoMovimiento;

    @Column(name = "observaciones", length = 255)
    private String observaciones;

    @ManyToOne
    @JoinColumn(name = "movimiento_id")
    private Movimiento movimiento;

    // Constructor vacío
    public DetalleMovimiento() {
    }

    public DetalleMovimiento(Long id) {
        this.id = id;
    }

    public DetalleMovimiento(Long id, int cantidad, String tipoMovimiento, String observaciones, Movimiento movimiento) {
        this.id = id;
        this.cantidad = cantidad;
        this.tipoMovimiento = tipoMovimiento;
        this.observaciones = observaciones;
        this.movimiento = movimiento;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    public String getTipoMovimiento() {
        return tipoMovimiento;
    }

    public void setTipoMovimiento(String tipoMovimiento) {
        this.tipoMovimiento = tipoMovimiento;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }

    public Movimiento getMovimiento() {
        return movimiento;
    }

    public void setMovimiento(Movimiento movimiento) {
        this.movimiento = movimiento;
    }
}
