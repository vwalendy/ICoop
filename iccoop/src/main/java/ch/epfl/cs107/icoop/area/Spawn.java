package ch.epfl.cs107.icoop.area;

import ch.epfl.cs107.play.engine.actor.Background;
import ch.epfl.cs107.play.engine.actor.Foreground;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.math.Vector;

public class Spawn extends ICoopArea{


    public String getTitle() {
        return "Spawn";
    }

    public DiscreteCoordinates getPlayerRedSpawnPosition() {
        return new DiscreteCoordinates(13, 6);
    }
    public DiscreteCoordinates getPlayerBlueSpawnPosition() {
        return new DiscreteCoordinates(14, 6);
    }

    protected void createArea() {
        registerActor(new Background(this));
        registerActor(new Foreground(this));
        registerActor(new SimpleGhost(new Vector(13, 6), "Red"));
        registerActor(new SimpleGhost(new Vector(14, 6), "Blue"));
    }


}
