package immersive_paintings;

import immersive_paintings.client.render.entity.renderer.ImmersiveGlowGraffitiRenderer;
import immersive_paintings.client.render.entity.renderer.ImmersiveGlowPaintingEntityRenderer;
import immersive_paintings.client.render.entity.renderer.ImmersiveGraffitiEntityRenderer;
import immersive_paintings.client.render.entity.renderer.ImmersivePaintingEntityRenderer;
import immersive_paintings.cobalt.registration.Registration;

public class Renderer {
    public static void bootstrap() {
        Registration.registerEntityRenderer(Entities.PAINTING.get(), ImmersivePaintingEntityRenderer::new);
        Registration.registerEntityRenderer(Entities.GLOW_PAINTING.get(), ImmersiveGlowPaintingEntityRenderer::new);
        Registration.registerEntityRenderer(Entities.GRAFFITI.get(), ImmersiveGraffitiEntityRenderer::new);
        Registration.registerEntityRenderer(Entities.GLOW_GRAFFITI.get(), ImmersiveGlowGraffitiRenderer::new);
    }
}
