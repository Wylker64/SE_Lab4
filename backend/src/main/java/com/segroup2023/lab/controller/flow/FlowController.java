package com.segroup2023.lab.controller.flow;

import com.segroup2023.lab.database.entity.Account;
import com.segroup2023.lab.database.entity.FlowEntity;
import org.springframework.web.bind.annotation.*;
import com.segroup2023.lab.service.FlowService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;

import java.util.List;


@RestController
@RequestMapping("/flow")
public class FlowController {


    @Autowired
    private FlowService flowService;

    @GetMapping("/get")
    public ResponseEntity<List<FlowEntity>> getFlow(@RequestParam("account_id") Long accountId, @RequestParam("page_num") int pageNum, @RequestParam("select") int select) {
        List<FlowEntity> entities = flowService.getFlow(accountId, select);
        return ResponseEntity.ok(entities);
    }


}

