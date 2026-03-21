package cn.ncw.javafx.ncwjavafx.particle;

import javafx.animation.AnimationTimer;
import javafx.scene.Group;
import javafx.scene.effect.BlendMode;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class ParticleEmitter extends Group {
    private final List<Particle> particles = new ArrayList<>();
    private final Random random = new Random();
    private final int maxParticles = 500;

    public ParticleEmitter(double x, double y) {
        // 创建发射器周期
        AnimationTimer emitter = new AnimationTimer() {
            @Override
            public void handle(long now) {
                if (particles.size() < maxParticles) {
                    // 添加新粒子
                    Particle p = new Particle(x, y);
                    getChildren().add(p);
                    particles.add(p);
                }

                // 更新所有粒子
                particles.removeIf(Particle::update);
            }
        };
        emitter.start();
    }

    // 粒子类
    private class Particle extends Circle {
        private double vx, vy;
        private double life = 1.0;
        private final double decayRate = 0.01 + random.nextDouble() * 0.05;
        private final Color startColor;

        public Particle(double x, double y) {
            super(3 + random.nextInt(5));
            setCenterX(x);
            setCenterY(y);

            // 随机速度和方向
            double angle = random.nextDouble() * Math.PI * 2;
            double speed = 1 + random.nextDouble() * 5;
            vx = Math.cos(angle) * speed;
            vy = Math.sin(angle) * speed;

            // 随机颜色
            startColor = Color.rgb(
                    255,
                    random.nextInt(100) + 155,
                    random.nextInt(100),
                    0.7
            );
            setFill(startColor);
            setBlendMode(BlendMode.ADD);
        }

        public boolean update() {
            // 更新位置
            setCenterX(getCenterX() + vx);
            setCenterY(getCenterY() + vy);

            // 应用重力
            vy += 0.1;

            // 减小生命值
            life -= decayRate;

            // 更新透明度
            setOpacity(life);

            // 生命结束时移除
            if (life <= 0) {
                getChildren().remove(this);
                return true;
            }
            return false;
        }
    }

    // 使用示例

}
