package com.alibaba.account.springboot;

import com.alibaba.account.AccountProto2Service;
import com.alibaba.account.AccountProtoService;
import com.alibaba.rsocket.invocation.RSocketRemoteServiceBuilder;
import com.alibaba.rsocket.metadata.RSocketMimeType;
import com.alibaba.rsocket.upstream.UpstreamManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
@Configuration
public class AccountServicesAutoConfiguration {
    @Bean
    public AccountProtoService accountService(UpstreamManager upstreamManager) {
        return RSocketRemoteServiceBuilder
                .client(AccountProtoService.class)
                .encodingType(RSocketMimeType.Protobuf)
                .upstreamManager(upstreamManager)
                .build();
    }

    @Bean
    public AccountProto2Service accountProto2Service(UpstreamManager upstreamManager) {
        return RSocketRemoteServiceBuilder
                .client(AccountProto2Service.class)
                .service(AccountProtoService.class.getCanonicalName())
                .encodingType(RSocketMimeType.Protobuf)
                .upstreamManager(upstreamManager)
                .build();
    }
}
