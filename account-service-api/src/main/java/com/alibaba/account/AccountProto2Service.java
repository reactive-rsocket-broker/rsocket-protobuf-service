package com.alibaba.account;


import com.google.protobuf.Int32Value;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface AccountProto2Service extends AccountProtoService {
    default Mono<Account> findById(int id) {
        return findById(Int32Value.of(id));
    }

    default Flux<Account> findByStatus(int status) {
        return findByStatus(Int32Value.of(status));
    }
}
