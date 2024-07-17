package org.example.fourtreesproject.gpbuy.service;


import lombok.RequiredArgsConstructor;
import org.example.fourtreesproject.gpbuy.model.request.GpbuyCreateRequest;
import org.example.fourtreesproject.gpbuy.repository.GpbuyRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GpbuyService {

    private final GpbuyRepository gpbuyRepository;

    public boolean save(GpbuyCreateRequest request) {
    }
}
