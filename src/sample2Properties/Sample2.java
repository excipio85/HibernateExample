package sample2Properties;

import javafx.application.Application;
import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import org.hibernate.Session;

import java.time.LocalDateTime;
import java.util.stream.Collectors;

/**
 * Created by Joachim on 29.08.2015.
 */
public class Sample2 extends Application {
    private Stage primaryStage;
    final TableView<Person> personTable = new TableView<>();
    final TableView<Pet> petTable = new TableView<>();
    private Person selectedPerson = null;
    private Person personDragFrom = null;
    private Pet draggedPet = null;
    Session session = null;

    @Override
    public void start(Stage primaryStage) throws Exception {
        this.primaryStage = primaryStage;
         createDbData();
        initTables();
    }

    private void createDbData(){

        Person p1 = new Person();
        p1.setFirstname("Joachim");
        p1.setLastname("Huszarek");

        Person p2 = new Person();
        p2.setFirstname("TestFirstname");
        p2.setLastname("TestLastname");

        Pet pet1 = new Pet();
        pet1.setName("Lisa");
        pet1.setOwner(p1);

        p1.getPets().add(pet1);

        Session s = HibernateUtil.openSession();
        s.beginTransaction();

        s.save(p1);
        s.save(p2);

        s.getTransaction().commit();
        s.close();
        s = null;

        s = HibernateUtil.openSession();
        s.beginTransaction();

        ObservableList<Person> persons = FXCollections.observableArrayList(s.createQuery("from Person").list());
        persons.forEach(person -> {
            System.out.print(person.getFirstname() + ": ");
            System.out.println(person.getPets().size());
        });
        System.out.println("--------------");

        s.getTransaction().commit();
        s.close();

        //System.exit(- 5);

    }

    private void initTables(){
        session = HibernateUtil.openSession();
        ObservableList<Person> personen = FXCollections.observableArrayList(session.createQuery("from Person").list());
        session.close();
        session = null;

        Text personTableHeader = new Text("Personen");
        personTable.setItems(personen);
        TableColumn<Person, Number> personIdCol = new TableColumn<>("Person ID");
        personIdCol.setCellValueFactory((data) -> data.getValue().id());
        TableColumn<Person, String> personFnCol = new TableColumn<>("Firstname");
        personFnCol.setCellValueFactory((data) -> data.getValue().firstname());
        TableColumn<Person, String> personLnCol = new TableColumn<>("Lastname");
        personLnCol.setCellValueFactory((data) -> data.getValue().lastname());
        TableColumn<Person, Number> countPetsCol = new TableColumn<>("Pets");
        countPetsCol.setCellValueFactory(data ->
                        Bindings.createLongBinding(() ->
                                        data.getValue().getPets().stream()
                                                .collect(Collectors.counting())
                        )
        );

        personTable.getColumns().addAll(personIdCol, personFnCol, personLnCol, countPetsCol);

        personTable.getSelectionModel().selectedItemProperty().addListener((observable, oldPerson, newPerson) -> {
            if (newPerson != null) {
                petTable.setItems(FXCollections.observableArrayList(newPerson.getPets()));
                selectedPerson = newPerson;
            }
        });

        VBox boxPerson = new VBox(10, personTableHeader, personTable);

        // ----------------------------------------------------------------------

        Text petTableHeader = new Text("Haustiere");
        TableColumn<Pet, Number> petIdCol = new TableColumn<>("Pet ID");
        petIdCol.setCellValueFactory(data -> data.getValue().id());
        TableColumn<Pet, String> petNameCol = new TableColumn<>("Pet name");
        petNameCol.setCellValueFactory(data -> data.getValue().name());
        TableColumn<Pet, Number> petOwnerCol = new TableColumn<>("OwnerID");
        petOwnerCol.setCellValueFactory(data -> data.getValue().owner().get().id());
        petTable.getColumns().addAll(petIdCol, petNameCol, petOwnerCol);


        //region Add Pet Button
        Button addPetButton = new Button("Add Pet");
        addPetButton.setOnAction((evt) -> {
            Pet tmpPet = new Pet();
            tmpPet.setName("neues Pet " + LocalDateTime.now());
            tmpPet.setOwner(selectedPerson);

            selectedPerson.getPets().add(tmpPet);

            session = HibernateUtil.openSession();
            session.beginTransaction();
            session.save(tmpPet);
            session.saveOrUpdate(selectedPerson);
            session.getTransaction().commit();

            session.close();
            session = null;

            setPersonTableData(selectedPerson);

        });
        //endregion

        //region EditOwnerButton
        Button editOwnerButton = new Button("Change Owner");
        editOwnerButton.setOnAction((evt) ->{
            Pet tmpPet = petTable.getSelectionModel().getSelectedItem();
            Person newOwner = personTable.itemsProperty().get().get(0);
            selectedPerson.getPets().remove(tmpPet);
            tmpPet.setOwner(newOwner);
            newOwner.getPets().add(tmpPet);

            Session s = HibernateUtil.openSession();
            s.beginTransaction();
            s.update(selectedPerson);
            s.update(newOwner);
            s.getTransaction().commit();
            s.close();

            setPersonTableData(newOwner);
            personTable.requestFocus();
            personTable.getSelectionModel().selectFirst();
        });
        //endregion

        //region Drag and drop Pet
        petTable.setRowFactory((tableView) -> {
            TableRow<Pet> petRow = new TableRow();
            petRow.setOnDragDetected((evt) -> {
                if (! petRow.isEmpty()) {
                    Dragboard dragboard = petRow.startDragAndDrop(TransferMode.MOVE);
                    ClipboardContent cc = new ClipboardContent();
                    cc.putString("Pet.class");
                    dragboard.setContent(cc);
                    dragboard.setDragView(petRow.snapshot(null, null));
                    draggedPet = petRow.getItem();
                    personDragFrom = petRow.getItem().getOwner();
                }
            });
            return petRow;
        });

        personTable.setRowFactory((tableView) -> {
            TableRow<Person> personRow = new TableRow<>();
            personRow.setOnDragOver((evt) -> {
                if (! personRow.isEmpty()) {
                    Dragboard dragboard = evt.getDragboard();
                    if ("Pet.class".equals(dragboard.getString()) && draggedPet != null) {
                        evt.acceptTransferModes(TransferMode.MOVE);
                        personRow.setStyle("-fx-background-color: cadetblue");
                    }
                }
            });

            personRow.setOnDragExited((evt) -> {
                personRow.setStyle("");
            });

            personRow.setOnDragDropped((evt) -> {
                if (! personRow.isEmpty()) {
                    Dragboard dragboard = evt.getDragboard();
                    if ("Pet.class".equals(dragboard.getString()) && draggedPet != null) {
                        draggedPet.setOwner(personRow.getItem());
                        personDragFrom.getPets().remove(draggedPet);
                        personRow.getItem().getPets().add(draggedPet);

                        Session s = HibernateUtil.openSession();
                        s.beginTransaction();

                        s.update(personDragFrom);
                        s.update(personRow.getItem());

                        s.getTransaction().commit();
                        s.close();

                        setPersonTableData(personRow.getItem());
                        personTable.requestFocus();
                        personTable.getSelectionModel().selectFirst();

                        evt.setDropCompleted(true);
                    } else {
                        evt.setDropCompleted(false);
                    }
                }
            });

            return personRow;
        });




        //endregion

        HBox buttonBox = new HBox(10, addPetButton, editOwnerButton);
        VBox boxPet = new VBox(10, petTableHeader, petTable, buttonBox);

        VBox root = new VBox(10, boxPerson, boxPet);
        root.setPadding(new Insets(10));

        Scene scene = new Scene(root);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void setPersonTableData(Person selPer){
        Session s = HibernateUtil.openSession();
        personTable.setItems(FXCollections.observableArrayList(s.createQuery("from Person").list()));
        s.close(); s = null;

        personTable.getSelectionModel().select(selPer);
    }
    public static void main(String[] args){
        launch(args);
    }
}
