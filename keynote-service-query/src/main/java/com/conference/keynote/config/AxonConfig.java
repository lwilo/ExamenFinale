package com.conference.keynote.config;

import com.thoughtworks.xstream.XStream;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AxonConfig {

    @Bean
    public XStream xStream() {
        XStream xStream = new XStream();
        
        // Autoriser les classes du package common
        xStream.allowTypesByWildcard(new String[] {
            "com.conference.common.**"
        });
        
        // Autoriser les classes du package keynote
        xStream.allowTypesByWildcard(new String[] {
            "com.conference.keynote.**"
        });
        
        return xStream;
    }
}
