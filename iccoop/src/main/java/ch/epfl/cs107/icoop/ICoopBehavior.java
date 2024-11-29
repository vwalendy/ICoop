package ch.epfl.cs107.icoop;

import ch.epfl.cs107.icoop.actor.ICoopPlayer;
import ch.epfl.cs107.play.areagame.actor.Interactable;
import ch.epfl.cs107.play.areagame.area.AreaBehavior;
import ch.epfl.cs107.play.areagame.handler.AreaInteractionVisitor;
import ch.epfl.cs107.play.window.Window;

public final class ICoopBehavior extends AreaBehavior {

    public ICoopBehavior(Window window, String name) {
        super(window,name);
        int height = getHeight();
        int width = getWidth();
        for(
        int y = 0;
        y<height;y++)

        {
            for (int x = 0; x < width; x++) {
                Tuto2CellType color = Tuto2CellType.toType(getRGB(height - 1 - y, x));
                setCell(x, y, new ICoopCell(x, y, color));
            }
        }
    }

    public enum Tuto2CellType {
        //https://stackoverflow.com/questions/25761438/understanding-bufferedimage-getrgb-output-values
        NULL(0, false, false),
        WALL(-16777216, false, false),
        IMPASSABLE(-8750470, false, true),
        INTERACT(-256, true, true),
        DOOR(-195580, true, true),
        WALKABLE(-1, true, true),
        ROCK(-16777204, true, true),
        OBSTACLE(-16723187, true,true),
        ;

        final int type;
        final boolean canWalk;
        final boolean canFly;

        Tuto2CellType(int type, boolean canWalk, boolean canFly) {
            this.type = type;
            this.canWalk = canWalk;
            this.canFly = canFly;
        }

        public static Tuto2CellType toType(int type) {
            for (Tuto2CellType ict : Tuto2CellType.values()) {
                if (ict.type == type)
                    return ict;
            }
            // When you add a new color, you can print the int value here before assign it to a type
            System.out.println(type);
            return NULL;
        }
    }

    /**
     * Cell adapted to the Tuto2 game
     */
    public class ICoopCell extends Cell {
        /// Type of the cell following the enum
        private final Tuto2CellType type;

        /**
         * Default Tuto2Cell Constructor
         *
         * @param x    (int): x coordinate of the cell
         * @param y    (int): y coordinate of the cell
         * @param type (EnigmeCellType), not null
         */
        public ICoopCell(int x, int y, Tuto2CellType type) {
            super(x, y);
            this.type = type;
        }

        @Override
        protected boolean canLeave(Interactable entity) {
            return true;
        }

        @Override
        protected boolean canEnter(Interactable entity) {
            for (Interactable interactable : entities) {
                if (interactable.takeCellSpace()) {
                    return false;
                }
            }
            return type.canWalk;
        }

        @Override
        public boolean isCellInteractable() {
            return true;
        }

        @Override
        public boolean isViewInteractable() {
            return false;
        }

        @Override
        public void acceptInteraction(AreaInteractionVisitor v, boolean isCellInteraction) {
        }

    }
}


