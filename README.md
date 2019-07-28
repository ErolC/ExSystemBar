## StatusBarControl
这是一个对状态栏可以任意控制的工具

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
	        implementation 'com.github.ErolC:StatusBarControl:1.0.0'
	}
```
### API
```kotlin
StatusBar.kt

```


### 使用
如果是kotlin，你可以直接这样
```kotlin
override fun onCreate(savedInstanceState: Bundle?){
    setStatusBarColor(Color.RED)//将状态栏背景颜色改为红色
    
}

```
