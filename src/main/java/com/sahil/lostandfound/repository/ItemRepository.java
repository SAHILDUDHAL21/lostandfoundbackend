package com.sahil.lostandfound.repository;

import com.sahil.lostandfound.entity.Item;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ItemRepository extends JpaRepository<Item, Long> {
    List<Item> findAllByOrderByIdDesc();
    List<Item> findByTypeOrderByIdDesc(String type);
}

