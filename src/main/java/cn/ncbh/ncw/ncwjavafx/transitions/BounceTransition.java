package cn.ncbh.ncw.ncwjavafx.transitions;

import javafx.animation.Interpolator;
import javafx.animation.Transition;
import javafx.scene.Node;
import javafx.util.Duration;

public class BounceTransition extends Transition {
    private final Node node;
    private final double startY;
    private final double height;

    public BounceTransition(Node node) {
        this(node, node.getBoundsInParent().getHeight());
    }

    public BounceTransition(Node node, double height) {
        this.node = node;
        this.startY = node.getTranslateY();
        this.height = height;
        setCycleDuration(Duration.millis(800));
        setInterpolator(Interpolator.EASE_OUT);
    }

    @Override
    protected void interpolate(double frac) {
        double bounce = 0.0;

        // 弹跳曲线计算
        if (frac < 0.25) {
            // 第一次弹跳
            bounce = -height * (1 - frac * 4);
        } else if (frac < 0.5) {
            // 第二次弹跳
            bounce = -height * (1 - (frac - 0.25) * 4) * 0.6;
        } else if (frac < 0.75) {
            // 第三次弹跳
            bounce = -height * (1 - (frac - 0.5) * 4) * 0.3;
        } else {
            // 第四次弹跳
            bounce = -height * (1 - (frac - 0.75) * 4) * 0.15;
        }

        node.setTranslateY(startY + bounce);
    }
}
