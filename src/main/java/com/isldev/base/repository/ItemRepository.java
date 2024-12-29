package com.isldev.base.repository;

import com.isldev.base.entity.Item;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author YISivlay
 */
public interface ItemRepository extends JpaRepository<Item, Long> {
}
