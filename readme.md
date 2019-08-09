# Introduce
This SEA (SunRun Email Analysis System) project is used to analysis email files.You can used it induce your tasks.

SEA now is used to analysis GZ Customs. They get the email file from suspects and read info from it, but as we know, the amount of file is so large that it is impossible for human resource to complete this 
work, so they need use some software to help with this progress. 

SEA is the best choice, it not only can parse the mail file quickly, but also can extract the information of the mail intelligently and extract some information closely related to the business.
Help the case handlers get the information that they care about at the first time.

# Package
1. Install the maven software in your computer;
2. Run command: `mvn clean package -DskipTests`;
3. If your select the gitlab to copy the project, you may be need to ask the develop to get the nlp resource(that contain more that 1G space, so developer not upload to gitlab).

# Command
**STATE**
```
jstat -class pid:显示加载class的数量，及所占空间等信息。
jstat -compiler pid:显示VM实时编译的数量等信息。
jstat -gc pid:可以显示gc的信息，查看gc的次数，及时间。其中最后五项，分别是young gc的次数，young gc的时间，full gc的次数，full gc的时间，gc的总时间。
jstat -gccapacity:可以显示，VM内存中三代（young,old,perm）对象的使用和占用大小，如：PGCMN显示的是最小perm的内存使用量，PGCMX显示的是perm的内存最大使用量，PGC是当前新生成的perm内存占用量，PC是但前perm内存占用量。其他的可以根据这个类推， OC是old内纯的占用量。
jstat -gcnew pid:new对象的信息。
jstat -gcnewcapacity pid:new对象的信息及其占用量。
jstat -gcold pid:old对象的信息。
jstat -gcoldcapacity pid:old对象的信息及其占用量。
jstat -gcpermcapacity pid: perm对象的信息及其占用量。
jstat -util pid:统计gc信息统计。
jstat -printcompilation pid:当前VM执行的信息。
除了以上一个参数外，还可以同时加上 两个数字，如：jstat -printcompilation 3024 250 6是每250毫秒打印一次，一共打印6次，还可以加上-h3每三行显示一下标题。
```

**jstack**
```
Usage:
    jstack [-l] <pid>
        (to connect to running process)
    jstack -F [-m] [-l] <pid>
        (to connect to a hung process)
    jstack [-m] [-l] <executable> <core>
        (to connect to a core file)
    jstack [-m] [-l] [server_id@]<remote server IP or hostname>
        (to connect to a remote debug server)

Options:
    -F  to force a thread dump. Use when jstack <pid> does not respond (process is hung)
    -m  to print both java and native frames (mixed mode)
    -l  long listing. Prints additional information about locks
    -h or -help to print this help message
```

**jmap**
```
jmap -heap [pid]
```


# Time Line
**week**
下周计划：
1、对同一目录下所涉邮件，可按照收发件时间进行设置，搜索出所需时间段内的邮件;
2、支持对附件单一或批量查阅、导出。导出数据包含所在邮件的基本要素（收、发件人、主题等） 。
3、支持目标邮件地址监控报警功能，将需要监控的邮箱或地址列入监控目标，当系统中有导入该邮箱或地址时，系统会提醒或报警。
4、修复当前版本存在的问题；

**2019.06.24 - 2019.06.28**
1. 修改旧版本问题；

2. 海关缉私局了解云盘问题；
3. 海关缉私局探讨邮件系统开发问题；

4. 发布邮件系统1.5版本；

**2019.06.17 - 2019.06.21**
1. 完善备注模块接口，支持备注的修改与删除；
2. 修复邮件解析监控接口返回数据格式；

3. 修改检索条件，支持对同一目录下所涉邮件，可按照收发件时间进行设置，搜索出所需时间段内的邮件;
4. 设计邮件告警模块；

5. 解决大附件内容解析问题的BUG；
6. 设计邮件告警模块管理接口；

7. 邮件解析模块支持告警配置；
8. 邮件获取支持告警文件条件查询；

9. 发布SEA 1.4版本包；
10. 完善邮件告警模块；



**2019.06.10 - 2019.06.14**
1. 修复解析邮件监控模块的监控问题；
2. 完善域名、账户、邮件三级体系模块；
3. 提取网络上有关IP地址的数据条目，采用离线IP查询；

4. 替换旧IP查询，重新构建IP查询模块；
5. 修复域名数据清洗接口，改解析过程执行；

6. 增加更多工具接口，例如查询邮政、地址信息等；

7. 开发附件管理模块；

8. 修复邮件解析过程中，部分邮件解析失败的问题；
9. 完善附件管理模块；




**2019.06.03 - 2019.06.07**
1. 调试解析过程中的线程卡顿问题；

2. 关键字、特征值检索邮件列表时，新增查询参数，支持模糊查询；
3. 调试云盘下载文件问题；
4. 编写域名三级邮件显示模块接口；

5. 组织体检；
6. 编写域名三级邮件显示模块接口；

7. 完善域名三级邮件显示模块接口；
8. 修改IP、物理地址映射模块接口；
9. 发布1.3版本；

**2019.05.27 - 2019.05.31**
1. 修复旧版本问题：特征三列显示邮件查询问题、特征提取非法空格问题； 
2. 开发关键字模块，构建关键字模块基本架构；

3. 后台加入图像识别功能，该功能交由于斯亮同时处理，并向其介绍后台项目架构、数据库架构等；
4. 开发关键字模块，添加功能接口；

5. 开发关键字模块，完善功能接口，并将模块功能确定，例如提供可通过关键字获取对应的邮件列表功能由特征值模块连接提供；

6. 开发工具模块：可根据IP地址获取对应的物理地址信息，例如所在城市、ISOCODE以及邮政编码等；

7. 邮件解析增加域名识别，可通过三级目录（域名、邮箱、邮箱涉及邮件）邮件列表；


**2019.05.20 - 2019.05.24**
1. 发布邮件系统的1.2版本；

2. 根据提出的需求，修改特征模块的设计方式；
3. 特征模块接口返回进行合并，并且提供通过特征值获取关联列表的接口；

4. IAM新版本云盘用户对接测试；
5. 修改邮件解析过程中对各个字段的索引规则；

6. 邮件搜索等加入更复杂的查询模式，例如支持发送时间段、IP 地址等搜索模式；
7. 修改邮件解析接口，将中英文解析配置合并为单一接口；

8. 前往黄埔海关学习其所使用的邮件系统；
9. 设计关键字功能模块，理清功能点以及是否划分为一个模块；

**2019.05.13 - 2019.05.17**
1. 实体相关解析信息中添加附件信息，方便进行第二步需要的相关附件信息，进行实体打标绑定；
2. 新增附件打标模块，目前将其达标功能映射到实体解析处，将附件的信息有实体信息进行绑定；

3. 添加附件列表查询模块，支持对各种类型附件的查询（类型（大类型、小类型）、文件名）；
4. 统计附件信息支持对所有项目的操作，而不是单一项目；

5. 添加邮件收发关系（收件人、发件人以及抄送人等为主体）统计模块；
6. 修改特征查询方式，支持组合查询模式；

7. 修改wiki接口文档，完善参数映射表，方便其他开发者快速上手开发相关信息以及客户端开发者查阅接口；
8. 完善特征查询过程中出现的BUG；

9. 完善收发关系统计信息，支持对指定条件下的关系进行全量统计（总和）；
10. 修改邮件解析接口，支持更细致化的解析配置；

**2019.05.05 - 2019.05.10**
1. 添加项目数量信息统计接口；
2. 添加解析任务进度监控状态接口，修改解析相关接口逻辑；

3. 为海关项目小组编写临时需求工具，邮件解析并对收发关系等进行频率统计；

4. 完善邮件工具功能，协助海关邮件小组处理统计的问题；
5. 为邮件系统服务添加统计模块，按照招标文档规划统计模块；
6. 新增附件类型统计模块，考虑统计消耗资源程度、时间程度，添加了辅助表作为数据更新凭证，二次查询不必再次计算，提升处理效率；

7. 隔离邮件列表接口，将数据和条目统计接口隔离，避免每次查询都查询条目总数；
8. 邮件解析中加入对发送者IP的嗅探；
9. 隔离实体信息列表接口，同样将数据和条目统计隔离；
10. 邮件的正文信息其实有所重复，提取特征不再针对两类正文数据，而只对文本数据提取，删除html_text_content信息；
11. 邮件解析过程调优，尽量提升解析性能。

12. 修改集装箱号（柜号）识别问题，
13. 解决第一版提出的问题列表：数据库影响客户端正常使用的问题；

**2019.4.29 - 2019.04.30**
1. Write the SEA used document;
2. Handed over the first step SEA to SEA project colleague;
3. Prepare the second step word list;

**2019.4.23 - 2019.4.27**
1. Solving the problem of matching arbitrary length substrings in fuzzy query pattern;
2. Constructing query system following the FoxMail query rules；
3. Comparing the efficiency of SmartChinese of Lucene and HanLP.Although the HanLP has a good word segmentation effect, but in other words, the good 
word segmentation will affect the adaptability of queries, so temporarily I using native query mode.

**2019.4.22**
1. Provide the email file download interface;
2. Perfect download interface documents(email and attachments);
3. Provide clear ones project information interface(clearCase)；
4. The table named 'ea_email_relation' added a new parameter 'case_id', shows the associated project information. 

**2019.4.21**
1. Provide the download interface for attachment, used the http protocol.
2. Combine the search email interface, provide a uniform access method, the method support various query modes.
Generally, it is divided into query for databases and lucene index libraries(Later it may be modified to ElasticSearch);

**2019.4.20**
1. Provided the entity interface;
2. The SEA temporary demand: Group a batch of mails by relationship(send and receive account).
3. The table named 'ea_entity' add a new parameter 'case_id ', shows the associated project information；

**2019.4.19**
1. （已完毕）修改解析结果文件保存路径的参数，兼容处理，配置之后只需检查根目录是否存在即可，其他的文件夹手动创建（已完毕）；
2. 解析邮件的逻辑中添加toName、toAddress到email基本信息中，数据库中添加这两个冗余字段；（已完毕）
3. 针对3，还要在相关的接口中加入这两个字段；（已完毕）
4. 解析的结果的保存路径分为三个：
	* 根目录/项目ID/index —— 索引存储路径
	* 根目录/项目ID/attach —— 附件存储路径
	* 根目录/项目ID/email —— 邮件存储路径，将分析完毕之后的邮件源文件存储到此处供以后的业务访问使用，但是后续是否进行这一步操作还有待考虑；
	* 根目录/项目ID/temp —— 如果是外部系统，那么可能需要一步将邮件下载到此处的操作，目前基于本地分析，并没有使用该配置路径（已完毕）；
5. 将配置项缩减为一个，即5中的根目录，其中的3个路径信息则保存在项目的信息中，即建立三个字段在项目表中，记录这些文件的存储路径以及协议类型（已完毕）；


**2019.4.18**
1. 将附件和邮件的索引建立，分隔开，减少冗余字段的存储；（已完毕：反而是将其放在了同一个索引库里面存储）
2. 完善附件模块反查邮件信息的接口；（已完毕：通过上一个工作点，该问题迎刃而解。）
3. 全文索引多字段查询问题（已完毕）
4. 全文索引多字段分页问题（已完毕）

**2019.4.17**
1. 对于邮件的HTML文本的分析（已完毕：将其使用parser工具解析）；
2. 邮件的content和附件content共用一个doc，是否会有影响（已完毕：代码深层次优化。）；
3. Unable to load native-hadoop library for your platform... using builtin-java classes where applicable警告。（已完毕，目前虽然考虑了HadoopHDFS文件，但是已经排出在项目外，后续增加。）

**2019.4.16**
1. 从HTML body部分提取出的文本信息作为HtmlTextContent字段的值；（已完毕）
2. 将邮件信息存储到lucene中的时候，最好做一次和数据库一样的副本copy；（已完毕）

**2019.4.15**
1. 采取三级分立制度对邮件依次进行：提取、索引、识别；（已完毕）
2. 梳理项目架构，使其阅读更佳；（已完毕）
3. 增加解析配置项例如是否启用中文分析，代表是否开启HanLP中文分词；启用英文分析，代表是否则开启Stanford进行英文分词；（已完毕）


**2019.4.12**
1. 完善lucene入库代码；（已完毕）
2. 梳理项目架构，Bean与静态变量如何处理；（已完毕）

**2019.4.11**
1. 开始编写lucene入库代码；（已完毕）
2. 开始编写对外接口；（长久过程）

**2019.4.10**
1. 学习JavaMail邮件处理框架，了解其分析模式；（已完毕）
2. 学习Mime4J框架，了解其分析模式；（已完毕）
3. 抽象解析接口，使系统对多种解析方式拥有兼容能力；（已完毕）

**2019.4.09**
1. 学习lucene架构、基本例子编写；（已完毕）
2. 搭建后台数据库，参考旧版本进行设计；（已完毕）

**2019.4.08**
1. 搭建后台框架，采用springboot框架；（已完毕）
2. 确认分析模式，从同步分析采取并发分析；（已完毕）

# Problem
1. Html内容提取过程中，内嵌于head的CSS区块无法处理；
2. HanLP使用默认的分词器 会默认把11点这样的词汇都当成机构名，如果要提高精度可以考虑使用N-最短路径分词;
3. Excel文件的特征提取还没做;
4. 特征插入数据库的时候，应该采用批量插入方式;
5. 特征插入的时候，index包里面耦合了mapper类，之后看看如何将其提取出来，不要和索引lucene混合在一起。
6. 我到底该限制可以分析哪些类型的文件，还是该排除不分析哪些文件？第二种感觉会出现考虑不周的情况;
7. 发送分析请求的时候，如果目标只是一个文件的话，记得检查后缀名是否符合需求，当前认为就是email文件;
8. 邮件的名称或许没有意义，但是附件的名称是不是也是分析的一个范畴之一？
9. 当附件是邮件的时候，没有进行进一步处理。
10. 全文索引的按字段进行排序，搜索结果高亮显示；
11. 全文索引的查询结果的去重问题；
12. 邮件判重：根据MD5最可靠，但是效率可能不理想；能否根据邮件的名字判别呢？
13. 多个数据库重用问题：
	* 考虑使用迁移接口，将当前库的信息迁移到一个库名差不多的库中；
	* 这样可以保证数据库一直可以供使用又不会太大，旧数据也可以得到保存；
	* 整个过程由用户指定；

#  Mail Mini Progress
邮件信息目前来源于多方：
* 本地
* 分布式文件系统
设计时请考虑各种情况。

每个步骤都是单独提取一个邮件文件，逐步分析，再没有稳定之前，不要好高骛远。
## Extract
开放多组件提取方式
* JavaMail
* Apache Mime4j

将邮件信息按邮件基本信息（例如收发人信息）、正文信息（纯文本、富文本）以及附件提取出来，存储地也需要考虑到各种环境的不同，目前假设都是本地。

## Recognition
1、将提取出来的实体分为邮件信息、正文信息、附件信息3部分。
* 邮件信息：直接入库
* 正文信息： 全文索引、NER
* 附件信息：本地存储、可分析文本全文索引、可分析文本NER

## Analysis
The analysis now we think should have two type, include：
1. text similarity;
2. image recognize;

# Search
The search is the SEA used function, we mini data is store in two place:
1. mysql database;
2. lucene index file system.

So we search just in the two place.




## Entity 
Entity is the ferry special function, is maybe include:
1. **Container Number**:  3 character + U + 7 number, like 123U1234567;


## porblem
**解析过程卡顿问题**
1. 内存占用过大；
2. lucene多线程阻塞；
3. HikariPool-1 - Thread starvation or clock leap detected 
(housekeeper delta=46s488ms725?s473ns)


# Epilogue
## En&Cn Comparison Table
* 货柜号 - Container Number

# 分词器的抉择
在lucene索引入库时，有一个分词器抉择的问题，这里有两个分词器可供选择，一个是lucene自带的SmartChinese、另一个则是我们特征提取的HanLP。

首先需要明确的是，HanLP有比较好的分词效果。如下所示：
```
====== Smart分词器: 
中华人民共和国|很|辽阔|
耗时：84398055
====== HanLP分词器: 
[中华人民共和国/ns, 很/d, 辽阔/a]
耗时：488782442
```
在解析的信息比较短小的情况下表现良好，但是当我们增大输入句子的长度的时候(sentence length)，整个解析消耗的时间会成倍的增长。


# 告警邮件
## 设计思路
1. 告警邮件是一个列表；

2. 一个全局的配置；

3. 每次解析的时候需要指定是否进行告警监控，然后勾选那些邮件需要监控，同时支持告警邮件列表的维护；

4. 勾选设置列表状态，服务器可获取这些标注了的需要监控的邮件；

5. 解析完成之后，统一设置监控邮件列表的状态；统一还原标注了的监控的邮件列表；

