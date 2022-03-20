package com.wf.adapter.jpa.repository;

import com.wf.adapter.jpa.entity.ErrorEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ErrorRepository extends JpaRepository<ErrorEntity, String> {
}
