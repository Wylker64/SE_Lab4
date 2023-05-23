package com.segroup2023.lab.controller.flow;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.segroup2023.lab.service.UserAuthorization;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

public class FlowFilterController {
    
    @ RestController
    @ RequestMapping("/flow/filter")
    public static class Request {
        @ Valid
        @ NotNull
        public UserAuthorization.Request authorize;


        public String start_date;
        public String end_date;
    }

    public static class Response {
        @ Valid
        @ NotNull
        public String start_date;
        public String end_date;
        public String profit;
    }


}
