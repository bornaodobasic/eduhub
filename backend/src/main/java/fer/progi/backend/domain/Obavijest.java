package fer.progi.backend.domain;


import java.util.Date;

import com.fasterxml.jackson.annotation.JsonBackReference;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class Obavijest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int sifObavijest;

    @Column(nullable = false)
    private String naslovObavijest;

    @Column(nullable = false)
    private String sadrzajObavijest;
    
    @Column(nullable = false)
    private Date datumObavijest;
    
    @ManyToOne
    @JsonBackReference 
    @JoinColumn(name = "sifPredmet", referencedColumnName = "sifPredmet", nullable = true)
    private Predmet predmet; 

    @Column(nullable = true)
    private String imeNastavnik;
    
    @Column(nullable = true)
    private String prezimeNastavnik;
    
    @Column(nullable = true)
    private String adresaLokacija;
    
    @Column(nullable = true)
    private String gradLokacija;
    
    @Column(nullable = true)
    private String drzavaLoakcija;
}

