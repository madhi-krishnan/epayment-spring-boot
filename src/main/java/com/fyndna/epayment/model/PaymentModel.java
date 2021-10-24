package com.fyndna.epayment.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data @AllArgsConstructor @NoArgsConstructor
public class PaymentModel {
    String paymentId;
    String transactionStatus;
    BigDecimal amount;
}
