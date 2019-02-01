package xzou.RPC_tiny.consumer.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import xzou.RPC_tiny.provider.service.Calculator;

public class CalculatorRemoteImpl implements Calculator{

	public static final int PORT = 9090;
	private static Logger log = LoggerFactory.getLogger(CalculatorRemoteImpl.class);
	
	
	public int add(int a, int b) {
		// TODO Auto-generated method stub
		return 0;
	}
	
}
