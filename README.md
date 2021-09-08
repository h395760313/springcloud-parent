# SpringCloud开发

## 一、父工程

用于管理SpringCloud和SpringBoot版本

```xml
<parent>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-parent</artifactId>
    <version>2.2.5.RELEASE</version>
</parent>

<properties>
        <spring-cloud-version>Hoxton.SR6</spring-cloud-version>
    </properties>

    <dependencyManagement>
        <dependencies>
            <dependency>
                <groupId>org.springframework.cloud</groupId>
                <artifactId>spring-cloud-dependencies</artifactId>
                <version>${spring-cloud-version}</version>
                <type>pom</type>
                <scope>import</scope>
            </dependency>
        </dependencies>
    </dependencyManagement>
```

## 二、服务注册中心

> 服务注册中心就是在整个微服务架构单独抽取一个服务，这个服务不完成项目中任何业务功能，仅仅用来在微服务中记录微服务以及对整个系统微服务进行健康检查，以及服务元数据信息存储

### 常用注册中心

> eureka（netflix）、zookeeper（java）、consul（go）、nacos（java阿里巴巴）

### Eureka

Eureka是Netflix开发的服务发现框架，springcloud-netflix-eureka 服务注册中心

Eureka包含两个组件：Eureka  Server 和 Eureka Client

#### Eureka Server 服务注册中心

1. 创建springboot项目

2. 引入eureka server依赖

```xml
<dependency>
    <groupId>org.springframework.cloud</groupId>
    <artifactId>spring-cloud-starter-netflix-eureka-server</artifactId>
</dependency>
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-web</artifactId>
</dependency>
```

3. 编写配置文件  制定eureka server端口 服务地址

```yml
server:
  port: 8761
spring:
  application:
    name: springcloud-server
eureka:
  client:
    service-url:
      defaultZone: http://localhost:8761/eureka
    fetch-registry: false  # 关闭eureka client 立即注册
    register-with-eureka: false # 关闭作为客户端向eureka注册
```

4. 启动类添加注解

> @EnableEurekaServer   -- 开启eureka服务端

```java
@SpringBootApplication
@EnableEurekaServer
public class EurekaServerApplication {...}
```

5. 服务注册中心开发成功

> 访问 http://localhost:8761/ 查看

 开发细节

![image-20210828163424096](/Users/xiehongyu/Desktop/代码Demo/images/springcloud图片1.png)

1. 注意：在微服务架构中服务名称代表服务唯一标识，至关重要，服务名称必须唯一，使用时必须通过如下配置指定服务名称：

```yml
spring:
  application:
    name: EUREKA-SERVER
```

推荐服务名称大写，服务名称不能使用下划线

2. eureka既会将自己作为一个服务注册中心，也会将自己作为客户端进行注册，使用如下配置关闭

   ```yml
   eureka:
     client:
       fetch-registry: false  # 关闭eureka client 立即注册
       register-with-eureka: false # 关闭作为客户端向eureka注册
   ```



#### Eureka Client客户端

开发eureka client，就是日后基于业务拆分出来的一个个微服务

1. 创建springboot项目

2. 引入eureka client依赖

```xml
<dependency>
  <groupId>org.springframework.cloud</groupId>
  <artifactId>spring-cloud-starter-netflix-eureka-client</artifactId>
</dependency>
```

3. 编写配置文件

```yml
server:
  port: 8801
spring:
  application:
    name: EUREKA-CLIENT
eureka:
  client:
    service-url:
      defaultZone: http://127.0.0.1:8761/eureka
```

4. 启动类添加注解

> @EnableEurekaClient   -- 开启eureka客户端

```java
@SpringBootApplication
@EnableEurekaClient
public class EurekaClientApplication {...}
```

####  Eureka 自我保护机制

1. 自我保护机制

```markdown
- 官网地址 https://github.com/Netflix/eureka/wiki/Server-Self-Preservation-Mode
- 默认情况下，如果Eureka Server在一定时间内(默认90秒)没有收到某个微服务实例的心跳，Eureka Server将会移除该实例。但是当网络分区故障发生时，微服务于Eureka Server之间无法正常通信，而微服务本身是正常运行的，此时不应该移除这个微服务，所以引入了自我保护机制。Eureka Server在运行期间回去统计心跳失败比例在15分钟内是否低于80%，如果低于85%，Eureka Server会将这些实例保护起来，让这些实例不会过期。这种设计的哲学原理就是“宁可信其有，不可信其无”。自我保护模式正式一种针对网络异常波动的安全保护措施，使用自我保护模式能使Eureka集群更加健壮、稳定的使用。
```

2. 关闭自我保护(<span style="color:red">官方不推荐关闭</span>)

```yml
eureka:
  server:
    enable-self-preservation: false # 关闭自我保护
    eviction-interval-timer-in-ms: 3000 # 超时3s自动清除
```

3. 微服务修改减短服务心跳的时间

```yml
eureka:
  instance:
    lease-expiration-duration-in-seconds: 10 # 用来修改Eureka Server默认接受心跳的最大时间 默认为90s
    lease-renewal-interval-in-seconds: 5 # 指定客户端多久想Eureka Server发一次心跳 默认为30s
```

###  Consul

简介：consul是基于go语言进行开发的轻量级服务注册中心(google)

作用：管理微服务中所有服务注册、发现，管理服务元数据信息存储(服务名、地址列表)，心跳健康检查

安装：

```markdown
下载地址：https://www.consul.io/downloads

mac: 
  brew tap hashicorp/tap
  brew install hashicorp/tap/consul
  
启动：consul agent -dev

ui页面：http://localhost:8500
```

#### 客户端开发

1. 引入依赖

```xml
<!--consul-client-->
<dependency>
  <groupId>org.springframework.cloud</groupId>
  <artifactId>spring-cloud-starter-consul-discovery</artifactId>
</dependency>
```

2. 编写配置文件

```yml
server:
  port: 8802
spring:
  application:
    name: CONSUL-CLIENT
  cloud:
    consul:
      host: localhost
      port: 8500
      discovery:
        service-name: ${spring.application.name} # 指定注册当前服务的服务名称  默认：${spring.application.name}
```

3. 启动类注解

```java
@SpringBootApplication
@EnableDiscoveryClient // 作用：通用服务注册客户端注解，代表 consul client zk
public class ConsulClientApplication {...}
```

4. 健康检查问题

consul server 检测所有客户端心跳，但是发送心跳时client必须给予响应才能使该服务正常运行，如果没有引入健康检查依赖，则会导致健康检查不能通过，导致服务不能使用。

```xml
<!--健康检查-->
<!--这个包是用来做健康度监控的-->
<dependency>
  <groupId>org.springframework.boot</groupId>
  <artifactId>spring-boot-starter-actuator</artifactId>
</dependency>
```

## 三、服务间通信

### RestTemplate

使用Spring中的RestTemplate直接调用

```java
RestTemplate restTemplate = new RestTemplate();
String result = restTemplate.getForObject("http://localhost:8804/order", String.class);
```

调用服务的路径主机和服务端口直接写死在url中，无法实现负载均衡、如果提供服务的路径发生变化时不利于后续维护工作。

### Ribbon

#### Ribbon实现负载均衡调用

1. 在服务中引入ribbon依赖

```xml
<!--Ribbon-->
<dependency>
  <groupId>org.springframework.cloud</groupId>
  <artifactId>spring-cloud-starter-netflix-ribbon</artifactId>
</dependency>
```

注意：consul client 依赖中已经包含了ribbon

2. 使用Ribbon组件根据服务id实现请求负载均衡

> DiscoveryClient   服务发现客户端对象，根据服务id去服务注册中心获取对应服务的实例列表到本地中
>
> ```java
> List<ServiceInstance> serviceInstances = discoveryClient.getInstances("ORDERS");
> ```
>
> ​		缺点：没有负载均衡，需要自己实现负载均衡

> LoadBalancerClient   负载均衡客户端对象，根据服务id去服务注册中心获取对应服务的实例列表，根据默认负载均衡策略选择一台机器进行返回。
>
> ```java
> ServiceInstance serviceInstance = loadBalancerClient.choose("ORDERS");
> ```
>
> ​		缺点：使用时需要每次先根据服务id获取一个负载均衡机器后再通过restTemplate调用服务

> @LoadBalanced + RestTemplate 负载均衡客户端注解
>
> ```java
> String resule = restTemplate.getForObject("http://ORDERS/order", String.class);
> ```
>
> ​		修饰范围：用在方法上
>
> ​		作用：使当前方法、当前对象具有ribbon的负载均衡特性
>
> ```java
> @Bean
> @LoadBalanced
> public RestTemplate restTemplate(){
>   return new RestTemplate();
> }
> ```

#### Ribbon组件实现负载均衡原理

原理：根据调用服务的服务id去服务注册中心获取对应服务的服务列表，并将服务列表拉去到本地进行缓存，然后在本地通过默认的轮询策略在现有列表中选择一个可用节点提供服务

注意：客户端负载均衡

#### Ribbon负载均衡策略

```markdown
- RoundRobinRule							轮询策略
- RandomRule									随机策略
- AvailabilityFilteringRule		可用过滤策略
	`会先过滤由于多次访问故障而处于断路器跳闸状态的服务，还有并发的连接数量超过阈值的服务，然后对剩余的服务列表按照轮询策略进行访问
- RetryRule										重试策略
	`先按照RoundrobinRule的策略获取服务，如果获取失败则在指定时间内进行重试，获取可用的服务。
- WeightedResponseTimeRule		响应时间加权策略
	`根绝平均响应的时间计算所有服务的权重，响应时间越快服务权重越大，被选中的概率越高，刚启动时如果统计信息不足，则使用RoundRobinRule策略，等统计信息足够会切换到 WeightedResponseTimeRule
- BestAvailableRule						最低并发策略
	`会先过滤由于多次访问故障而处于断路器跳闸状态的服务，然后选择一个并发量最小的服务
```

#### 修改Ribbon负载均衡策略

在配置文件中添加配置：

`服务id.ribbon.NFLoadBalancerRuleClassName=com.netflix.loadbalancer.RandomRule`

```yml
ORDERS:
  ribbon:
    NFLoadBalancerRuleClassName: com.netflix.loadbalancer.RandomRule
```

### OpenFeign

调用方引入依赖

```xml
<dependency>
  <groupId>org.springframework.cloud</groupId>
  <artifactId>spring-cloud-starter-openfeign</artifactId>
</dependency>
```

编写配置文件

```yml
server:
  port: 8807
spring:
  application:
    name: CATEGORY
  cloud:
    consul:
      port: 8500
      host: localhost
```

编写调用客户端

```java
@FeignClient(value = "PRODUCT") //用于服务的调用
public interface ProductClient {

    @RequestMapping("/product") // 需要调用的接口
    String product();

    @RequestMapping("/list") // 需要调用的接口
    String list();
}
```

调用服务

```java
@Autowired
private ProductClient productClient;

@RequestMapping("/category")
public String category(){
    log.info("category service ...");
    String product = productClient.product(); // 调用服务
    String list = productClient.list(); // 调用服务
    return "category ok!  " + product +"   "+ list;
}
```

#### 服务间通信的参数传递

1. 零散类型的参数   

queryString方式传递参数： ?name=xxx

```java
// openFeign
@GetMapping("/test")
String test(@RequestParam("name") String name, @RequestParam("age") Integer age);

// 业务接口
@GetMapping("/test")
public String test(@RequestParam("name") String name, @RequestParam("age") Integer age){...}
```

路径传递参数：/url/xxx/yyy

```java
// openFeign
@GetMapping("/test1/{id}/{name}")
String test1(@PathVariable("id") Integer id, @PathVariable("name") String name);

// 业务接口
@GetMapping("/test1/{id}/{name}")
public String test1(@PathVariable("id") Integer id, @PathVariable("name") String name){...}
```

2. 传递对象类型参数   

Application/json方式：

```java
// openFeign
@PostMapping("/test2")
String test2(@RequestBody Product product);

// 业务接口
@PostMapping("/test2")
public String test2(@RequestBody Product product){...}
```

#### OpenFeign超时时间

1. 默认的调用超时

使用OpenFeign组件在进行服务间通信时要求被调用服务必须在1s内给予响应，一旦服务执行业务逻辑时间超过2s，OpenFeign组件讲直接报错：

> Read timed out executing GET http://PRODUCT/test4

2. 修改OpenFeign超时时间

```yml
# 指定修改某个服务调用超时时间
feign:
  client:
    com.xiehongyu.config:
      # PRODUCT: # 指定服务器
      default: # 所有服务器
        connectTimeout: 5000 # 配置服务器连接超时时间
        readTimeout: 5000 # 配置服务器等待超时时间
```

```yml
# 修改所有服务调用超时时间
feign:
  client:
    com.xiehongyu.config:
      PRODUCT: # 指定服务器
      # default: # 所有服务器
        connectTimeout: 5000 # 配置服务器连接超时时间
        readTimeout: 5000 # 配置服务器等待超时时间
```

#### OpenFeign日志展示

OpenFeign是一个伪HttpClient客户端，用来帮助我们完成服务见通信，底层用http协议完成服务间调用

1. 日志

为了更方便在开发过程中调试OpenFeign数据传递和响应处理，OpenFeign在设计时添加了日志功能，默认OpenFeign日志功能是需要手动开启的。

2. 日志使用

首先设置OpenFeign日志级别

```yml
# 展示openFeign日志
logging:
  level:
    com:
      xiehongyu:
        openfeign: debug
```

OpenFeign每个客户端提供一个日志对象

`NONE`  不记录任何日志

`BASIC`  仅仅记录请求方法、url、响应状态代码及执行时间

`HEADERS`  记录Basic级别的基础上，记录请求喝响应的header

`FULL`  记录请求喝响应的header、body和元数据，也就是展示全部http协议状态

```yml
feign:
  client:
    com.xiehongyu.config:
      # PRODUCT: # 指定服务器
      default: # 所有服务器
        loggerLevel: FULL
```



### Hystrix

#### Hystrix组件

##### 1. 服务雪崩

```markdown
# 1.服务雪崩
- 在微服务之间进行服务调用时由于某一个服务故障，导致级联服务故障的现象，成为雪崩效应。雪崩效应描述的是提供方不可用，导致消费芳不可用并将不可用逐渐放大的过程。
```

ServiceA   -------->    ServiceB    -------->    ServiceC

```markdown
- 而此时，ServiceA的流量波动很大流量经常会突然性增加！那么在这种情况下，就算ServiceA能扛得住请求，ServiceB和ServiceC未必能扛得住这突发的请求。此时，如果ServiceC因为扛不住请求，变得不可用。那么ServiceB的请求也会阻塞，慢慢耗尽ServiceB的线程资源，ServiceB就会变得不可用。紧接着，ServiceA也会不可用。
```

##### 2. 服务熔断

```markdown
# 服务熔断
- “熔断器”本身是一种开关装置，当某个服务单元发生故障之后，通过断路器(Hystrix)的故障监控，某个异常条件被触发，直接熔断整个服务。向调用方返回一个符合预期的、可处理的被选响应(FallBack)，而不是长时间的等待活着抛出调用方法无法处理的异常，就保证了服务调用方的县城不会被长时间占用，避免故障在分布式系统中蔓延，乃至雪崩。如果目标服务情况好转则恢复调用。服务熔断是解决服务雪崩的重要手段。
```



##### 3. 服务降级

```markdown
# 服务降级说明
- 服务压力剧增的时候根据当前的业务情况及流量对一些服务和页面有策略的降级，以此缓解服务器的压力，以保证核心任务的进行。同事保证部分甚至大部分任务客户能得到正确的响应。也就是当前的请求处理不了了或出错了，给一个默认的返回。

- 服务降级：关闭微服务系统中某些边缘服务，保证系统核心服务正常运行
```

##### 4. 降级和熔断总结

```markdown
# 1.共同点
- 目标很一致，都是从可用性可靠性着想，为防止系统的整体缓慢甚至崩溃，采用的技术手段；
- 最终表现类，对于二者来说，最终让用户体验到的是某些功能暂时不可达或不可用；
- 粒度一般都是服务级别，当然，业界也有不少更细粒度的做法，比如做到数据持久层(允许查询，不允许增删改)；
- 自治性要求很高，熔断模式一般都是服务基于策略的自动触发，降级虽说可人工干预，但在微服务架构下，完全靠人显然不可能，开关预置、配置中心都是必要手段；sentinel

# 2.不同点
- 出发原因不太一样，服务熔断一般是某个服务（下游服务）故障引起，而服务降级一般是从整体负荷考虑；
- 管理目标的层次不太一样，熔断其实是一个框架及的处理，每个微服务都需要（无层级之分），而降级一般需要对业务有层级之分（比如降级一般是从最外围服务边缘服务开始）

# 3.总结
- 熔断必会触发降级，所以熔断也是降级的一种，区别在于熔断是对调用链路的保护，而降级是对系统过载的一种保护处理
```

##### 5. 服务熔断的实现

1. 引入Hystrix依赖

```xml
<dependency>
  <groupId>org.springframework.cloud</groupId>
  <artifactId>spring-cloud-starter-netflix-hystrix</artifactId>
</dependency>
```

2. 启动类开启熔断功能

```java
@SpringBootApplication
@EnableDiscoveryClient
@EnableCircuitBreaker // 开启Hystrix服务熔断
public class HystrixApplication {...}
```

3. 在控制器方法中加入备选处理

```java
@GetMapping("hystrix")
@HystrixCommand(fallbackMethod = "demoFallback")
public String hystrix(Integer id){
    System.out.println("hystrix demo");
    if (id <= 0) {
        throw new RuntimeException("无效ID！");
    }
    return "hystrix ok!!!";
}

public String demoFallback(Integer id){
    return "当前活动过于火爆，服务已经熔断了！";
}
```

4. Hystrix断路器打开条件

>a. 当满足一定的阈值时(默认10秒内超过20个请求失败)
>
>b. 当失败率达到一定时(默认10秒内超过50%的请求失败)

5. Hystrix监控流程 触发熔断器机制流程

![image-20210907145204983](/Users/xiehongyu/Desktop/代码Demo/images/image-20210907145204983.png)

> 整个流程：当Hystrix监控到对该服务接口触发a、b两个阈值时，会在系统中自动触发熔断器，在熔断器打开期间，任何到该接口的请求均不可用。在断路器打开5s后，断路器会处于半开状态，此时允许放行一个请求到该服务接口，如果该请求执行成功，断路器彻底关闭，如果该请求执行失败断路器重新打开。

6. 在实战过程中断路器使用

```markdown
# a.为每一个调用接口提供自定义备选处理
- @HystrixCommand(fallbackMethod = "demoFallback")   // 熔断之后处理fallbackMethod 书写快速失败方法名

# b. 使用Hystrix提供默认备选处理
- @HystrixCommand(defaultFallback = "默认处理方法名")
```



##### 6. 服务降级的实现



##### 7. openfeign调用服务过程集成Hystrix实现默认备选处理

1. 引入Hystrix依赖

openfeign组件底层自动依赖Hystrix，项目无须引入

2. 开启openfeign对Hystrix支持

```yml
feign:
  hystrix:
    enabled: true # 开启openfeign在调用服务过程中 开启hystrix支持 默认没有开启
```

3. 开发openfeign服务调用失败默认处理的实现类

```java
/**
 * @Description: 自定义HystrixClient默认备选处理
 * @Author: xiehongyu
 * @Date: 2021/9/7 15:43
 */
@Component
public class HystrixClientFallback implements HystrixClient{

    @Override
    public String hystrix(Integer id){
        return "当前服务不可用，请稍后再试! id:" + id;
    }
}
```

4. 在openfeign客户端接口中的@FeignClients(value="服务id", fallback=默认处理.class)

```java
/**
 * @Description: openfeign客户端
 * @Author: xiehongyu
 * @Date: 2021/9/7 15:43
 */
@FeignClient(value = "HYSTRIX", fallback = HystrixClientFallback.class)
public interface HystrixClient {
    @GetMapping("hystrix")
    String hystrix(@RequestParam("id") Integer id);
}
```

5. 当调用服务不可用时，直接会执行自定义默认处理



### Gateway

#### 什么是网关

Gateway (Service Gateway) 服务网关。

网管统一服务入口，可方便实现对平台众多服务接口进行管控。

#### 网关作用

> 1. 网关统一所有微服务入口
> 2. 网关可以实现请求路由转发(router dispatcher) ，以及请求过程负载均衡
> 3. 访问服务的身份认证、防报文重放与防数据篡改、功能调用的业务鉴权、响应数据的脱敏、流量与并发控制，甚至基于API调用的计量或者计费等等

#### Springclod Gateway

Gateway: 动态路由、服务统一管理、请求过滤

Gateway = 路由转发(router) + 请求过滤(filter)

#### Gateway 网关使用

1. 引入依赖  

注意：不能添加spring-boot-starter-web依赖，会与WebFlux产生冲突

```xml
<dependency>
  <groupId>org.springframework.cloud</groupId>
  <artifactId>spring-cloud-starter-gateway</artifactId>
</dependency>
<dependency>
  <groupId>org.springframework.cloud</groupId>
  <artifactId>spring-cloud-starter-consul-discovery</artifactId>
</dependency>
<dependency>
  <groupId>org.springframework.boot</groupId>
  <artifactId>spring-boot-starter-actuator</artifactId>
</dependency>
```

2. 编写配置文件

> 通过配置文件配置网关路由

```yml
server:
  port: 7979
spring:
  application:
    name: GATEWAY
  cloud:
    consul:
      port: 8500
      host: localhost
    gateway:
      routes:
        - id: category-router  # 路由对象唯一标识
          uri: http://localhost:8807  # 服务地址
          predicates:  # 断言 配置路由规则
            - Path=/category/**
        - id: product-router
          uri: http://localhost:8806
          predicates: 
            - Path=/product/**

```

> 通过Java代码配置网关路由  Java配置优先于配置文件

```java
@Configuration
public class GatewayConfig {
    @Bean
    public RouteLocator customRouteLocator(RouteLocatorBuilder builder){
        return builder.routes()
                .route("category_router",
                       r -> r.path("/category/**").uri("http://localhost:8807"))
                .route("product_router",
                       r -> r.path("/product/**").uri("http://localhost:8806"))
                .build();
    }
}
```

