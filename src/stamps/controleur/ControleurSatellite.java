package stamps.controleur;

import javafx.animation.*;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.util.Duration;
import stamps.model.CollectionSatellites;
import stamps.model.Compteur;

import java.io.IOException;
import java.util.Objects;

/**
 * Le contrôleur permet de manipuler les satellites se trouvant dans la vue globale.
 * À l'intérieur de la liste view
 *
 * @author baptistedie
 */
public class ControleurSatellite extends Controleur {

    /**
     * image où se trouve l'image du satellite
     */
    @FXML
    private ImageView image;

    /**
     * label contenant le nom du satellite
     */
    @FXML
    private Label nom;

    /**
     * menu permettant en outre de supprimer le satellite ou la totalité des satellites
     */
    @FXML
    private ContextMenu menuContext;

    /**
     * position du satellite observé
     */
    private final int posSatellite;

    /**
     * compteur de satellite
     */
    private final Compteur compteur ;



    /**
     * constructeur de la classe
     *
     * @param satellites satellites manipulés
     */
    public ControleurSatellite(CollectionSatellites satellites, int pos, Compteur compteur){
        super(satellites);
        posSatellite = pos;
        this.compteur = compteur;
    }



    /**
     * méthode de qui permet d'initialiser les éléments de la page
     */
    @FXML
    void initialize(){
        String url = collectionSatellites.getSatellite(posSatellite).getUrl();
        Image imageUrl = new Image(Objects.requireNonNull(getClass().getResourceAsStream(url)),
                200, 200, true, true) ;
        image.setImage(imageUrl);
        nom.setText(collectionSatellites.getSatellite(posSatellite).getNom());
    }



    /**
     * méthode qui permet d'aller sur la vue détaillée d'un satellite
     *
     * @throws IOException exception liée au chargement de fichier
     */
    @FXML
    void changerDetail(MouseEvent event) throws IOException {
        if(event.getButton() == MouseButton.SECONDARY) { // action pour faire apparaitre le menu contextuel
            menuContext.show(nom, event.getScreenX(), event.getScreenY());
        }else {
            collectionSatellites.clear();
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/vue/PanneauDetail.fxml"));
            ControleurDetail detail = new ControleurDetail(collectionSatellites,posSatellite);
            loader.setControllerFactory(ic ->
                    detail
            );
            Parent root = loader.load();
            transition(root);
        }
    }



    /**
     * méthode qui permet d'appliquer une transition lors du changement de vue
     *
     * @param root élément principal de la fenêtre à afficher lors de la transition
     */
    private void transition(Parent root){
        // Récupérer la référence de la stackPane actuelle
        StackPane stackPane = (StackPane) image.getScene().getRoot();
        root.translateYProperty().set(image.getScene().getHeight());
        stackPane.getChildren().clear();
        // Changer la scène de la stackPane actuelle
        stackPane.getChildren().add(root);

        Timeline timeline = new Timeline();
        KeyValue kv = new KeyValue(root.translateYProperty(), 0, Interpolator.EASE_IN);
        KeyFrame kf = new KeyFrame(Duration.seconds(0.5), kv);
        timeline.getKeyFrames().add(kf);

        //lancement de l'animation
        timeline.play();
    }



    /**
     * méthode qui permet de supprimer définitivement un satellite
     */
    @FXML
    void supprimer(){
        collectionSatellites.supprimer(collectionSatellites.getSatellite(posSatellite));
        collectionSatellites.notifierObservateurs();
        compteur.decrementer();
    }



    /**
     * méthode qui permet de tout supprimer
     */
    @FXML
    void supprimerAll(){
        collectionSatellites.supprimerAll();
        collectionSatellites.notifierObservateurs();
        compteur.setValeur(0);
    }



    /**
     * méthode réagir qui sera activée à chaque action
     */
    @Override
    public void reagir() {}
}
