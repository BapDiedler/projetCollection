package stamps.model;

import java.util.*;

/**
 * Classe principale du model de la collection. La classe permet la gestion
 * des manipulations sur la collection
 *
 * @author baptistedie
 */
public class CollectionSatellites extends SujetObserve implements Iterable<Satellite>{

    /**
     * collection de satellites référencée par leur identifiant unique
     */
    private ArrayList<Satellite> satellites;

    /**
     * ensemble des mots clefs de la collection de satellites
     */
    private HashMap<String, Integer> motsClefs;

    /**
     * champ qui nous permet de savoir si la vue est en mode consultation ou non
     */
    private boolean estConsulte;



    /**
     * constructeur principal de la classe
     */
    public CollectionSatellites(){
        super();
        satellites = new ArrayList<>(10);
        motsClefs = new HashMap<>(10);
        estConsulte=true;
    }



    /**
     * constructeur principal de la classe
     */
    public void copieCollectionSatellites(CollectionSatellites collectionSatellites){
        satellites = collectionSatellites.satellites;
        motsClefs = collectionSatellites.motsClefs;
        estConsulte= true;
    }



    /**
     * méthode qui permet l'ajout de satellite dans la collection
     *
     * @param nom nom donné au nouveau satellite
     */
    public void ajouter(String nom){
        ajouter(nom,null);
    }



    /**
     * méthode qui permet d'ajouter un satellite qui existe déjà
     *
     * @param satellite satellite ajouté
     */
    public void ajouter(Satellite satellite){
        satellites.add(satellite);
    }



    /**
     * méthode qui permet l'ajout de satellite dans la collection
     *
     * @param nom nom donné au nouveau satellite
     * @param url url de l'image du satellite
     */
    public void ajouter(String nom, String url){
        if(url == null) url = "/image/utilisateur/pasImage.jpeg";
        Satellite satellite = new Satellite(nom,url);
        satellites.add(satellite);
        notifierObservateurs();
    }



    /**
     * suppression d'un satellite
     *
     * @param satellite satellite supprimer
     */
    public void supprimer(Satellite satellite){
        satellites.remove(satellite);
    }



    /**
     * méthode qui permet de tout supprimer
     */
    public void supprimerAll(){
        satellites.clear();
    }



    /**
     * méthode pour changer de mode de consultation
     */
    public void setEstConsulte() {
        estConsulte = !estConsulte;
    }



    /**
     * setter de mot clef dans le monde
     * @param motClef nouveau mot clef
     */
    public void setMotsClefs(int i,String... motClef) {
        int nbTags;
        for(String newTag : motClef){
            this.motsClefs.putIfAbsent(newTag,i);
            if(i == 1) {
                nbTags = this.motsClefs.get(newTag);
                this.motsClefs.replace(newTag,nbTags==-1?i:nbTags+1);
            }
        }
    }



    /**
     * méthode qui permet de trier les satellites en fonction de leur date de création
     */
    public void trierDate(){
        satellites.sort(Comparator.comparing(Satellite::getDateString));
        notifierObservateurs();
    }



    /**
     * méthode qui permet de trier les satellites en fonction de leur apparition dans la collection
     */
    public void trierApparition(){
        satellites.sort(Comparator.comparing(Satellite::getIdentifiant));
        notifierObservateurs();
    }



    /**
     * méthode qui permet de trier les satellites en fonction de leur nom
     */
    public void trierNom(){
        satellites.sort(Comparator.comparing(Satellite::getNom));
        notifierObservateurs();
    }



    /**
     * iterator qui permet d'itérer sur les satellites de la collection
     *
     * @return un iterator de satellite
     */
    @Override
    public Iterator<Satellite> iterator() {
        return satellites.iterator();
    }



    /**
     * getter sur le nombre d'éléments dans la collection
     *
     * @return le nombre de satellites dans la collection
     */
    public int nbSatellites(){
        return satellites.size();
    }



    /**
     * getter sur la collection de satellites qui permet de récupérer le satellite en fonction de sa position dans arrayList
     *
     * @param pos position du satellite dans arrayList
     * @return le satellite correspondant à la position
     */
    public Satellite getSatellite(int pos){
        return satellites.get(pos);
    }



    /**
     * getter sur les satellites qui possèdent le mot clef
     *
     * @param motClef mot clef testé
     */
    public void getSatellites(String motClef){
        HashMap<Integer,Satellite> satellitesValides = new HashMap<>(10);
        int val=0;
        for (Satellite satellite: satellites){
            val = satellite.getMotsClefs(motClef);
            if (val!=0){
                satellitesValides.put(val,satellite);
            }
        }
        trierSatelliteMotClef(satellitesValides);
    }



    /**
     * getter de motsClefs en fonction du mot rentrée en paramètre
     *
     * @param mot mot dont l'on cherche la correspondance
     * @return une arrayList de String avec les mots qui commence pareil
     */
    public ArrayList<String> getMotsClefs(String mot) {
        ArrayList<String> suggestions = new ArrayList<>();
        for (String motClef : motsClefs.keySet()) {
            if (motClef.toLowerCase().startsWith(mot.toLowerCase())) {
                suggestions.add(motClef);
            }
        }
        return suggestions;
    }



    /**
     * méthode qui permet de trier une hashMap de satellite
     *
     * @param satellitesValides la collection de satellites que l'on doit trier
     */
    private void trierSatelliteMotClef(
            HashMap<Integer,Satellite> satellitesValides){
        ArrayList<Map.Entry<Integer,Satellite>> arrayList = new ArrayList<>(satellitesValides.entrySet());
        arrayList.sort((o1, o2) -> o2.getKey().compareTo(o1.getKey()));
        int size = arrayList.size();
        ArrayList<Satellite> newArray = new ArrayList<>(size);
        for(Map.Entry<Integer,Satellite> sat : arrayList){
            newArray.add(sat.getValue());
        }

        satellites.sort((s1, s2) -> {
            if (newArray.contains(s1) && newArray.contains(s2)) {
                return newArray.indexOf(s1) - newArray.indexOf(s2);
            } else if (newArray.contains(s1)) {
                return -1;
            } else if (newArray.contains(s2)) {
                return 1;
            } else {
                return 0;
            }
        });
    }



    /**
     * méthode pour connaitre le mode de vue
     *
     * @return true on est en mode consultation
     */
    public boolean isEstConsulte() {
        return estConsulte;
    }



    /**
     * le toString de la classe permet de voir les satellites dans l'ordre de la collection
     *
     * @return collection des satellites
     */
    public String toString(){
        StringBuilder affichage = new StringBuilder();
        for (Satellite satellite: satellites){
            affichage.append(satellite.getNom()).append("-");
        }
        return affichage.toString();
    }



    /**
     * méthode qui permet de retirer les tags non utilisés par les satellites
     *
     * @param text tag à enlever
     */
    public void removeTags(String text) {
        int val = motsClefs.get(text);
        System.out.println(val);
        if(val == 1 || val == -1){
            motsClefs.remove(text);
        }else{
            motsClefs.replace(text,val,val-1);
        }
    }
}
