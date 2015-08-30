package soccer;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.stage.Stage;
import org.hibernate.Session;

/**
 * Created by Joachim on 30.08.2015.
 */
public class MainApp extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        Session session = HibernateUtilSooccer.openSession();
        session.beginTransaction();

        Team team1 = new Team();
        team1.setName("FC Barcelona");
        Team team2 = new Team();
        team2.setName("FC Bayern München");

        Player p1 = new Player(); p1.setFirstname("Lionel");
        p1.setLastname("Messi"); p1.setTeam(team1);

        team1.getPlayers().clear();
        team1.getPlayers().add(p1);

        session.save(team1);
        session.save(team2);

        session.getTransaction().commit();



        ObservableList<Team> teams = FXCollections.observableArrayList(session.createQuery("from Team").list());
        teams.forEach(team -> {
            System.out.println(team.getName());
            System.out.println("-------------");
            System.out.println(team.getPlayers().size());
        });

        session.close();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
