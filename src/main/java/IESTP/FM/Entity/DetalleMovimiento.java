package IESTP.FM.Entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
@Setter
@Getter
@Entity
@Table(name = "Detalle_Movimiento")
public class DetalleMovimiento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

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

    public DetalleMovimiento(Integer id) {
        this.id = id;
    }

    public DetalleMovimiento(Integer id, int cantidad, String tipoMovimiento, String observaciones, Movimiento movimiento) {
        this.id = id;
        this.cantidad = cantidad;
        this.tipoMovimiento = tipoMovimiento;
        this.observaciones = observaciones;
        this.movimiento = movimiento;
    }
}
