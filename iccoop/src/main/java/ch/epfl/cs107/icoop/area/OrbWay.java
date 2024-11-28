package ch.epfl.cs107.icoop.area;

import ch.epfl.cs107.play.engine.actor.Background;
import ch.epfl.cs107.play.engine.actor.Foreground;
import ch.epfl.cs107.play.math.DiscreteCoordinates;

public class OrbWay extends ICoopArea{

    public String getTitle() {
        return "Orbway";
    }

    public DiscreteCoordinates getPlayerRedSpawnPosition() {
        return new DiscreteCoordinates(1, 12);
    }
    public DiscreteCoordinates getPlayerBlueSpawnPosition() {
        return new DiscreteCoordinates(1, 5);
    }

    protected void createArea() {
        registerActor(new Background(this));
        registerActor(new Foreground(this));
    }
}
