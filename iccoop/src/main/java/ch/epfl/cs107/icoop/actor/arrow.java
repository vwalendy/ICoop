package ch.epfl.cs107.icoop.actor;


import ch.epfl.cs107.icoop.actor.*;
import ch.epfl.cs107.icoop.handler.ICoopInteractionVisitor;
import ch.epfl.cs107.icoop.handler.ICoopItem;
import ch.epfl.cs107.play.areagame.actor.Interactable;
import ch.epfl.cs107.play.areagame.actor.Interactor;
import ch.epfl.cs107.play.areagame.area.Area;
import ch.epfl.cs107.play.areagame.handler.AreaInteractionVisitor;
import ch.epfl.cs107.play.engine.actor.Animation;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.math.Orientation;
import ch.epfl.cs107.play.math.Vector;
import ch.epfl.cs107.play.window.Canvas;

import java.util.Collections;
import java.util.List;

public class arrow extends Projectile implements Interactor {

    private final Animation arrowAnimation;
    private final int MOVE_DURATION;
    private DiscreteCoordinates positionInit = getCurrentMainCellCoordinates();
    private final int MaxDist = 6;

    /**
     * extension
     * les fleches sont faites de 4 animations differentes en fonction de l'orientation courrante du joueur
     * @param area
     * @param orientation
     * @param position
     * @param moveDuration
     */
    public arrow(Area area, Orientation orientation, DiscreteCoordinates position, int moveDuration) {
        super(area, orientation, position, moveDuration);

        this.MOVE_DURATION = moveDuration;
        switch (orientation) {
            case UP:
                this.arrowAnimation = new Animation("icoop/arrowup", 1, 1, 1, this, 160, 160, 1, false);
                break;
            case DOWN:
                this.arrowAnimation = new Animation("icoop/arrowdown", 1, 1, 1, this, 240, 260, 1, false);
                break;
            case LEFT:
                this.arrowAnimation = new Animation("icoop/arrowleft", 1, 1, 1, this, 250, 240, 1, false);
                break;
            case RIGHT:
                this.arrowAnimation = new Animation("icoop/arrowright", 1, 1, 1, this, 260, 260, 1, false);
                break;
            default:
                this.arrowAnimation = new Animation("icoop/arrowup", 1, 1, 1, this, 160, 160, 1, false);
                break;
        }

    }
    /**
     * Mise à jour de l'état de l'entité à chaque cycle de jeu.
     *
     * @param deltaTime Temps écoulé depuis la dernière mise à jour, en secondes.
     *
     * Fonctionnement :
     * - Vérifie si l'entité peut continuer à avancer :
     *   - `canMoveAvant()`: Vérifie si la cellule devant l'entité est accessible.
     *   - Vérifie si la distance entre la position initiale et la position actuelle
     *     reste inférieure à une distance maximale autorisée (`MaxDist`).
     * - Si l'une des conditions n'est pas remplie, l'entité s'arrête en appelant `stop()`.
     * - Met à jour l'animation de la flèche (`arrowAnimation.update(deltaTime)`).
     * - Si les conditions sont remplies, l'entité tente de se déplacer en appelant `move(MOVE_DURATION)`.
     * - Appelle la méthode `update` de la classe parente pour gérer d'autres mises à jour éventuelles.
     */


    @Override
    public void update(float deltaTime) {
        if (!canMoveAvant() || !(DiscreteCoordinates.distanceBetween(positionInit, getCurrentMainCellCoordinates()) < MaxDist)) {
            stop();
        }

        arrowAnimation.update(deltaTime);

        move(MOVE_DURATION);
        super.update(deltaTime);

    }


    private boolean canMoveAvant() {
        DiscreteCoordinates prochainePos = getCurrentMainCellCoordinates().jump(getOrientation().toVector());
        return getOwnerArea().canEnterAreaCells(this, Collections.singletonList(prochainePos));
    }

    @Override
    public void draw(Canvas canvas) {
        arrowAnimation.draw(canvas);
    }

    @Override
    public void interactWith(Interactable other, boolean isCellInteraction) {
        other.acceptInteraction(new ArrowInteractionHandler(), isCellInteraction);
    }

    @Override
    public List<DiscreteCoordinates> getCurrentCells() {
        return List.of(getCurrentMainCellCoordinates());
    }

    @Override
    public boolean takeCellSpace() {
        return false;
    }

    @Override
    public boolean isCellInteractable() {
        return false;
    }

    @Override
    public boolean isViewInteractable() {
        return false;
    }

    @Override
    public void acceptInteraction(AreaInteractionVisitor v, boolean isCellInteraction) {
        ((ICoopInteractionVisitor) v).interactWith(this, isCellInteraction);
    }

    @Override
    public List<DiscreteCoordinates> getFieldOfViewCells() {
        return List.of();
    }

    @Override
    public boolean wantsCellInteraction() {
        return true;
    }

    @Override
    public boolean wantsViewInteraction() {
        return false;
    }

    private class ArrowInteractionHandler implements ICoopInteractionVisitor {



        @Override
        public void interactWith(darkLord lord, boolean isCellInteraction) {
            if (lord instanceof darkLord dark) {
                dark.damage(1, ElementalEntity.Element.FIRE, DamageType.damageType.PHYSIQUE);
            }
        }
     


    }
}
