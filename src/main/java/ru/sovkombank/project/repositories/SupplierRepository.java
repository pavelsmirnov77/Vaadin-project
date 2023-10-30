package ru.sovkombank.project.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.sovkombank.project.entities.Supplier;

@Repository
public interface SupplierRepository extends JpaRepository<Supplier, Long> {
}
