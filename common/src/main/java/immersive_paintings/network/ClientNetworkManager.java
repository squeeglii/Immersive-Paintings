package immersive_paintings.network;

import immersive_paintings.client.gui.ImmersivePaintingScreen;
import immersive_paintings.cobalt.network.NetworkHandler;
import immersive_paintings.entity.ImmersivePaintingEntity;
import immersive_paintings.network.c2s.PaintingModifyRequest;
import immersive_paintings.network.s2c.OpenGuiRequest;
import immersive_paintings.network.s2c.PaintingListMessage;
import immersive_paintings.network.s2c.PaintingModifyMessage;
import immersive_paintings.network.s2c.RegisterPaintingResponse;
import immersive_paintings.resources.ClientPaintingManager;
import immersive_paintings.resources.Painting;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

import java.util.Map;

public class ClientNetworkManager implements NetworkManager {
    @Override
    public void handleOpenGuiRequest(OpenGuiRequest request) {
        if (request.gui == OpenGuiRequest.Type.EDITOR) {
            MinecraftClient.getInstance().setScreen(new ImmersivePaintingScreen(
                    request.entity,
                    request.minResolution, request.maxResolution,
                    request.showOtherPlayersPaintings, request.uploadPermissionLevel
            ));
        }
    }

    @Override
    public void handlePaintingListResponse(PaintingListMessage response) {
        if (response.shouldClear()) {
            ClientPaintingManager.getPaintings().clear();
        }

        for (Map.Entry<Identifier, Painting> entry : response.getPaintings().entrySet()) {
            if (entry.getValue() == null) {
                ClientPaintingManager.getPaintings().remove(entry.getKey());
            } else {
                ClientPaintingManager.getPaintings().put(entry.getKey(), entry.getValue());
            }
        }

        if (MinecraftClient.getInstance().currentScreen instanceof ImmersivePaintingScreen screen) {
            screen.refreshPage();
        }
    }

    @Override
    public void handlePaintingModifyMessage(PaintingModifyMessage message) {
        ClientPlayerEntity e = MinecraftClient.getInstance().player;
        if (e != null && e.getWorld().getEntityById(message.getEntityId()) instanceof ImmersivePaintingEntity painting) {
            painting.setMotive(message.getMotive());
            painting.setFrame(message.getFrame());
            painting.setMaterial(message.getMaterial());
            painting.setFacing(message.getFacing(), message.getRotation());
            painting.setAttachmentPos(message.getPos());
        }
    }

    @Override
    public void handleRegisterPaintingResponse(RegisterPaintingResponse response) {
        if (MinecraftClient.getInstance().currentScreen instanceof ImmersivePaintingScreen screen) {
            if (response.error.isEmpty()) {
                if (screen.entity != null) {
                    screen.entity.setMotive(new Identifier(response.identifier));
                    NetworkHandler.sendToServer(new PaintingModifyRequest(screen.entity));

                    if (screen.entity.isGraffiti()) {
                        MinecraftClient.getInstance().setScreen(null);
                    } else {
                        screen.setPage(ImmersivePaintingScreen.Page.FRAME);
                    }
                }
            } else {
                screen.setPage(ImmersivePaintingScreen.Page.CREATE);
                screen.setError(Text.translatable("immersive_paintings.error." + response.error));
            }
        }
    }
}
