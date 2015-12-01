## Android I2C Tool Manual

App可以用于探测Android设备I2C总线、读取/设置I2C设备寄存器值，并写入log，硬件工程师可以直接把log导入excel，就可以进一步分析数据。
方便不熟悉命令行和ADB的硬件工程师测试。

通过`Runtime.getRuntime().exec()`调用命令行工具，然后读取进程的标准输出/标准错误。核心代码在`I2cToolHelper.java`和`I2cParserHelper.java`

### 使用App前需要做的事情

1. 需要在Android BSP中编译[开源命令行工具i2c-tools](https://github.com/suapapa/i2c-tools)；
2. Root Android 机器；
3. 把 i2cdump, i2cset 这两个文件上传到机器的`/system/xbin`路径下，并修改权限为 755;

```sh
adb-platform-tools>adb remount
adb-platform-tools>adb push i2cdump /system/xbin
adb-platform-tools>adb push i2cset /system/xbin
adb-platform-tools>adb shell chmod 755 /system/xbin/i2c*
```
4. 如果遇到错误

```sh
Error: Could not open file `/dev/i2c-2': Permission denied, Run as root?
```
需要修改Android I2C设备文件的权限：`root@android:/ # chmod 666 /dev/i2c*`


![i2c-command-line-tools](https://github.com/li2/Android_I2C_Tool/blob/master/assets/i2c-command-line-tools.png)


### Detect: 探测I2C设备

探测 Android 设备支持的 I2C 总线及设备。
![i2c-detect](https://github.com/li2/Android_I2C_Tool/blob/master/assets/detect.png)


### Dump: 读取指定 I2C 设备的所有寄存器

1. 点击右侧的按钮,在弹出的对话框中设置总线号、设备地址、读模式;
2. 支持循环读取
3. 支持 Log,
存储位置:/mnt/sdcard/i2clog/i2c_log_parsed.txt 和 i2c_log_raw.txt

```sh
    14:54:59 - 
    24 02 00 00 64 00 12 0c 3f 10 88 02 8c 0b 8c 0b
    c9 0c c9 0c 08 00 ff ff c9 0c fa ff 27 0b 5b fd
    27 0b c9 0c 21 00 00 00 12 0c 02 00 64 00 60 00 
    68 10 fa 05 ff ff 5e 00 53 00 76 11 f4 0b 00 00
    00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00
    00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00
    00 00 0a 42 51 32 37 35 34 58 2d 47 31 ff 00 00
    00 00 00 00 00 00 00 00 XX XX 95 ff 3f 10 12 0c
    24 02 00 00 64 00 12 0c 3f 10 88 02 8c 0b 8c 0b
    c9 0c c9 0c 08 00 ff ff c9 0c fa ff 27 0b 5b fd
    27 0b c9 0c 21 00 00 00 12 0c 02 00 64 00 60 00
    68 10 fa 05 ff ff 5e 00 53 00 76 11 f4 0b 00 00
    00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00
    00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00
    00 00 0a 42 51 32 37 35 34 58 2d 47 31 ff 00 00
    00 00 00 00 00 00 00 00 XX XX 95 ff 3f 10 12 0c
14:55:00 - 
```
![i2c-dump](https://github.com/li2/Android_I2C_Tool/blob/master/assets/dump.png)


### Set: 向指定I2C设备的某个寄存器写入数据

![i2c-set](https://github.com/li2/Android_I2C_Tool/blob/master/assets/set.png)


## 关于

之所以开发这个App，是因为我负责的驱动模块出现了问题，需要和硬件工程师排一起查软、硬问题。因为硬件工程师不熟悉命令行、ADB，并且使用ADB打印的log不便于处理。

第一版于2013.08开发完成，这是第一次开发Android，所有细节都得搜索，很琐碎很差劲，至于有多差劲，可以看代码：[branch-v1](https://github.com/li2/Android_I2C_Tool/tree/v1)，
第二版于2015.10开发完成，重构了之前的代码。

之所以开源这个App，是因为没有上述提到的开源的命令行工具，这个App没有任何作用；另外一点，虽然是利用上班时间开发的，但是不涉及公司的业务逻辑，不属于公司的项目。
希望对别人有所帮助。

weiyi.just2@gmail.com
li2.me




