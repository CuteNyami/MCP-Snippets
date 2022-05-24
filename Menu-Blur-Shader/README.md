# Menu Blur Shader

### How can I load the shader?
* Minecraft.getMinecraft().entityRenderer.loadShader(new ResourceLocation("shaders/post/menu_blur.json"));

### How can I unload the shader?
* Minecraft.getMinecraft().entityRenderer.stopUseShader();

### Example gui with the blur shader:
```java
public class ExampleGui extends GuiScreen {

    private final Minecraft mc = Minecraft.getMinecraft();

    @Override
    public void initGui() {
        mc.entityRenderer.loadShader(new ResourceLocation("shaders/post/menu_blur.json"));
    }
    
    @Override
    public void onGuiClosed() {
        mc.entityRenderer.stopUseShader();
    }
}
```
