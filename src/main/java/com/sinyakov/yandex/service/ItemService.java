package com.sinyakov.yandex.service;

import com.mongodb.client.result.UpdateResult;
import com.sinyakov.yandex.domain.ImportRequestBody;
import com.sinyakov.yandex.domain.Item;
import com.sinyakov.yandex.domain.ResponseItem;
import com.sinyakov.yandex.repository.ItemRepository;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class ItemService {

    static @AllArgsConstructor
    @Getter class CategoryInfo {
        private int sum;
        private int amount;
    }

    ItemRepository itemRepository;
    final private MongoTemplate mongoTemplate;


    public void importItems(ImportRequestBody body) {
        String dateTime = body.getUpdateDate();

        List<Item> items = body.getItems()
                .stream()
                .map(requestItem -> new Item(
                        requestItem.getId(),
                        requestItem.getName(),
                        requestItem.getParentId(),
                        requestItem.getPrice(),
                        requestItem.getType(),
                        dateTime
                ))
                .toList();

        itemRepository.insert(items);

        items.forEach(item -> updateParentDate(item.getParentId(), dateTime));
    }

    private void updateParentDate(String parentId, String date) {
        if (parentId == null) {
            return;
        }

//        Query query = new Query(Criteria.where("id").is(parentId));
//        Update update = new Update();
//        update.set("date", date);
//        UpdateResult updateResult = mongoTemplate.updateFirst(query, update, Item.class);


        Optional<Item> parent = itemRepository.findById(parentId);
        if (parent.isEmpty()) {
            return;
        }

        Item parentItem = parent.get();
        parentItem.setDate(date);
        itemRepository.save(parentItem);

        updateParentDate(parentItem.getParentId(), date);

    }

    public void deleteNode(String id) {
        List<Item> children = itemRepository.findByParentId(id);
        children.forEach(child -> deleteNode(child.getId()));

        itemRepository.deleteById(id);
    }

    public ResponseItem getNodes(String id) {
        ResponseItem nodes = fetchNodes(id);
        enrichResponseItemTreeWithPrice(nodes);
        return nodes;
    }

    private ResponseItem fetchNodes(String id) {
        Optional<Item> node = itemRepository.findById(id);

        if (node.isEmpty()) {
            return null;
        }

        Item item = node.get();

        ResponseItem responseItem = new ResponseItem(
                item.getId(),
                item.getName(),
                item.getParentId(),
                item.getPrice(),
                item.getType(),
                item.getDate(),
                null
        );

        List<Item> children = itemRepository.findByParentId(id);
        List<ResponseItem> childrenNodes = children
                .stream()
                .map(child -> fetchNodes(child.getId()))
                .toList();

        if (!childrenNodes.isEmpty()) {
            responseItem.setChildren(childrenNodes);
        }

        return responseItem;
    }

    private CategoryInfo enrichResponseItemTreeWithPrice(ResponseItem responseItem) {
        if ("OFFER".equals(responseItem.getType())) {
            return new CategoryInfo(responseItem.getPrice(), 1);
        }

        List<ResponseItem> children = responseItem.getChildren();

        if (children == null) {
            return new CategoryInfo(0, 0);
        }

        List<CategoryInfo> infos = children.stream().map(this::enrichResponseItemTreeWithPrice).toList();

        int sum = infos.stream().mapToInt(CategoryInfo::getSum).sum();
        int amount = infos.stream().mapToInt(CategoryInfo::getAmount).sum();

        responseItem.setPrice(sum / amount);


        return new CategoryInfo(sum, amount);
    }
}
