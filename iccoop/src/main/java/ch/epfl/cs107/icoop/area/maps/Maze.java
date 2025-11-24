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

public class Maze extends ICoopArea {


    private final DialogHandler dialogHandler;
    private List<DiscreteCoordinates> otherCell;


    public Maze(DialogHandler dialogHandler){
        super(dialogHandler);
        this.dialogHandler = dialogHandler;
    }

    List<DiscreteCoordinates> spawnPosition = List.of(
            new DiscreteCoordinates(2, 39),
            new DiscreteCoordinates(3, 39));


    @Override
    public String getTitle() {
        return "Maze";
    }

    /**
     * ici je register tout les acteurs presents sur la map avec leurs coordonnes et paramtres propres
     */

    protected void createArea() {
        registerActor(new Background(this));
        registerActor(new Foreground(this));

        DiscreteCoordinates[] coordsArena = {new DiscreteCoordinates(14,15),new DiscreteCoordinates(4,5)};
        otherCell = List.of(new DiscreteCoordinates(19, 7));
        registerActor(new Door(this, Orientation.DOWN, new DiscreteCoordinates(19, 6),"Arena", Logic.TRUE, coordsArena, otherCell));

        registerActor(new Explosif(this, Orientation.DOWN, new DiscreteCoordinates(6, 25), 4f));

        registerActor(new Heart(this, Orientation.DOWN, new DiscreteCoordinates(15, 18)));
        registerActor(new Heart(this, Orientation.DOWN, new DiscreteCoordinates(16, 19)));
        registerActor(new Heart(this, Orientation.DOWN, new DiscreteCoordinates(14, 19)));
        registerActor(new Heart(this, Orientation.DOWN, new DiscreteCoordinates(14, 17)));

        PressurePlate plateFire = new PressurePlate(this, Orientation.DOWN, new DiscreteCoordinates(6, 33));
        registerActor(plateFire);
        PressurePlate plateWater = new PressurePlate(this, Orientation.DOWN, new DiscreteCoordinates(8, 23));
        registerActor(plateWater);

        for(int i = 0; i < 2; i++) {
            registerActor(new ElementalWall(this, Orientation.LEFT, new DiscreteCoordinates(4, 35 + i), "water_wall", Logic.TRUE, WATER));
            registerActor(new ElementalWall(this, Orientation.LEFT, new DiscreteCoordinates(6, 35 + i), "fire_wall", new Not(plateFire),  FIRE));
        }

        for(int i = 0; i < 2; i++) {
            registerActor(new ElementalWall(this, Orientation.DOWN, new DiscreteCoordinates(2+i, 34), "fire_wall", Logic.TRUE, FIRE));
            registerActor(new ElementalWall(this, Orientation.DOWN, new DiscreteCoordinates(5+i, 24), "water_wall", new Not(plateWater), WATER));
        }

        registerActor(new ElementalWall(this, Orientation.DOWN, new DiscreteCoordinates(8, 4 ), "water_wall", Logic.TRUE, WATER));
        registerActor(new ElementalWall(this, Orientation.DOWN, new DiscreteCoordinates(13, 4 ), "fire_wall", Logic.TRUE, FIRE));
        registerActor(new ElementalWall(this, Orientation.DOWN, new DiscreteCoordinates(8, 21 ), "fire_wall", Logic.TRUE, FIRE));

        registerActor(new HellSkull(this , Orientation.RIGHT, new DiscreteCoordinates(12,33)));
        registerActor(new HellSkull(this , Orientation.RIGHT, new DiscreteCoordinates(12,31)));
        registerActor(new HellSkull(this , Orientation.RIGHT, new DiscreteCoordinates(12,29)));
        registerActor(new HellSkull(this , Orientation.RIGHT, new DiscreteCoordinates(12,27)));
        registerActor(new HellSkull(this , Orientation.RIGHT, new DiscreteCoordinates(12,25)));
        registerActor(new HellSkull(this , Orientation.RIGHT, new DiscreteCoordinates(10,33)));
        registerActor(new HellSkull(this , Orientation.RIGHT, new DiscreteCoordinates(10,32)));
        registerActor(new HellSkull(this , Orientation.RIGHT, new DiscreteCoordinates(10,30)));
        registerActor(new HellSkull(this , Orientation.RIGHT, new DiscreteCoordinates(10,28)));
        registerActor(new HellSkull(this , Orientation.RIGHT, new DiscreteCoordinates(10,26)));

        registerActor(new Staff(this, Orientation.DOWN, new DiscreteCoordinates(8, 2), ElementalEntity.Element.WATER));
        registerActor(new Staff(this, Orientation.DOWN, new DiscreteCoordinates(13, 2), ElementalEntity.Element.FIRE));

        registerActor(new BombFoe(this, Orientation.DOWN, new DiscreteCoordinates(5, 15)));
        registerActor(new BombFoe(this, Orientation.DOWN, new DiscreteCoordinates(6, 17)));
        registerActor(new BombFoe(this, Orientation.DOWN, new DiscreteCoordinates(10, 17)));
        registerActor(new BombFoe(this, Orientation.DOWN, new DiscreteCoordinates(5, 14)));

    }

    public List<DiscreteCoordinates> getPlayerSpawnPosition() {
        return spawnPosition;
    }
}
