欢迎来到它山石!
============

**它山石**是一个由个人开发并维护的应用基础平台，专注于提供快速开发各类应用软件所需的技术框架及常用功能组件，以此为基础，应用开发者可以专注于业务逻辑本身的开发，从而极大的减轻开发工作量，同时还能把不同的应用无缝衔接到统一的基础平台之上，自动实现**统一用户登陆（SSO）、统一资源授权、统一聚合展示（门户）。**  目前提供的常用功能组件有：

- 它山石技术框架，包括后端框架和前端框架
- 统一用户管理（UM）
- 门户管理（Portal）
- 内容管理（CMS）
- 数据缓存管理
- 系统参数管理
- 系统日志管理




###它山石技术框架
-------------

主要开发语言为JAVA和javascript，数据库支持MySQL、H2、Oracle。后端采用hibernate3.5 + spring3.2 + spring mvc。

- Hibernate负责对业务对象进行持久化和常用查询，同时提供自定义的数据库连接池，允许同时对多数据源进行访问。
- Spring提供IOC容器及AOP机制，对业务操作对象的生成、依赖注入、事务、权限过滤、日志搜集、缓存等进行统一管理，。
- Spring MVC将后端服务发布成web服务，采用restful风格的接口定义形式。
- Freemarker提供模板引擎解析功能
- 前后端交换一律采用AJAX，数据格式同时支持XML和JSON
- 前端框架采用独立的自主开发的它山石web框架，使用纯HTML5 + CSS3 + Javascript，提供统一的界面样式布局，及菜单、Tree、Grid、Form等常用组件,且组件数据的格式和后端接口服务提供的一致。

> **Note:**

> - 技术框架最大特色是量身定制，较少采用各类流行但体量庞大的框架，从而最大限度的实现易维护、易扩展.
> - 前后端充分解耦合，开发时各自独立进行、互不依赖，只需彼此约定好数据格式，前端直接依据模拟数据文件进行开发，待后端开发好正式的接口服务，前端将 **数据服务地址** 由模拟文件替换为后端正式的服务地址即可.

#### 统一用户管理（UM）

The document panel is accessible using the <i class="icon-folder-open"></i> button in the navigation bar. You can create a new document by clicking <i class="icon-file"></i> **New document** in the document panel.

#### 门户管理（Portal）

All your local documents are listed in the document panel. You can switch from one to another by clicking a document in the list or you can toggle documents using <kbd>Ctrl+[</kbd> and <kbd>Ctrl+]</kbd>.

#### 内容管理（CMS）

You can rename the current document by clicking the document title in the navigation bar.

#### 数据缓存管理

You can delete the current document by clicking <i class="icon-trash"></i> **Delete document** in the document panel.

#### 系统参数管理

You can save the current document to a file by clicking <i class="icon-hdd"></i> **Export to disk** from the <i class="icon-provider-stackedit"></i> menu panel.

#### 系统日志管理
