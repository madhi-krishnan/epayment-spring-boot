package com.fyndna.epayment;

import com.fyndna.epayment.model.PaymentModel;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@RestController
public class EPaymentController {

    private static List<PaymentModel> paymentModels = new ArrayList<>();
    static {
        paymentModels.add(new PaymentModel("txn001","success",new BigDecimal("4566.00")));
        paymentModels.add(new PaymentModel("txn002","failed",new BigDecimal("2323.00")));
        paymentModels.add(new PaymentModel("txn003","success",new BigDecimal("33.00")));
        paymentModels.add(new PaymentModel("txn004","failed",new BigDecimal("1323.00")));
        paymentModels.add(new PaymentModel("txn005","success",new BigDecimal("22222.00")));
    }

    @GetMapping(path = "/getTransactionStatus/{transactionId}")
    public PaymentModel getTransaction(@PathVariable(name = "transactionId",required = true) String transactionId){
        for(PaymentModel model : paymentModels){
            if(model.getPaymentId().equals(transactionId)){
                return model;
            }else {
                throw new TransactionNotFoundException("txnId - "+transactionId +"Not Found");
            }
        }
        return null;
    }

    @GetMapping(path = "/v3.0/healthz")
    public String getHealth(){
        return "E-Payment is up and Running";
    }

}
