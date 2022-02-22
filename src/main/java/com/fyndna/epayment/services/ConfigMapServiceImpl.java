package com.fyndna.epayment.services;

import com.fyndna.epayment.entity.ConfigMapDAO;
import com.fyndna.epayment.model.ConfigMapModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ConfigMapServiceImpl implements ConfigMapService{
    @Autowired
    ConfigMapDAO configMapDAO;

    @Override
    public ConfigMapModel getConfigMap(int configMapId) {
        return configMapDAO.findAllByConfigMapId(1);
    }
}
