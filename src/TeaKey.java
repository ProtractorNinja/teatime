import java.util.*;
import java.awt.event.*;

public class TeaKey extends KeyAdapter {
	public static BitSet keys;

	public TeaKey() {
		super();
		keys = new BitSet(525);
	}

	public void keyPressed(KeyEvent e) {
 		keys.set(e.getKeyCode(), true);
 		e.consume();
 	}

 	public void keyReleased(KeyEvent e) {
 		keys.set(e.getKeyCode(), false);
 		e.consume();
 	}

 	public void keyTyped(KeyEvent e) {

 	}

 	public static boolean testKey(int keyNum) {
 		return keys.get(keyNum);
 	}
}