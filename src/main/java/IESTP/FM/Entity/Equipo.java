package IESTP.FM.Entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "Equipo")
public class Equipo {

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

    @Column(name = "fecha_compra", nullable = false)
    @JsonFormat(pattern = "dd-MM-yyyy")
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
    private Foto foto;

    public Equipo() {
    }

    public Equipo(int id) {
        this.id = id;
    }

    public Equipo(int id, String tipoEquipo, String codigoBarra, String codigoPatrimonial, String descripcion, String estado, LocalDate fechaCompra, String marca, String modelo, String nombreEquipo, String numeroOrden, String serie, Empleado responsable, Ubicacion ubicacion) {
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
    public Equipo(InventoryItems inventoryItems) {
        this.id = id;
        this.tipoEquipo = inventoryItems.getTipoEquipo();
        this.codigoBarra = inventoryItems.getCodigoBarra();
        this.codigoPatrimonial = inventoryItems.getCodigoPatrimonial();
        this.descripcion = inventoryItems.getDescripcion();
        this.estado = inventoryItems.getEstado();
        this.fechaCompra = inventoryItems.getFechaCompra();
        this.marca = inventoryItems.getMarca();
        this.modelo = inventoryItems.getModelo();
        this.nombreEquipo = inventoryItems.getNombreEquipo();
        this.numeroOrden = inventoryItems.getNumeroOrden();
        this.serie = inventoryItems.getSerie();
        this.responsable = inventoryItems.getResponsable();
        this.ubicacion = inventoryItems.getUbicacion();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTipoEquipo() {
        return tipoEquipo;
    }

    public void setTipoEquipo(String tipoEquipo) {
        this.tipoEquipo = tipoEquipo;
    }

    public String getCodigoBarra() {
        return codigoBarra;
    }

    public void setCodigoBarra(String codigoBarra) {
        this.codigoBarra = codigoBarra;
    }

    public String getCodigoPatrimonial() {
        return codigoPatrimonial;
    }

    public void setCodigoPatrimonial(String codigoPatrimonial) {
        this.codigoPatrimonial = codigoPatrimonial;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public LocalDate getFechaCompra() {
        return fechaCompra;
    }

    public void setFechaCompra(LocalDate fechaCompra) {
        this.fechaCompra = fechaCompra;
    }

    public String getMarca() {
        return marca;
    }

    public void setMarca(String marca) {
        this.marca = marca;
    }

    public String getModelo() {
        return modelo;
    }

    public void setModelo(String modelo) {
        this.modelo = modelo;
    }

    public String getNombreEquipo() {
        return nombreEquipo;
    }

    public void setNombreEquipo(String nombreEquipo) {
        this.nombreEquipo = nombreEquipo;
    }

    public String getNumeroOrden() {
        return numeroOrden;
    }

    public void setNumeroOrden(String numeroOrden) {
        this.numeroOrden = numeroOrden;
    }

    public String getSerie() {
        return serie;
    }

    public void setSerie(String serie) {
        this.serie = serie;
    }

    public Empleado getResponsable() {
        return responsable;
    }

    public void setResponsable(Empleado responsable) {
        this.responsable = responsable;
    }

    public Ubicacion getUbicacion() {
        return ubicacion;
    }

    public void setUbicacion(Ubicacion ubicacion) {
        this.ubicacion = ubicacion;
    }

    public Foto getFoto() {
        return foto;
    }

    public void setFoto(Foto foto) {
        this.foto = foto;
    }
}
