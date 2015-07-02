/*
 * Copyright 2002-2013 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.p2p.express.lane.example.web;

import java.security.Principal;
import java.util.List;

import com.p2p.express.lane.example.Portfolio;
import com.p2p.express.lane.example.PortfolioPosition;
import com.p2p.express.lane.example.service.PortfolioService;
import com.p2p.express.lane.example.service.Trade;
import com.p2p.express.lane.example.service.TradeService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageExceptionHandler;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.annotation.SendToUser;
import org.springframework.messaging.simp.annotation.SubscribeMapping;
import org.springframework.stereotype.Controller;


@Controller
public class PortfolioController {

	private static final Log logger = LogFactory.getLog(PortfolioController.class);

	private final PortfolioService portfolioService;

	private final TradeService tradeService;


	@Autowired
	public PortfolioController(PortfolioService portfolioService, TradeService tradeService) {
		this.portfolioService = portfolioService;
		this.tradeService = tradeService;
	}

	@SubscribeMapping("/positions")
	public List<PortfolioPosition> getPositions(Principal principal) throws Exception {
		logger.debug("Positions for " + principal.getName());
		Portfolio portfolio = this.portfolioService.findPortfolio(principal.getName());
		return portfolio.getPositions();
	}

	@MessageMapping("/trade")
	public void executeTrade(Trade trade, Principal principal) {
		trade.setUsername(principal.getName());
		logger.debug("Trade: " + trade);
		this.tradeService.executeTrade(trade);
	}

	@MessageExceptionHandler
	@SendToUser("/queue/errors")
	public String handleException(Throwable exception) {
		return exception.getMessage();
	}

}
