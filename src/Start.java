import java.awt.Font;

import javax.swing.UIManager;

public class Start {
	private static final Font font = new Font("微软雅黑", Font.BOLD, 12);

	public static void main(String args[]) {
		UIManager.put("Button.font", font);
		UIManager.put("TextArea.font", font);
		UIManager.put("ComboBox.font", font);
		UIManager.put("TextField.font", font);
		UIManager.put("Label.font", font);
		new InstallApk();
	}
}
