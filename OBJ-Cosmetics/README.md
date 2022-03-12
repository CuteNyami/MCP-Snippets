# Obj Cosmetics

### The Original OBJ Loader:

### https://github.com/Blunderchips/LWJGL-OBJ-Loader

## How to use:

### first create a new instance of the OBJLoader class in your Main

````java 
private OBJLoader objLoader;

public void startup() {
    objLoader = new OBJLoader();
}

public OBJLoader getObjLoader() {
    return objLoader;
}
````

### then you need to register your Cosmetics

````java 
private OBJLoader objLoader;

public void startup() {
    objLoader = new OBJLoader();

    CosmeticsManager.registerCosmetic(new Bandana());
}

public OBJLoader getObjLoader() {
    return objLoader;
}
````

### and now you need to go into the ModelPlayer class and replace the render method

````java 
public void render(Entity entityIn, float p_78088_2_, float p_78088_3_, float p_78088_4_, float p_78088_5_, float p_78088_6_, float scale) {
        super.render(entityIn, p_78088_2_, p_78088_3_, p_78088_4_, p_78088_5_, p_78088_6_, scale);
        GlStateManager.pushMatrix();

        if (this.isChild) {
            float f = 2.0F;
            GlStateManager.scale(1.0F / f, 1.0F / f, 1.0F / f);
            GlStateManager.translate(0.0F, 24.0F * scale, 0.0F);
            this.bipedLeftLegwear.render(scale);
            this.bipedRightLegwear.render(scale);
            this.bipedLeftArmwear.render(scale);
            this.bipedRightArmwear.render(scale);
            this.bipedBodyWear.render(scale);
        } else {
            if (entityIn.isSneaking()) {
                GlStateManager.translate(0.0F, 0.2F, 0.0F);
            }

            this.bipedLeftLegwear.render(scale);
            this.bipedRightLegwear.render(scale);
            this.bipedLeftArmwear.render(scale);
            this.bipedRightArmwear.render(scale);
            this.bipedBodyWear.render(scale);


            if (entityIn.getName().equals(Minecraft.getMinecraft().getSession().getProfile().getName())) {
                for (CosmeticsHandler handler : CosmeticsManager.cosmetics) {
                    if (handler.getType() == CosmeticsHandler.CosmeticsType.HAT) {
                        handler.render(entityIn, this.bipedHead);
                    } else if (handler.getType() == CosmeticsHandler.CosmeticsType.BODY) {
                        handler.render(entityIn, this.bipedBody);
                    }
                }
            }
        }

        GlStateManager.popMatrix();
    }
````
