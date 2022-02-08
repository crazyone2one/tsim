![GitHub](https://img.shields.io/github/license/crazyone2one/tsim)
![echarts](https://img.shields.io/badge/echarts-5.1.2-blue.svg)
![jquery](https://img.shields.io/badge/jquery-3.6.0-blue.svg)
![bootstrap](https://img.shields.io/badge/bootstrap-5.1.1-blue.svg)  
# tsim   
测试用例、项目、缺陷管理系统。   
参照test-link功能流程设计的本系统。

## 技术选型
* 核心框架 spring boot (2.5.4)  
* 持久层 mybatis-plus(3.4.1)
* 数据库连接池 Alibaba Druid
* 前端组件
  > thymeleaf  
  > bootstrap(5.1.1)  
  > jQuery
* 数据库
  > mysql(5.7)
## 启动说明
* 启动前 请配置 application.yml 中相关地址
* 数据库脚本位于 sql 下面，启动前请自行导入
* 配置完成，运行Application
## 开发功能
> 项目管理模块
>> 进入系统后台可以在左边的菜单中选择项目管理模块，进入后可以对待测试的项目进行添加、修改、删除操作。也可以根据需要输入组件条件进行搜索查询列表。 
![img.png](pic/project.png)

> 测试需求管理
>> 进入系统后台可以在左边的菜单中选择需求模块，进入后可以对测试需求进行添加、修改、删除操作。也可以根据需要输入组件条件进行搜索查询列表。
![img.png](pic/story.png)

> 测试计划管理
>> 进入系统后台可以在左边的菜单中选择测试计划模块，进入后可以对测试计划进行添加、修改、删除操作。也可以根据需要输入组件条件进行搜索查询列表。
![img.png](pic/plan.png)
 
> 测试用例  
>> - [x] 添加
>> - [ ] 添加测试用例时若选择不存在任务时间的项目，无法保存
> 
> 统计图
## 版权信息
该项目签署了MIT 授权许可，详情请参阅[LICENSE](https://github.com/crazyone2one/tsim/blob/main/LICENSE)
