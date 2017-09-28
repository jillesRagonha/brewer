package com.algaworks.brewer.repository;

import com.algaworks.brewer.models.Estado;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface Estados extends JpaRepository<Estado, Long>{
}
