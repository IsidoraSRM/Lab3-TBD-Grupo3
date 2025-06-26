package com.Docdelivery.Backend.Service;

import com.Docdelivery.Backend.Entity.OrderSummaryEntity;
import com.Docdelivery.Backend.Repository.OrderSummaryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class OrderSummaryService {

    private final OrderSummaryRepository orderSummaryRepository;

    @Autowired
    public OrderSummaryService(OrderSummaryRepository orderSummaryRepository) {
        this.orderSummaryRepository = orderSummaryRepository;
    }

    // Create
    public void createOrderSummary(OrderSummaryEntity summary) {
        orderSummaryRepository.save(summary);
    }

    // Read All
    public List<OrderSummaryEntity> getOrderSummaries() {
        return orderSummaryRepository.findAll();
    }

    // Read by ID
    public Optional<OrderSummaryEntity> getOrderSummaryById(Long clienteId) {
        return orderSummaryRepository.findById(clienteId);
    }

    // Update
    public boolean updateOrderSummary(OrderSummaryEntity summary) {
        return orderSummaryRepository.update(summary);
    }

    // Delete
    public boolean deleteOrderSummary(Long clienteId) {
        return orderSummaryRepository.deleteById(clienteId);
    }
}