# RESTful架构

## 1 什么是REST

REST这个词，是 [Roy Thomas Fielding](https://en.wikipedia.org/wiki/Roy_Fielding) 在他2000年的 [博士论文](http://www.ics.uci.edu/~fielding/pubs/dissertation/top.htm) 中提出的。

> Fielding是一个非常重要的人，他是HTTP协议（1.0版和1.1版）的主要设计者、Apache服务器软件的作者之一、Apache基金会的第一任主席。所以，他的这篇论文一经发表，就引起了关注，并且立即对互联网开发产生了深远的影响。

Fielding 将他对互联网软件的架构原则，定名为REST，即 **Representational State Transfer** 的缩写。我对这个词组的翻译是**表现层状态转化**。

> 如果一个架构符合REST原则，就称它为RESTful架构。

### 1.1 资源（Resources）

REST的名称"表现层状态转化"中，省略了主语。"表现层"其实指的是"资源"（Resources）的"表现层"。

所谓"资源"，就是网络上的一个实体，或者说是网络上的一个具体信息。它可以是一段文本、一张图片、一首歌曲、**一种服务**，总之就是一个具体的实在。你可以用一个URI（统一资源定位符）指向它，每种资源对应一个特定的URI。要获取这个资源，访问它的URI就可以，因此URI就成了每一个资源的地址或独一无二的识别符。

所谓"上网"，就是与互联网上一系列的"资源"互动，调用它的URI。

### 1.2 表现层（Representation）

"资源"是一种信息实体，它可以有多种外在表现形式。我们把"资源"具体呈现出来的形式，叫做它的"表现层"（Representation）。

比如，文本可以用txt格式表现，也可以用HTML格式、XML格式、JSON格式表现，甚至可以采用二进制格式；图片可以用JPG格式表现，也可以用PNG格式表现。

URI只代表资源的实体，不代表它的形式。严格地说，有些网址最后的".html"后缀名是不必要的，因为这个后缀名表示格式，属于"表现层"
范畴，而URI应该只代表"资源"的位置。它的具体表现形式，应该在HTTP请求的头信息中用`Accept`和`Content-Type`
字段指定，这两个字段才是对"表现层"的描述。

### 1.3 状态转化（State Transfer）

访问一个网站，就代表了客户端和服务器的一个互动过程。在这个过程中，势必涉及到数据和状态的变化。

互联网通信协议HTTP协议，是一个无状态协议。这意味着，所有的状态都保存在服务器端。因此，如果客户端想要操作服务器，必须通过某种手段，让服务器端发生"状态转化"（State Transfer）。而这种转化是建立在表现层之上的，所以就是"表现层状态转化"。

客户端用到的手段，只能是HTTP协议。具体来说，就是HTTP协议里面，四个表示操作方式的动词：GET、POST、PUT、DELETE。它们分别对应四种基本操作：GET用来获取资源，POST用来新建资源（也可以用于更新资源），PUT用来更新资源，DELETE用来删除资源。

## 2 RESTful API 设计指南

### 2.1 请求（Request)

#### 2.1.1 协议 (Protocal)

```
HTTPs
```

#### 2.1.2 域名（Domain）

##### 专用子域名

```
https://api.example.com
```

##### 放主域名后面

```
https://www.example.com/api
```

#### 2.1.3 版本（Versioning）

##### 放在URL里

```
https://api.example.com/v1
https://www.example.com/api/v1
```

##### Accept HTTP Header

```http
Accept: application/json+v1
```

##### 自定义 HTTP Header

```http
X-Api-Version: 1
```

参考 github: https://docs.github.com/en/rest/overview/api-versions

```http
X-GitHub-Api-Version:2022-11-28
```

##### 失效或者迁移的API

随着系统发展，总有一些API失效或者迁移（最好不要），

对失效的API，返回404 not found 或 410 gone；

对迁移的API，返回 301 重定向。

#### 2.1.4 路径（Endpoint）

在RESTful架构中，每个网址代表一种资源（resource）的 "集合"（collection），所以网址中不能有动词，只能有名词，API中的名词也应该使用复数。

```
https://api.example.com/v1/books
https://www.example.com/api/v1/authors
```

#### 2.1.5 HTTP 动词 (HTTP Method)

对于资源的具体操作类型，由HTTP动词表示。

##### 常用的HTTP动词有下面五个

- GET（SELECT）：从服务器取出资源（一项或多项）。
- POST（CREATE）：在服务器新建一个资源。
- PUT（UPDATE）：在服务器更新资源（客户端提供改变后的完整资源）。
- PATCH（UPDATE）：在服务器更新资源（客户端提供改变的属性）。
- DELETE（DELETE）：从服务器删除资源。

##### 还有两个不常用的HTTP动词

- HEAD：获取资源的元数据。
- OPTIONS：获取信息，关于资源的哪些属性是客户端可以改变的。

##### 一些例子

- GET /books 列出所有的书
- GET /books/{ISBN} 列出指定的书
- POST /books 添加一本书
- PUT /books/{ISBN} 修改一本书（全量）
- PATCH /books/{ISBN} 修改一本书一引起属性（部分）
- DELETE /books/{ISBN} 删除一本书

##### 冷知识：HTTP Method Override

有些客户端只能使用`GET`和`POST`这两种方法。服务器必须接受`POST`模拟其他三个方法（`PUT`、`PATCH`、`DELETE`）。

这时，客户端发出的 HTTP 请求，要加上`X-HTTP-Method-Override`属性，告诉服务器应该使用哪一个动词，覆盖`POST`方法。

```http
POST /api/users/4 HTTP/1.1  
X-HTTP-Method-Override: PUT
```

上面代码中，`X-HTTP-Method-Override`指定本次请求的方法是`PUT`，而不是`POST`。

#### 2.1.6 过滤信息（Filtering）

一般用于 GET /resources 的 filter

```
GET /resources?page=2&size=10&sort=name&order=asc
```

#### 2.1.7 Request Content Type

- Content-Type: application/json

```http
POST /api/v1/users
Content-Type: application/json
 
{
    "name": "user",
    "password": "secured"
}
```

- Content-Type: application/x-www-form-urlencoded (浏览器POST表单用的格式)

```http
POST /api/v1/users
Content-Type: application/x-www-form-urlencoded
 
username=user&password=secured
```

- Content-Type: multipart/form-data; boundary=—-RANDOM_jDMUxq4Ot5 (表单有文件上传时的格式)

#### 2.1.8  Content Negotiation

资源可以有多种表示方式，如json、xml、pdf、excel等等，客户端可以指定自己期望的格式，通常有两种方式：

- HTTP Header Accept：

```http
Accept:application/xml;q=0.6,application/atom+xml;q=1.0
```

q为各项格式的偏好程度

- url中加文件后缀

```
/api/v1/users.json
/api/v1/users/1.xml
```

### 2.2 返回（Response）

#### 2.2.1 状态码（Status Codes）

1xx (Informational Response) 和 3xx (Redirection) 基本用不到，主要是2x x(Success)，4xx (Client Errors) 和 5xx (Server
Errors)

##### 2xx (Success)

- 200 OK - [GET]：服务器成功返回用户请求的数据，该操作是幂等的（Idempotent）。
- 201 CREATED - [POST/PUT/PATCH]：用户新建或修改数据成功。
- 202 Accepted - [*]：表示一个请求已经进入后台排队（异步任务）
- 204 NO CONTENT - [DELETE]：用户删除数据成功。

##### 4xx (Client Errors)

- 400 INVALID REQUEST - [POST/PUT/PATCH]：用户发出的请求有错误，服务器没有进行新建或修改数据的操作，该操作是幂等的。
- 401 Unauthorized - [*]：表示用户没有权限（令牌、用户名、密码错误）。
- 403 Forbidden - [*] 表示用户得到授权（与401错误相对），但是访问是被禁止的。
- 404 NOT FOUND - [*]：用户发出的请求针对的是不存在的记录，服务器没有进行操作，该操作是幂等的。
- 405 Method Not Allowed：用户已经通过身份验证，但是所用的 HTTP 方法不在他的权限之内。
- 410 Gone -[GET]：用户请求的资源被永久删除，且不会再得到的。
- 415 Unsupported Media Type：客户端要求的返回格式不支持。比如，API 只能返回 JSON 格式，但是客户端要求返回 XML 格式。
- 429 Too Many Requests (RFC 6585)：客户端的请求次数超过限额。

##### 5xx (Server Errors)

- 500 INTERNAL SERVER ERROR - [*]：服务器发生错误，用户将无法判断发出的请求是否成功。
- 503 Service Unavailable：服务器无法处理请求，一般用于网站维护状态。

#### 2.2.2 Response Content Type

推荐使用 `JSON` 格式返回，当然用 `XML` 也没有人拦着。

#### 2.2.3 Response Body

- GET /collection: 返回资源对象的集合
- GET /collection/resource: 返回单独的资源对象
- POST /collection: 返回新创建的资源对象
- PUT /collection/resource: 返回完整的资源对象
- PATCH /collection/resource: 返回完整的资源对象
- DELETE /collection/resource: 返回一个空文档

#### 2.2.4 错误处理（Error handling）

参考 [Restful API 中的错误处理](https://scarletsky.github.io/2016/11/30/error-handling-in-restful-api/)，Error
handling的最大的分歧就是国内使用比较多的 `200 模型`：

总是返回 Http Status 200，好处是对于 Client，服务端返回成功或失败数据类型一致，缺点是多了一层解析， 客户端可能会缓存成功的http请求。

```json
{
  "success": 0,
  "status": 401,
  "message": "error message",
  "data": {
  }
}
```

推荐的错误处理：

- 服务器端返回正确的 HTTP Status Code， 客户端第一步根据此Code, 再决定是否解析 Response Body

- 如果有错误，使用统一的返回结果

  ```json
  {
    "code": 40020,
    "message": "error message",
    "trackingId": "uuid"
  }
  ```

## 3 最佳实践 (Best Practices)

### 3.1 URL

- 字母小写

- 使用`-`，而不是`_`

- Resources使用复数形式

- 避免过长 `/authors/1/books/isbn` => `/books/isbn?author=1`

- Composite Resources URL 导航

  比如有二个 Resources: authors, books。很明显：authors, books，可以互相导航

  ```
  GET /authors/ID
  GET /books/ISBN/authors
  GET /books/ISBN/authors/ID
  
  GET /books/ISBN
  GET /authors/ID/books
  GET /authors/ID/books/ID
  ```

  但有一种组合实体不是 first-class 的实体，它的生命周期完全依赖父实体，无法独立存在，在实现上通常是对数据库表中某些列的抽象，不直接对应表，也无id。一个常见的例子是
  Author — Address，Address是对
  Author表中zipCode/country/city三个字段的简单抽象，无法独立于Author存在。必须通过Author索引到Address：`GET /authors/1/addresses`

- 标签化；对于经常使用的、复杂的查询标签化，降低维护成本。比如
  ```
  GET /trades?status=closed&sort=created,desc
  ```
  标签化后
  ```
  GET /trades#recently-closed
  GET /trades/recently-closed
  GET /trades?recently-closed=true(Better)
  ```

### 3.2 服务型的资源

除了资源简单的CRUD，服务器端经常还会提供其他服务，这些服务无法直接用上面提到的URI映射。如：

- 按关键字搜索
- 银行系统转账

可以把这些服务看成资源，计算的结果是资源的presentation，按服务属性选择合适的HTTP方法。

```http
### Search Service
GET /search?q=keyword&size=100&order=name&sort=asc
```

```http
### Transfer Service
POST /transfer
Content-Type: application/json
 
{
    "from": 1,
    "to": 2,
    "amount": 100
}
```

### 3.3 Long-running task service

对于比较耗时的服务，客户端提交数据后，服务器端会起一个异步线程去做这个任务，同时马上返回给客户端相着的信息，以供客户端再次发出请求。

1. Request

```http
GET /long-time-search?q=keyword
```

1. Response

```json
{
  "id": "uuid"
}
```

1. Request

```http
GET /long-time-search/{id}
```

#### 参考

- [Long-running searches](https://www.elastic.co/guide/en/elasticsearch/reference/current/async-search-intro.html)
- [Async search](https://www.elastic.co/guide/en/elasticsearch/reference/current/async-search.html#submit-async-search)

### 3.4 Response JSON

#### 3.4.1 建议返回`JSON`对象，而不是`JSON`数组

对于 `GET /resources` 的 Response, 最好不要返回`JSON`数组，否则无法扩展，建议返回`JSON`对象, 比如需要加个分页的需求，参考一下Spring
data 的分页返回。

```json
{
  "pagination": {
    "page": 2,
    "size": 10,
    "total": 1024
  },
  "data": [
    {},
    {}
  ]
}
```

#### 3.4.2 不返回`null`字段

这个简单，如果使用`spring-boot`，一个配置的事情

## 4 安全性和幂等性

1. **安全性**：不会改变资源状态，可以理解为只读的；
2. **幂等性**：执行1次和执行N次，对资源状态改变的效果是等价的。

|        | 安全性 | 幂等性 |
|:-------|:----|:----|
| GET    | √   | √   |
| POST   | ×   | ×   |
| PUT    | ×   | √   |
| DELETE | ×   | √   |

安全性和幂等性均不保证反复请求能拿到相同的 Response。以 DELETE 为例，第一次DELETE返回 204 表示删除成功，第二次返回 404
提示资源不存在，这是允许的。

## 5 超媒体API（Hypermedia API）

Hypermedia API的设计被称为[HATEOAS](https://en.wikipedia.org/wiki/HATEOAS)
。Github的API就是这种设计：参考 [api.github.com](https://api.github.com/)，不赘述。

## 6 参考（Reference）

- 阮一峰的博客
    - [理解RESTful架构](https://www.ruanyifeng.com/blog/2011/09/restful.html)
    - [RESTful API 设计指南](https://www.ruanyifeng.com/blog/2014/05/restful_api.html)
    - [RESTful API 最佳实践](https://www.ruanyifeng.com/blog/2018/10/restful-api-best-practices.html)
- [restful-api-design-references](https://github.com/jinqinghua/restful-api-design-references)
- [Restful API 的设计规范](https://novoland.github.io/%E8%AE%BE%E8%AE%A1/2015/08/17/Restful%20API%20%E7%9A%84%E8%AE%BE%E8%AE%A1%E8%A7%84%E8%8C%83.html)
- [Principles of good RESTful API Design](https://www.cnblogs.com/moonz-wu/p/4211626.html)
- [api.github.com](https://api.github.com/)
- [List_of_HTTP_status_codes](https://en.wikipedia.org/wiki/List_of_HTTP_status_codes)
- [Restful API 中的错误处理](https://scarletsky.github.io/2016/11/30/error-handling-in-restful-api/)
- [理解 HTTP 幂等性](http://www.cnblogs.com/weidagang2046/archive/2011/06/04/2063696.html)