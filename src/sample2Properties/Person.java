package sample2Properties;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collection;

/**
 * Created by Joachim on 29.08.2015.
 */
@Entity
public class Person {

    private int _id;
    private IntegerProperty id = new SimpleIntegerProperty();

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "PersonID")
    public int getId(){
        if(id == null){
            return _id;
        }else{
            return id.get();
        }
    }

    public void setId(int id){
        if(this.id == null){
            _id = id;
        }else {
            this.id.set(id);
        }
    }

    public IntegerProperty id(){
        if(id == null){
            id = new SimpleIntegerProperty(this, "id", _id);
        }
        return id;
    }

    private String _firstname;
    private StringProperty firstname = new SimpleStringProperty();

    @Column(name = "Firstname")
    public String getFirstname(){
        if(firstname == null){
            return _firstname;
        }else{
            return firstname.get();
        }
    }

    public void setFirstname(String firstname){
        if(this.firstname == null){
            _firstname = firstname;
        }else{
            this.firstname.set(firstname);
        }
    }

    public StringProperty firstname(){
        if(firstname == null){
            firstname = new SimpleStringProperty(this, "firstname", _firstname);
        }
        return firstname;
    }

    private String _lastname;
    private StringProperty lastname = new SimpleStringProperty();

    @Column(name = "Lastname")
    public String getLastname(){
        if(lastname == null){
            return _lastname;
        }else{
            return lastname.get();
        }
    }

    public void setLastname(String lastname){
        if(this.lastname == null){
            _lastname = lastname;
        }else{
            this.lastname.set(lastname);
        }
    }

    public StringProperty lastname(){
        if(lastname == null){
            lastname = new SimpleStringProperty(this, "lastname", _lastname);
        }
        return lastname;
    }

    private Collection<Pet> pets = new ArrayList<>();

    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL, mappedBy = "owner")
    public Collection<Pet> getPets(){
       return pets;
    }

    public void setPets(Collection<Pet> pets){
        this.pets = pets;
    }

    public ObservableList<Pet> pets(){
        return FXCollections.observableArrayList(pets);
    }

}
