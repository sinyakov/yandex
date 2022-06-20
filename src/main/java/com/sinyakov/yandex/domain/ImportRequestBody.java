package com.sinyakov.yandex.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

import java.time.ZonedDateTime;
import java.util.List;

@AllArgsConstructor
@Getter
@ToString
public class ImportRequestBody {
    private List<RequestItem> items;
    private String updateDate;
}
