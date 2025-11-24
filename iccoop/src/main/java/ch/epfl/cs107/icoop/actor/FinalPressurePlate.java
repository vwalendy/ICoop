package ch.epfl.cs107.icoop.actor;

import ch.epfl.cs107.icoop.area.maps.FinalBoss;
import ch.epfl.cs107.icoop.handler.DialogHandler;
import ch.epfl.cs107.icoop.handler.ICoopInteractionVisitor;
import ch.epfl.cs107.play.areagame.actor.AreaEntity;
import ch.epfl.cs107.play.areagame.actor.Interactable;
import ch.epfl.cs107.play.areagame.actor.Interactor;
import ch.epfl.cs107.play.areagame.area.Area;
import ch.epfl.cs107.play.areagame.handler.AreaInteractionVisitor;
import ch.epfl.cs107.play.engine.actor.Dialog;
import ch.epfl.cs107.play.engine.actor.RPGSprite;
import ch.epfl.cs107.play.engine.actor.Sprite;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.math.Orientation;
import ch.epfl.cs107.play.signal.logic.Logic;
import ch.epfl.cs107.play.window.Canvas;
import java.util.Collections;
import java.util.List;


public class FinalPressurePlate extends AreaEntity implements Interactor, Interactable, Logic {

    private boolean signal;
    private Sprite[] sprites;
    private DiscreteCoordinates position;
    private ICoopPlayer player;
    private PressurePlateInteractionHandler handler = new PressurePlateInteractionHandler();
    private boolean firstWin = true;
    /**
     *
     * @param area
     * @param orientation
     * @param position
     * permet de créer une plaque de pression
     */
    public FinalPressurePlate(Area area, Orientation orientation, DiscreteCoordinates position){
        super(area, orientation, position);

        this.position = position;
        this.sprites = RPGSprite.extractSprites("GroundPlateOff", 1, 1, 1, this, 256, 256);

    }

    public void winDialog(){
        if (firstWin) {
            ((FinalBoss) getOwnerArea()).publishWin(new Dialog("victory"));
            firstWin = false;
            DiscreteCoordinates[] coords = {new DiscreteCoordinates(14, 6), new DiscreteCoordinates(13, 6)};
            List<DiscreteCoordinates> otherCell = List.of(getCurrentMainCellCoordinates());
            getOwnerArea().registerActor(new Door(getOwnerArea(), Orientation.DOWN, getCurrentMainCellCoordinates(), "Spawn", Logic.TRUE, coords, otherCell));
        }
    }


    /**
     *
     * @param deltaTime elapsed time since last update, in seconds, non-negative
     * permet une fois que le joueur ne marche plus sur la palque de pression de remettre l'objet qui a été modifié dans son état initiale
     */
    public void update(float deltaTime){
        if (signal && !(player.getCurrentMainCellCoordinates().equals(position))){
            signal = false;
            player = null;
        }

        super.update(deltaTime);
    }


    /**
     *
     * @param canvas target, not null
     * permet de dessiner le sprite d'une plaque de pression
     */
    public void draw (Canvas canvas){
        sprites[0].draw(canvas);
    }

    /**
     *
     * @return
     * redefinition de methode
     */
    @Override
    public  List<DiscreteCoordinates> getCurrentCells(){
        return Collections.singletonList(getCurrentMainCellCoordinates());
    }

    /**
     *
     * @return
     * redefinition de methode
     */
    @Override
    public boolean takeCellSpace(){
        return false;
    }

    /**
     *
     * @return
     * redefinition de methode
     */
    @Override
    public boolean isCellInteractable(){
        return true;
    }

    /**
     *
     * @return
     * redefinition de methode
     */
    @Override
    public boolean isViewInteractable(){
        return false;
    }

    /**
     *
     * @return
     * redefinition de methode
     */
    @Override
    public void acceptInteraction(AreaInteractionVisitor v, boolean isCellInteraction){
        ((ICoopInteractionVisitor) v).interactWith(this, isCellInteraction);
    }

    /**
     *
     * @return
     * redefinition de methode de Logic
     */
    @Override
    public boolean isOn(){
        return signal;
    }


    /**
     *
     * @return
     * redefinition de methode de Logic
     */
    @Override
    public boolean isOff(){
        return !signal;
    }

    /**
     *
     * @return
     * redefinition de methode
     */
    @Override
    public List<DiscreteCoordinates> getFieldOfViewCells(){
        return List.of(getCurrentMainCellCoordinates());
    }


    /**
     *
     * @return
     * redefinition de methode
     */
    @Override
    public boolean wantsCellInteraction(){
        return true;
    }

    /**
     *
     * @return
     * redefinition de methode
     */
    @Override
    public boolean wantsViewInteraction(){
        return false;
    }

    /**
     *
     * @return
     * redefinition de methode
     */
    @Override
    public void interactWith(Interactable other, boolean isCellInteraction){
        other.acceptInteraction(handler, isCellInteraction);
    }



    /**
     * class imbriquée qui permet de gérer les interactions avec les plaques de pressions
     */
    private class PressurePlateInteractionHandler implements ICoopInteractionVisitor{

        /**
         *
         * @param iCoopPlayer
         * @param isCellInteraction
         * permet lorsque le joueur marche sur la plaque de pression d'activer l'objet relié a la plaque
         */
        @Override
        public void interactWith(ICoopPlayer iCoopPlayer, boolean isCellInteraction){
            if(isCellInteraction){
                signal = true;
                player = iCoopPlayer;
                winDialog();
            }
        }
    }
}
