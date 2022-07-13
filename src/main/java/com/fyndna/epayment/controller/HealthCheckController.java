package com.fyndna.epayment.controller;

import com.fyndna.epayment.services.EpaymentHealthCheckService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDate;
import java.time.LocalDateTime;

@RestController
@RequestMapping("/health")
public class HealthCheckController {


    private final EpaymentHealthCheckService epaymentHealthCheckService;
    private static final Logger LOGGER = LoggerFactory.getLogger(HealthCheckController.class.getName());

    public HealthCheckController(EpaymentHealthCheckService epaymentHealthCheckService) {
        this.epaymentHealthCheckService = epaymentHealthCheckService;
    }


    @GetMapping(path = "/health-check")
    public ResponseEntity<String> getHealth(HttpServletRequest httpServletRequest){
        LocalDateTime localDateTime = LocalDateTime.now();
        LOGGER.info("Request Time is "+localDateTime.toString()+" Request client ip address is "+httpServletRequest.getLocalAddr().toString());
       if(!epaymentHealthCheckService.isHealthy()){
           LOGGER.info("Pod is in Unready State");
           return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
       }
        LOGGER.info("Pod is in ready State");
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/pre-stop")
    public void makeUnhealthyState(){
        epaymentHealthCheckService.unhealthy();
    }
}
