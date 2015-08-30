package test;

import javax.persistence.*;

/**
 * Created by Joachim on 29.08.2015.
 */

@Entity
public class Tier {

    private int id;
    private String name;
    private Mensch besitzer;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "TierID")
    public int getId(){
        return id;
    }
    public void setId(int id){
        this.id = id;
    }

    @Column(name = "Tiername")
    public String getName(){
        return name;
    }
    public void setName(String name){
        this.name = name;
    }

    @OneToOne
    public Mensch getBesitzer(){
        return besitzer;
    }

    public void setBesitzer(Mensch besitzer){
        this.besitzer = besitzer;
    }
}
