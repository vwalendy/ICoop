package ch.epfl.cs107.icoop.actor;

import ch.epfl.cs107.icoop.handler.ICoopInteractionVisitor;
import ch.epfl.cs107.play.areagame.actor.Interactable;
import ch.epfl.cs107.play.areagame.actor.Interactor;
import ch.epfl.cs107.play.areagame.actor.MovableAreaEntity;
import ch.epfl.cs107.play.areagame.area.Area;
import ch.epfl.cs107.play.areagame.handler.AreaInteractionVisitor;
import ch.epfl.cs107.play.engine.actor.OrientedAnimation;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.math.Orientation;
import ch.epfl.cs107.play.math.Transform;
import ch.epfl.cs107.play.math.Vector;
import ch.epfl.cs107.play.math.random.RandomGenerator;
import ch.epfl.cs107.play.window.Canvas;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static ch.epfl.cs107.play.math.Orientation.*;
import static ch.epfl.cs107.play.math.Orientation.RIGHT;

public class logMonster extends Foe implements Interactor, Interactable {
    private static final int ANIMATION_DURATION = 24;
    private static final double ORIENTATION_CHANGE_PROBABILITY = 0.4;
    private int speedFactor = 1;
    private OrientedAnimation animation;
    private final Vector anchor = new Vector(-0.5f, 0);
    private final Orientation[] orders = {DOWN, UP, RIGHT, LEFT};
    private int idleSteps=10;
    private final int MAX_LIFE = 50;



    public logMonster (Area area, Orientation orientation, DiscreteCoordinates position) {
        super(area, orientation, position);
        animation = new OrientedAnimation("icoop/logMonster", ANIMATION_DURATION / 3, this, anchor, orders, 4, 2, 2, 32, 32, true);
        this.setAnimation(animation);
        this.setBarreDeVie(new Health(this, Transform.I.translated(0, 1.75f), MAX_LIFE, false));



    }



    @Override
    public void interactWith(Interactable other, boolean isCellInteractable) {

        // other.acceptInteraction(handler, isCellInteractable);
    }

    @Override
    public List<DiscreteCoordinates> getFieldOfViewCells() {

        List<DiscreteCoordinates> fieldOfViewCells = new ArrayList<>();
        DiscreteCoordinates coordinates = getCurrentCells().getFirst();
        for (int i = 0; i < 32; i++) {
            coordinates = coordinates.jump(getOrientation().toVector());
            fieldOfViewCells.add(coordinates);
        }
        return fieldOfViewCells;
    }
    private void randomMove() {
        if (RandomGenerator.getInstance().nextDouble() <= ORIENTATION_CHANGE_PROBABILITY) {
            List<Orientation> newOrientations = Arrays.stream(Orientation.values()).filter(orientation -> orientation != getOrientation()).toList();
            Orientation newOrientation = newOrientations.get(RandomGenerator.getInstance().nextInt(3));
            orientate(newOrientation);
        }
        move(ANIMATION_DURATION / speedFactor);
        if (move(ANIMATION_DURATION / speedFactor)) {
            System.out.println("Move successful");
        } else {
            System.out.println("Move failed");
        }
    }


    @Override
    public boolean wantsCellInteraction() {
        return true;
    }


    @Override
    public boolean wantsViewInteraction() {
        return true;
    }

    @Override
    public List<DiscreteCoordinates> getCurrentCells() {
        return List.of(new DiscreteCoordinates(Math.round(getPosition().x), Math.round(getPosition().y)));
    }

    @Override
    public void acceptInteraction(AreaInteractionVisitor v, boolean isCellInteraction) {
        ((ICoopInteractionVisitor) v).interactWith(this, isCellInteraction);
    }

    @Override
    public boolean isViewInteractable() {
        return true;
    }

    @Override
    public boolean isCellInteractable() {
        return true;
    }

    @Override
    public boolean takeCellSpace() {
        return true;
    }
    @Override
    public void draw(Canvas canvas) {
        animation.draw(canvas);

    }
    @Override
    public void update(float deltaTime) {

        if (idleSteps <= 0) {
            randomMove();

            idleSteps = 10;

        } else {
            idleSteps--;
        }
        animation.update(deltaTime);
        super.update(deltaTime);
    }
}

