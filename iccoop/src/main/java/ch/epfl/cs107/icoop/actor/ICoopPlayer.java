package ch.epfl.cs107.icoop.actor;

import ch.epfl.cs107.icoop.KeyBindings;
import ch.epfl.cs107.icoop.handler.ICoopInteractionVisitor;
import ch.epfl.cs107.play.areagame.actor.Interactor;
import ch.epfl.cs107.play.areagame.actor.MovableAreaEntity;
import ch.epfl.cs107.play.areagame.area.Area;
import ch.epfl.cs107.play.areagame.handler.AreaInteractionVisitor;
import ch.epfl.cs107.play.engine.actor.*;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.math.Orientation;
import ch.epfl.cs107.play.math.Vector;
import ch.epfl.cs107.play.window.Button;
import ch.epfl.cs107.play.window.Keyboard;
import ch.epfl.cs107.play.areagame.actor.Interactable;

import java.awt.*;
import java.util.Collections;
import java.util.List;

import static ch.epfl.cs107.play.math.Orientation.*;

/**
 * A ICoopPlayer is a player for the ICoop game.
 */
public class ICoopPlayer extends MovableAreaEntity implements ElementalEntity, Interactor {


    private final static int MOVE_DURATION = 8;
    private float hp;
    private Element element;
    final Vector anchor = new Vector(0, 0);
    final Orientation[] orders = {DOWN, RIGHT, UP, LEFT};
    private final static int ANIMATION_DURATION = 4;
    private OrientedAnimation animation;
    private final KeyBindings.PlayerKeyBindings key;


    /**
     * @param owner       (Area) area to which the player belong
     * @param orientation (Orientation) the initial orientation of the player
     * @param coordinates (DiscreteCoordinates) the initial position in the grid
     * @param spriteName  (String) name of the sprite used as graphical representation
     */
    public ICoopPlayer(Area owner, Orientation orientation, DiscreteCoordinates coordinates, String spriteName, Element element, KeyBindings.PlayerKeyBindings key) {
        super(owner, orientation, coordinates);
        this.key = key;
        this.hp = 10;
        this.element = element;
        resetMotion();
        animation = new OrientedAnimation(spriteName, ANIMATION_DURATION, this, anchor, orders, 4, 1, 2, 16, 32,
                true);

    }

    @Override
    public Element element() {
        return element;
    }

    /**
     * @param deltaTime elapsed time since last update, in seconds, non-negative
     */
    @Override
    public void update(float deltaTime) {
        Keyboard keyboard = getOwnerArea().getKeyboard();
        moveIfPressed(Orientation.LEFT, keyboard.get(key.left()));
        moveIfPressed(UP, keyboard.get(key.up()));
        moveIfPressed(RIGHT, keyboard.get(key.right()));
        moveIfPressed(DOWN, keyboard.get(key.down()));
        super.update(deltaTime);
    }

    /**
     * @param canvas target, not null
     */
    @Override
    public void draw(ch.epfl.cs107.play.window.Canvas canvas) {
        animation.draw(canvas);
    }

    @Override
    public boolean takeCellSpace() {
        return true;
    }

    @Override
    public boolean isCellInteractable() {
        return true;
    }

    @Override
    public boolean isViewInteractable() {
        return true;
    }

    @Override
    public List<DiscreteCoordinates> getCurrentCells() {
        return Collections.singletonList(getCurrentMainCellCoordinates());
    }

    @Override
    public List<DiscreteCoordinates> getFieldOfViewCells() {
        return Collections.singletonList(getCurrentMainCellCoordinates().jump(getOrientation().toVector()));
    }

    @Override
    public boolean wantsCellInteraction() {
        return true;
    }

    @Override
    public void acceptInteraction(AreaInteractionVisitor v, boolean isCellInteraction) {
        if (v instanceof ICoopInteractionVisitor) {
            ((ICoopInteractionVisitor) v).interactWith(this);
        }
    }

    @Override
    public boolean wantsViewInteraction() {
        Keyboard keyboard = getOwnerArea().getKeyboard();
        return keyboard.get(key.useItem()).isDown();
    }

    /**
     * Orientate and Move this player in the given orientation if the given button is down
     *
     * @param orientation (Orientation): given orientation, not null
     * @param b           (Button): button corresponding to the given orientation, not null
     */
    private void moveIfPressed(Orientation orientation, Button b) {
        if (b.isDown()) {
            if (!isDisplacementOccurs()) {
                animation.reset();
                animation.orientate(orientation);
                orientate(orientation);
                move(MOVE_DURATION);
            } else {
                animation.update(MOVE_DURATION);
            }
        }
    }

    /**
     * Leave an area by unregister this player
     */
    public void leaveArea() {
        getOwnerArea().unregisterActor(this);
    }

    /**
     * makes the player entering a given area
     *
     * @param area     (Area):  the area to be entered, not null
     * @param position (DiscreteCoordinates): initial position in the entered area, not null
     */
    public void enterArea(Area area, DiscreteCoordinates position) {
        area.registerActor(this);
        area.setViewCandidate(this);
        setOwnerArea(area);
        setCurrentPosition(position.toVector());
        resetMotion();
    }

    /**
     * @return true if the hp level is <= 0
     */
    public boolean isWeak() {
        return (hp <= 0.f);
    }


    /**
     * heals the player
     */
    public void strengthen() {
        hp = 10;
    }

    //public class ICoopPlayerInteractionHandeler implements ICoopInteractionVisitor {

        @Override
        public void interactWith(Interactable other, boolean isCellInteraction) {
            if (other instanceof Door door) {
                if (door.isCellInteractable() && door.getSignal().isOn()) {
                    leaveArea();
                    enterArea(door.getDestinationArea(), door.getArrivalCoords());
                }
            }
        }
    }