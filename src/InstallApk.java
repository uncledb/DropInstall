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
								System.out.println("拖拽的不是apk文件,手不要抖哦...");
								alertError("拖拽的不是apk文件,手不要抖哦...");
								initJta();
								continue;
							} else {
								List<String> devices = ADBUtil.getDevices();
								if (devices.size() != 1) {
									System.out.println("未发现设备或发现了不只一个设备！");
									alertError("未发现设备或发现了不只一个设备！");
									initJta();
									continue;
								} else {
									jta.setText("正在安装... ");
									String result = ADBUtil.runCommand(
											ADBUtil.INSTALL + " " + filePath,
											devices.get(0), false);
									result = result.toLowerCase();
									if (result.contains("success")) {
										alertInfo("安装成功！");
										System.out.println("安装成功！");
										initJta();
										continue;
									} else {
										if (result
												.contains("install_failed_already_exists")) {
											System.out
													.println("该apk已经存在，请先卸载！");
											alertError("该应用已经存在，请先卸载！");
											initJta();
											continue;
										} else if (result.contains("")) {
											System.out.println("签名冲突,安装失败！");
											alertError("签名冲突,安装失败！");
											initJta();
											continue;
										} else if (result.contains("'adb'")) {
											System.out
													.println("抱歉，您的电脑未安装 Android SDK，暂时无法使用。");
											alertError("抱歉，您的电脑未安装 Android SDK，暂时无法使用。");
											initJta();
											continue;
										} else {
											alertError("安装失败！请重试...");
											System.out.println("安装失败！请重试...");
											initJta();
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

	}

	/**
	 * 信息提示
	 * 
	 * @param message
	 */
	public void alertInfo(String message) {
		JOptionPane.showMessageDialog(null, message, "提示",JOptionPane.INFORMATION_MESSAGE);
	}

	public void initJta() {
		jta.setText(INFO);
	}

	public void aa() {

	}
}