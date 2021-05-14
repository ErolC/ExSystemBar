## ExStatusBar
这是对状态栏的各种操作进行封装之后的库,而且对生命周期感知，可以让你快速简单的操作状态栏。

### 加入
在根的build.gradle文件中加入
```gradle
allprojects {
    repositories {
        ...
        maven { url 'https://jitpack.io' }
    }
}

```
在build.gradle(app)文件加入
```gradle
dependencies {
 implementation 'com.gitee.erolc:ExStatusBar:1.0.8'
}
```
### API
```
    getHeight //获取状态栏高度
    getBackgroundColor //获取背景色
    setBackgroundColor //设置背景色
    setBackground     //设置背景，可以是drawable，也可以是drawable/color的资源id
    getBackground   //获取背景，是drawable
    setTextColor    //设置字体颜色，只有两种，亮系（白色）和暗系（黑色）
    hide(boolean)        //隐藏状态栏，在状态栏位置下滑可临时呼出状态栏，一段时间后会自动收起；适配刘海：设置为true，内容会完全入侵到刘海位置
    show        //展示状态栏，和上面是一对
    invasion   //"侵入式",应用内容侵入到状态栏中
    isShow //状态栏是否展示
    isDark //字体颜色是否是暗系
```
### 使用
通过`ExStatusBar`可以创建一个StatusBar，但是我更加建议使用我提供的`StatusBarInit`文件中的顶级方法。
可以直接使用委托的方式，这也是推荐的使用方式：
```
#Activity/fragment
private  val statusBar: StatusBar by statusBar(Lifecycle.Event.ON_CREATE) { //默认指定onCreate时期创建StatusBar对象并执行该方法
        //使用上述api实现状态栏的设置
}
```
如果需要适配旧版本懒加载需要在`setUserVisibleHint`方法中调用：
```
  override fun setUserVisibleHint(isVisibleToUser: Boolean) {
        super.setUserVisibleHint(isVisibleToUser)
        //code...
        setUserVisibleHint()
        //code...
    }
```

## 注意
```xml
 <style name="AppTheme" parent="Theme.AppCompat.Light.NoActionBar">
    <item name="android:windowDrawsSystemBarBackgrounds">false</item>
</style>
```
`windowDrawsSystemBarBackgrounds`这个属性为false时会让状态栏背景设置失效(这个名字就很明显了)。背景会被黑色霸占，所以无法任何的背景设置，但是其他操作还是可以的。

## 最后
如果使用有什么问题或者建议，请务必要在issues上留下您的问题或想法，这将对我非常有帮助，如果觉得满意，希望能给个star。谢谢