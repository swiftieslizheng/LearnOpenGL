# LearnOpenGL
***OpenGL 学习 demo***

## 相机和投影
- 投影
这种转换可根据显示绘制对象的 GLSurfaceView 的宽度和高度调整绘制对象的坐标。如果不进行这种计算，OpenGL ES 绘制的对象会被不等比例的视图窗口所扭曲。通常只有在渲染程序的 onSurfaceChanged() 方法中确定或更改 OpenGL 视图的比例时，才需要计算投影转换。如需详细了解 OpenGL ES 投影和坐标映射，请参阅映射绘制对象的坐标。
- 相机视图
这种转换可根据虚拟相机的位置调整绘制对象的坐标。请务必注意，OpenGL ES 不会定义实际的相机对象，而是通过转换绘制对象的显示方式提供模拟相机的实用程序方法。相机视图转换可能仅在您确定 GLSurfaceView 时计算一次，也可能会根据用户操作或应用的功能动态变化。

### 2020/12/5
**commit** 增加圆形，重写了GLRenderer，简化代码，添加抽象类BaseGLRenderer.kt

### 2020/12/10
**commit** 增加立方体，添加渲染程序类CubeGLRenderer.kt（暂时未复用BaseGLRenderer）
- 启用深度测试：使用 glEnable(GL_DEPTH_TEST);
