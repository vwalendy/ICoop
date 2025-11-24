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


public class OrbWay extends ICoopArea {


    private final DialogHandler dialogHandler;
    List<DiscreteCoordinates> doorOtherCell1;
    List<DiscreteCoordinates> doorOtherCell2;
    List<DiscreteCoordinates> spawnPosition = List.of(new DiscreteCoordinates(1, 12), new DiscreteCoordinates(1, 8));
    private Orb fireOrb;
    private Orb waterOrb;


    public OrbWay(DialogHandler dialogHandler) {
        super(dialogHandler);
        this.dialogHandler = dialogHandler;
    }

    public String getTitle() {
        return "OrbWay";
    }

    @Override
    public List<DiscreteCoordinates> getPlayerSpawnPosition() {
        return  spawnPosition;
    }


    /**
     * ici je register tout les acteurs presents sur la map avec leurs coordonnes et paramtres propres
     */
    protected void createArea() {
        doorOtherCell1 = List.of(
                new DiscreteCoordinates(0, 13),
                new DiscreteCoordinates(0, 12),
                new DiscreteCoordinates(0, 11),
                new DiscreteCoordinates(0, 10)
                );
        doorOtherCell2 = List.of(
                new DiscreteCoordinates(0, 7),
                new DiscreteCoordinates(0, 6),
                new DiscreteCoordinates(0, 5),
                new DiscreteCoordinates(0, 4)
        );

        registerActor(new Background(this));
        registerActor(new Foreground(this));

        DiscreteCoordinates [] coords = {new DiscreteCoordinates(18, 16), new DiscreteCoordinates(18, 15)};

        registerActor(new Door(this, Orientation.DOWN, new DiscreteCoordinates(0, 8), "Spawn", Logic.TRUE, coords,doorOtherCell1));
        registerActor(new Door(this, Orientation.DOWN, new DiscreteCoordinates(0, 14), "Spawn", Logic.TRUE, coords,doorOtherCell2));

        registerActor(fireOrb = new Orb(this, Orientation.UP, new DiscreteCoordinates(17, 12), FIRE));
        registerActor(waterOrb = new Orb(this, Orientation.UP, new DiscreteCoordinates(17, 6), WATER));

        PressurePlate plateFire = new PressurePlate(this, Orientation.DOWN, new DiscreteCoordinates(5, 7));
        registerActor(plateFire);
        PressurePlate plateWater = new PressurePlate(this, Orientation.DOWN, new DiscreteCoordinates(5, 10));
        registerActor(plateWater);

        registerActor(new ElementalWall(this, Orientation.LEFT, new DiscreteCoordinates(7, 6), "fire_wall", Logic.TRUE, FIRE));
        registerActor(new ElementalWall(this, Orientation.LEFT, new DiscreteCoordinates(7, 12), "water_wall",Logic.TRUE, WATER));

        for(int i = 0; i <= 4; i++) {
            registerActor(new ElementalWall(this, Orientation.LEFT, new DiscreteCoordinates(12, 4 + i), "fire_wall", new Not(plateWater), WATER));
            registerActor(new ElementalWall(this, Orientation.LEFT, new DiscreteCoordinates(12, 10 + i), "fire_wall", new Not(plateFire),  FIRE));
        }

        registerActor(new Heart(this, Orientation.DOWN, new DiscreteCoordinates(8, 4)));
        registerActor(new Heart(this, Orientation.DOWN, new DiscreteCoordinates(10, 6)));
        registerActor(new Heart(this, Orientation.DOWN, new DiscreteCoordinates(5, 13)));
        registerActor(new Heart(this, Orientation.DOWN, new DiscreteCoordinates(10, 11)));
    }
}