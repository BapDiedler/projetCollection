package stamps.controleur;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import stamps.model.CollectionSatellites;
import stamps.model.Satellite;

import java.util.Objects;

public class PanneauDetail extends Controleur{

    @FXML
    private HBox hbox;
    private int posSatellite;

    /**
     * constructeur principal de la classe
     *
     * @param collectionSatellites collection manipulée par la classe
     */
    public PanneauDetail(CollectionSatellites collectionSatellites) {
        super(collectionSatellites);
        posSatellite = 0;
    }

    @FXML
    void suivant(){
        if(posSatellite!=collectionSatellites.nbSatellites()-1)
            posSatellite+=1;
        Satellite satellite = collectionSatellites.getSatellite(posSatellite);
        Image image = new Image(Objects.requireNonNull(getClass().getResourceAsStream(satellite.getUrl())),
                300, 300, true, true) ;
        hbox.getChildren().set(0,new ImageView(image));
        hbox.getChildren().set(1,new Label(satellite.getIdentifiant()));
    }

    @FXML
    void precedant(){
        if(posSatellite!=0)
            posSatellite-=1;
        Satellite satellite = collectionSatellites.getSatellite(posSatellite);
        Image image = new Image(Objects.requireNonNull(getClass().getResourceAsStream(satellite.getUrl())),
                300, 300, true, true) ;
        hbox.getChildren().set(0,new ImageView(image));
        hbox.getChildren().set(1,new Label(satellite.getIdentifiant()));
    }

    /**
     * méthode réagir qui sera activée à chaque action
     */
    @Override
    public void reagir() {

    }
}