package socket;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Server.java
 * 
 * @Author : YLD10
 * @EditTime : 2017/6/3 15:07
 **/
public class Server {
	private static int port = 30000; // 监听的端口
	private Socket socket = null; // socket 连接的载体
	private ServerSocket serversocket = null; // 监听端口，等待连接的一个服务对象
	private BufferedReader buffer = null; // 输入流对象

	public Server() {

		// Run();
		new Thread() {
			public void run() {
				Run();
			};
		}.start();
	}

	public void Run() {

		try {
			// serversocket = new ServerSocket(port);
			setServersocket(new ServerSocket(port)); // 开始监听
			System.out.println("服务端启动成功！");
			while (true) {
				try {
					// socket = serversocket.accept();
					setSocket(getServersocket().accept()); // 等待连接的到来
					System.out.println("服务端监听到连接！");
					// 获取输入流
					setBuffer(new BufferedReader(new InputStreamReader(getSocket().getInputStream(), "UTF-8")));
					// 从输入流中读取一行数据
					String msg = getBuffer().readLine();
					getBuffer().close(); // 关闭输入流
					getSocket().close(); // 关闭连接
					System.out.println("消息接收完成！");

					Client.getLabManifest().setText(msg);
				} catch (IOException e) {
					// e.printStackTrace();
					System.out.println("服务端连接失败！");
				}
			}
		} catch (IOException e) {
			// e.printStackTrace();
			System.out.println("服务端启动失败！");
		} finally {
			try {
				if (getServersocket() != null) {
					getServersocket().close(); // 关闭监听的对象
					System.out.println("成功关闭服务器！");
				}
			} catch (IOException e) {
				// e.printStackTrace();
				System.out.println("关闭服务器失败！");
			}
		}
	}

	public Socket getSocket() {
		return socket;
	}

	public void setSocket(Socket socket) {
		this.socket = socket;
	}

	public ServerSocket getServersocket() {
		return serversocket;
	}

	public void setServersocket(ServerSocket serversocket) {
		this.serversocket = serversocket;
	}

	public BufferedReader getBuffer() {
		return buffer;
	}

	public void setBuffer(BufferedReader buffer) {
		this.buffer = buffer;
	}
}
