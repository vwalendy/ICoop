package ch.epfl.cs107.icoop.area;

import ch.epfl.cs107.icoop.actor.Door;
import ch.epfl.cs107.play.areagame.area.Area;
import ch.epfl.cs107.play.engine.actor.Background;
import ch.epfl.cs107.play.engine.actor.Foreground;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.math.Orientation;
import ch.epfl.cs107.play.signal.logic.Logic;

import java.util.ArrayList;
import java.util.Arrays;


public class OrbWay extends ICoopArea {

    public OrbWay (){
        super("OrbWay"); 
    }

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
        ArrayList<DiscreteCoordinates> coords1 = new ArrayList<>(Arrays.asList(
                new DiscreteCoordinates(0, 13),
                new DiscreteCoordinates(0, 12),
                new DiscreteCoordinates(0, 11),
                new DiscreteCoordinates(0, 10)
                ));

        ArrayList<DiscreteCoordinates> coords2 = new ArrayList<>(Arrays.asList(
                new DiscreteCoordinates(0, 7),
                new DiscreteCoordinates(0, 6),
                new DiscreteCoordinates(0, 5),
                new DiscreteCoordinates(0, 4)
                ));

        registerActor(new Background(this));
        registerActor(new Foreground(this));
        registerActor(new Door(this, new DiscreteCoordinates(0, 14), "Spawn", Logic.TRUE, coords1, new DiscreteCoordinates(18, 16), new DiscreteCoordinates(18, 15)));
    }
}