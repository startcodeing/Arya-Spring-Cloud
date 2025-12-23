# 1.sentinel
## sentinel集中资源限流的异常处理方式
```
controller接口：通过自定义BlcokExceptionHandler异常处理
service层使用@SentinelResource标记的资源：通过callback方法返回兜底数据，如果没有配置callback，会使用默认的fallback处理机制（返回默认错误页面），
但是在项目中需要配置全局异常，用于兜底业务中没有处理的所有异常
OpenFegin调用：通过OpenFegin的callback方法返回兜底数据,如果沒有配置callback处理类，最后还是会走到SpringBoot的全局异常处理器
```