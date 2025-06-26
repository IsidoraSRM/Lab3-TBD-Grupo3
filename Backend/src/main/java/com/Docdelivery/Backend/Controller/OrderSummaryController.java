package com.Docdelivery.Backend.Controller;

import com.Docdelivery.Backend.Entity.OrderSummaryEntity;
import com.Docdelivery.Backend.Service.OrderSummaryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/order-summaries")
@CrossOrigin(origins = "*")
public class OrderSummaryController {

    private final OrderSummaryService orderSummaryService;

    @Autowired
    public OrderSummaryController(OrderSummaryService orderSummaryService) {
        this.orderSummaryService = orderSummaryService;
    }

    // Create
    @PostMapping
    @Secured("ROLE_ADMIN")
    public ResponseEntity<Void> createOrderSummary(@RequestBody OrderSummaryEntity summary) {
        try {
            orderSummaryService.createOrderSummary(summary);
            return new ResponseEntity<>(HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Read All
    @GetMapping("/all")
    @Secured({"ROLE_ADMIN"})
    public ResponseEntity<List<OrderSummaryEntity>> getAllOrderSummaries() {
        List<OrderSummaryEntity> summaries = orderSummaryService.getOrderSummaries();
        return ResponseEntity.ok(summaries);
    }

    // Read by ID
    @GetMapping("/{clienteId}")
    @Secured({"ROLE_ADMIN", "ROLE_CLIENTE"})
    public ResponseEntity<OrderSummaryEntity> getOrderSummaryById(@PathVariable Long clienteId) {
        return orderSummaryService.getOrderSummaryById(clienteId)
                .map(ResponseEntity::ok)
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    // Update
    @PutMapping("/{clienteId}")
    @Secured("ROLE_ADMIN")
    public ResponseEntity<Void> updateOrderSummary(
            @PathVariable Long clienteId,
            @RequestBody OrderSummaryEntity summary) {
        summary.setClienteId(clienteId);
        boolean updated = orderSummaryService.updateOrderSummary(summary);
        if (updated) {
            return new ResponseEntity<>(HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    // Delete
    @DeleteMapping("/{clienteId}")
    @Secured("ROLE_ADMIN")
    public ResponseEntity<Void> deleteOrderSummary(@PathVariable Long clienteId) {
        boolean deleted = orderSummaryService.deleteOrderSummary(clienteId);
        if (deleted) {
            return new ResponseEntity<>(HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
}