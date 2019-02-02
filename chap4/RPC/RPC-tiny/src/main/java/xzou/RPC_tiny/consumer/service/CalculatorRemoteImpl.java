package xzou.RPC_tiny.consumer.service;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import xzou.RPC_tiny.provider.service.Calculator;
import xzou.RPC_tiny.request.CalculateRpcRequest;

public class CalculatorRemoteImpl implements Calculator{

	public static final int PORT = 9090;
	private static Logger log = LoggerFactory.getLogger(CalculatorRemoteImpl.class);
	
	
	public int add(int a, int b) {
		// TODO Auto-generated method stub
		List<String> addressList = lookupProviders("Calculator.add");
		String address = chooseTarget(addressList);
		try {
			Socket socket = new Socket(address, PORT);
			
			CalculateRpcRequest calculateRpcRequest = generateRequest(a, b);
			ObjectOutputStream objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
			
			objectOutputStream.writeObject(calculateRpcRequest);
			
			ObjectInputStream objectInputStream = new ObjectInputStream(socket.getInputStream());
			Object  response = objectInputStream.readObject();
			
			log.info("response is {}", response);
			
			if(response instanceof Integer) {
				return (Integer) response;
			} else {
				throw new InternalError();
			} 
		} catch (Exception e) {
            log.error("fail", e);
            throw new InternalError();
        }
	}

	private CalculateRpcRequest generateRequest(int a, int b) {
		// TODO Auto-generated method stub
		CalculateRpcRequest calculateRpcRequest = new CalculateRpcRequest();
		calculateRpcRequest.setA(a);
		calculateRpcRequest.setB(b);
		calculateRpcRequest.setMethod("add");
		return calculateRpcRequest;
	}

	private String chooseTarget(List<String> addressList) {
		// TODO Auto-generated method stub
		if(addressList == null || addressList.size() == 0) {
			throw new IllegalArgumentException();
		}
		return addressList.get(0);
	}

	private List<String> lookupProviders(String name) {
		// TODO Auto-generated method stub
		List<String> strings = new ArrayList();
		strings.add("127.0.0.1");
		return strings;
	}
	
}
