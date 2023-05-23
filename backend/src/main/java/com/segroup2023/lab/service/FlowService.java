package com.segroup2023.lab.service;

import com.segroup2023.lab.database.entity.FlowEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.segroup2023.lab.database.repository.FlowRepository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Calendar;
import java.util.List;
import java.util.Date;


@ Service
public class FlowService {
    //商户与管理员可以查看与⾃⼰账户相关的流⽔记录，流⽔应按照账户划分，如商户有个⼈账户和商店账户，管理员有商城利润账户和中间账户。
    //每条流⽔记录应包括必要信息，如转出⽅、转⼊⽅、⾦额、备注信息
    @Autowired
    private static FlowRepository flowRepository;

    //write according to FlowEntity
    public static void addFlow(Long fromAccount, Long toAccount, BigDecimal amount, String remark)
    {
        FlowEntity flowEntity = flowRepository.findByFromAccountAndToAccount(fromAccount, toAccount);
        if (flowEntity == null)
        {
            flowRepository.save(new FlowEntity(fromAccount,toAccount,amount,remark));
        }
        else
        {
            flowEntity.increaseCount(amount);
            flowRepository.save(flowEntity);
        }
    }

    public static List<FlowEntity> getFlow(Long userId, int select)
    {
        // Implement the logic to filter the flow records based on select (0: all flows, 1: last month, 2: last week)

        Date startDate;
        Date endDate = new Date();
        Calendar cal = Calendar.getInstance();
        switch (select) {
            case 1:
                cal.add(Calendar.MONTH, -1);
                startDate = cal.getTime();
                break;
            case 2:
                cal.add(Calendar.WEEK_OF_YEAR, -1);
                startDate = cal.getTime();
                break;
            default:
                startDate = null;
        }


        List <FlowEntity> flowEntities;
        if (startDate == null) {
            flowEntities = flowRepository.findByUserId(userId);
        } else {
            flowEntities = flowRepository.findFlowsByUserIdAndDate(userId, startDate, endDate);
        }

        return flowEntities;

    }



}
