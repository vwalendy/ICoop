package ch.epfl.cs107.icoop.area;

import ch.epfl.cs107.play.engine.actor.Background;
import ch.epfl.cs107.play.engine.actor.Foreground;
import ch.epfl.cs107.play.math.DiscreteCoordinates;


public class OrbWay extends ICoopArea{

    public String getTitle() {
        return "Orbway";
    }

    @Override
    public DiscreteCoordinates getBluePlayerSpawnPosition() {
        return new DiscreteCoordinates (1,5);
    }

    @Override
    public DiscreteCoordinates getRedPlayerSpawnPosition() {
        return new DiscreteCoordinates (1,12);
    }

    protected void createArea() {
        registerActor(new Background(this));
        registerActor(new Foreground(this));
    }
}

