package socket;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

/**
 * Client.java
 * 
 * @Author : YLD10
 * @EditTime : 2017/6/3 15:07
 **/
public class Client extends JFrame implements ActionListener {

	private static final long serialVersionUID = 1L;
	private String ipAddress = null; // ip 地址
	private static int port = 30000; // 端口号
	private Socket socket = null; // socket 连接的载体
	private PrintWriter out = null; // 输入流对象
	private JPanel panel = null;
	private JButton button = null; // 发送按钮
	private JTextField textField = null;
	private JTextField textFieldIp = null;
	private static JLabel labManifest = null;
	private JLabel labIp = null;
	private static JLabel labIpAlert = null;

	public Client() {
		
		Dimension screensize = Toolkit.getDefaultToolkit().getScreenSize();
		this.setTitle("Socket");
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setBounds((int)(screensize.getWidth()/2-225), (int)(screensize.getHeight()/2-150), 450, 300);
		setPanel(new JPanel());
		getPanel().setBorder(new EmptyBorder(5, 5, 5, 5));
		this.setContentPane(getPanel());
		getPanel().setLayout(null);

		setLabManifest(new JLabel("对话准备好了"));
		setLabIp(new JLabel("ip地址："));
		setLabIpAlert(new JLabel(""));
		setTextField(new JTextField());
		setTextFieldIp(new JTextField());
		setButton(new JButton("发送"));

		this.getRootPane().setDefaultButton(getButton());

		getLabManifest().setBounds(150, 30, 150, 30);
		getTextField().setBounds(30, 100, 250, 30);
		getLabIp().setBounds(30, 150, 100, 30);
		getTextFieldIp().setBounds(30, 180, 200, 30);
		getLabIpAlert().setBounds(250, 180, 150, 30);
		getButton().setBounds(300, 100, 115, 30);

		getPanel().add(getLabManifest());
		getPanel().add(getTextField());
		getPanel().add(getLabIp());
		getPanel().add(getTextFieldIp());
		getPanel().add(getLabIpAlert());
		getPanel().add(getButton());

		getButton().addActionListener(this); // 绑定发送按钮的触发事件

		getLabManifest().setFont(new Font("楷体", 1, 18));
		getLabIp().setFont(new Font("楷体", 1, 18));
		getLabIpAlert().setFont(new Font("楷体", 1, 14));
		getLabIpAlert().setForeground(Color.red);

		this.setResizable(false);
		this.setVisible(true);
		System.out.println("客户端打开成功！");
	}

	// 建立连接，发送数据
	private void sendMsg() {

		String message = getTextField().getText().trim();
		
		if (message.isEmpty()) {
			JOptionPane.showMessageDialog(null, "发送内容不能为空！", "警告", JOptionPane.ERROR_MESSAGE);
		} else {
			try {
				// socket = new Socket(ip, port);
				setSocket(new Socket(getIpAddress(), port)); // 发送请求建立连接
				System.out.println("客户端发送消息中...");
				// 获取输入流
				setOut(new PrintWriter(
						new BufferedWriter(new OutputStreamWriter(getSocket().getOutputStream(), "UTF-8")), true));
				// 往输入流中写数据
				getOut().print(message);
				getOut().close(); // 关闭输入流
				System.out.println("消息发送成功！");

				getTextField().setText("");

			} catch (IOException e) {
				// e.printStackTrace();
				getLabIpAlert().setText("此ip地址无法通信");
				System.out.println("客户端连接失败！");
			} finally {
				try {
					if (getSocket() != null) {
						getSocket().close(); // 关闭连接
					}
					System.out.println("连接关闭成功！\n");
				} catch (IOException e) {
					// e.printStackTrace();
					System.out.println("连接关闭失败！");
				}
			}

		}
	}

	// 发送按钮触发事件
	public void actionPerformed(ActionEvent arg0) {
		setIpAddress(getTextFieldIp().getText().trim());
		Client.getLabIpAlert().setText("");

		if (getIpAddress().isEmpty()) {
			JOptionPane.showMessageDialog(null, "ip地址不能为空！", "警告", JOptionPane.ERROR_MESSAGE);
		} else {
			String address[] = getIpAddress().split("\\.");

			if (address.length != 4) {
				JOptionPane.showMessageDialog(null, "ip地址格式有误！", "警告", JOptionPane.ERROR_MESSAGE);
			} else {
				try {
					int a = Integer.valueOf(address[0]), b = Integer.valueOf(address[1]),
							c = Integer.valueOf(address[2]), d = Integer.valueOf(address[3]);
					if (a >= 0 && a <= 255 && b >= 0 && b <= 255 && c >= 0 && c <= 255 && d >= 0 && d <= 255) {
						new Thread() {
							public void run() {
								sendMsg(); // 另开线程发送数据
							};
						}.start();
					} else {
						JOptionPane.showMessageDialog(null, "ip地址范围有误！", "警告", JOptionPane.ERROR_MESSAGE);
					}
				} catch (Exception e) {
					JOptionPane.showMessageDialog(null, "ip地址有误！", "警告", JOptionPane.ERROR_MESSAGE);
				}
			}
		}
	}

	public String getIpAddress() {
		return ipAddress;
	}

	public void setIpAddress(String ipAddress) {
		this.ipAddress = ipAddress;
	}

	public Socket getSocket() {
		return socket;
	}

	public void setSocket(Socket socket) {
		this.socket = socket;
	}

	public PrintWriter getOut() {
		return out;
	}

	public void setOut(PrintWriter out) {
		this.out = out;
	}

	public JPanel getPanel() {
		return panel;
	}

	public void setPanel(JPanel panel) {
		this.panel = panel;
	}

	public JButton getButton() {
		return button;
	}

	public void setButton(JButton button) {
		this.button = button;
	}

	public JTextField getTextField() {
		return textField;
	}

	public void setTextField(JTextField textField) {
		this.textField = textField;
	}

	public JTextField getTextFieldIp() {
		return textFieldIp;
	}

	public void setTextFieldIp(JTextField textFieldIp) {
		this.textFieldIp = textFieldIp;
	}

	public static JLabel getLabManifest() {
		return labManifest;
	}

	public static void setLabManifest(JLabel labManifest) {
		Client.labManifest = labManifest;
	}

	public JLabel getLabIp() {
		return labIp;
	}

	public void setLabIp(JLabel labIp) {
		this.labIp = labIp;
	}

	public static JLabel getLabIpAlert() {
		return labIpAlert;
	}

	public static void setLabIpAlert(JLabel labIpAlert) {
		Client.labIpAlert = labIpAlert;
	}
}
