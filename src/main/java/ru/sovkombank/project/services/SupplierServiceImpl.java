package ru.sovkombank.project.services;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.sovkombank.project.entities.Product;
import ru.sovkombank.project.entities.Supplier;
import ru.sovkombank.project.repositories.SupplierRepository;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class SupplierServiceImpl implements SupplierService {
    private final SupplierRepository supplierRepository;

    @Autowired
    public SupplierServiceImpl(SupplierRepository supplierRepository) {
        this.supplierRepository = supplierRepository;
    }

    @Override
    @Transactional
    public void createSupplier(Supplier supplier) {
        log.info("Сохраняем поставщика");
        supplierRepository.save(supplier);
    }

    @Override
    @Transactional
    public void updateSupplier(Supplier supplier) {
        log.info("Обновляем поставщика с id {}", supplier.getId());
        supplierRepository.save(supplier);
    }

    @Override
    @Transactional
    public void deleteSupplier(Long supplierId) {
        log.info("Удаляем поставщика с id {}", supplierId);
        Optional<Supplier> supplier = supplierRepository.findById(supplierId);
        if (supplier.isPresent()) {
            Supplier supplierToDelete = supplier.get();
            List<Product> productsWithSupplier = supplierToDelete.getProducts();

            if (productsWithSupplier != null && !productsWithSupplier.isEmpty()) {
                log.warn("В магазине есть товары этого поставщика, удаление невозможно");
                return;
            }

            supplierRepository.delete(supplierToDelete);
            log.info("Поставщик с id {} успешно удален", supplierId);
        }
    }

    @Override
    public List<Supplier> getAllSuppliers() {
        log.info("Получаем список всех поставщиков");
        return supplierRepository.findAll();
    }
}
