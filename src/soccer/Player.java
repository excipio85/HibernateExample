package soccer;

import javax.persistence.*;

/**
 * Created by Joachim on 30.08.2015.
 */
@Entity
@Table(name = "Player")
public class Player {

    private int id;
    private String firstname;
    private String lastname;
    private Team team;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "PlayerID")
    public int getId(){
        return id;
    }
    public void setId(int id){
        this.id = id;
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
        return  lastname;
    }
    public void setLastname(String lastname){
        this.lastname = lastname;
    }

    @ManyToOne
    @JoinColumn(name = "TeamID")
    public Team getTeam(){
        return team;
    }
    public void setTeam(Team team){
        this.team = team;
    }
}
