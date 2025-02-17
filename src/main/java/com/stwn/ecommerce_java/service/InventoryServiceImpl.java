package com.stwn.ecommerce_java.service;

import com.stwn.ecommerce_java.common.errors.InventoryException;
import com.stwn.ecommerce_java.entity.Product;
import com.stwn.ecommerce_java.repository.ProductRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class InventoryServiceImpl implements InventoryService {
    private final ProductRepository productRepository;
    @Override
    @Transactional
    public boolean checkAndLockInventory(Map<Long, Integer> productQuantities) {
        for(Map.Entry<Long, Integer> entry : productQuantities.entrySet()){
            Product product = productRepository.findByIdPessimisticLock(entry.getKey())
                    .orElseThrow(()-> new InventoryException("product with id entry "
                            + entry.getKey() + "not found"));
            if(product.getStockQuantity() < entry.getValue()){
                return false;
            }
        }
        return true;
    }

    @Override
    @Transactional
    public void decreaseQuantity(Map<Long, Integer> productQuantities) {
        for(Map.Entry<Long, Integer> entry : productQuantities.entrySet()){
            Product product = productRepository.findByIdPessimisticLock(entry.getKey())
                    .orElseThrow(()-> new InventoryException("product with id entry "
                            + entry.getKey() + "not found"));
            if(product.getStockQuantity() < entry.getValue()){
                throw new InventoryException("insufficient inventory for product " + entry.getKey());
            }

            Integer newQuantity = product.getStockQuantity() - entry.getValue();
            product.setStockQuantity(newQuantity);
            productRepository.save(product);
        }
    }

    @Override
    @Transactional
    public void increaseQuantity(Map<Long, Integer> productQuantities) {
        for(Map.Entry<Long, Integer> entry : productQuantities.entrySet()){
            Product product = productRepository.findByIdPessimisticLock(entry.getKey())
                    .orElseThrow(()-> new InventoryException("product with id entry "
                            + entry.getKey() + "not found"));
            Integer newQuantity = product.getStockQuantity() + entry.getValue();
            product.setStockQuantity(newQuantity);
            productRepository.save(product);
        }
    }
}
