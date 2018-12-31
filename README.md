# Java期末大作业说明文档
![](documents/video.gif)
## 1 代码解析
本项目中，我主要定义了4种类：生物类、环境元素类、GUI类、工具类、测试类，它们分别放在main.java.creature、main.java.environment、main.java.jfxgui、main.java.tools包和test.java包下。

项目结构图如下所示：
- ![](documents/directory.png)
### 1.1 生物类
#### 1.1.1 Class Creature
- 这是所有生物类的基类，同时也是一个抽象类。它描述了生物的一些基本属性（数据成员），还定义了许多生物的基本行为（方法）。
- Creature类还声明子类需要实现Runnable接口，所以在运行时，每个生物都是一个独立的线程。由于不同种类的生物的行为各不相同，所以run方法的具体实现留到子类中实现。
#### 1.1.2 Class Fighter
Fighter类继承自Creature类，用于描述带有战斗属性的生物。其实现了具体的run方法，不过我依然将其定义为抽象类，因为我不想出现独立的Fighter类对象，而是将其分为葫芦娃和妖精。
#### 1.1.3 Class CalabrashBrother
CalabrashBrother类继承自Fighter类，用于描述葫芦娃们，其身份通过其序号也就是葫芦娃的年龄来标识。
#### 1.1.4 Class Monster
Monster类继承自Fighter类，用于描述除蛇精以外的妖精们。
#### 1.1.5 Class Leader
- Leader类直接继承自Creature类，用于描述爷爷和蛇精这两个“领导”，它们不具备战斗属性，但是Leader类对象有一个Enhancing接口成员，这个成员相当于一个魔法（游戏术语：增益BUFF）。
- Leader可以通过自己的Enhancing接口成员来增强己方战斗人员。
- Leader类的成员管理使用了工厂方法：Leader类成员不能直接在类外通过new来创建，而是必须调用Leader类的getInstance函数来获得。这么设计的原因是因为我想将Leader类限制成只能有*爷爷*和*蛇精*两种对象，所以要防止其他的类创建不正确的Leader对象。
#### 1.1.6 Interface Enhancing
- Enhancing是一个抽象的函数类接口，其用于来为战斗人员增强属性。我在代码中的设定是：爷爷的buff是为葫芦娃们加血，蛇精的buff是为妖精们增加攻击力。
- 在我的代码中，其具体的定义是直接通过lambda表达式来创建，比如爷爷和蛇精的buff的定义如下：
```
grandpa.setBuff(obj -> { obj.setLife(obj.getLife() + 20); });
snaker.setBuff(obj -> { obj.setAtk(obj.getAtk() + 20); });
```
### 1.2 环境元素类
#### 1.2.1 Class Game
- Game类用于描述一局游戏，其可以是开始一局新的游戏，也可以是一局回放的游戏。
- 每一个Game类对象都要绑定一个view(Class MainWindow)，background(Class BattleField)和gameLogger(Class GameLogger)。
- 启动新的游戏是通过start函数实现，回放游戏是通过replay函数实现。
#### 1.2.2 Class Battlefield
BattleField类用于描述游戏发生的地理空间，其基本数据结构由一个二维的Position数组组成。
#### 1.2.3 Class Position
Position类用于描述BattleField的内部数据结构，每一个Position对象可以存放一个生物类对象，同时它还有一个vestige标志位，用于记录其上是否有生物的尸体。
#### 1.2.4 Class SortController
SortController类是用于对葫芦娃进行排序的功能类，其所有方法均为静态方法，不需要具体的对象即可发挥功能。
### 1.3 GUI类
#### 1.3.1 Class Main
Main类继承自Application类，用于描述整个应用程序，其也是游戏程序所有UI的基础设施。
#### 1.3.2 Class MainWindow
MainWindow类是与mainwindow.fxml文件绑定的，在Javafx的MVC架构中担任Controller角色的类，其主要负责对游戏的UI做控件刷新和动画绘制。
### 1.4 工具类
#### 1.4.1 @ AtomicOperation
AtomicOperation注解是我自定义的一个注解，其用于标注一个方法是一个原子操作，与线程无关（不代表不需要做访问控制），不一定由生物线程调用。
这么定义是为了提醒程序员需要对这个函数进行Log操作，同时这个函数在Log中的标识符由该注解的type域决定。
#### 1.4.2 @ ThreadOperation
ThreadOperation注解也是我自定义的一个注解，其用于标注一个方法是一个线程操作，需要对其加操作锁。
#### 1.4.3 Class GameLogger
GameLogger类用于记录游戏运行时产生的操作记录和读取Log文件来回放游戏
### 1.5 测试类
#### 1.5.1 Class CreatureTest
该类用于对Creature类的hurt方法做测试
#### 1.5.2 Class SortControllerTest
该类用于对SortController类的sort方法做测试
## 2 游戏介绍
### 2.1 主界面
在结束一局游戏或回放后，再点击胜利或者失败图标，即可回到初始主界面。
![](documents/mainwindow.png)
### 2.2 启动一局新游戏
点击开始游戏按钮后，即会启动一局游戏。游戏的运行不需要用户的干预，其结果由生物之间的交互和一些随机因素决定（如增益是随机发动的）。

游戏运行中的画面
![](documents/battle.png)

胜利画面
![](documents/win.png)

失败画面
![](documents/fail.png)
1. 当一个Fighter攻击另一个生物时，会出现一把剑刺向对方的动画
2. 当一个Fighter获得增益时，会出现绿色箭头闪烁（生命值提升）或者红色箭头闪烁（攻击力提升）的动画
### 2.3 回放一局游戏
点击回放游戏按钮后，再选择一个游戏记录文件，即可回放一局游戏。

回放结束
![](documents/replay.png)
由于一些未知的原因，目前回放只能看到回放结果。回放过程中由于Javafx的UI不会刷新，所以无法看到战斗画面。