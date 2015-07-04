package com.p2p.express.lane.example.config;

import org.springframework.security.web.header.writers.StaticHeadersWriter;

/**
 * Created by hliang on 7/3/15.
 */
public class EnableAuthenticateHeaderWriter extends StaticHeadersWriter {
    public EnableAuthenticateHeaderWriter() {
        super("Access-Control-Allow-Credentials",  new String[]{"true"});
    }

}