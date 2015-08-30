package soccer;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Joachim on 30.08.2015.
 */
@Entity
@Table(name = "Team")
public class Team {

    private int id;
    private String name;
    private List<Player> players = new ArrayList<>();

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "TeamID")
    public int getId(){
        return id;
    }
    public void setId(int id){
        this.id = id;
    }

    @Column(name = "TeamName")
    public String getName(){
        return name;
    }

    public void setName(String name){
        this.name = name;
    }

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "team")
    public List<Player> getPlayers(){
        return players;
    }

    public void setPlayers(List<Player> players){
        this.players = players;
    }


}
