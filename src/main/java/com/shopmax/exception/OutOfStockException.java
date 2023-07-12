package com.shopmax.exception;

public class OutOfStockException extends RuntimeException{
	
	//상품 주문 수량보다 재고가 적으면 발생되는 exception
	public OutOfStockException(String message) {
		super(message);
	}
}
