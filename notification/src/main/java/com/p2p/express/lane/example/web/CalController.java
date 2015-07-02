package com.p2p.express.lane.example.web;

import com.p2p.express.lane.example.GeoLocation;
import com.p2p.express.lane.example.vo.CalcInput;
import com.p2p.express.lane.example.vo.Result;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.annotation.SubscribeMapping;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Controller;
import org.springframework.util.MimeTypeUtils;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.HashMap;
import java.util.Map;

@Controller
public class CalController {


	@MessageMapping("/add" )
    @SendTo("/topic/showResult")
    public Result addNum(CalcInput input) throws Exception {
        Thread.sleep(2000);
        Result result = new Result(input.getNum1()+"+"+input.getNum2()+"="+(input.getNum1()+input.getNum2())); 
        return result;
    }

    
    @RequestMapping("/start")
    public String start() {
        return "start";
    }
}
