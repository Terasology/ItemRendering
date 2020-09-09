// Copyright 2020 The Terasology Foundation
// SPDX-License-Identifier: Apache-2.0

package org.terasology.itemRendering;

import org.terasology.engine.logic.location.LocationComponent;
import org.terasology.itemRendering.components.AnimateBounceComponent;
import org.terasology.itemRendering.components.AnimateWobbleComponent;
import org.terasology.math.geom.Vector3f;

import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.Timer;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.Line2D;

/**
 * A visual test for animated movement. Use the main method to start.
 */
public final class VisualBounceTest {

    private VisualBounceTest() {
        // private
    }

    /**
     * @param args (ignored)
     */
    public static void main(String[] args) {
        // Create and set up the window.
        final JFrame frame = new JFrame("Bounce Test");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(false);

        JComponent comp = new JComponent() {
            private static final long serialVersionUID = -3019274194814342555L;
            private final AnimateBounceComponent bounceComp = new AnimateBounceComponent();
            private final AnimateWobbleComponent wobbleComp = new AnimateWobbleComponent();
            private final LocationComponent loc1Comp = new LocationComponent(new Vector3f(0, 10, 0));
            private final LocationComponent loc2Comp = new LocationComponent(new Vector3f(0, 20, 0));
            private float time = 0;

            @Override
            protected void paintComponent(final Graphics g1) {
                super.paintComponent(g1);
                final Graphics2D g = (Graphics2D) g1;
                int scale = 10;
                g.scale(scale, scale);

                // draw grid
                g.translate(-0.5, -0.5);
                g.setStroke(new BasicStroke(0f));
                g.setColor(Color.LIGHT_GRAY);
                for (int i = 0; i < getWidth() / scale + 1; i += 10) {
                    g.drawLine(i, 0, i, getHeight());
                }
                for (int i = 0; i < getHeight() / scale + 1; i += 10) {
                    g.drawLine(0, i, getWidth(), i);
                }

                g.translate(0.5, 0.5);
                g.setStroke(new BasicStroke(1f));
                g.setColor(Color.BLACK);

                renderBounce(g, bounceComp, loc1Comp, time);
                renderWobble(g, wobbleComp, loc2Comp, time);

                time += 0.2f;
            }
        };

        frame.add(comp);

        Timer t = new Timer(40, event -> comp.repaint());
        t.start();
        frame.setSize(600, 400);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    private static void renderBounce(Graphics2D g, AnimateBounceComponent comp, LocationComponent loc, float time) {

        float modTime = time / comp.period;
        float dt = modTime % 1f;
        if (dt > 0.5f) {
            dt = 1f - dt;
        }
        float relPos = dt * dt;
        float pos = loc.getWorldPosition().y() + comp.maxHeight * relPos * 4f;

        float x = (time) % 60;
        g.draw(new Line2D.Float(x, pos, x, pos));
    }

    private static void renderWobble(Graphics2D g, AnimateWobbleComponent comp, LocationComponent loc, float time) {

        float relPos = (float) -Math.cos(time * Math.PI * 2f / comp.period) * 0.5f + 0.5f;
        float pos = loc.getWorldPosition().y() + comp.maxHeight * relPos;

        float x = (time) % 60;
        g.draw(new Line2D.Float(x, pos, x, pos));
    }
}
