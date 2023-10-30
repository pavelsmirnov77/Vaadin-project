package ru.sovkombank.project.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.sovkombank.project.entities.Category;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {
}
