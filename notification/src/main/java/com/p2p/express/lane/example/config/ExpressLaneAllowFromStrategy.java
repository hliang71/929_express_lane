package com.p2p.express.lane.example.config;

import org.springframework.security.web.header.writers.frameoptions.AllowFromStrategy;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by hliang on 7/2/15.
 */
public class ExpressLaneAllowFromStrategy  implements AllowFromStrategy {
    @Override
    public String getAllowFromValue(HttpServletRequest httpServletRequest) {
        return "*";
    }
}
