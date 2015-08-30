package sample2Properties;

import javafx.beans.property.*;

import javax.persistence.*;

/**
 * Created by Joachim on 29.08.2015.
 */

@Entity
public class Pet {

    private int _id;
    private IntegerProperty id = new SimpleIntegerProperty();

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "PetID")
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
        }else{
            this.id.set(id);
        }
    }

    public IntegerProperty id(){
        if(id == null){
            id = new SimpleIntegerProperty(this, "id", _id);
        }
        return id;
    }

    private String _name;
    private StringProperty name = new SimpleStringProperty();

    @Column(name = "Name")
    public String getName(){
        if(name == null){
            return _name;
        }else{
            return name.get();
        }
    }

    public void setName(String name){
        if(this.name == null){
            _name = name;
        }else{
            this.name.set(name);
        }
    }

    public StringProperty name(){
        if(name == null){
            name = new SimpleStringProperty(this, "name", _name);
        }
        return name;
    }

    private Person _owner;
    private ObjectProperty<Person> owner = new SimpleObjectProperty<>();


   @OneToOne
   @JoinColumn(name = "OwnerId")
    public Person getOwner(){
        if(owner == null){
            return _owner;
        }else{
            return owner.get();
        }
    }


    public void setOwner(Person owner){
        if(this.owner == null){
            _owner = owner;
        }else{
            this.owner.set(owner);
        }
    }

    public ObjectProperty<Person> owner(){
        if(owner == null){
            owner = new SimpleObjectProperty<Person>(this, "owner", _owner);
        }
        return owner;
    }

}
