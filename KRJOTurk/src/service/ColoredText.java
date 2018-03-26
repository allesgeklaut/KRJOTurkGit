package service;

import javafx.scene.paint.Color;

/** class to establish automatic resizing images
 *
 */

public class ColoredText {

    private final String text ;
    private final Color color ;

    public ColoredText(String text, Color color) {
        this.text = text ;
        this.color = color ;
    }

    public String getText() {
        return text ;
    }

    public Color getColor() {
        return color ;
    }
}

