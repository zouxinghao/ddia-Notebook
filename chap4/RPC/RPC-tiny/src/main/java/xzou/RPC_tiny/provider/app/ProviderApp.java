package xzou.RPC_tiny.provider.app;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import xzou.RPC_tiny.provider.service.Calculator;
import xzou.RPC_tiny.provider.service.CalculatorImpl;
import xzou.RPC_tiny.request.CalculateRpcRequest;

public class ProviderApp {
	private static Logger log = LoggerFactory.getLogger(ProviderApp.class);
	
	private Calculator calculator = new CalculatorImpl();
	
	public static void main(String[] args) throws IOException {
		new ProviderApp().run();
	} 
	
	private void run() throws IOException {
		ServerSocket listerner = new ServerSocket(9090);
		try {
			while(true) {
				Socket socket = listerner.accept();
				try {
					/**
					 * in RFC, 2-binary value is used to stranfer the data
					 * 
					 * So, the import in RFC is decode and encode the object so that
					 * it can be transferred through the socket
					 */
					ObjectInputStream objectInputStream = new ObjectInputStream(socket.getInputStream());
					Object object = objectInputStream.readObject();
					
					log.info("request {}", object);
					
					// call the service
					int result = 0;
					if(object instanceof CalculateRpcRequest) {
						CalculateRpcRequest calculateRpcRequest = (CalculateRpcRequest) object;
						if(calculateRpcRequest.getMethod().equals("add")) {
							result = calculator.add(calculateRpcRequest.getA(), calculateRpcRequest.getB());
						} else {
							throw new UnsupportedOperationException();
						}
					}
					
					ObjectOutputStream objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
					objectOutputStream.writeObject(new Integer(result));
				} catch (Exception e) {
					log.error("fail", e);
				} finally {
					socket.close();
				}
			}
		} finally {
			listerner.close();
		}
	}
}
