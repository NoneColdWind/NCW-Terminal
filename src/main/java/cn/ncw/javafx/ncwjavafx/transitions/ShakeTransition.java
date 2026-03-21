package cn.ncw.javafx.ncwjavafx.transitions;

import javafx.animation.Interpolator;
import javafx.animation.Transition;
import javafx.scene.Node;
import javafx.util.Duration;

public class ShakeTransition extends Transition {
    private final Node node;
    private final double originalX;
    private final double shakeIntensity;

    public ShakeTransition(Node node) {
        this(node, 10);
    }

    public ShakeTransition(Node node, double shakeIntensity) {
        this.node = node;
        this.originalX = node.getTranslateX();
        this.shakeIntensity = shakeIntensity;
        setCycleCount(6);
        setCycleDuration(Duration.millis(50));
        setAutoReverse(true);
        setInterpolator(Interpolator.LINEAR);
    }

    @Override
    protected void interpolate(double frac) {
        double shake = Math.sin(frac * Math.PI * 8) * shakeIntensity;
        node.setTranslateX(originalX + shake);
    }
}