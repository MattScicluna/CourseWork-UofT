package GUI;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class AdListener implements ActionListener{
	
	private guiBuild guiObject;

	public AdListener(guiBuild guiObject) {
		this.guiObject = guiObject;
	}
	
	public void actionPerformed(ActionEvent e) {
		guiObject.admin = true;
		}

}
