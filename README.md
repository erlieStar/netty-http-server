# 用netty搭建http服务器

niodemo包是一些LZ学习io（bio）和nio，aio时写的一些小例子，客户端输入字符，服务端转成大写并返回

nettyserver包是用netty搭建http服务器的代码

## 用处

对于一个web服务，有很多模块，每个模块前端都得配置地址，就用netty搭了一个http服务，前端的请求统一访问这个http服务，
然后这个服务通过RPC调用各个子模块的接口，返回json

## 思路

对于每个请求必须填写一个methodName参数，用methodName参数的前缀来判断应该用哪个service，然后通过反射生成这个类，
并且通过这个methodName参数来调用响应的方法返回结果, 所有service都要继承BaseHttpSerice这个类，因为要通过统一的参数来反射生成类

## 访问

> GET   [http://localhost:8080/?methodName=userDetail&username=张三](http://localhost:8080/?methodName=userDetail&username=张三)

> GET   [http://localhost:8080/?methodName=productDetail](http://localhost:8080/?methodName=productDetail)

当然你可以指定任何前缀，只不过这个项目没有用前缀的内容，如

> GET   [http://localhost:8080/req?methodName=userDetail&username=张三](http://localhost:8080/req?methodName=userDetail&username=张三)

![欢迎fork和star](https://github.com/erlieStar/image/blob/master/%E6%AC%A2%E8%BF%8Efork%E5%92%8Cstar.jpg)