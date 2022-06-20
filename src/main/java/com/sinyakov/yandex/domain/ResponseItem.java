package com.sinyakov.yandex.domain;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.ZonedDateTime;
import java.util.List;

@AllArgsConstructor
@Getter
@Setter
public class ResponseItem {
    private String id;
    private String name;
    private String parentId;
    private Integer price;
    private String type;
    private String date;
    private List<ResponseItem> children;
}
