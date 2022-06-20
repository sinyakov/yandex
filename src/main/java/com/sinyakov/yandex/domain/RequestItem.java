package com.sinyakov.yandex.domain;


import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class RequestItem {
    private String id;
    private String name;
    private String parentId;
    private Integer price;
    private String type;
}
