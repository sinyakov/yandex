package com.sinyakov.yandex.controller;

import com.sinyakov.yandex.domain.ImportRequestBody;
import com.sinyakov.yandex.domain.ResponseItem;
import com.sinyakov.yandex.service.ItemService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

@AllArgsConstructor
@RestController
public class ItemController {
    private ItemService itemService;

    @PostMapping("/imports")
    public void importItems(@RequestBody ImportRequestBody body) {
        itemService.importItems(body);
    }

    @GetMapping("/nodes/{id}")
    public ResponseItem getNodes(@PathVariable String id) {
        return itemService.getNodes(id);
    }

    @DeleteMapping("/delete/{id}")
    public void deleteNode(@PathVariable String id) {
        itemService.deleteNode(id);
    }
}
