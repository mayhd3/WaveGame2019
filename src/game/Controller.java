package game;

import java.awt.event.*;
import java.util.Arrays;

public class Controller implements KeyListener, MouseListener, MouseMotionListener {
    private double x, y; // controls player movement
    private boolean use; // controls player ability

    public double getX(double pos) {
        return x;
    }
    public double getY(double pos) {
        return y;
    }
    public boolean getUse() { // one off, only use player ability once per key press
        boolean u = use;
        use = false;
        return u;
    }

    // adapter methods, we will override these again
    @Override
    public void keyPressed(KeyEvent e) {

    }
    @Override
    public void keyReleased(KeyEvent e) {

    }
    @Override
    public void mousePressed(MouseEvent e) {

    }
    @Override
    public void mouseReleased(MouseEvent e) {

    }
    @Override
    public void mouseMoved(MouseEvent e) {

    }

    // Unused input events
    @Override public final void keyTyped(KeyEvent e) { }
    @Override public final void mouseClicked(MouseEvent e) { }
    @Override public final void mouseEntered(MouseEvent e) { }
    @Override public final void mouseExited(MouseEvent e) { }
    @Override public final void mouseDragged(MouseEvent e) { }

    public static class Keyboard extends Controller { // control players with the keyboard
        private boolean[] presses; // which keys are pressed
        private int[] keys; // which key codes to listen for

        public Keyboard(int... k) {
            keys = k;
            presses = new boolean[keys.length];
        }

        @Override
        public void keyPressed(KeyEvent e) {
            for(int c = 0; c < presses.length; c += 1) {
                presses[c] |= e.getKeyCode() == keys[c]; // start moving if key is pressed
            }
            super.x = Boolean.compare(presses[3], presses[1]);
            super.y = Boolean.compare(presses[2], presses[0]);

            super.use |= e.getKeyLocation() == keys[keys.length-1];
        }

        @Override
        public void keyReleased(KeyEvent e) {
            for (int c = 0; c < presses.length; c += 1) {
                presses[c] &= e.getKeyCode() != keys[c]; // stop moving if key is released
            }
            super.x = Boolean.compare(presses[3], presses[1]);
            super.y = Boolean.compare(presses[2], presses[0]);
        }
    }

    public static class Mouse extends Controller { // control players with the mouse
        @Override
        public double getX(double pos) {
            return Math.floor((super.getX(pos) - pos)/16)*16;
        }

        @Override
        public double getY(double pos) {
            return Math.floor((super.getY(pos) - pos)/16)*16;
        }

        @Override
        public void mousePressed(MouseEvent e) {
            super.use = true;
        }

        @Override
        public void mouseMoved(MouseEvent e) { // player follows the cursor
            super.x = e.getX();
            super.y = e.getY();
        }
    }

    public static class Multi extends Controller { // compose multiple controllers to act as one
        private Controller[] components;

        public Multi(Controller... c) {
            components = c;
        }

        @Override
        public double getX(double pos) {
            return Arrays.stream(components).mapToDouble(c -> c.getX(pos)).sum();
        }

        @Override
        public double getY(double pos) {
            return Arrays.stream(components).mapToDouble(c -> c.getY(pos)).sum();
        }

        @Override
        public boolean getUse() {
            return Arrays.stream(components).map(Controller::getUse).reduce(Boolean::logicalOr).orElse(false);
        }

        @Override
        public void keyPressed(KeyEvent e) {
            Arrays.stream(components).forEach(c -> c.keyPressed(e));
        }

        @Override
        public void keyReleased(KeyEvent e) {
            Arrays.stream(components).forEach(c -> c.keyReleased(e));
        }

        @Override
        public void mousePressed(MouseEvent e) {
            Arrays.stream(components).forEach(c -> c.mousePressed(e));
        }

        @Override
        public void mouseReleased(MouseEvent e) {
            Arrays.stream(components).forEach(c -> c.mouseReleased(e));
        }

        @Override
        public void mouseMoved(MouseEvent e) {
            Arrays.stream(components).forEach(c -> c.mouseMoved(e));
        }
    }
}
