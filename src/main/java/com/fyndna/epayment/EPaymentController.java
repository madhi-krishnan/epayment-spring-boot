package com.fyndna.epayment;

import com.fyndna.epayment.model.Greeting;
import com.fyndna.epayment.model.PaymentModel;
import com.fyndna.epayment.model.SystemProperty;
import io.micrometer.core.annotation.Timed;
import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;
import java.util.Random;

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

    @Autowired
    private Environment environment;

    private static final String template = "Hello, %s!";
    private final AtomicLong counter  = new AtomicLong();

    private final MeterRegistry meterRegistry;

    public EPaymentController(MeterRegistry meterRegistry) {
        this.meterRegistry=meterRegistry;
    }

    @GetMapping(path = "/getTransactionStatus/{transactionId}")
    public PaymentModel getTransaction(@PathVariable(name = "transactionId",required = true) String transactionId){
        for(PaymentModel paymentModel : paymentModels){
            if(paymentModel.getPaymentId().equals(transactionId)){
                return paymentModel;
            }
        }
        return null;
    }

    @GetMapping(path = "/v3.0/healthz")
    public String getHealth(){
        return "E-Payment is up and Running";
    }

    @GetMapping("/greeting")
    @Timed(value = "greeting.time", description = "Time taken to return greeting",
            percentiles = {0.5, 0.90})
    public Greeting greeting(@RequestParam(value = "name", defaultValue = "World") String name) {
        return new Greeting(counter.incrementAndGet(), String.format(template, name));
    }

    @GetMapping("/getSystemProperty")
    public SystemProperty getSystemProperty() throws UnknownHostException {
        return new SystemProperty(InetAddress.getLocalHost().getHostAddress(),Integer.parseInt(environment.getProperty("server.port")),InetAddress.getLocalHost().getHostName());
    }

    @GetMapping("/profiler")
    public String doProfile() {
        System.out.println("Let's profile!");

        // generate random number between 1 and 10
        Random rand = new Random();
        int delay = rand.nextInt(10) + 1;

        // spin CPU for random number
        int blocked = BlockCPU(delay);
        // return
        return ("Blocked CPU for " + delay);
    }

    public int BlockCPU(int blockFor) {
        System.out.println("entering CPU block");
        // get current time
        long startTime = System.currentTimeMillis();
        System.out.println("starting block at " + String.valueOf(startTime));
        // + String.valueOf(startTime));
        // current time + delay
        long endTime = startTime + (blockFor * 1000);
        System.out.println("will end block at " + endTime);
        int result = 0;
        Random blockRand = new Random();
        while (true) {
            result += blockRand.nextInt(1000) * blockRand.nextInt(1000);
            if (System.currentTimeMillis() > endTime) {
                return blockFor;
            }
        }

    }

}
