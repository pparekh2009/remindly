package com.priyanshparekh.remindly.dto;

import com.priyanshparekh.remindly.entity.Priority;

public interface PriorityProjection {

    Long getCount();
    Priority getPriority();

}
