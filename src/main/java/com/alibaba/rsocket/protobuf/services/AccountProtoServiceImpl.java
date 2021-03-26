package com.alibaba.rsocket.protobuf.services;


import com.alibaba.account.Account;
import com.alibaba.account.AccountProtoService;
import com.alibaba.rsocket.RSocketService;
import com.google.protobuf.Int32Value;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@RSocketService(serviceInterface = AccountProtoService.class, encoding = "protobuf")
public class AccountProtoServiceImpl implements AccountProtoService {
    @Override
    public Mono<Account> findById(Int32Value id) {
        Account account = Account.newBuilder()
                .setId(id.getValue())
                .setNick("nick")
                .setEmail("xxx@yyy.com")
                .setStatus(1)
                .build();
        return Mono.just(account);
    }

    @Override
    public Flux<Account> findByStatus(Int32Value status) {
        Account account = Account.newBuilder()
                .setId(1)
                .setNick("nick")
                .setEmail("xxx@yyy.com")
                .setStatus(status.getValue())
                .build();
        return Flux.just(account);
    }

    @Override
    public Flux<Account> findByIdStream(Flux<Int32Value> idFlux) {
        return idFlux.map(id -> {
            return Account.newBuilder()
                    .setId(id.getValue())
                    .setNick("nick")
                    .setEmail("xxx@yyy.com")
                    .setStatus(1)
                    .build();
        });
    }
}
