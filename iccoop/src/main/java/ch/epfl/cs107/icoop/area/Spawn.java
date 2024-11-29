package ch.epfl.cs107.icoop.area;

import ch.epfl.cs107.icoop.actor.Element;
import ch.epfl.cs107.icoop.actor.ICoopPlayer;
import ch.epfl.cs107.play.engine.actor.Background;
import ch.epfl.cs107.play.engine.actor.Foreground;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.math.Orientation;
import ch.epfl.cs107.play.math.Vector;




public class Spawn extends ICoopArea{


    public String getTitle() {
        return "Spawn";
    }

    @Override
    public DiscreteCoordinates getBluePlayerSpawnPosition() {
        return new DiscreteCoordinates (14,6);
    }

    @Override
    public DiscreteCoordinates getRedPlayerSpawnPosition() {
        return new DiscreteCoordinates (13,6);
    }




    protected void createArea() {
        registerActor(new Background(this));
        registerActor(new Foreground(this));
        registerActor(new ICoopPlayer(this, Orientation.DOWN, new DiscreteCoordinates(13, 6), "icoop/player", Element.Fire));
        registerActor(new ICoopPlayer(this, Orientation.DOWN, new DiscreteCoordinates(14, 6), "icoop/player2", Element.Water));
    }


}
