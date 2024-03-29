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
package com.p2p.express.lane.example.service;

import com.p2p.express.lane.example.GeoLocation;
import com.p2p.express.lane.example.Portfolio;
import com.p2p.express.lane.example.PortfolioPosition;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import com.p2p.express.lane.example.service.Trade.TradeAction;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.util.MimeTypeUtils;
import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;


@Service
public class TradeServiceImpl implements TradeService {

	private static final Log logger = LogFactory.getLog(TradeServiceImpl.class);

	private final SimpMessageSendingOperations messagingTemplate;

	private final PortfolioService portfolioService;
	private volatile String replyTo;

	private final List<TradeResult> tradeResults = new CopyOnWriteArrayList<>();

	private volatile double x=37.78721;
	private volatile double y=-122.4109302;


	@Autowired
	public TradeServiceImpl(SimpMessageSendingOperations messagingTemplate, PortfolioService portfolioService) {
		this.messagingTemplate = messagingTemplate;
		this.portfolioService = portfolioService;
	}

	/**
	 * In real application a trade is probably executed in an external system, i.e. asynchronously.
	 */
	public void executeTrade(Trade trade) {

		Portfolio portfolio = this.portfolioService.findPortfolio(trade.getUsername());
		String ticker = trade.getTicker();
		int sharesToTrade = trade.getShares();

		PortfolioPosition newPosition = (trade.getAction() == TradeAction.Buy) ?
				portfolio.buy(ticker, sharesToTrade) : portfolio.sell(ticker, sharesToTrade);

		if (newPosition == null) {
			String payload = "Rejected trade " + trade;
			this.messagingTemplate.convertAndSendToUser(trade.getUsername(), "/queue/errors", payload);
			return;
		}

		this.tradeResults.add(new TradeResult(trade.getUsername(), newPosition));
	}

	//@Scheduled(fixedDelay=15000)
	public void sendTradeNotifications() {

		Map<String, Object> map = new HashMap<>();
		map.put(MessageHeaders.CONTENT_TYPE, MimeTypeUtils.APPLICATION_JSON);
		for (TradeResult result : this.tradeResults) {
			if (System.currentTimeMillis() >= (result.timestamp + 1500)) {
				logger.debug("Sending position update: " + result.position);
				this.messagingTemplate.convertAndSendToUser(result.user, "/queue/position-updates", result.position, map);
				this.tradeResults.remove(result);
			}
		}
	}
	@Scheduled(fixedDelay=5500)
	public void sendGeoNotifications() {
        if(!StringUtils.isEmpty(this.replyTo)) {
			Map<String, Object> map = new HashMap<>();
			map.put(MessageHeaders.CONTENT_TYPE, MimeTypeUtils.APPLICATION_JSON);
			GeoLocation loc = new GeoLocation();
			loc.setId("120");
			x += 0.01;
			y += 0.01;
			loc.setPositon_x(x);
			loc.setPosition_y(y);this.messagingTemplate.convertAndSend(replyTo, loc, map);
		}


	}
	@Override
	public void setReplyTo(String replyTo) {
		this.replyTo = replyTo;
	}

	private static class TradeResult {

		private final String user;
		private final PortfolioPosition position;
		private final long timestamp;

		public TradeResult(String user, PortfolioPosition position) {
			this.user = user;
			this.position = position;
			this.timestamp = System.currentTimeMillis();
		}
	}

}
