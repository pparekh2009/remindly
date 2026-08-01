package com.priyanshparekh.remindly.dto;

import lombok.Builder;

@Builder
public record TodoResult(
        String title,
        String dueAt,
        String category,
        String priority
) {

}
