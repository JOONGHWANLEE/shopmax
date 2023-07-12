package com.shopmax.dto;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import com.shopmax.constant.OrderStatus;
import com.shopmax.entity.Order;

import lombok.*;

@Getter
@Setter
public class OrderHistDto {
	// entity -> dto로 변환
		public OrderHistDto(Order order) {
			this.orderId = order.getId();
			this.orderDate = order.getOrderDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
			this.orderStatus = order.getOrderStatus();
		}
		
		private Long orderId;	// 주문아이디
		private String orderDate;	// 주문날짜
		private OrderStatus orderStatus;	// 주문상태
		private List<OrderItemDto> orderItemDtoList = new ArrayList<>();
		
		// orderItemDto객체를 주문상품 리스트에 추가하는 메소드
		public void addOrderItemDto(OrderItemDto orderItemDto) {
			this.orderItemDtoList.add(orderItemDto);
		}
}
