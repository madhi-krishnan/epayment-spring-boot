package com.fyndna.epayment.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data @AllArgsConstructor @NoArgsConstructor
public class SystemProperty {
    private String hostName;
    private Integer port;
    private String ipAddress;
}
