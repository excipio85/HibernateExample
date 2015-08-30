package sample;

import org.hibernate.annotations.CollectionId;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Entity
@Table(name = "Personelein")
public class Person{

    private int personId;
    private String firstname;
    private String lastname;
    private Collection<Address> listOfAddress = new ArrayList<>();

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "PersonId")
    public int getPersonId(){
        return personId;
    }

    public void setPersonId(int personId){
        this.personId = personId;
    }

    @Column(name = "Firstname")
    public String getFirstname(){
        return firstname;
    }

    public void setFirstname(String firstname){
        this.firstname = firstname;
    }

    @Column(name = "Lastname")
    public String getLastname(){
        return lastname;
    }

    public void setLastname(String lastname){
        this.lastname = lastname;
    }


    @ElementCollection(fetch = FetchType.EAGER)
    @JoinTable(name="ZentralerAdressstamm")
    @GenericGenerator(name = "hilo-gen", strategy = "hilo")
    @CollectionId(columns = @Column(name = "AddressId"), type = @Type(type = "long"), generator = "hilo-gen")
    public Collection<Address> getListOfAddress() {
        return listOfAddress;
    }

    public void setListOfAddress(List<Address> listOfAddress) {
        this.listOfAddress = listOfAddress;
    }
}