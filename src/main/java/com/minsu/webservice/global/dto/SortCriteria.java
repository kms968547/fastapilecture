package com.minsu.webservice.global.dto;

import org.springframework.data.domain.Sort;

public class SortCriteria {
    private String sortBy;
    private Sort.Direction sortDirection;

    public SortCriteria(String sort) {
        if (sort == null || sort.trim().isEmpty()) {
            this.sortBy = null;
            this.sortDirection = Sort.Direction.ASC; // Default to ASC if no sort provided
            return;
        }

        String[] parts = sort.split(",");
        this.sortBy = parts[0].trim();
        if (parts.length > 1) {
            String direction = parts[1].trim().toUpperCase();
            try {
                this.sortDirection = Sort.Direction.valueOf(direction);
            } catch (IllegalArgumentException e) {
                // Default to ASC if an invalid direction is provided
                this.sortDirection = Sort.Direction.ASC;
            }
        } else {
            this.sortDirection = Sort.Direction.ASC; // Default to ASC
        }
    }

    public Sort toSort() {
        if (sortBy == null || sortBy.isEmpty()) {
            return Sort.unsorted();
        }
        return Sort.by(sortDirection, sortBy);
    }

    // Getters if needed
    public String getSortBy() {
        return sortBy;
    }

    public Sort.Direction getSortDirection() {
        return sortDirection;
    }
}
