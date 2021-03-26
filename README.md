RSocket service with protobuf/gPRC IDL definition
=================================================

基于Protobuf/gRPC接口定义开发RSocket服务，主要特性如下：

* 使用proto文件定义通讯接口，兼容gRPC服务接口定义
* 使用Protobuf作为数据序列化格式，满足高性能的要求
* 使用Reactive接口，对比gRPC接口更简单

# 模块说明

* account-service-api: Account对应的服务接口，包括proto文件接口
* account-service-provider: Spring Boot应用，负责实现服务

# 开发步骤

###  定义proto文件

和标准的gRPC服务接口完全一样，样例如下：

```proto
syntax = "proto3";

option java_multiple_files = true;
option java_outer_classname = "AccountProto";

package com.alibaba.account;

import "google/protobuf/any.proto";
import "google/protobuf/wrappers.proto";

message Account {
    int32 id = 1;
    string email = 2;
    string phone = 3;
    int32 status = 4;
    string nick = 5;
}

service AccountProtoService {

    rpc findById (google.protobuf.Int32Value) returns (Account);

    rpc findByStatus (google.protobuf.Int32Value) returns (stream Account);

    rpc findByIdStream (stream google.protobuf.Int32Value) returns (stream Account);
}
```

### 添加protobuf-maven-plugin和proto-rsocket-plugin插件

其中protobuf-maven-plugin主要负责生成Message对应的Java对象，而proto-rsocket-plugin负责生成service定义对应的Reactive interface接口。

```xml
            <plugin>
                <groupId>org.xolstice.maven.plugins</groupId>
                <artifactId>protobuf-maven-plugin</artifactId>
                <version>0.6.1</version>
                <configuration>
                    <protocArtifact>com.google.protobuf:protoc:${protobuf-java.version}:exe:${os.detected.classifier}
                    </protocArtifact>
                    <pluginId>java</pluginId>
                </configuration>
                <executions>
                    <execution>
                        <phase>generate-sources</phase>
                        <goals>
                            <goal>compile</goal>
                        </goals>
                    </execution>
                </executions>
            </plugin>
            <plugin>
                <groupId>org.mvnsearch</groupId>
                <artifactId>proto-rsocket-plugin</artifactId>
                <version>1.0.0-SNAPSHOT</version>
                <executions>
                    <execution>
                        <phase>generate-sources</phase>
                        <goals>
                            <goal>proto2rsocket</goal>
                        </goals>
                    </execution>
                </executions>
            </plugin>
```

### 实现服务接口

编写对应的服务实现类，只要继承代码生成的Reactive服务接口即可，如下：

```java
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
}
```

### 启动服务

添加alibaba-rsocket-spring-boot-starter依赖，如下：

```xml
<dependency>
  <groupId>com.alibaba.rsocket</groupId>
  <artifactId>alibaba-rsocket-spring-boot-starter</artifactId>
  <version>${alibaba-rsocket.version}</version>
</dependency>
```

然后在application.properties中添加Alibaba RSocket Broker对应的配置，如下：

```
# alibaba rsocket broker configuration
rsocket.brokers=tcp://127.0.0.1:9999
### Please fill jwt-token for production env
rsocket.jwt-token=your_token_here
```

最后启动应用，这样服务就会注册到Alibaba RSocket Broker上，然后其他第三方就可以进行调用。

# 相关实践

* 继承gRPC对应的Reactive接口：为了方便客户端调用，你可以继承接口，添加对应的default method
* 如果你有对象转换的需求，如POJO到Protobuf message对象转换，可以考虑使用MapStruct https://mapstruct.org/

# References

* proto-rsocket-plugin: https://github.com/linux-china/proto-rsocket-plugin
* gRPC Service definition: https://grpc.io/docs/what-is-grpc/core-concepts/
* Google Protobuf wrappers: https://github.com/protocolbuffers/protobuf/blob/master/src/google/protobuf/wrappers.proto
