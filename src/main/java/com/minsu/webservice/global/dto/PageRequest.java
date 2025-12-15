package com.minsu.webservice.global.dto;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

public class PageRequest {
    private int page;
    private int size;

    private static final int DEFAULT_PAGE = 0;
    private static final int DEFAULT_SIZE = 10;
    private static final int MAX_SIZE = 100;

    public PageRequest(Integer page, Integer size) {
        this.page = (page == null || page < 0) ? DEFAULT_PAGE : page;
        
        if (size == null || size <= 0) {
            this.size = DEFAULT_SIZE;
        } else if (size > MAX_SIZE) {
            this.size = MAX_SIZE;
        } else {
            this.size = size;
        }
    }

    public Pageable toPageable(Sort sort) {
        return org.springframework.data.domain.PageRequest.of(page, size, sort);
    }

    public Pageable toPageable() {
        return org.springframework.data.domain.PageRequest.of(page, size);
    }

    // Getters for page, size if needed
    public int getPage() {
        return page;
    }

    public int getSize() {
        return size;
    }
}
