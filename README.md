AppMonitor
========

An [Xposed](http://repo.xposed.info/) based module which can monitor the applications behavior.


Features
--------
* Able to hook Android system APIs.
* Able to print the APIs’s call stacks.
* Able to modify the API list as needed.


Usage
--------
1. Select the target application.

2. Log Format.

3. Modify the APIs list.


TODO
--------

- [ ] dump all files which the application had opened.

- [ ] hook application methods.

- [ ] Add native hook

- [ ] Fake Phone Info.

- [ ] Update UI for setting api, phone info, etc.

- [ ] Unpack


注意
--------

通过Application，加载so，so加载dex，dex再启动UI的应用。

由于hook loadLibrary方法，有可能会导致主线程阻塞时间过长，而导致应用启动崩溃，此时，需要去掉该方法。
