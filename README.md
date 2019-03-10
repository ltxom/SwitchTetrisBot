## <a href="http://ltxom.me/tags/#arduino">开发日志</a>




>最近switch上一个小游戏非常火，叫Tetris 99。玩了一下发现实在手残遍出于对技术的追求（其实是因为操作苦手），打算开发一个tetris机器人。

#### 这个机器人有三个部分
1. 获取switch屏幕图像，传给本地服务端
2. 服务端实时分析收到的图像，返回结果给控制器
3. 控制器根据相应的指令操作switch主机

---

### 下面是硬件的清单：
1. Switch主机
2. 采集卡：HD60 Pro
3. 主控上位机：Arduino Mega 2560
4. 控制器下位机：ATmega32u4的单片机
6. 2*16 lcd(用来debug)
7. 16*16 led matrix

### 软件和依赖库方面：
1. Maven搭建的服务端，详细查阅<a href="https://github.com/ltxom/TetrisPlayer/blob/master/pom.xml">pom.xml</a>
2. LiquidCrystal 用来驱动lcd
3. AVR开发环境
4. Lufa库

### <a href="https://github.com/ltxom/TetrisPlayer/tree/master/hardware/layout">电路图</a>