package com.kannect.achieve.dto.request;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EmailRequest {
    private List<String> to;
    private List<String> cc; 
    private String subject;
    private String body;
}

