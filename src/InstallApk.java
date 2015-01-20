import java.awt.dnd.*;
import java.awt.datatransfer.*;
import javax.swing.*;
import java.util.*;
import java.io.File;

public class InstallApk extends JFrame {

	private static final long serialVersionUID = 1L;

	JTextArea jta;
	String INFO = "*******************************\r\n\r\n\r\n   请将apk文件拖拽到此窗口 \r\n\r\n\r\n*******************************";

	public InstallApk() {
		this.setTitle("应用安装小工具");
		this.setBounds(550, 270, 300, 300);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setLayout(null);
		jta = new JTextArea();
		initJta();
		DropTargetAdapter kgd = new DropTargetAdapter() {
			public void drop(DropTargetDropEvent dtde) {
				try {
					Transferable tf = dtde.getTransferable();
					if (tf.isDataFlavorSupported(DataFlavor.javaFileListFlavor)) {
						jta.setText("正在安装... ");
						dtde.acceptDrop(DnDConstants.ACTION_COPY_OR_MOVE);
						@SuppressWarnings("rawtypes")
						List lt = (List) tf
								.getTransferData(DataFlavor.javaFileListFlavor);
						@SuppressWarnings("rawtypes")
						Iterator itor = lt.iterator();
						while (itor.hasNext()) {

							File f = (File) itor.next();
							String filePath = f.getAbsolutePath();
							if (!filePath.endsWith(".apk")) {
								alertError("拖拽的不是apk文件,手不要抖哦...");
								continue;
							} else {
								List<String> devices = ADBUtil.getDevices();
								if (devices.size() != 1) {
									alertError("未发现设备或发现了不只一个设备！");
									continue;
								} else {
									jta.setText("正在安装... ");
									String result = ADBUtil.runCommand(
											ADBUtil.INSTALL + " " + filePath,
											devices.get(0), false);
									result = result.toLowerCase();
									if (result.contains("success")) {
										alertInfo("安装成功！");
										continue;
									} else {
										if (result
												.contains("install_failed_already_exists")) {
											alertError("该应用已经存在，请先卸载！");
											continue;
										} else if (result.contains("")) {
											alertError("签名冲突,安装失败！");
											continue;
										} else if (result.contains("'adb'")) {
											alertError("抱歉，您的电脑未安装 Android SDK，暂时无法使用。");
											continue;
										} else {
											alertError("安装失败！请重试...");
											continue;
										}
									}
								}
							}
						}
						dtde.dropComplete(true);
					} else {
						dtde.rejectDrop();
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		};

		new DropTarget(jta, DnDConstants.ACTION_COPY_OR_MOVE, kgd);

		jta.setBounds(0, 0, 300, 300);
		jta.setEditable(false);
		this.add(jta);
		this.setVisible(true);
		this.setResizable(false);
	}

	/**
	 * 错误提示
	 * 
	 * @param message
	 */
	public void alertError(String message) {
		JOptionPane.showMessageDialog(null, message, "错误",
				JOptionPane.ERROR_MESSAGE);
		System.out.println(message);
		initJta();

	}

	/**
	 * 信息提示
	 * 
	 * @param message
	 */
	public void alertInfo(String message) {
		JOptionPane.showMessageDialog(null, message, "提示",
				JOptionPane.INFORMATION_MESSAGE);
		System.out.println(message);
		initJta();
	}

	/**
	 * 重置文本域
	 */
	public void initJta() {
		jta.setText(INFO);
	}

}