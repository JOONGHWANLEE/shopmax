package com.shopmax.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.shopmax.entity.ItemImg;

public interface ItemImgRepository extends JpaRepository<ItemImg, Long>{
	
	//select * from item_img where item_id = ? order by item_id asc;
	List<ItemImg> findByItemIdOrderByIdAsc(Long itemId);
	
	//select * from item_img where item_id = ? and repimg_yn = ?
	ItemImg findByItemIdAndRepimgYn(Long itemId, String repimgYn);
}
