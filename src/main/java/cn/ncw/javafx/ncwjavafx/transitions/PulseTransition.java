package cn.ncw.javafx.ncwjavafx.transitions;

import javafx.animation.Interpolator;
import javafx.animation.Transition;
import javafx.scene.Node;
import javafx.util.Duration;

public class PulseTransition extends Transition {
    private final Node node;
    private final double originalScale;
    private final double intensity;

    public PulseTransition(Node node) {
        this(node, 1.1, 600);
    }

    public PulseTransition(Node node, double intensity, int durationMillis) {
        this.node = node;
        this.originalScale = node.getScaleX();
        this.intensity = intensity;
        setCycleCount(2);
        setAutoReverse(true);
        setCycleDuration(Duration.millis(durationMillis));
        setInterpolator(Interpolator.EASE_BOTH);
    }

    @Override
    protected void interpolate(double frac) {
        double scale = originalScale + (intensity - originalScale) * Math.sin(frac * Math.PI);
        node.setScaleX(scale);
        node.setScaleY(scale);
    }
}