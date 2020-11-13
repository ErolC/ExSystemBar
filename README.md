## ExStatusBar
这是对状态栏的各种操作进行封装之后的库，可以让你快速简单的操作状态栏。

### 加入
在build.gradle文件中加入
```gradle
allprojects {
    repositories {
        ...
        maven { url 'https://jitpack.io' }
    }
}

```
在app.gradle文件加入
```gradle
dependencies {
 implementation 'com.github.ErolC:ExStatusBar:1.1.3'
}
```
### API
```
    getHeight //获取状态栏高度
    getBackgroundColor //获取背景色
    setBackgroundColor //设置背景色
    setBackground       //设置背景，可以是drawable，也可以是drawable的资源id
    getBackground   //获取背景，是drawable
    setTextColor    //设置字体颜色，只有两种，亮系和暗系
    hide        //隐藏状态栏，在状态栏位置下滑可临时呼出状态栏，一段时间后会自动收起
    show        //展示状态栏，和上面是一对
    immersive   //所谓的"沉浸式"，我更喜欢称之为"侵入式"
    isShow //状态栏是否展示
    isDark //字体颜色是否是暗系
```
### 使用
可通过`ExStatusBar`创建一个StatusBar
```
//方式一
val statusBar = ExStatusBar.create(this)
//方式二
 val statusBar = statusBar {
         //可以将设置状态栏的操作都放在这里
     }
//方式三
val statusBarr = getStatusBar()
```
#### 两种懒加载使用场景
目前对于Fragment+viewpager，懒加载的方式有两种，对于setUserVisibleHint的懒加载方式，只需要在这个回调中调用setUserVisibleHint()/ExStatusBar.setUserVisibleHint(this)方法就可以了。

## 注意
```xml
 <style name="AppTheme" parent="Theme.AppCompat.Light.NoActionBar">
    <item name="android:windowDrawsSystemBarBackgrounds">false</item>
</style>
```
`windowDrawsSystemBarBackgrounds`这个属性为false时会让状态栏背景设置失效(这个名字就很明显了)。背景会被黑色霸占，所以无法任何的背景设置，但是其他操作还是可以的。

## 最后
如果使用有什么问题或者建议，请务必要在issues上留下您的问题或想法，这将对我非常有帮助，如果觉得满意，希望能给个star。谢谢