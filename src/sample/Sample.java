package sample;

import javafx.application.Application;
import javafx.beans.property.SimpleLongProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.hibernate.Session;

/**
 * Created by Joachim on 24.08.2015.
 */
public class Sample extends Application{

    public static void main(String[] args){
        launch(args);
       /* Address address = new Address();
        address.setPostalcode("6433");
        address.setCity("Oetz");
        address.setStreet("Ochsengarten 30");

        Address address2 = new Address();
        address2.setPostalcode("64331");
        address2.setCity("Ötz");
        address2.setStreet("Ochsengarten 301");

        Person p = new Person();
        p.setFirstname("Joachim");
        p.setLastname("Huszarek");

        List<Address> addresselen = new ArrayList<Address>(){{
            add(address); add(address2);
        }};

        p.setListOfAddress(addresselen);

        Session s = HibernateUtil.openSession();
        s.beginTransaction();
        s.save(p);
        s.getTransaction().commit();
        s.close();

        Session x = HibernateUtil.openSession();
        x.beginTransaction();

        ObservableList<Person> personen = FXCollections.observableArrayList( x.createQuery("from Person").list() );
        x.getTransaction().commit();



        x.close();

        p = null;
        x = HibernateUtil.openSession();
        p = (Person) x.get(Person.class, 1);
        x.close();
        System.out.println(p.getListOfAddress().size());

    */

    }



    @Override
    public void start(Stage primaryStage) throws Exception {
        primaryStage.setTitle("Test App");
        primaryStage.setWidth(600);
        primaryStage.setHeight(400);
        Session s = HibernateUtil.openSession();
        s.beginTransaction();
        ObservableList<Person> personen = FXCollections.observableArrayList(s.createQuery("from Person").list());
        ObservableList<Address> addressen = FXCollections.observableArrayList();
        personen.forEach(person -> {
            addressen.addAll(person.getListOfAddress());
        });

        TableView<Person> table = new TableView(personen);
        TableColumn<Person, String> firstnameCol = new TableColumn<>("First name");
        firstnameCol.setCellValueFactory((data) -> new SimpleStringProperty(data.getValue().getFirstname()));

        TableColumn<Person, Number> countPetsCol = new TableColumn<>("Addresses");
        countPetsCol.setCellValueFactory((data) -> new SimpleLongProperty(data.getValue().getListOfAddress().size()));


        table.getColumns().add(firstnameCol);
        table.getColumns().add(countPetsCol);

        table.getSelectionModel().selectFirst();

        TableView<Address> addressTable = new TableView();
        TableColumn<Address, String> streetCol = new TableColumn<>("Street");

        table.selectionModelProperty().addListener((observable, oldvalue, newvalue) -> {
            addressTable.setItems(FXCollections.observableArrayList(newvalue.getSelectedItem().getListOfAddress()));
            //addressTable.getColumns().add(streetCol);
            System.out.println("zres");
        });

        table.getSelectionModel().selectedItemProperty().addListener((observable, oldVal, newVal) -> {
            addressTable.setItems(FXCollections.observableArrayList(newVal.getListOfAddress()));
            //addressTable.getColumns().add(streetCol);
            System.out.println("zres");
        });

        addressTable.setItems(FXCollections.observableArrayList(table.getSelectionModel().getSelectedItem().getListOfAddress()));
        streetCol.setCellValueFactory((data) -> new SimpleStringProperty(data.getValue().getStreet()));
        addressTable.getColumns().add(streetCol);


        VBox box = new VBox(10, table, addressTable);
        box.setPadding(new Insets(10));
        Scene scene = new Scene(box);
        primaryStage.setScene(scene);
        primaryStage.show();
    }
}
