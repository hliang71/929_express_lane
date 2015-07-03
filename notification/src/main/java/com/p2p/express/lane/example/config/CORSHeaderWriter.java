package com.p2p.express.lane.example.config;

import org.springframework.security.web.header.writers.StaticHeadersWriter;

/**
 * Created by hliang on 7/2/15.
 */
public final class CORSHeaderWriter extends StaticHeadersWriter{
    public CORSHeaderWriter() {
        super("Access-Control-Allow-Origin", new String[]{"*"});
    }

}
