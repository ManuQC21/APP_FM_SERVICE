package IESTP.FM.Entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDate;
@Setter
@Getter
@Entity
@Table(name = "Informacion")
public class InventoryItems {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

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

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate fechaCompra;

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

    @OneToOne
    @JoinColumn(name = "responsable_id", nullable = true)
    private Empleado responsable;

    @OneToOne
    @JoinColumn(name = "ubicacion_id", nullable = true)
    private Ubicacion ubicacion;

    @OneToOne
    @JoinColumn(nullable = true)
    private Foto foto;

    public InventoryItems() {
    }

    public InventoryItems(int id) {
        this.id = id;
    }

    public InventoryItems(int id, String tipoEquipo, String codigoBarra, String codigoPatrimonial, String descripcion, String estado, LocalDate fechaCompra, String marca, String modelo, String nombreEquipo, String numeroOrden, String serie, Empleado responsable, Ubicacion ubicacion) {
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
