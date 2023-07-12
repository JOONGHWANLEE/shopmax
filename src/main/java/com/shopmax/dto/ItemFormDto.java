package com.shopmax.dto;

import java.util.ArrayList;
import java.util.List;

import org.modelmapper.ModelMapper;

import com.shopmax.constant.ItemSellStatus;
import com.shopmax.entity.Item;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ItemFormDto {
	private Long id;
	
	@NotBlank(message = "상품명은 필수 입력입니다.")
	private String itemNm;
	
	@NotNull(message = "가격은 필수 입력입니다.")
	private int price;
	
	@NotNull(message = "재고는 필수 입력입니다.")
	private int stockNumber;
	
	@NotBlank(message = "상품 상세 설명은 필수 입력입니다.")
	private String itemDetail;
	
	private ItemSellStatus itemSellStatus;
	
	//상품 이미지 정보를 저장
	private List<ItemImgDto> itemImgDtoList = new ArrayList<>();
	
	//상품 이미지 아이디들을 저장 -> 수정시에 이미지 아이디들을 담아둘 용도
	private List<Long> itemImgIds = new ArrayList<>();
	
	private static ModelMapper modelMapper = new ModelMapper();
	
	//dto -> entity로 바꿈
	public Item createItem() {
		return modelMapper.map(this, Item.class);
	}
	
	//entity -> dto로 바꿈
	public static ItemFormDto of(Item item) {
		return modelMapper.map(item, ItemFormDto.class);
	}
}
