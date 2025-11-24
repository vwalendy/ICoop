package ch.epfl.cs107.icoop.area.maps;

import ch.epfl.cs107.icoop.actor.*;
import ch.epfl.cs107.icoop.area.ICoopArea;
import ch.epfl.cs107.icoop.handler.DialogHandler;
import ch.epfl.cs107.play.engine.actor.Background;
import ch.epfl.cs107.play.engine.actor.Foreground;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.math.Orientation;
import ch.epfl.cs107.play.signal.logic.Logic;
import ch.epfl.cs107.play.signal.logic.Not;

import java.util.List;

import static ch.epfl.cs107.icoop.actor.ElementalEntity.Element.FIRE;
import static ch.epfl.cs107.icoop.actor.ElementalEntity.Element.WATER;
import static ch.epfl.cs107.play.io.ResourcePath.getBehavior;


public class FinalBoss extends ICoopArea {


    private final DialogHandler dialogHandler;
    List<DiscreteCoordinates> spawnPosition = List.of(new DiscreteCoordinates(10, 10), new DiscreteCoordinates(10, 10));


    public FinalBoss(DialogHandler dialogHandler) {
        super(dialogHandler);
        this.dialogHandler = dialogHandler;
    }

    public String getTitle() {
        return "FinalBoss";
    }

    @Override
    public List<DiscreteCoordinates> getPlayerSpawnPosition() {
        return spawnPosition;
    }


    /**
     * ici je register tout les acteurs presents sur la map avec leurs coordonnes et paramtres propres
     */
    protected void createArea() {
        registerActor(new Background(this));

        registerActor(new darkLord(this, Orientation.DOWN, new DiscreteCoordinates(10, 10)));

        registerActor(new Heart(this, Orientation.DOWN, new DiscreteCoordinates(3, 3)));
        registerActor(new Heart(this, Orientation.DOWN, new DiscreteCoordinates(3, 16)));
        registerActor(new Heart(this, Orientation.DOWN, new DiscreteCoordinates(16, 16)));
        registerActor(new Heart(this, Orientation.DOWN, new DiscreteCoordinates(16, 3)));

        registerActor(new HellSkull(this, Orientation.UP, new DiscreteCoordinates(10, 2)));
        registerActor(new HellSkull(this, Orientation.RIGHT, new DiscreteCoordinates(2, 10)));
        registerActor(new HellSkull(this, Orientation.DOWN, new DiscreteCoordinates(10, 17)));
        registerActor(new HellSkull(this, Orientation.LEFT, new DiscreteCoordinates(17, 10)));

        registerActor(new Bow(this,Orientation.DOWN,new DiscreteCoordinates(9,12),"icoop/bow.icon"));
        registerActor(new Bow(this,Orientation.DOWN,new DiscreteCoordinates(9,11),"icoop/bow.icon"));

    }

}