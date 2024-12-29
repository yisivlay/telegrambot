package com.isldev.base.controller;

import com.isldev.base.entity.SaleRequest;
import com.isldev.base.repository.SaleRepository;
import com.isldev.base.service.SaleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author YISivlay
 */
@RestController
@RequestMapping("/saleitems")
public class SaleItemApiResource {

    private final SaleService service;

    @Autowired
    public SaleItemApiResource(SaleService service) {
        this.service = service;
    }

    @PostMapping
    public String sellItem(@RequestBody SaleRequest request) {
        return service.sellItem(request);
    }
}
