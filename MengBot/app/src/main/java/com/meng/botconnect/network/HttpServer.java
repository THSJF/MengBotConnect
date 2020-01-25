package com.meng.botconnect.network;

import com.meng.botconnect.*;
import com.meng.botconnect.bean.*;
import java.io.*;
import java.net.*;
import com.meng.botconnect.lib.*;

public class HttpServer implements Runnable {

    @Override
    public void run() {
        try {
            ServerSocket serverSocket = new ServerSocket(7778);
			while (true) {
                new SocketConfigRunnable(serverSocket).run();
                Thread.sleep(10);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

	class SocketConfigRunnable {
		private Socket socket = null;
		private String recStr=null;
		private String sendStr=null;
		public SocketConfigRunnable(ServerSocket serverSocket) {
			try {
				this.socket = serverSocket.accept();
			} catch (IOException e) {
				e.printStackTrace();
				System.out.println("SocketConfigManager init failed");
			}
			InetAddress address = socket.getInetAddress();
			System.out.println("当前客户端的IP ： " + address.getHostAddress());
		}

		public void run() {
			try {
				MainActivity2.instence.threadPool.execute(new Runnable(){

						@Override
						public void run() {
							while (!socket.isClosed()) {
								try {
									InputStream inputStream = socket.getInputStream();
									DataInputStream dataInputStream = new DataInputStream(inputStream);
									recStr = dataInputStream.readUTF();
									if (recStr != null) {
										// string = new String(Base64.decryptBASE64(string), "utf-8");
										System.out.println("服务器读取客户端的：" + recStr);
									}
								} catch (IOException e) {
									LogTool.e(MainActivity2.instence, e);
								}
								try {
									Thread.sleep(10);
								} catch (InterruptedException e) {
									LogTool.e(MainActivity2.instence, e);
								}
							}
						}
					});

				MainActivity2.instence.threadPool.execute(new Runnable(){

						@Override
						public void run() {
							while (!socket.isClosed()) {
								if (sendStr != null) {
									try {
										OutputStream outputStream = socket.getOutputStream();
										DataOutputStream dataOutputStream = new DataOutputStream(outputStream);
										dataOutputStream.writeUTF(sendStr);
										if (sendStr != null) {
											// string = new String(Base64.decryptBASE64(string), "utf-8");
											System.out.println("发送：" + sendStr);
										}
										Thread.sleep(10);
									} catch (Exception e) {
										LogTool.e(MainActivity2.instence, e);
									}
								}
							}
						}
					});
				socket.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		private String processText(String string) {

			return null;	
		}
	}
}
