package com.shopmax.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.shopmax.dto.ItemFormDto;
import com.shopmax.dto.ItemImgDto;
import com.shopmax.dto.ItemRankDto;
import com.shopmax.dto.ItemSearchDto;
import com.shopmax.dto.MainItemDto;
import com.shopmax.entity.Item;
import com.shopmax.entity.ItemImg;
import com.shopmax.repository.ItemImgRepository;
import com.shopmax.repository.ItemRepository;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class ItemService {
	private final ItemRepository itemRepository;
	private final ItemImgService itemImgService;
	private final ItemImgRepository itemImgRepository;
	
	//item 테이블에 상품등록(insert)
	public Long saveItem(ItemFormDto itemFormDto, List<MultipartFile> itemImgFileList) throws Exception {
		
		//1.상품등록
		Item item = itemFormDto.createItem(); //dto -> entity
		itemRepository.save(item); //insert(저장)
		
		//2.이미지등록
		for(int i=0; i<itemImgFileList.size(); i++) {
			//fk키를 사용 시 부모테이블에 해당하는 entity를 먼저 넣어줘야 한다.
			ItemImg itemImg = new ItemImg();
			itemImg.setItem(item);
			
			//첫번째 이미지일 때 대표상품 이미지 지정
			if(i == 0) {
				itemImg.setRepimgYn("Y");
			} else {
				itemImg.setRepimgYn("N");
			}
			
			itemImgService.saveItemImg(itemImg, itemImgFileList.get(i));
		}
		
		return item.getId(); //등록한 상품 id를 리턴
	}
	
	//상품 가져오기
	@Transactional(readOnly = true) //트랜잭션 읽기 전용(변경감지 수행하지 않음) -> 성능향상
	public ItemFormDto getItemDtl(Long itemId) {
		//1. item_img 테이블의 이미지를 가져온다.
		List<ItemImg> itemImgList = itemImgRepository.findByItemIdOrderByIdAsc(itemId);
		
		//ItemImg 엔티티 객체 -> ItemImgDto로 변환
		List<ItemImgDto> itemImgDtoList = new ArrayList<>();
		for(ItemImg itemImg : itemImgList) {
			ItemImgDto itemImgDto = ItemImgDto.of(itemImg);
			itemImgDtoList.add(itemImgDto);
		}
		
		//2. item 테이블에 있는 데이터를 가져온다.
		Item item = itemRepository.findById(itemId)
								  .orElseThrow(EntityNotFoundException::new);
		
		//Item 엔티티 객체 -> dto로 변환
		ItemFormDto itemFormDto = ItemFormDto.of(item);
		
		//3. ItemFormDto에 이미지 정보(itemImgDtoList)를 넣어준다.
		itemFormDto.setItemImgDtoList(itemImgDtoList);
		
		return itemFormDto;
		
	}
	
	//상품 수정하기
	public Long updateItem(ItemFormDto itemFormDto,
			List<MultipartFile> itemImgFileList) throws Exception{
		
		//1. item 엔티티 가져와서 바꾼다.
		Item item = itemRepository.findById(itemFormDto.getId())
								  .orElseThrow(EntityNotFoundException::new);
		
		//update쿼리문 실행
		// ★ 한번 insert를 진행하면 엔티티가 영속성 컨텍스트에 저장이 되므로
		//그 이후에는 변경감지 기능이 동작하기 때문에 개발자는 update 쿼리문을 쓰지 않고 엔티티만 변경해주면 된다.
		item.updateItem(itemFormDto);
		
		//2. item_img를 바꿔준다. -> 5개의 레코드 전부 변경
		List<Long> itemImgIds = itemFormDto.getItemImgIds(); //상품 이미지 아이디 리스트 조회
		
		for(int i = 0; i < itemImgFileList.size(); i++) {
			itemImgService.updateItemImg(itemImgIds.get(i), itemImgFileList.get(i));
		}
		
		return item.getId(); //변경한 item의 id 리턴
	}
	
	@Transactional(readOnly = true)
	public Page<Item> getAdminItemPage(ItemSearchDto itemSearchDto, Pageable pageable) {
		Page<Item> itemPage = itemRepository.getAdminItemPage(itemSearchDto, pageable);
		return itemPage;
	}
	
	public Page<MainItemDto> getMainItemPage(ItemSearchDto itemSearchDto, Pageable pageable){
		Page<MainItemDto> itemDto = itemRepository.getMainItemPage(itemSearchDto, pageable);
		return itemDto;
	}
	
	@Transactional(readOnly = true)
	public List<ItemRankDto> getItemRankList(){
		return itemRepository.getItemRankList();
	}
	
	
	
}
