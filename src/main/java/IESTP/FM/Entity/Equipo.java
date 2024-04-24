package IESTP.FM.Entity;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "Equipo")
public class Equipo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "tipo_equipo", nullable = false, length = 100)
    private String tipoEquipo;

    @Column(name = "codigo_barra", nullable = false, length = 255)
    private String codigoBarra;

    @Column(name = "codigo_patrimonial", nullable = false, length = 255)
    private String codigoPatrimonial;

    @Column(name = "descripcion", nullable = false, length = 255)
    private String descripcion;

    @Column(name = "estado", nullable = false, length = 100)
    private String estado;

    @Column(name = "fecha_compra", nullable = false)
    private LocalDateTime fechaCompra;

    @Column(name = "marca", nullable = false, length = 100)
    private String marca;

    @Column(name = "modelo", nullable = false, length = 100)
    private String modelo;

    @Column(name = "nombre_equipo", nullable = false, length = 255)
    private String nombreEquipo;

    @Column(name = "numero_orden", nullable = false, length = 100)
    private String numeroOrden;

    @Column(name = "serie", nullable = false, length = 255)
    private String serie;

    @ManyToOne
    @JoinColumn(name = "responsable_id")
    private Empleado responsable;

    @ManyToOne
    @JoinColumn(name = "ubicacion_id")
    private Ubicacion ubicacion;

    public Equipo() {
    }

    public Equipo(Long id) {
        this.id = id;
    }

    public Equipo(Long id, String tipoEquipo, String codigoBarra, String codigoPatrimonial, String descripcion, String estado, LocalDateTime fechaCompra, String marca, String modelo, String nombreEquipo, String numeroOrden, String serie, Empleado responsable, Ubicacion ubicacion) {
        this.id = id;
        this.tipoEquipo = tipoEquipo;
        this.codigoBarra = codigoBarra;
        this.codigoPatrimonial = codigoPatrimonial;
        this.descripcion = descripcion;
        this.estado = estado;
        this.fechaCompra = fechaCompra;
        this.marca = marca;
        this.modelo = modelo;
        this.nombreEquipo = nombreEquipo;
        this.numeroOrden = numeroOrden;
        this.serie = serie;
        this.responsable = responsable;
        this.ubicacion = ubicacion;
    }
}
