package ch.epfl.cs107.icoop.area.maps;

import ch.epfl.cs107.icoop.actor.*;
import ch.epfl.cs107.icoop.area.ICoopArea;
import ch.epfl.cs107.icoop.handler.DialogHandler;
import ch.epfl.cs107.play.engine.actor.Background;
import ch.epfl.cs107.play.engine.actor.Dialog;
import ch.epfl.cs107.play.engine.actor.Foreground;
import ch.epfl.cs107.play.io.FileSystem;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.math.Orientation;
import ch.epfl.cs107.play.signal.logic.Logic;
import ch.epfl.cs107.play.window.Window;
import java.util.List;


public final class Spawn extends ICoopArea {

    private List<DiscreteCoordinates> otherCell;
    private final DialogHandler dialogHandler;
    private boolean firstVisit = true;
    List<DiscreteCoordinates> spawnPosition=List.of(
            new DiscreteCoordinates(14,6),
            new DiscreteCoordinates(13,6));
    private List<DiscreteCoordinates> otherCellMaze;
    private List<DiscreteCoordinates> otherCellManoir;



    public Spawn(DialogHandler dialogHandler){
        super(dialogHandler);
        this.dialogHandler = dialogHandler;
    }

    @Override
    public boolean begin(Window window, FileSystem fileSystem) {
        return super.begin(window, fileSystem);
    }

    /**
     *
     * @param deltaTime
     *                  permet de faire afficher le dialogue lors du lancement duu jeux
     */
    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);
        if (firstVisit) {
            dialogHandler.publish(new Dialog("welcome"));
            firstVisit = false;
        }
    }


    public String getTitle() {
        return "Spawn";
    }

    /**
     *
     * @return
     */
    public List<DiscreteCoordinates> getPlayerSpawnPosition() {
        return spawnPosition;

    }

    /**
     * ici je register tout les acteurs presents sur la map avec leurs coordonnes et paramtres propres
     */

    protected void createArea() {
        registerActor(new Background(this));
        registerActor(new Foreground(this));

        otherCell = List.of(new DiscreteCoordinates(19, 16));
        DiscreteCoordinates[] coords = {new DiscreteCoordinates(1, 12), new DiscreteCoordinates(1, 5)};
        DiscreteCoordinates[] coordsMaze = {new DiscreteCoordinates(2,39),new DiscreteCoordinates(3,39)};
        otherCellMaze= List.of(new DiscreteCoordinates(5,0));

        registerActor(new Door(this, Orientation.DOWN, new DiscreteCoordinates(19, 15),"OrbWay", Logic.TRUE, coords, otherCell));
        registerActor(new Door(this, Orientation.DOWN, new DiscreteCoordinates(4,0),"Maze",Logic.TRUE,coordsMaze,otherCellMaze));

        registerActor(new Rock(this,Orientation.DOWN,new DiscreteCoordinates(12,10),"rock.1"));
        registerActor(new Rock(this,Orientation.DOWN,new DiscreteCoordinates(10,10),"rock.1"));
        registerActor(new Rock(this,Orientation.DOWN,new DiscreteCoordinates(11,11),"rock.1"));
        registerActor(new Rock(this,Orientation.DOWN,new DiscreteCoordinates(11,11),"rock.2"));

        registerActor(new Explosif(this, Orientation.DOWN, new DiscreteCoordinates(11, 10), 3f));
        registerActor(new Explosif(this, Orientation.DOWN, new DiscreteCoordinates(11, 8), 3f));

        registerActor(new Heart(this, Orientation.DOWN, new DiscreteCoordinates(11, 5)));
        registerActor(new Heart(this, Orientation.DOWN, new DiscreteCoordinates(10, 5)));

        registerActor(new coin(this,Orientation.DOWN,new DiscreteCoordinates(5,7)));
        registerActor(new coin(this,Orientation.DOWN,new DiscreteCoordinates(5,8)));
        registerActor(new coin(this,Orientation.DOWN,new DiscreteCoordinates(5,9)));

        registerActor(new coin(this,Orientation.DOWN,new DiscreteCoordinates(8,9)));

        registerActor(new coin(this,Orientation.DOWN,new DiscreteCoordinates(7,9)));
        registerActor(new coin(this,Orientation.DOWN,new DiscreteCoordinates(6,9)));

        registerActor(new coin(this,Orientation.DOWN,new DiscreteCoordinates(5,5)));
        registerActor(new coin(this,Orientation.DOWN,new DiscreteCoordinates(5,6)));

        registerActor(new logMonster(this,Orientation.DOWN,new DiscreteCoordinates(18,11)));









    }
}