                                          Sentinel如何实现支持全局接口限流功能
# 目的  
  在实际应用中， 一个服务可能有上百个接口，如果每个接口都去配置限流策略， 会非常繁琐，如果接口没有做独立的限流配置，那么就采用全局的配置策略。<br>
  查阅官方资料，也有提出相似的问题， 并且也给出了代码实现，但官方目前还没正式发布推出，那么就需要我们改造源码去做对应的实现。<br>

# 实现  
两部分改造， 一部分是修改Sentinel-Core核心组件， 另一部分是增加sentinel-extension插件，通过SPI扩展方式实现。<br>
在实际应用中， POM要加入重新修改源码后的sentinel-core依赖，并且要加入sentinel-configure-global依赖， 才能实现全局流控规则管理。

# 操作  
为了与普通资源的流控规则区分，新增流控规则资源名必须以"global:"开头，后台匹配时会自动识别过滤匹配,支持正则匹配。比如global:.*则表示所有接口生效。<br>


注：基于Sentinel1.8.5版本改造
  