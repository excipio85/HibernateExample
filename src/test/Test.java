package test;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.hibernate.Session;

/**
 * Created by Joachim on 29.08.2015.
 */
public class Test {

    public static void main(String[] args){
        Session s = HibernateUtility.openSession();
        s.beginTransaction();

        Mensch m = new Mensch();
        m.setName("Joachim");

        Tier t = new Tier();
        t.setName("Fiffid");
        m.getTiere().add(t);
        t.setBesitzer(m);
        s.save(t);
        s.save(m);
        s.getTransaction().commit();
        s.close();
        s = null;

        s = HibernateUtility.openSession();
        s.beginTransaction();

        ObservableList<Mensch> menschen = FXCollections.observableArrayList(s.createQuery("from Mensch").list());
        menschen.forEach(men -> {
            System.out.print(men.getName() + ": ");
            System.out.println(men.getTiere().size());
        });

        s.getTransaction().commit();
        s.close();
    }
}
