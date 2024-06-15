package IESTP.FM.Entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import javax.persistence.*;
import javax.validation.constraints.*;
import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Entity
@Table(name = "Equipo")
public class Equipo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @NotNull
    @Size(min = 1, max = 255)
    @Pattern(regexp = "^[A-Za-z ]+$")
    @Column(name = "tipo_equipo", nullable = false, length = 255)
    private String tipoEquipo;

    @NotNull
    @Size(min = 1, max = 255)
    @Pattern(regexp = "^[A-Za-z0-9]+$")
    @Column(name = "codigo_barra", nullable = false, length = 255)
    private String codigoBarra;

    @NotNull
    @Size(min = 1, max = 255)
    @Pattern(regexp = "^[A-Za-z0-9]+$")
    @Column(name = "codigo_patrimonial", nullable = false, length = 255)
    private String codigoPatrimonial;

    @NotNull
    @Size(min = 1, max = 255)
    @Column(name = "descripcion", nullable = false, length = 255)
    private String descripcion;

    @NotNull
    @Size(min = 1, max = 255)
    @Pattern(regexp = "^[A-Za-z ]+$")
    @Column(name = "estado", nullable = false, length = 255)
    private String estado;

    @NotNull
    @Column(name = "fecha_compra", nullable = false)
    @JsonFormat(pattern = "dd/MM/yyyy")
    private LocalDate fechaCompra;

    @NotNull
    @Size(min = 1, max = 255)
    @Column(name = "marca", nullable = false, length = 255)
    private String marca;

    @NotNull
    @Size(min = 1, max = 255)
    @Column(name = "modelo", nullable = false, length = 255)
    private String modelo;

    @NotNull
    @Size(min = 1, max = 255)
    @Column(name = "nombre_equipo", nullable = false, length = 255)
    private String nombreEquipo;

    @NotNull
    @Size(min = 1, max = 255)
    @Pattern(regexp = "^[A-Za-z0-9]+$")
    @Column(name = "numero_orden", nullable = false, length = 255)
    private String numeroOrden;

    @NotNull
    @Size(min = 1, max = 255)
    @Pattern(regexp = "^[A-Za-z0-9]+$")
    @Column(name = "serie", nullable = false, length = 255)
    private String serie;

    @ManyToOne
    @JoinColumn(name = "responsable_id", nullable = false)
    private Empleado responsable;

    @ManyToOne
    @JoinColumn(name = "ubicacion_id", nullable = false)
    private Ubicacion ubicacion;
}
