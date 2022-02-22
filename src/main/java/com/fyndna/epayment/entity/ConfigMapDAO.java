package com.fyndna.epayment.entity;

import com.fyndna.epayment.model.ConfigMapModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

public interface ConfigMapDAO extends JpaRepository<ConfigMapModel,Integer> {
    public ConfigMapModel findAllByConfigMapId(int configMapId);
}
