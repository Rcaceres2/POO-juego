package controllers;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class InputController implements KeyListener {

    public boolean left = false;
    public boolean right = false;
    public boolean up = false;
    public boolean down = false;
    public boolean space = false;

    @Override
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void keyPressed(KeyEvent e) {
        int code = e.getKeyCode();

        if (code == KeyEvent.VK_LEFT || code == KeyEvent.VK_A) {
            left = true;
        }
        if (code == KeyEvent.VK_RIGHT || code == KeyEvent.VK_D) {
            right = true;
        }
        if (code == KeyEvent.VK_UP || code == KeyEvent.VK_W) {
            up = true;
        }
        if (code == KeyEvent.VK_DOWN || code == KeyEvent.VK_S) {
            down = true;
        }
        if (code == KeyEvent.VK_SPACE) {
            space = true;
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        int code = e.getKeyCode();

        if (code == KeyEvent.VK_LEFT || code == KeyEvent.VK_A) {
            left = false;
        }
        if (code == KeyEvent.VK_RIGHT || code == KeyEvent.VK_D) {
            right = false;
        }
        if (code == KeyEvent.VK_UP || code == KeyEvent.VK_W) {
            up = false;
        }
        if (code == KeyEvent.VK_DOWN || code == KeyEvent.VK_S) {
            down = false;
        }
        if (code == KeyEvent.VK_SPACE) {
            space = false;
        }
    }
}
