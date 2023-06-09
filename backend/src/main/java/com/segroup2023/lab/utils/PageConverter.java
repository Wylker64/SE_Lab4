package com.segroup2023.lab.utils;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.ArrayList;
import java.util.List;

public class PageConverter {
    public static <T> Page<T> listToPage(List<T> list, Pageable pageable) {
        int start = (int)pageable.getOffset();
        int end = Math.min(start + pageable.getPageSize(), list.size());
        if(start > end)
            return new PageImpl<>(new ArrayList<>(), pageable, list.size());
        return new PageImpl<>(list.subList(start, end), pageable, list.size());
    }
}
