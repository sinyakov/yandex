package com.sinyakov.yandex.domain;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.bson.json.Converter;
import org.springframework.data.annotation.Id;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.Date;

@AllArgsConstructor
@Getter
@Setter
public class Item {
    @Id
    private String id;
    private String name;
    private String parentId;
    private Integer price;
    private String type;
    private String date;
}
