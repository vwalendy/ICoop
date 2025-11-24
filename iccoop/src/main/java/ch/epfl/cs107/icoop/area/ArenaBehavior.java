package ch.epfl.cs107.icoop.area;

import ch.epfl.cs107.icoop.actor.*;
import ch.epfl.cs107.icoop.handler.ICoopInteractionVisitor;
import ch.epfl.cs107.play.areagame.actor.Interactable;
import ch.epfl.cs107.play.areagame.area.AreaBehavior;
import ch.epfl.cs107.play.areagame.handler.AreaInteractionVisitor;
import ch.epfl.cs107.play.window.Window;


public class ArenaBehavior extends AreaBehavior {


    /**
     *
     * @param window
     * @param name
     * gère la création de l'Arena avec des pires particulière sur certaine cases
     */
    public ArenaBehavior(Window window, String name) {
        super(window, name);
        int height = getHeight();
        int width = getWidth();
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                ArenaCellType color = ArenaCellType.toType(getRGB(height - 1 - y, x));
                setCell(x, y, new ArenaCell(x, y, color));
            }
        }
    }

    /**
     * gère les types de cellules en fonction de leutr couleur
     * défini sur quelles cases ont peut marcher ou voler
     */
    public enum ArenaCellType {
        NULL(0, false, false),
        WALL(-16777216, false, false),
        IMPASSABLE(-8750470, false, true),
        INTERACT(-256, true, true),
        DOOR(-195580, true, true),
        WALKABLE(-1, true, true),
        ROCK(-16777204, true, true),
        OBSTACLE(-16723187, true, true);
        final int type;
        final boolean canWalk;
        final boolean canFly;

        /**
         *
         * @param type
         * @param canWalk
         * @param canFly
         *initialise les différents attributs de la class
         */
        ArenaCellType(int type, boolean canWalk, boolean canFly) {
            this.type = type;
            this.canWalk = canWalk;
            this.canFly = canFly;
        }

        public static ArenaCellType toType(int type) {
            for (ArenaCellType ict : ArenaCellType.values()) {
                if (ict.type == type)
                    return ict;
            }
            System.out.println(type);
            return NULL;
        }
    }

    /**
     * class imbriquée  qui gère lestypes de cellules de arena
     */
    public class ArenaCell extends Cell {
        private final ArenaCellType type;

        /**
         * Default Tuto2Cell Constructor
         *
         * @param x    (int): x coordinate of the cell
         * @param y    (int): y coordinate of the cell
         * @param type (EnigmeCellType), not null
         */
        public ArenaCell(int x, int y, ArenaCellType type) {
            super(x, y);
            this.type = type;
        }

        /**
         *
         * @param entity (Interactable), not null
         * @return
         * redefinition de methode
         */
        @Override
        protected boolean canLeave(Interactable entity) {
            return true;
        }

        /**
         *
         * @param entity (Interactable), not null
         * @return
         * permet de gérer sur quelle case ont peut marcher
         * ici sur quel rocher ont peu marcher une fois cassé
         */
        @Override
        protected boolean canEnter(Interactable entity) {
            if (entity instanceof ICoopPlayer){
                if (!entities.stream().filter(ent -> (ent instanceof Rock || ent instanceof Obstacle)).toList().isEmpty()){
                    return false;
                }
            }
            if(entity instanceof Projectile) {
                return true;
            }
            if (entity instanceof Rock){
                return this.type == ArenaCellType.ROCK;
            }
            if (entity instanceof Obstacle){
                return this.type == ArenaCellType.OBSTACLE;
            }
            return type.canWalk;
        }

        /**
         *
         * @return
         * redefinition de methode
         */
        @Override
        public boolean takeCellSpace() {
            return false;
        }

        /**
         *
         * @return
         * redefinition de methode
         */
        @Override
        public boolean isCellInteractable() {
            return true;
        }

        /**
         *
         * @return
         * redefinition de methode
         */
        @Override
        public boolean isViewInteractable() {
            return false;
        }

        /**
         *
         * @return
         * redefinition de methode
         */
        @Override
        public void acceptInteraction(AreaInteractionVisitor v, boolean isCellInteraction) {
            ((ICoopInteractionVisitor)v).interactWith(this, isCellInteraction);
        }
    }
}
