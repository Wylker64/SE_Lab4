package com.segroup2023.lab.controller.flow;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.segroup2023.lab.service.UserAuthorization;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import com.segroup2023.lab.database.entity.User;
import com.segroup2023.lab.exception.type.LoginFailureException;

public class FlowFilterController {
    //springboot controller for flow filter
    //能按近⼀周和近⼀个⽉筛选流⽔记录，并且显示该账户这⼀段时间的纯利润（⼊账-出账）。
    
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
