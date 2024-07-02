package com.api_polleria.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.function.Function;

@Service
public class UtilsService {

    public <T, U> ResponseEntity<Page<U>> createPageResponse(List<T> list, Pageable pageable, Function<T, U> converter) {
        int start = (int) pageable.getOffset();
        int end = Math.min((start + pageable.getPageSize()), list.size());
        List<T> subList = list.subList(start, end);
        Page<T> page = new PageImpl<>(subList, PageRequest.of(pageable.getPageNumber(), pageable.getPageSize()), list.size());
        Page<U> dtoPage = page.map(converter);
        return ResponseEntity.status(HttpStatus.OK).body(dtoPage);
    }
}
