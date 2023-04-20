package stamps.controleur;

import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import stamps.model.CollectionSatellites;
import stamps.model.Satellite;

import java.io.IOException;
import java.io.InputStream;
import java.util.Objects;

/**
 * classe permettant de gérer la collection
 */
public class PanneauCentral extends Controleur{

    @FXML
    private VBox vbox;

    /**
     * constructeur principal de la classe
     *
     * @param collectionSatellites collection manipulée par la classe
     */
    public PanneauCentral(CollectionSatellites collectionSatellites) {
        super(collectionSatellites);
    }

    @FXML
    void ajouter(Satellite satellite){
        String url = satellite.getUrl();
        Image image = new Image(Objects.requireNonNull(getClass().getResourceAsStream(url)),
                300, 300, true, true) ;
        ImageView imageView = new ImageView(image);
        Label label = new Label(satellite.getNom());
        HBox hbox = new HBox(imageView,label);
        vbox.getChildren().add(hbox);
        vbox.getScene().getRoot().layout();
    }

    /**
     * méthode réagir qui sera activée à chaque action
     */
    @Override
    public void reagir() {

    }
}