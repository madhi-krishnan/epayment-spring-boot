package com.fyndna.epayment.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class EpaymentHealthCheckService implements IEpaymentHealthCheckService{

    private boolean isHealthy=true;
    private static final Logger LOGGER = LoggerFactory.getLogger(EpaymentHealthCheckService.class.getName());

    public boolean isHealthy() {
        return isHealthy;
    }

    public void setHealthy(boolean healthy) {
        isHealthy = healthy;
    }

    @Override
    public void unhealthy() {
        setHealthy(false);
        try{
            Thread.sleep(1000L);
        }catch (InterruptedException e){
            Thread.currentThread().interrupt();
        }
    }
}
