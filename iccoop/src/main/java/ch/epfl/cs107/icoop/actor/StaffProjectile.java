package ch.epfl.cs107.icoop.actor;

import ch.epfl.cs107.icoop.area.maps.FinalBoss;
import ch.epfl.cs107.icoop.handler.ICoopInteractionVisitor;
import ch.epfl.cs107.play.areagame.actor.Interactable;
import ch.epfl.cs107.play.areagame.actor.Interactor;
import ch.epfl.cs107.play.areagame.area.Area;
import ch.epfl.cs107.play.areagame.handler.AreaInteractionVisitor;
import ch.epfl.cs107.play.engine.actor.Animation;
import ch.epfl.cs107.play.engine.actor.Dialog;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.math.Orientation;
import ch.epfl.cs107.play.window.Canvas;
import java.util.Collections;
import java.util.List;


public class StaffProjectile extends Projectile implements Interactor, ElementalEntity {

    private final int ANIMATION_DURATION = 12;
    private final int MaxLength = 6;

    private Animation animationWaterStaff;
    private Animation animationFireStaff;
    private int speed;
    private StaffProjectilleInteractionHandler handler = new StaffProjectilleInteractionHandler();
    private Element element;
    private final int MOVE_DURATION;
    private DiscreteCoordinates positionInit = getCurrentMainCellCoordinates();
    private boolean firstWin = true;


    /**
     * @param area
     * @param orientation
     * @param position
     * @param element
     * @param MOVE_DURATION
     * @param speed         permet la création d'une boule de feu ou d'eau qui sort de la baguette
     */
    public StaffProjectile(Area area, Orientation orientation, DiscreteCoordinates position, Element element, int MOVE_DURATION, int speed) {
        super(area, orientation, position, MOVE_DURATION);

        this.element = element;
        this.speed = speed;
        this.MOVE_DURATION = MOVE_DURATION;
        this.animationWaterStaff = new Animation("icoop/magicFireProjectile", 4, 1, 1, this, 32, 32, ANIMATION_DURATION / 4, true);
        this.animationFireStaff = new Animation("icoop/magicWaterProjectile", 4, 1, 1, this, 32, 32, ANIMATION_DURATION / 4, true);
    }

    /**
     * @param deltaTime elapsed time since last update, in seconds, non-negative
     *                  permet de géré en fonction de deltaTime :
     *                  1. l'arret du projectille si il heurte certaine choses prédéfini
     *                  2. de lancer les projectiles
     *                  3. gérer l'animation en fonction de l'élément du joueur
     */
    @Override
    public void update(float deltaTime) {
        if (!canMoveAvant() || !(DiscreteCoordinates.distanceBetween(positionInit, getCurrentMainCellCoordinates()) < MaxLength)) {
            stop();
        }

        projectileThrow(animationFireStaff, speed);
        projectileThrow(animationWaterStaff, speed);

        if (element.equals(Element.FIRE)) {
            animationFireStaff.update(deltaTime);
        } else {
            animationWaterStaff.update(deltaTime);
        }

        super.update(deltaTime);
    }

    /**
     * @return gère la position du projectile en fonction de la case devant lui
     */
    private boolean canMoveAvant() {
        DiscreteCoordinates prochainePos = getCurrentMainCellCoordinates().jump(getOrientation().toVector());
        return getOwnerArea().canEnterAreaCells(this, Collections.singletonList(prochainePos));
    }

    /**
     * @param other             (Interactable). Not null
     * @param isCellInteraction True if this is a cell interaction
     *                          gère les interactions du projectile
     */
    @Override
    public void interactWith(Interactable other, boolean isCellInteraction) {
        other.acceptInteraction(handler, isCellInteraction);
    }

    /**
     * @return redefinition de methode
     */
    @Override
    public List<DiscreteCoordinates> getCurrentCells() {
        return List.of(getCurrentMainCellCoordinates());
    }

    /**
     * @return redefinition de methode
     */
    @Override
    public boolean takeCellSpace() {
        return false;
    }

    /**
     * @return redefinition de methode
     */
    @Override
    public boolean isCellInteractable() {
        return false;
    }

    /**
     * @return redefinition de methode
     */
    @Override
    public boolean isViewInteractable() {
        return false;
    }

    /**
     * @return redefinition de methode
     */
    @Override
    public void acceptInteraction(AreaInteractionVisitor v, boolean isCellInteraction) {
        ((ICoopInteractionVisitor) v).interactWith(this, isCellInteraction);
    }

    /**
     * @return redefinition de methode
     */
    @Override
    public List<DiscreteCoordinates> getFieldOfViewCells() {
        return List.of(getCurrentMainCellCoordinates());
    }

    /**
     * @return redefinition de methode
     */
    @Override
    public boolean wantsCellInteraction() {
        return true;
    }

    /**
     * @return redefinition de methode
     */
    @Override
    public boolean wantsViewInteraction() {
        return false;
    }

    /**
     * @param canvas target, not null
     *               permet de dessiner les différentes animations du projectiles
     */
    @Override
    public void draw(Canvas canvas) {
        if (element.equals(Element.FIRE)) {
            animationFireStaff.draw(canvas);
        } else {
            animationWaterStaff.draw(canvas);
        }
    }

    /**
     * @return redefinition de methode
     */
    @Override
    public Element getElement() {
        return this.element;
    }

    /**
     * class imbriquée qui gère les interactions du projectile
     */
    private class StaffProjectilleInteractionHandler implements ICoopInteractionVisitor {

        /**
         * @param explosif
         * @param isCellInteraction permet au projectile lorsqu'il heurte une bombe de l'activée
         */
        @Override
        public void interactWith(Explosif explosif, boolean isCellInteraction) {
            if (isCellInteraction) {
                explosif.isActivated();
                stop();
            }
        }

        /**
         * @param rock
         * @param isCellInteraction permet de casser seulement certains rochers mais pas tous
         */
        @Override
        public void interactWith(Rock rock, boolean isCellInteraction) {
            if (rock.getName().equals("rock.1")) {
                getOwnerArea().unregisterActor(rock);
                rock.isDestroyed(true);
            } else if (rock.getName().equals("rock.2")) {
                stop();
                return;
            }
        }


    }

    /**
     * @param bomb
     * @param isCellInteraction permet de mettre des dégats a bombFoe
     */

    public void interactWith(BombFoe bomb, boolean isCellInteraction) {
        if (bomb instanceof BombFoe bombFoe) {
            bombFoe.damage(1, ElementalEntity.Element.FIRE, DamageType.damageType.PHYSIQUE);
        }
    }
}


