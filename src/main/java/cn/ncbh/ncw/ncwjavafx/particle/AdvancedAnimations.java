package cn.ncbh.ncw.ncwjavafx.particle;

import cn.ncbh.ncw.ncwjavafx.transitions.BounceTransition;
import cn.ncbh.ncw.ncwjavafx.transitions.PulseTransition;
import cn.ncbh.ncw.ncwjavafx.transitions.ShakeTransition;
import javafx.animation.*;
import javafx.scene.Node;
import javafx.scene.transform.Rotate;
import javafx.util.Duration;

public class AdvancedAnimations {

    // 1. 淡入动画
    public static FadeTransition fadeIn(Node node) {
        return fadeIn(node, 500);
    }

    public static FadeTransition fadeIn(Node node, int durationMillis) {
        FadeTransition ft = new FadeTransition(Duration.millis(durationMillis), node);
        ft.setFromValue(0);
        ft.setToValue(1);
        return ft;
    }

    // 2. 淡出动画
    public static FadeTransition fadeOut(Node node) {
        return fadeOut(node, 500);
    }

    public static FadeTransition fadeOut(Node node, int durationMillis) {
        FadeTransition ft = new FadeTransition(Duration.millis(durationMillis), node);
        ft.setFromValue(1);
        ft.setToValue(0);
        return ft;
    }

    // 3. 滑动进入 (左滑入)
    public static TranslateTransition slideInFromLeft(Node node) {
        TranslateTransition tt = new TranslateTransition(Duration.millis(600), node);
        double originalX = node.getTranslateX();
        node.setTranslateX(-node.getBoundsInParent().getWidth());
        tt.setToX(originalX);
        return tt;
    }

    // 4. 滑动离开 (右滑出)
    public static TranslateTransition slideOutToRight(Node node) {
        TranslateTransition tt = new TranslateTransition(Duration.millis(600), node);
        double originalX = node.getTranslateX();
        tt.setToX(node.getScene().getWidth() + node.getBoundsInParent().getWidth());
        return tt;
    }

    // 5. 缩放动画 (放大)
    public static ScaleTransition zoomIn(Node node) {
        return zoomIn(node, 1.2);
    }

    public static ScaleTransition zoomIn(Node node, double scaleFactor) {
        ScaleTransition st = new ScaleTransition(Duration.millis(400), node);
        st.setToX(scaleFactor);
        st.setToY(scaleFactor);
        return st;
    }

    // 6. 缩放动画 (缩小)
    public static ScaleTransition zoomOut(Node node) {
        return zoomOut(node, 0.8);
    }

    public static ScaleTransition zoomOut(Node node, double scaleFactor) {
        ScaleTransition st = new ScaleTransition(Duration.millis(400), node);
        st.setToX(scaleFactor);
        st.setToY(scaleFactor);
        return st;
    }

    // 7. 3D翻转动画
    public static ParallelTransition flip(Node node) {
        RotateTransition rt = new RotateTransition(Duration.millis(800), node);
        rt.setAxis(Rotate.Y_AXIS);
        rt.setFromAngle(0);
        rt.setToAngle(360);

        FadeTransition ft = new FadeTransition(Duration.millis(400), node);
        ft.setFromValue(0.5);
        ft.setToValue(1);

        ParallelTransition pt = new ParallelTransition(rt, ft);
        return pt;
    }

    // 8. 弹跳动画
    public static BounceTransition bounce(Node node) {
        return new BounceTransition(node);
    }

    // 9. 摇摆动画 (Swing)
    public static RotateTransition swing(Node node) {
        RotateTransition rt = new RotateTransition(Duration.millis(1000), node);
        rt.setAxis(Rotate.Z_AXIS);
        rt.setFromAngle(-15);
        rt.setToAngle(15);
        rt.setCycleCount(3);
        rt.setAutoReverse(true);
        rt.setInterpolator(Interpolator.EASE_BOTH);
        return rt;
    }

    // 10. 波浪动画
    public static ParallelTransition wave(Node node) {
        ScaleTransition st = new ScaleTransition(Duration.millis(300), node);
        st.setToY(1.2);
        st.setAutoReverse(true);
        st.setCycleCount(2);

        TranslateTransition tt = new TranslateTransition(Duration.millis(600), node);
        tt.setByY(-20);
        tt.setAutoReverse(true);
        tt.setCycleCount(2);

        return new ParallelTransition(st, tt);
    }

    // 11. 高亮闪光动画
    public static Timeline flash(Node node) {
        Timeline timeline = new Timeline();
        timeline.setCycleCount(3);
        timeline.setAutoReverse(true);

        KeyValue kv1 = new KeyValue(node.opacityProperty(), 0.3);
        KeyFrame kf1 = new KeyFrame(Duration.millis(100), kv1);

        KeyValue kv2 = new KeyValue(node.opacityProperty(), 1);
        KeyFrame kf2 = new KeyFrame(Duration.millis(200), kv2);

        timeline.getKeyFrames().addAll(kf1, kf2);
        return timeline;
    }

    // 12. 橡皮筋拉伸动画
    public static Timeline rubberBand(Node node) {
        Timeline timeline = new Timeline();
        timeline.setCycleCount(1);

        KeyValue kv1 = new KeyValue(node.scaleXProperty(), 1.25);
        KeyValue kv2 = new KeyValue(node.scaleYProperty(), 0.75);
        KeyFrame kf1 = new KeyFrame(Duration.millis(0), kv1, kv2);

        KeyValue kv3 = new KeyValue(node.scaleXProperty(), 0.75);
        KeyValue kv4 = new KeyValue(node.scaleYProperty(), 1.25);
        KeyFrame kf2 = new KeyFrame(Duration.millis(150), kv3, kv4);

        KeyValue kv5 = new KeyValue(node.scaleXProperty(), 1.15);
        KeyValue kv6 = new KeyValue(node.scaleYProperty(), 0.85);
        KeyFrame kf3 = new KeyFrame(Duration.millis(300), kv5, kv6);

        KeyValue kv7 = new KeyValue(node.scaleXProperty(), 0.95);
        KeyValue kv8 = new KeyValue(node.scaleYProperty(), 1.05);
        KeyFrame kf4 = new KeyFrame(Duration.millis(450), kv7, kv8);

        KeyValue kv9 = new KeyValue(node.scaleXProperty(), 1.05);
        KeyValue kv10 = new KeyValue(node.scaleYProperty(), 0.95);
        KeyFrame kf5 = new KeyFrame(Duration.millis(600), kv9, kv10);

        KeyValue kv11 = new KeyValue(node.scaleXProperty(), 1);
        KeyValue kv12 = new KeyValue(node.scaleYProperty(), 1);
        KeyFrame kf6 = new KeyFrame(Duration.millis(750), kv11, kv12);

        timeline.getKeyFrames().addAll(kf1, kf2, kf3, kf4, kf5, kf6);
        return timeline;
    }

    // 13. 脉冲动画
    public static PulseTransition pulse(Node node) {
        return new PulseTransition(node);
    }

    // 14. 摇晃动画
    public static ShakeTransition shake(Node node) {
        return new ShakeTransition(node);
    }
}