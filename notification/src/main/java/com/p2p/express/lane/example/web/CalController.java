package com.p2p.express.lane.example.web;

import com.p2p.express.lane.example.GeoLocation;
import com.p2p.express.lane.example.service.PortfolioService;
import com.p2p.express.lane.example.service.TradeService;
import com.p2p.express.lane.example.vo.CalcInput;
import com.p2p.express.lane.example.vo.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.messaging.simp.annotation.SubscribeMapping;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Controller;
import org.springframework.util.MimeTypeUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;

import java.security.Principal;
import java.util.HashMap;
import java.util.Map;

@Controller
public class CalController {

    private final SimpMessageSendingOperations messagingTemplate;
    private final TradeService tradeService;

    @Autowired
    public CalController(SimpMessageSendingOperations messagingTemplate, TradeService tradeService) {
        this.messagingTemplate = messagingTemplate;
        this.tradeService = tradeService;
    }

	@MessageMapping("/add" )
    @SendTo("/topic/showResult")
    public Result addNum(CalcInput input) throws Exception {
        String replyTo = input.getReplyTo();
        if(!StringUtils.isEmpty(replyTo)) {
            this.tradeService.setReplyTo(replyTo);
        }
        Result result = new Result("subscribed to : " + replyTo);
        return result;
    }

    
    @RequestMapping("/start")
    public String start() {
        return "start";
    }
}
