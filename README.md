highcommunity Android stuio项目:
    

    以下是对项目的简单介绍:

    1.这是版本3.0，将团队协作进行。

    2.目前整个项目分成四个module：app DongTools pulltorefresh slidinglibrary
      其中 app是主工程项目，其他三个都是作为library项目而使用的，为主项目提供各种工具、控件
      
      Dongtools里面集成了http请求、图片加载和EventBus，还有滚轮控件 圆形图片等。目前主工程里没有是用EventBus
      
      pulltorefresh：这个是第三方的下拉加载控件 提供各种下拉刷新的控件。

      slidinglibrary:这是个侧滑第三方控件 我们在其中加了查看大图的photo的代码。
	
      后续增加的module将一一说明

    3.android studio 是用gradle 编译 compileSdkVersion 19
                                     buildToolsVersion '23.0.2'
      这是gradle的编译的SDK和Tools版本，可以修改对应的版本号。