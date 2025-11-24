package ch.epfl.cs107.icoop.actor;

import ch.epfl.cs107.icoop.handler.ICoopInteractionVisitor;
import ch.epfl.cs107.play.areagame.actor.AreaEntity;
import ch.epfl.cs107.play.areagame.actor.Interactable;
import ch.epfl.cs107.play.areagame.actor.Interactor;
import ch.epfl.cs107.play.areagame.area.Area;
import ch.epfl.cs107.play.areagame.handler.AreaInteractionVisitor;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.math.Orientation;
import ch.epfl.cs107.play.signal.logic.Logic;

import java.util.Collections;
import java.util.List;

public class Sword extends AreaEntity implements Interactor {

    private final SwordInteractionHandler handler;

    /**
     *
     * @param area
     * @param orientation
     * @param position
     * permet de créer une épée
     */
    public Sword(Area area, Orientation orientation, DiscreteCoordinates position) {
        super(area, orientation, position);
        this.handler = new SwordInteractionHandler();
    }

    /**
     *
     * @return
     * redefinition de methode
     */
    @Override
    public List<DiscreteCoordinates> getCurrentCells() {
        return Collections.singletonList(getCurrentMainCellCoordinates());
    }

    /**
     *
     * @return
     * redefinition de methode
     */
    @Override
    public boolean takeCellSpace() {
        return false;
    }

    /**
     *
     * @return
     * redefinition de methode
     */
    @Override
    public boolean isCellInteractable() {
        return true;
    }

    /**
     *
     * @return
     * redefinition de methode
     */
    @Override
    public boolean isViewInteractable() {
        return false;
    }

    /**
     *
     * @return
     * redefinition de methode
     */
    @Override
    public List<DiscreteCoordinates> getFieldOfViewCells() {
        return getCurrentMainCellCoordinates().getNeighbours();
    }

    /**
     *
     * @return
     * redefinition de methode
     */
    @Override
    public boolean wantsCellInteraction() {
        return true;
    }

    /**
     *
     * @return
     * redefinition de methode
     */
    @Override
    public boolean wantsViewInteraction() {
        return false;
    }

    /**
     *
     * @return
     * gère les interaction de l'épée
     */
    @Override
    public void interactWith(Interactable other, boolean isCellInteraction) {
        other.acceptInteraction(handler, isCellInteraction);
    }

    /**
     *
     * @return
     * redefinition de methode
     */
    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);
    }

    /**
     *
     * @return
     * redefinition de methode
     */
    @Override
    public void acceptInteraction(AreaInteractionVisitor v, boolean isCellInteraction) {
        ((ICoopInteractionVisitor) v).interactWith(this, isCellInteraction);
    }

    /**
     * class imbriquée qui gère les interactions de l'épée avec d'autres acteurs
     */
    private class SwordInteractionHandler implements ICoopInteractionVisitor {

        /**
         *
         * @param explosif
         * @param isCellInteraction
         * interaction qui fait que lorsque on tape une bombe avec l'épée elle s'active
         */
        @Override
        public void interactWith(Explosif explosif, boolean isCellInteraction) {
            if (!isCellInteraction) {
                explosif.setSignal(Logic.TRUE);
            }
        }

        /**
         *
         * @param foe
         * @param isCellInteraction
         * permet a l'épée de faire des dégats a l'ennemi tete de mort
         */
        @Override
        public void interactWith(Foe foe, boolean isCellInteraction){
            if (foe instanceof HellSkull hellSkul) {
                hellSkul.damage(1, ElementalEntity.Element.FIRE, DamageType.damageType.PHYSIQUE);
            }
        }

        /**
         *
         * @param bomb
         * @param isCellInteraction
         * permet a l'épée de mettre des dégats au ennemi bombFoe
         */
        @Override
        public void interactWith(BombFoe bomb, boolean isCellInteraction) {
            if (bomb instanceof BombFoe bombFoe) {
                bombFoe.damage(1, ElementalEntity.Element.FIRE, DamageType.damageType.PHYSIQUE);
            }
        }
    }
}
