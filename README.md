搭建指南：

一、项目环境

	后端：Spring Boot 2.4 + MyBatis-Plus + MySQL + Redis + RocketMQ + Spring WebSocket
	前端：Vue3 + TypeScript + Ant Design Vue + ECharts + Vuex
	支撑层：Swagger + MyBatis-Plus Generator + ESLint

二、后端搭建

 	拉取项目后，找到obwiki-backend目录下的wiki.sql和x.sql文件，把他们运行到自己的MySql数据库里（运行sql文件前 先创建数据库表obwiki)

	找到application.properties配置文件，修改自己的数据库账号密码

 	启动项目

三、前端初始化

	终端安装依赖model：终端输入npm install

 	安装好后，输入npm run serve-dev 启动项目

四、设置密码

	找到登录按钮，用户名test 密码输入你自己想要的，然后点击登录，再去idea控制台里找到req的输出信息，
 
 	把那加密后的密码复制到数据库表里面，再次登录即可成功
