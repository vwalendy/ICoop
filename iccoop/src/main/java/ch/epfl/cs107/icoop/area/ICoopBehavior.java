package ch.epfl.cs107.icoop.area;

import ch.epfl.cs107.icoop.actor.ElementalWall;
import ch.epfl.cs107.icoop.actor.ICoopPlayer;
import ch.epfl.cs107.icoop.actor.Obstacle;
import ch.epfl.cs107.icoop.handler.ICoopInteractionVisitor;
import ch.epfl.cs107.play.areagame.actor.Interactable;
import ch.epfl.cs107.play.areagame.area.AreaBehavior;
import ch.epfl.cs107.play.areagame.handler.AreaInteractionVisitor;
import ch.epfl.cs107.play.window.Window;


public final class ICoopBehavior extends AreaBehavior {

    /**
     * Initialise le comportement de l'aire en créant des cellules adaptées au jeu ICoop.
     * @param window (Window): la fenêtre associée à l'aire
     * @param name (String): le nom de l'aire
     */
    public ICoopBehavior(Window window, String name) {
        super(window, name);
        int height = getHeight();
        int width = getWidth();
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                ICoopCellType color = ICoopCellType.toType((getRGB(height - 1 - y, x)));
                setCell(x, y, new ICoopCell(x, y, color));
            }
        }
    }

    /**
     * Enumération représentant les différents types de cellules du jeu ICoop.
     */
    public enum ICoopCellType {
        //https://stackoverflow.com/questions/25761438/understanding-bufferedimage-getrgb-output-values
        NULL(0, false, false),
        WALL(-16777216, false, false),
        IMPASSABLE(-8750470, false, true),
        INTERACT(-256, true, true),
        DOOR(-195580, true, true),
        WALKABLE(-1, true, true),
        ROCK(-16777204, true, true),
        OBSTACLE(-16723187, true, true),
        ;


        final int type;
        final boolean canWalk;
        final boolean canFly;

        /**
         * Constructeur de ICoopCellType.
         * @param type (int): code RGB de la cellule
         * @param canWalk (boolean): indique si on peut marcher sur cette cellule
         * @param canFly (boolean): indique si on peut voler au-dessus de cette cellule
         */
        ICoopCellType(int type, boolean canWalk, boolean canFly) {
            this.type = type;
            this.canWalk = canWalk;
            this.canFly = canFly;
        }

        /**
         * Convertit une valeur RGB en un type de cellule.
         * @param type (int): valeur RGB de la cellule
         * @return ICoopCellType correspondant au RGB donné
         */
        public static ICoopCellType toType(int type) {
            for (ICoopCellType ict : ICoopCellType.values()) {
                if (ict.type == type)
                    return ict;
            }
            // When you add a new color, you can print the int value here before assign it to a type
            //System.out.println(type);
            return NULL;
        }
    }

    /**
     * Cell adapted to the Tuto2 game
     */
    public class ICoopCell extends Cell implements Interactable {


        private final ICoopCellType type;

        /**
         * Default Tuto2Cell Constructor
         *
         * @param x    (int): x coordinate of the cell
         * @param y    (int): y coordinate of the cell
         * @param type (EnigmeCellType), not null
         */
        public ICoopCell(int x, int y, ICoopCellType type) {
            super(x, y);
            this.type = type;
        }

        /**
         * @param entity (Interactable): entité à vérifier
         * @return true si l'entité peut quitter la cellule.
         */

        @Override
        protected boolean canLeave(Interactable entity) {
            return true;
        }

        /**
         * Détermine si une entité peut entrer dans cette cellule.
         * @param entity (Interactable): entité à vérifier
         * @return true si l'entité peut entrer, false sinon.
         */
        @Override
        protected boolean canEnter(Interactable entity) {
            for (Interactable interactable : entities) {
                if (interactable instanceof ElementalWall && entity instanceof ICoopPlayer) {

                    ICoopPlayer player = (ICoopPlayer) entity;
                    ElementalWall wall = (ElementalWall) interactable;
                    if (((ElementalWall) interactable).getActiveLogic().isOff()) {
                        return true;
                    } else if (player.getElement() == wall.getElement()) {
                        return true;
                    } else {
                        return false;
                    }
                }

                if (interactable.takeCellSpace() && entity.takeCellSpace()) {
                    return false;
                }
            }

            if (!type.canWalk && (entity instanceof ICoopPlayer || entity instanceof Obstacle)) {
                return false;
            }

            return type.canWalk;
        }


        /**
         * @return true si la cellule est interactive au niveau cellulaire.
         */
        @Override
        public boolean isCellInteractable() {
            return true;
        }

        /**
         * @return false car la vue n'est pas interactive.
         */
        @Override
        public boolean isViewInteractable() {
            return false;
        }

        /**
         * Accepte une interaction avec un visiteur.
         * @param v (AreaInteractionVisitor): le visiteur d'interaction
         * @param isCellInteraction (boolean): true si c'est une interaction cellulaire
         */
        @Override
        public void acceptInteraction(AreaInteractionVisitor v, boolean isCellInteraction) {
            ((ICoopInteractionVisitor) v).interactWith(this, isCellInteraction);
        }
    }
}



