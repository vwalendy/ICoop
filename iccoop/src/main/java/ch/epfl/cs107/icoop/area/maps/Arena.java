package ch.epfl.cs107.icoop.area.maps;

import ch.epfl.cs107.icoop.actor.*;
import ch.epfl.cs107.icoop.area.ICoopArea;
import ch.epfl.cs107.icoop.handler.DialogHandler;
import ch.epfl.cs107.play.areagame.area.AreaBehavior;
import ch.epfl.cs107.play.engine.actor.Background;
import ch.epfl.cs107.play.engine.actor.Foreground;
import ch.epfl.cs107.play.io.FileSystem;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.math.Orientation;
import ch.epfl.cs107.play.window.Window;

import java.util.List;

public class Arena extends ICoopArea {

    private DialogHandler dialogHandler;
    List<DiscreteCoordinates> spawnPosition = List.of(
            new DiscreteCoordinates(4, 5),
            new DiscreteCoordinates(14, 15));
    private Rock rock;


    public Arena(DialogHandler dialogHandler) {
        super(dialogHandler);
        this.dialogHandler = dialogHandler;
    }


    @Override
    public String getTitle() {
        return "Arena";
    }

    @Override
    public List<DiscreteCoordinates> getPlayerSpawnPosition() {
        return spawnPosition;
    }

    @Override
    public boolean begin(Window window, FileSystem fileSystem) {
        return super.begin(window, fileSystem);
    }


    @Override
    public void createArea() {
        registerActor(new Background(this));
        registerActor(new Foreground(this));

        this.rock = new Rock(this, Orientation.DOWN, new DiscreteCoordinates(10, 10),"rock.1");
        registerActor(this.rock);

        for (int i=0;i< this.getWidth()-1;i++) {
            for (int j=0; j < this.getHeight()-1; j++) {
                if (i == 10 && j == 10) {
                    this.rock = new Rock(this, Orientation.DOWN, new DiscreteCoordinates(i, j), "rock.1");
                    registerActor(this.rock);
                    continue;
                }
                    registerActor(new Rock(this, Orientation.DOWN, new DiscreteCoordinates(i, j), "rock.1"));
                    registerActor(new Obstacle(this, Orientation.DOWN, new DiscreteCoordinates(i, j),"rock.2"));
                }
            }

        Key keyWater = new Key(this, Orientation.DOWN, new DiscreteCoordinates(9, 16), ElementalEntity.Element.WATER);
        registerActor(keyWater);
        Key keyFire = new Key(this, Orientation.DOWN, new DiscreteCoordinates(9, 4), ElementalEntity.Element.FIRE);
        registerActor(keyFire);

        registerActor(new Portal(this, Orientation.DOWN, new DiscreteCoordinates(10, 10), keyFire, keyWater));;

        DiscreteCoordinates[] coordsSpawn = {new DiscreteCoordinates(13, 6), new DiscreteCoordinates(14, 6)};
        registerActor(new Door(this, Orientation.DOWN, new DiscreteCoordinates(10, 10), "Spawn", keyFire, keyWater, coordsSpawn));



    }

    public void setArenaBehaviour(AreaBehavior behaviour){
        super.setBehavior(behaviour);
    }
}
