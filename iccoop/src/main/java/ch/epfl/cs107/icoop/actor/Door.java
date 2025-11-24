package ch.epfl.cs107.icoop.actor;


import ch.epfl.cs107.icoop.handler.ICoopInteractionVisitor;
import ch.epfl.cs107.play.areagame.actor.AreaEntity;
import ch.epfl.cs107.play.areagame.actor.Interactor;
import ch.epfl.cs107.play.areagame.area.Area;
import ch.epfl.cs107.play.areagame.handler.AreaInteractionVisitor;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.math.Orientation;
import ch.epfl.cs107.play.signal.logic.Logic;
import java.util.ArrayList;
import java.util.List;

public class Door extends AreaEntity {

    private Logic signal;
    private DiscreteCoordinates[] coordinates;
    private String transit;
    private List<DiscreteCoordinates> otherCell;
    private Key keyFire;
    private Key keyWater;

    /**
     *
     * @param area
     * @param orientation
     * @param cellPrincipale
     * @param transit
     * @param signal
     * @param coordinates
     * @param otherCell
     * la porte depend d''un signal Logic qui dans le cas le plus courrant est TRUE, ca veut dire qu'on peut y acceder sans avoir besoin de clef
     */
    public Door (Area area, Orientation orientation, DiscreteCoordinates cellPrincipale, String transit, Logic signal, DiscreteCoordinates[] coordinates, List<DiscreteCoordinates> otherCell) {
        super(area, orientation, cellPrincipale);

        this.transit = transit;
        this.signal = signal;
        this.coordinates = coordinates;
        this.otherCell = otherCell;
    }

    /**
     * Cette deuxieme porte prend deux parametre en plus qui sont les clefs, c'est une porte accessible seulement lorsque les deux joueurs ont collecte leur clef
     * @param area
     * @param orientation
     * @param cellPrincipale
     * @param transit
     * @param keyFire
     * @param keyWater
     * @param coordinates
     */

    public Door(Area area, Orientation orientation, DiscreteCoordinates cellPrincipale, String transit, Key keyFire, Key keyWater, DiscreteCoordinates[] coordinates) {
        super(area, orientation, cellPrincipale);

        this.transit = transit;
        this.keyFire = keyFire;
        this.keyWater = keyWater;
        this.coordinates = coordinates;
        this.otherCell = cellPrincipale.getNeighbours();
    }


    public void update(float deltaTime){
        if (keyFire != null && keyWater != null) {
            signal = (keyFire.isCollected() && keyWater.isCollected()) ? Logic.TRUE : Logic.FALSE;
        }

        super.update(deltaTime);
    }

    /**
     * transiif est l'aire vers laquelle nos joueurs vont etre deplace
     * Lorsque l'on construit la porte dans une map, on doit alors a chaque fois lui indiquer vers quelle aire elle transit
     * @return
     */

    public String getTransit(){
        return transit;
    }

    public DiscreteCoordinates[] getCoordinates(){
        return coordinates;
    }

    @Override
    public List<DiscreteCoordinates> getCurrentCells() {
        List<DiscreteCoordinates> allCells = new ArrayList<>(otherCell);
        allCells.add(getCurrentMainCellCoordinates());
        return allCells;
    }

    @Override
    public boolean takeCellSpace(){
        return false;
    }

    @Override
    public boolean isCellInteractable(){
        return true;
    }

    @Override
    public boolean isViewInteractable(){
        return false;
    }

    @Override
    public void acceptInteraction(AreaInteractionVisitor v, boolean isCellInteraction) {
        if (signal != null && signal.isOn()) {
            ((ICoopInteractionVisitor) v).interactWith(this, isCellInteraction);
        }
    }

   // private class DoorInteractionHandler implements ICoopInteractionVisitor{
    //
    //        @Override
    //        public void interactWith(ICoopPlayer player, boolean isCellInteraction){
    //            if (player.getCurrentMainCellCoordinates().equals(new DiscreteCoordinates(6, 11))){
    //                if (signal.isOn()){
    //                    player.hasKey();
    //                }
    //            }
    //        }
    //    }
}
