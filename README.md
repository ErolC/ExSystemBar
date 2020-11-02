## StatusBarControl
这是一个对状态栏可以任意控制的工具，可以添加给给状态栏添加任意颜色的背景，图像，渐变都行

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
	        implementation 'com.github.ErolC:StatusBarControl:1.1.0'
	}
```
### API

### 使用

## 注意
```xml
 <style name="AppTheme" parent="Theme.AppCompat.Light.NoActionBar">
    <item name="android:windowDrawsSystemBarBackgrounds">false</item>
</style>
```
`windowDrawsSystemBarBackgrounds`这个属性会让状态栏设置失效。