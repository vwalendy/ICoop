package ch.epfl.cs107.icoop.area;

import ch.epfl.cs107.icoop.actor.Door;
import ch.epfl.cs107.play.areagame.area.Area;
import ch.epfl.cs107.play.engine.actor.Background;
import ch.epfl.cs107.play.engine.actor.Foreground;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.math.Orientation;
import ch.epfl.cs107.play.signal.logic.Logic;


public class OrbWay extends ICoopArea {

    public static Area Title;

    public String getTitle() {
        return "Orbway";
    }

    public static Area getAreaOW(){
        return new OrbWay();
    }

    @Override
    public DiscreteCoordinates getBluePlayerSpawnPosition() {
        return new DiscreteCoordinates(1, 5);
    }

    @Override
    public DiscreteCoordinates getRedPlayerSpawnPosition() {
        return new DiscreteCoordinates(1, 12);
    }

    protected void createArea() {
        registerActor(new Background(this));
        registerActor(new Foreground(this));
        registerActor(new Door(Spawn.getAreaS(), Logic.TRUE, new DiscreteCoordinates(0, 14), this, Orientation.LEFT, new DiscreteCoordinates(18, 16)));
    }
}