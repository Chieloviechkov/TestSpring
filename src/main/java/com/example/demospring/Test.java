package com.example.demospring;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.persistence.EntityManager;
import java.util.List;

@RestController
public class Test {
    @GetMapping("/test")
    public String showMessage() {
        String message = "Привет, это сообщение от класса TestController!";
        return message;
    }
    @GetMapping("/transactions")
    public ResponseEntity<List<Transactions>> viewTransactions() {
        EntityManager em = Main.createEntityManager();
        List<Transactions> transactions = Main.viewOperation(em);
        em.close();

        return ResponseEntity.ok(transactions);
    }
    @GetMapping("/total-balance")
    public ResponseEntity<Double> getTotalBalanceForAllCustomers() {
        EntityManager em = Main.createEntityManager();
        double totalBalance = Main.calculateTotalBalanceForAllCustomersInUan(em);
        em.close();
        return ResponseEntity.ok(totalBalance);
    }
}

