package sample;

import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 * Created by Joachim on 24.08.2015.
 */

@Embeddable
public class Address {
    private String street;
    private String city;
    private String postalcode;

    @Column(name = "Street")
    public String getStreet(){
        return street;
    }

    public void setStreet(String street){
        this.street = street;
    }

    @Column(name = "City")
    public String getCity(){
        return city;
    }

    public void setCity(String city){
        this.city = city;
    }

    @Column(name = "Postalcode")
    public String getPostalcode(){
        return postalcode;
    }

    public void setPostalcode(String postalcode){
        this.postalcode = postalcode;
    }

    @Override
    public String toString(){
        return getStreet() + getCity() + getPostalcode();
    }
}
