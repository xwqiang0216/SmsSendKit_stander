package com.hskj.threads.send;

import java.io.IOException;
import java.net.ServerSocket;

public class KeepSingleThread extends Thread {
	private ServerSocket socket;

	public KeepSingleThread(ServerSocket socket) {
		this.socket = socket;
	}

	public void run() {
		try {
			socket.accept();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
