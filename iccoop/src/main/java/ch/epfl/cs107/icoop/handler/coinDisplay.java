package ch.epfl.cs107.icoop.handler;

import ch.epfl.cs107.play.engine.actor.Graphics;
import ch.epfl.cs107.play.engine.actor.ImageGraphics;
import ch.epfl.cs107.play.io.ResourcePath;
import ch.epfl.cs107.play.math.RegionOfInterest;
import ch.epfl.cs107.play.math.Vector;
import ch.epfl.cs107.play.window.Canvas;

import static ch.epfl.cs107.icoop.actor.coin.coinCount;

public class coinDisplay implements Graphics {

    private final static int DEPTH = 2000;
    private final boolean flipped;


    /**
     * c'est les dimensions de chaque chiffre dans l'imager digits
     */
    private final static int DIGIT_WIDTH = 16;
    private final static int DIGIT_HEIGHT = 16;

    public coinDisplay(boolean flipped) {
        this.flipped = flipped;
    }

    @Override
    public void draw(Canvas canvas) {

        /**
         * on calcule la dimension et l'ancrage
         */

        float width = canvas.getTransform().getX().getX();
        float height = canvas.getTransform().getY().getY();

        float ratio = canvas.getWidth() / (float) canvas.getHeight();
        if (ratio > 1)
            height = width / ratio;
        else
            width = height * ratio;

        /**
         * on decal pour l'avoir a la bonne position en haut de l'ecrant
         */
        float horizontalOffset = 2.5f;

        Vector anchor = canvas.getTransform().getOrigin()
                .sub(new Vector(flipped ? (-width / 2 + horizontalOffset) : width / 2, height / 2));


        float spriteWidth = 2.0f;
        float spriteHeight = 1.0f;
        /**
         * position du coinDisplay
         */
        ImageGraphics coinIcon = new ImageGraphics(
                ResourcePath.getSprite("icoop/coinsDisplay"),
                spriteWidth, spriteHeight,
                new RegionOfInterest(0, 0, 64, 32),
                anchor.add(new Vector(horizontalOffset-1, height - 1.75f-1)),
                1, DEPTH
        );
        coinIcon.draw(canvas);

        /**affiche le nombre de pieces
         *on est limite a 9 pieces
         *
         */
        String coinString = String.valueOf(Math.min(coinCount, 9));

        float digitWidth = 0.5f;
        float digitHeight = 0.5f;

        /**
         * permet d'afficher chaque chiffre individuellement
         */
        for (int i = 0; i < coinString.length(); i++) {
            char digitChar = coinString.charAt(i);
            int digitValue = Character.getNumericValue(digitChar);

            /**
             * ici on calcule les coordonnes dans l'image digits
             * Colonne (1 à 4 par ligne)
             * Ligne (0 = 1-4, 1 = 5-8, etc.)
             * Cas particulier pour '0'
             */

            int xIndex = (digitValue - 1) % 4;
            int yIndex = (digitValue - 1) / 4;
            if (digitValue == 0) {
                xIndex = 3;
                yIndex = 2;
            }

            /**
             * calcul des nouvelles positions pour chaque chiffre
             */

            float xPosition = horizontalOffset + i * (digitWidth + 2.0f);
            float yPosition = height - 2.5f - i * (digitHeight + 4.0f);


            ImageGraphics digitGraphic = new ImageGraphics(
                    ResourcePath.getSprite("icoop/digits"),
                    digitWidth, digitHeight,
                    new RegionOfInterest(xIndex * DIGIT_WIDTH, yIndex * DIGIT_HEIGHT, DIGIT_WIDTH, DIGIT_HEIGHT),
                    anchor.add(new Vector(xPosition, yPosition)),
                    1, DEPTH
            );
            digitGraphic.draw(canvas);
        }
    }
}
