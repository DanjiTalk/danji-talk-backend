package com.danjitalk.danjitalk.openapi.danjilist.dto;

import java.util.List;
import lombok.Getter;

@Getter
public class Body {

    private List<Item> items;
    private int numOfRows;
    private int pageNo;
    private int totalCount;
}