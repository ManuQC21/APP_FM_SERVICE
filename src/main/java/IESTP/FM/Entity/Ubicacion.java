package IESTP.FM.Entity;

import javax.persistence.*;

@Entity
@Table(name = "Ubicacion")
public class Ubicacion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "ambiente", nullable = false, length = 255)
    private String ambiente;

    @Column(name = "ubicacion_fisica", nullable = false, length = 255)
    private String ubicacionFisica;

    public Ubicacion() {
    }

    public Ubicacion(Long id) {
        this.id = id;
    }

    public Ubicacion(Long id, String ambiente, String ubicacionFisica) {
        this.id = id;
        this.ambiente = ambiente;
        this.ubicacionFisica = ubicacionFisica;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAmbiente() {
        return ambiente;
    }

    public void setAmbiente(String ambiente) {
        this.ambiente = ambiente;
    }

    public String getUbicacionFisica() {
        return ubicacionFisica;
    }

    public void setUbicacionFisica(String ubicacionFisica) {
        this.ubicacionFisica = ubicacionFisica;
    }
}
