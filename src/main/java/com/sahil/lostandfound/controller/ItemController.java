package com.sahil.lostandfound.controller;

import com.sahil.lostandfound.entity.Item;
import com.sahil.lostandfound.repository.ClaimRepository;
import com.sahil.lostandfound.repository.CommentRepository;
import com.sahil.lostandfound.repository.ItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/items")
@CrossOrigin(origins = "*")
public class ItemController {

    @Autowired
    private ItemRepository itemRepository;

    @Autowired
    private ClaimRepository claimRepository;

    @Autowired
    private CommentRepository commentRepository;

    @GetMapping
    public List<Item> getAllItems() {
        List<Item> items = itemRepository.findAllByOrderByIdDesc();
        for (Item item : items) {
            item.setClaims(claimRepository.findByItemIdOrderByIdDesc(item.getId()));
            item.setComments(commentRepository.findByItemIdOrderByIdAsc(item.getId()));
        }
        return items;
    }

    @GetMapping("/{id}")
    public ResponseEntity<Item> getItemById(@PathVariable Long id) {
        return itemRepository.findById(id).map(item -> {
            item.setClaims(claimRepository.findByItemIdOrderByIdDesc(item.getId()));
            item.setComments(commentRepository.findByItemIdOrderByIdAsc(item.getId()));
            return ResponseEntity.ok(item);
        }).orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Item> createFoundItem(@RequestBody Item item) {
        System.out.println(">>> Saving Found Item to PostgreSQL: " + item.getTitle());
        item.setType("FOUND");
        if (item.getTimeAgo() == null || item.getTimeAgo().isBlank()) {
            item.setTimeAgo("Just now");
        }
        if (item.getImage() == null || item.getImage().isBlank()) {
            item.setImage("data:image/svg+xml;utf8,<svg xmlns='http://www.w3.org/2000/svg' viewBox='0 0 400 300'><rect width='400' height='300' fill='%23f1eadb'/><rect x='40' y='40' width='320' height='220' rx='16' fill='%23faf5e9' stroke='%2317120d' stroke-width='4'/><path d='M100 180 L160 120 L220 170 L280 110 L340 180 Z' fill='%23574b3c'/><circle cx='130' cy='90' r='25' fill='%23f1eadb'/></svg>");
        }
        Item savedItem = itemRepository.save(item);
        System.out.println(">>> Successfully Saved Item ID: " + savedItem.getId());
        return ResponseEntity.ok(savedItem);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Item> updateItem(@PathVariable Long id, @RequestBody Item updatedItem) {
        return itemRepository.findById(id).map(existing -> {
            existing.setTitle(updatedItem.getTitle());
            existing.setDescription(updatedItem.getDescription());
            existing.setLocation(updatedItem.getLocation());
            if (updatedItem.getImage() != null && !updatedItem.getImage().isBlank()) {
                existing.setImage(updatedItem.getImage());
            }
            Item saved = itemRepository.save(existing);
            return ResponseEntity.ok(saved);
        }).orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteItem(@PathVariable Long id) {
        if (itemRepository.existsById(id)) {
            itemRepository.deleteById(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}
