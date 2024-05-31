package com.aburakkontas.manga_payment.infrastructure;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.security.AnyTypePermission;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AxonXStreamConfig {

    @Bean
    public XStream xStream() {
        XStream xStream = new XStream();
        xStream.addPermission(AnyTypePermission.ANY);
        xStream.allowTypesByWildcard(new String[] {
                "com.aburakkontas.**",
                "java.**"
        });

        return xStream;
    }
}