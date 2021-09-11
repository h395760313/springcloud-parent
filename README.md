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
@SpringBootApplication@EnableEurekaClientpublic class EurekaClientApplication {...}
```

####  Eureka 自我保护机制

1. 自我保护机制

```markdown
- 官网地址 https://github.com/Netflix/eureka/wiki/Server-Self-Preservation-Mode- 默认情况下，如果Eureka Server在一定时间内(默认90秒)没有收到某个微服务实例的心跳，Eureka Server将会移除该实例。但是当网络分区故障发生时，微服务于Eureka Server之间无法正常通信，而微服务本身是正常运行的，此时不应该移除这个微服务，所以引入了自我保护机制。Eureka Server在运行期间回去统计心跳失败比例在15分钟内是否低于80%，如果低于85%，Eureka Server会将这些实例保护起来，让这些实例不会过期。这种设计的哲学原理就是“宁可信其有，不可信其无”。自我保护模式正式一种针对网络异常波动的安全保护措施，使用自我保护模式能使Eureka集群更加健壮、稳定的使用。
```

2. 关闭自我保护(<span style="color:red">官方不推荐关闭</span>)

```yml
eureka:  server:    enable-self-preservation: false # 关闭自我保护    eviction-interval-timer-in-ms: 3000 # 超时3s自动清除
```

3. 微服务修改减短服务心跳的时间

```yml
eureka:  instance:    lease-expiration-duration-in-seconds: 10 # 用来修改Eureka Server默认接受心跳的最大时间 默认为90s    lease-renewal-interval-in-seconds: 5 # 指定客户端多久想Eureka Server发一次心跳 默认为30s
```

###  Consul

简介：consul是基于go语言进行开发的轻量级服务注册中心(google)

作用：管理微服务中所有服务注册、发现，管理服务元数据信息存储(服务名、地址列表)，心跳健康检查

安装：

```markdown
下载地址：https://www.consul.io/downloadsmac:   brew tap hashicorp/tap  brew install hashicorp/tap/consul  启动：consul agent -devui页面：http://localhost:8500
```

#### 客户端开发

1. 引入依赖

```xml
<!--consul-client--><dependency>  <groupId>org.springframework.cloud</groupId>  <artifactId>spring-cloud-starter-consul-discovery</artifactId></dependency>
```

2. 编写配置文件

```yml
server:  port: 8802spring:  application:    name: CONSUL-CLIENT  cloud:    consul:      host: localhost      port: 8500      discovery:        service-name: ${spring.application.name} # 指定注册当前服务的服务名称  默认：${spring.application.name}
```

3. 启动类注解

```java
@SpringBootApplication@EnableDiscoveryClient // 作用：通用服务注册客户端注解，代表 consul client zkpublic class ConsulClientApplication {...}
```

4. 健康检查问题

consul server 检测所有客户端心跳，但是发送心跳时client必须给予响应才能使该服务正常运行，如果没有引入健康检查依赖，则会导致健康检查不能通过，导致服务不能使用。

```xml
<!--健康检查--><!--这个包是用来做健康度监控的--><dependency>  <groupId>org.springframework.boot</groupId>  <artifactId>spring-boot-starter-actuator</artifactId></dependency>
```

## 三、服务间通信

### RestTemplate

使用Spring中的RestTemplate直接调用

```java
RestTemplate restTemplate = new RestTemplate();String result = restTemplate.getForObject("http://localhost:8804/order", String.class);
```

调用服务的路径主机和服务端口直接写死在url中，无法实现负载均衡、如果提供服务的路径发生变化时不利于后续维护工作。

### Ribbon

#### Ribbon实现负载均衡调用

1. 在服务中引入ribbon依赖

```xml
<!--Ribbon--><dependency>  <groupId>org.springframework.cloud</groupId>  <artifactId>spring-cloud-starter-netflix-ribbon</artifactId></dependency>
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
> @Bean@LoadBalancedpublic RestTemplate restTemplate(){return new RestTemplate();}
> ```

#### Ribbon组件实现负载均衡原理

原理：根据调用服务的服务id去服务注册中心获取对应服务的服务列表，并将服务列表拉去到本地进行缓存，然后在本地通过默认的轮询策略在现有列表中选择一个可用节点提供服务

注意：客户端负载均衡

#### Ribbon负载均衡策略

```markdown
- RoundRobinRule							轮询策略- RandomRule									随机策略- AvailabilityFilteringRule		可用过滤策略	`会先过滤由于多次访问故障而处于断路器跳闸状态的服务，还有并发的连接数量超过阈值的服务，然后对剩余的服务列表按照轮询策略进行访问- RetryRule										重试策略	`先按照RoundrobinRule的策略获取服务，如果获取失败则在指定时间内进行重试，获取可用的服务。- WeightedResponseTimeRule		响应时间加权策略	`根绝平均响应的时间计算所有服务的权重，响应时间越快服务权重越大，被选中的概率越高，刚启动时如果统计信息不足，则使用RoundRobinRule策略，等统计信息足够会切换到 WeightedResponseTimeRule- BestAvailableRule						最低并发策略	`会先过滤由于多次访问故障而处于断路器跳闸状态的服务，然后选择一个并发量最小的服务
```

#### 修改Ribbon负载均衡策略

在配置文件中添加配置：

`服务id.ribbon.NFLoadBalancerRuleClassName=com.netflix.loadbalancer.RandomRule`

```yml
ORDERS:  ribbon:    NFLoadBalancerRuleClassName: com.netflix.loadbalancer.RandomRule
```

### OpenFeign

调用方引入依赖

```xml
<dependency>  <groupId>org.springframework.cloud</groupId>  <artifactId>spring-cloud-starter-openfeign</artifactId></dependency>
```

编写配置文件

```yml
server:  port: 8807spring:  application:    name: CATEGORY  cloud:    consul:      port: 8500      host: localhost
```

编写调用客户端

```java
@FeignClient(value = "PRODUCT") //用于服务的调用public interface ProductClient {    @RequestMapping("/product") // 需要调用的接口    String product();    @RequestMapping("/list") // 需要调用的接口    String list();}
```

调用服务

```java
@Autowiredprivate ProductClient productClient;@RequestMapping("/category")public String category(){    log.info("category service ...");    String product = productClient.product(); // 调用服务    String list = productClient.list(); // 调用服务    return "category ok!  " + product +"   "+ list;}
```

#### 服务间通信的参数传递

1. 零散类型的参数   

queryString方式传递参数： ?name=xxx

```java
// openFeign@GetMapping("/test")String test(@RequestParam("name") String name, @RequestParam("age") Integer age);// 业务接口@GetMapping("/test")public String test(@RequestParam("name") String name, @RequestParam("age") Integer age){...}
```

路径传递参数：/url/xxx/yyy

```java
// openFeign@GetMapping("/test1/{id}/{name}")String test1(@PathVariable("id") Integer id, @PathVariable("name") String name);// 业务接口@GetMapping("/test1/{id}/{name}")public String test1(@PathVariable("id") Integer id, @PathVariable("name") String name){...}
```

2. 传递对象类型参数   

Application/json方式：

```java
// openFeign@PostMapping("/test2")String test2(@RequestBody Product product);// 业务接口@PostMapping("/test2")public String test2(@RequestBody Product product){...}
```

#### OpenFeign超时时间

1. 默认的调用超时

使用OpenFeign组件在进行服务间通信时要求被调用服务必须在1s内给予响应，一旦服务执行业务逻辑时间超过2s，OpenFeign组件讲直接报错：

> Read timed out executing GET http://PRODUCT/test4

2. 修改OpenFeign超时时间

```yml
# 指定修改某个服务调用超时时间feign:  client:    config:      # PRODUCT: # 指定服务器      default: # 所有服务器        connectTimeout: 5000 # 配置服务器连接超时时间        readTimeout: 5000 # 配置服务器等待超时时间
```

```yml
# 修改所有服务调用超时时间feign:  client:    config:      PRODUCT: # 指定服务器      # default: # 所有服务器        connectTimeout: 5000 # 配置服务器连接超时时间        readTimeout: 5000 # 配置服务器等待超时时间
```

#### OpenFeign日志展示

OpenFeign是一个伪HttpClient客户端，用来帮助我们完成服务见通信，底层用http协议完成服务间调用

1. 日志

为了更方便在开发过程中调试OpenFeign数据传递和响应处理，OpenFeign在设计时添加了日志功能，默认OpenFeign日志功能是需要手动开启的。

2. 日志使用

首先设置OpenFeign日志级别

```yml
# 展示openFeign日志logging:  level:    com:      xiehongyu:        openfeign: debug
```

OpenFeign每个客户端提供一个日志对象

`NONE`  不记录任何日志

`BASIC`  仅仅记录请求方法、url、响应状态代码及执行时间

`HEADERS`  记录Basic级别的基础上，记录请求喝响应的header

`FULL`  记录请求喝响应的header、body和元数据，也就是展示全部http协议状态

```yml
feign:  client:    config:      # PRODUCT: # 指定服务器      default: # 所有服务器        loggerLevel: FULL
```



### Hystrix

#### Hystrix组件

##### 1. 服务雪崩

```markdown
# 1.服务雪崩- 在微服务之间进行服务调用时由于某一个服务故障，导致级联服务故障的现象，成为雪崩效应。雪崩效应描述的是提供方不可用，导致消费芳不可用并将不可用逐渐放大的过程。
```

ServiceA   -------->    ServiceB    -------->    ServiceC

```markdown
- 而此时，ServiceA的流量波动很大流量经常会突然性增加！那么在这种情况下，就算ServiceA能扛得住请求，ServiceB和ServiceC未必能扛得住这突发的请求。此时，如果ServiceC因为扛不住请求，变得不可用。那么ServiceB的请求也会阻塞，慢慢耗尽ServiceB的线程资源，ServiceB就会变得不可用。紧接着，ServiceA也会不可用。
```

##### 2. 服务熔断

```markdown
# 服务熔断- “熔断器”本身是一种开关装置，当某个服务单元发生故障之后，通过断路器(Hystrix)的故障监控，某个异常条件被触发，直接熔断整个服务。向调用方返回一个符合预期的、可处理的被选响应(FallBack)，而不是长时间的等待活着抛出调用方法无法处理的异常，就保证了服务调用方的县城不会被长时间占用，避免故障在分布式系统中蔓延，乃至雪崩。如果目标服务情况好转则恢复调用。服务熔断是解决服务雪崩的重要手段。
```



##### 3. 服务降级

```markdown
# 服务降级说明- 服务压力剧增的时候根据当前的业务情况及流量对一些服务和页面有策略的降级，以此缓解服务器的压力，以保证核心任务的进行。同事保证部分甚至大部分任务客户能得到正确的响应。也就是当前的请求处理不了了或出错了，给一个默认的返回。- 服务降级：关闭微服务系统中某些边缘服务，保证系统核心服务正常运行
```

##### 4. 降级和熔断总结

```markdown
# 1.共同点- 目标很一致，都是从可用性可靠性着想，为防止系统的整体缓慢甚至崩溃，采用的技术手段；- 最终表现类，对于二者来说，最终让用户体验到的是某些功能暂时不可达或不可用；- 粒度一般都是服务级别，当然，业界也有不少更细粒度的做法，比如做到数据持久层(允许查询，不允许增删改)；- 自治性要求很高，熔断模式一般都是服务基于策略的自动触发，降级虽说可人工干预，但在微服务架构下，完全靠人显然不可能，开关预置、配置中心都是必要手段；sentinel# 2.不同点- 出发原因不太一样，服务熔断一般是某个服务（下游服务）故障引起，而服务降级一般是从整体负荷考虑；- 管理目标的层次不太一样，熔断其实是一个框架及的处理，每个微服务都需要（无层级之分），而降级一般需要对业务有层级之分（比如降级一般是从最外围服务边缘服务开始）# 3.总结- 熔断必会触发降级，所以熔断也是降级的一种，区别在于熔断是对调用链路的保护，而降级是对系统过载的一种保护处理
```

##### 5. 服务熔断的实现

1. 引入Hystrix依赖

```xml
<dependency>  <groupId>org.springframework.cloud</groupId>  <artifactId>spring-cloud-starter-netflix-hystrix</artifactId></dependency>
```

2. 启动类开启熔断功能

```java
@SpringBootApplication@EnableDiscoveryClient@EnableCircuitBreaker // 开启Hystrix服务熔断public class HystrixApplication {...}
```

3. 在控制器方法中加入备选处理

```java
@GetMapping("hystrix")@HystrixCommand(fallbackMethod = "demoFallback")public String hystrix(Integer id){    System.out.println("hystrix demo");    if (id <= 0) {        throw new RuntimeException("无效ID！");    }    return "hystrix ok!!!";}public String demoFallback(Integer id){    return "当前活动过于火爆，服务已经熔断了！";}
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
# a.为每一个调用接口提供自定义备选处理- @HystrixCommand(fallbackMethod = "demoFallback")   // 熔断之后处理fallbackMethod 书写快速失败方法名# b. 使用Hystrix提供默认备选处理- @HystrixCommand(defaultFallback = "默认处理方法名")
```



##### 6. 服务降级的实现



##### 7. openfeign调用服务过程集成Hystrix实现默认备选处理

1. 引入Hystrix依赖

openfeign组件底层自动依赖Hystrix，项目无须引入

2. 开启openfeign对Hystrix支持

```yml
feign:  hystrix:    enabled: true # 开启openfeign在调用服务过程中 开启hystrix支持 默认没有开启
```

3. 开发openfeign服务调用失败默认处理的实现类

```java
/** * @Description: 自定义HystrixClient默认备选处理 * @Author: xiehongyu * @Date: 2021/9/7 15:43 */@Componentpublic class HystrixClientFallback implements HystrixClient{    @Override    public String hystrix(Integer id){        return "当前服务不可用，请稍后再试! id:" + id;    }}
```

4. 在openfeign客户端接口中的@FeignClients(value="服务id", fallback=默认处理.class)

```java
/** * @Description: openfeign客户端 * @Author: xiehongyu * @Date: 2021/9/7 15:43 */@FeignClient(value = "HYSTRIX", fallback = HystrixClientFallback.class)public interface HystrixClient {    @GetMapping("hystrix")    String hystrix(@RequestParam("id") Integer id);}
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

### Config

统一配置中心

作用：用来实现微服务系统中服务配置统一管理组件   netflix config ===>  spring config

组件：统一配置中心服务端（集中管理配置文件）、统一配置中心客户端client

#### Config 组件使用

##### Config Server

1. 引入Config Server 依赖

```xml
<dependency>
    <groupId>org.springframework.cloud</groupId>
    <artifactId>spring-cloud-config-server</artifactId>
</dependency>
```

2. 编写配置文件

```yml
server:
  port: 8848
spring:
  application:
    name: CONFIG-SERVER
  cloud:
    consul:
      host: localhost
      port: 8500
    config:
      server:
        git:
          uri: https://github.com/h395760313/configs.git  # 远程仓库地址
          default-label: main # 远程仓库分支
```

3. 启动类添加注解

```java
@SpringBootApplication
@EnableDiscoveryClient
@EnableConfigServer
public class ConfigServerApplication {...}
```

##### Config Client

1. 加入Config Client依赖

```xml
<dependency>
  <groupId>org.springframework.cloud</groupId>
  <artifactId>spring-cloud-config-client</artifactId>
</dependency>
```

2. 编写配置文件

**文件名必须使用bootstrap.yml 或 bootstrap.properties, 在启动时预先拉取远端配置信息到本地，然后以获取的配置信息启动**

```yml
spring:
  cloud:
    config:
      discovery:
        enabled: true # 告诉当前configclient统一配置中心在注册中心的服务id
        service-id: CONFIG-SERVER # 告诉当前configclient根据服务id去注册中心获取
      label: main # 1.确定分支
      name: configclient # 2.确认文件名
      profile: prod # 3.确定环境
    consul:
      port: 8500
      host: localhost
```

#### 手动配置刷新

当远端git仓库中配置发生变化时，不需要重启微服务就可以直接读取远端修改之后的配置信息。

##### 当前项目支持手动配置刷新

1. 在controller中加入`@RefreshScope`注解

```java
@RestController
@RefreshScope // 作用：用来在不需要重启微服务的情况下，将当前scope域中信息刷新为最新配置信息
public class ConfigClientController {...}
```

2. 修改完远端git仓库配置文件之后，向每一个微服务发送一个post请求

> curl -X POST http://8849/actuator/refresh

3. 必须在微服务配置文件中暴露远端配置刷新端点(endpoint)

```yml
management:
  endpoints:
    web:
      exposure:
        include: "*" # 开启所有web 端点暴露
```

### Bus

> Spring Cloud Bus links nodes of a distributed system with a lightweight message broker.

springcloud bus 利用轻量级消息中间件将分布式系统中所有服务连接到一起

作用：利用Bus广播特性，当一个状态(配置文件)发生改变时，通知到bus中所有服务节点更新当前状态(更新自身配置)

原理

![image-20210909170444054](/Users/xiehongyu/IdeaProject/springcloud_parent/images/image-20210909170444054.png)

#### 利用SpringcloudBus 实现远端配置修改自动更新

1. 准备RabbitMQ服务

2. 配置统一配置中心通过Bus连接到MQ服务

   1. 引入bug依赖

   ```xml
   <dependency>
     <groupId>org.springframework.cloud</groupId>
     <artifactId>spring-cloud-starter-bus-amqp</artifactId>
   </dependency>
   ```

   2. 编写配置文件

   ```yml
   rabbitmq:
     host: localhost
     port: 5672
     username: guest
     password: guest
   ```

   3. 重启config server

3. 配置微服务（config client）通过bug连接MQ服务

   1. 在所有微服务项目中引入bus服务

   ```xml
   <dependency>
     <groupId>org.springframework.cloud</groupId>
     <artifactId>spring-cloud-starter-bus-amqp</artifactId>
   </dependency>
   ```

   2. 在所有微服务项目中配置MQ链接配置，主要这段配置要放入远端仓库管理

   ```yml
   rabbitmq:
     host: localhost
     port: 5672
     username: guest
     password: guest
   ```

   3. 在引入bus依赖之后，重启所有微服务时出现如下错误

   ```java
   ***************************
   APPLICATION FAILED TO START
   ***************************
   
   Description:
   
   A component required a bean named 'configServerRetryInterceptor' that could not be found.
   
   
   Action:
   
   Consider defining a bean named 'configServerRetryInterceptor' in your configuration.
   ```

   错误原因：引入bus依赖启动立即根据配置文件bus配置连接MQ服务器，但是此时MQ配置信息都在远端，因此bus连接不到MQ，阻止了应用启动

   解决方案：允许项目启动时bus组件立即连接MQ这个失败，因为获取远端配置之后可以再以远端配置初始化bus组件，添加如下配置即可

   ```yml
   spring:
     cloud:
       config:
         fail-fast: true # 代表在启动时还没有拉取远端配置完成时的失败都是允许的
   ```

   4. 通过向config server 统一配置中心发送POST请求实现自动配置刷新

   注意：/actuator/bus-refresh 必须在config server 中暴露： 

   ```yml
   management:
     endpoints:
       web:
         exposure:
           include: "*" # 开启所有web 端点暴露
   ```

   > 刷新所有服务：curl -X POST "http://localhost:8848(configserverAddress)/actuator/bus-refresh"
   >
   > 刷新指定服务：curl -X POST "http://localhost:8848(configserverAddress)/actuator/bus-refresh/服务id"

#### 利用git中 webhooks(web 钩子)

1. 钩子hooks

根据仓库触发事件执行对应操作

2. webhooks

根据远程仓库触发对应事件发送一个web请求，这个请求默认就是POST请求方式

3. 在远端仓库中配置webhooks
   1. 添加webhooks

4. 首次配置完成后，会出现400错误，需要在configServer中加入filter配置

```java
package com.xiehongyu.filters;
 
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ReadListener;
import javax.servlet.ServletException;
import javax.servlet.ServletInputStream;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import javax.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
 
/**
 * 过滤器
 */
@Component
public class UrlFilter implements Filter {
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        HttpServletResponse httpServletResponse = (HttpServletResponse) response;

        String url = new String(httpServletRequest.getRequestURI());

        //只过滤/actuator/bus-refresh请求
        if (!url.endsWith("/actuator/bus-refresh")) {
            chain.doFilter(request, response);
            return;
        }

        //获取原始的body
        String body = readAsChars(httpServletRequest);

        System.out.println("original body:   " + body);

        //使用HttpServletRequest包装原始请求达到修改post请求中body内容的目的
        CustometRequestWrapper requestWrapper = new CustometRequestWrapper(httpServletRequest);

        chain.doFilter(requestWrapper, response);

    }

    @Override
    public void destroy() {

    }

    private class CustometRequestWrapper extends HttpServletRequestWrapper {
        public CustometRequestWrapper(HttpServletRequest request) {
            super(request);
        }

        @Override
        public ServletInputStream getInputStream() throws IOException {
            byte[] bytes = new byte[0];
            ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(bytes);

            return new ServletInputStream() {
                @Override
                public boolean isFinished() {
                    return byteArrayInputStream.read() == -1 ? true : false;
                }

                @Override
                public boolean isReady() {
                    return false;
                }

                @Override
                public void setReadListener(ReadListener readListener) {

                }

                @Override
                public int read() throws IOException {
                    return byteArrayInputStream.read();
                }
            };
        }
    }

    public static String readAsChars(HttpServletRequest request) {

        BufferedReader br = null;
        StringBuilder sb = new StringBuilder("");
        try {
            br = request.getReader();
            String str;
            while ((str = br.readLine()) != null) {
                sb.append(str);
            }
            br.close();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (null != br) {
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return sb.toString();
    }
}


```

在启动类加如下注解，包名指向filter位置

```java
@ServletComponentScan(basePackages = "com.xiehongyu.filters")
```

### SpringCloud总结

![image-20210910094927180](/Users/xiehongyu/IdeaProject/springcloud_parent/images/image-20210910094927180.png)

## SpringCloud Alibaba

![image-20210910100514388](/Users/xiehongyu/IdeaProject/springcloud_parent/images/image-20210910100514388.png)

### Nacos

Nacos组件  Name Service(服务注册中心)  Configuration Service（配置中心）

Nacos = Name + Configuration + Service

​			= Na       + Co                     +S

作用：服务注册中心、统一配置中心

#### Nacos的安装和配置

> 官网地址：https://nacos.io/zh-cn/

> github地址：https://github.com/alibaba/nacos

>下载地址：https://github.com/alibaba/nacos/releases

##### 启动Nacos

默认nacos启动以集群模式启动，必须满足多个节点

单机启动：sh bin/start.sh -m standalone

集群启动：sh bin/startup.sh -p embedded

查看日志：tail -f logs/nacos.log

##### 访问Nacos Web地址

http://localhost:8848/nacos

默认用户名和密码都是 nacos

#### Nacos Client开发

1. 引入依赖

```xml
<dependency>
  <groupId>com.alibaba.cloud</groupId>
  <artifactId>spring-cloud-starter-alibaba-nacos-discovery</artifactId>
</dependency>
```

2. 编写配置

```yml
server:
  port: 8081
spring:
  application:
    name: NACOSCLIENT
  cloud:
    nacos:
      server-addr: localhost:8848 # nacos server 总地址 包括注册与配置
      discovery:
        server-addr: ${spring.cloud.nacos.server-addr} # 注册nacos server 地址 默认为${spring.cloud.nacos.server-addr}
        service: ${spring.application.name} # 指定向nacos server注册服务名称 默认为${spring.application.name}
```

#### Nacos Config

Nacos 作为统一配置中心：

1. 管理配置文件方式是在自己坐在服务器上形成一个版本库，因此不需要再创建远程版本库
2. Nacos作为统一配置中心管理配置文件时，同样也是存在版本控制

##### Nacos Config 开发

1. 将自身配置交给Nacos管理

![image-20210910152444862](/Users/xiehongyu/IdeaProject/springcloud_parent/images/image-20210910152444862.png)

2. 引入Nacos Config Client依赖

```xml
<dependency>
    <groupId>com.alibaba.cloud</groupId>
    <artifactId>spring-cloud-starter-alibaba-nacos-discovery</artifactId>
</dependency>
<dependency>
    <groupId>com.alibaba.cloud</groupId>
    <artifactId>spring-cloud-starter-alibaba-nacos-config</artifactId>
</dependency>
```

3. 编写配置  要预先拉取远程配置，必须使用bootstrap.yml或者bootstrap.properties

```yml
spring:
  cloud:
    nacos:
      config:
        server-addr: localhost:8848 #config server 地址
        group: DEFAULT_GROUP # 从哪个组获取配置
        name: configclient-dev # 从这个组中获取哪个配置文件
        file-extension: yml # 配置文件的后缀名
```

4. 在控制器加入刷新注解

```java
@RestController
@RefreshScope
public class DemoController {...}
```

##### Nacos Config 细节

1. dataId细节： 代表完整配置文件名称 ===> spring.cloud.nacos.config.name

   ​						完整配置文件名称 = 前缀(prefix) + 环境(env) + 后缀(file-extension)

dataId = spring.cloud.nacos.config.name + spring.cloud.nacos.config.file-extension

dataId = ${prefix} + ${spring.profile.active}.${file-extension}

2. 微服务拉取配置

   a. 第一种获取方式

```yml
spring:
  cloud:
    nacos:
      config:
        server-addr: localhost:8848 #config server 地址
        group: DEFAULT_GROUP # 从哪个组获取配置
        # 第一种 根据文件名(name) + 后缀(extension) 获取远程配置
        name: configclient-dev # 从这个组中获取哪个配置文件
        file-extension: yml # 配置文件的后缀名
```



​		b. 第二种获取方式

```yml
spring:
  cloud:
    nacos:
      config:
        server-addr: localhost:8848 #config server 地址
        group: DEFAULT_GROUP # 从哪个组获取配置
        # 第二种 根据 前缀(prefix) + 环境(env) + 后缀(file-extension) 获取远程配置
        prefix: configclient # 前缀(prefix)
        file-extension: yml # 后缀(extension)
  profiles:
    active: dev # 环境(env)
```

3. 统一配置中心 nacos 三个重要概念

> 命名空间：namespace 默认nacos安装按成之后会有一个默认命名空间，这个命名空间名字为public。
>
> ​		作用：站在项目的角度隔离每一个项目配置文件

> 组：group  默认nacos中在管理配置文件时不指定group时默认的组名称为DEFAULT_GROUP
>
> ​		作用： 站在项目中每个服务角度，隔离同一个项目中不同服务的配置

> 文件名： dataId  获取一个配置文件唯一标识

配置迁移可导入导出

在历史版本可回退配置版本

#### Nacos 持久化

持久化： 管理的配置信息持久化

注意：默认nacos存在配置信息持久化，默认的持久化方式为内嵌数据库derby

缺点：无法友好的展示数据

官方建议：在生产情况下推荐将配置存入mysql数据库 注意：nacos到目前为止仅仅支持myql

##### 将nacos持久化到mysql中

1. 准备mysql

2. 建立一个数据库nacos_config

3. 在nacos_config库中执行nacos-mysql.sql

4. 修改nacos配置文件持久化信息到mysql中

   ```properties
   ### If use MySQL as datasource:
   spring.datasource.platform=mysql
   
   ### Count of DB:
   db.num=1
   
   ### Connect URL of DB:
   db.url.0=jdbc:mysql://127.0.0.1:3306/nacos_config?characterEncoding=utf8&connectTimeout=1000&socketTimeout=3000&autoReconnect=true&useUnicode=true&useSSL=false&serverTimezone=UTC
   db.user.0=root
   db.password.0=root
   ```

   

#### Nacos集群搭建

> 1.将nacos中data目录删除
>
> 2.将nacos copy 三份 nacos01 nacos02 nacos03
>
> 3.在nacos01 nacos02 nacos03的cluster.conf中均加入	
>
> ​		127.0.0.1:8845
> ​		127.0.0.1:8846
> ​		127.0.0.1:8847
>
> 4.修改nacos01 nacos02 nacos03的端口
>
> 5.分别启动nacos01 nacos02 nacos03

#### Nginx实现Nacos集群高可用

> 1.在nginx配置中  http内  server外加入配置
>
> ​		upstream nacos-servers {
>
> ​				server 127.0.0.1:8845;
>
> ​				server 127.0.0.1:8846;
>
> ​				server 127.0.0.1:8847;
>
> ​		}
>
> 2.修改
>
> ​		location / {
>
> ​				proxy_pass http://nacos-server/;
>
> ​		}
