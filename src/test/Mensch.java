package test;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collection;

/**
 * Created by Joachim on 29.08.2015.
 */

@Entity
public class Mensch {

    private int id;
    private String name;
    private Collection<Tier> tiere = new ArrayList<>();

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "MenschID")
    public int getId(){
        return id;
    }

    public void setId(int id){
        this.id = id;
    }

    @Column(name = "MenschName")
    public String getName(){
        return name;
    }

    public void setName(String name){
        this.name = name;
    }


    @OneToMany(fetch = FetchType.LAZY)
    public Collection<Tier> getTiere(){
        return tiere;
    }

    public void setTiere(Collection<Tier> tiere){
        this.tiere = tiere;
    }
}
