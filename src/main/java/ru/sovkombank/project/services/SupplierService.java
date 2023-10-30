package ru.sovkombank.project.services;

import ru.sovkombank.project.entities.Supplier;

import java.util.List;

public interface SupplierService {
    /**
     * Создает поставщика товаров
     *
     * @param supplier поставщик товаров
     */
    void createSupplier(Supplier supplier);

    /**
     * Обновляет информацию о поставщике
     *
     * @param supplier поставщик товаров
     */
    void updateSupplier(Supplier supplier);

    /**
     * Удаляет поставщика товаров по его id
     *
     * @param supplierId уникальный идентификатор поставщика
     */
    void deleteSupplier(Long supplierId);

    /**
     * Получает всех поставщиков товаров
     *
     * @return список поставщиков товаров
     */
    List<Supplier> getAllSuppliers();
}
