package immersive_paintings;

import immersive_paintings.cobalt.registration.Registration;
import immersive_paintings.item.ImmersiveGlowGraffitiItem;
import immersive_paintings.item.ImmersiveGlowPaintingItem;
import immersive_paintings.item.ImmersiveGraffitiItem;
import immersive_paintings.item.ImmersivePaintingItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.function.Supplier;

public interface Items {
    List<Supplier<Item>> items = new LinkedList<>();

    Supplier<Item> PAINTING = register("painting", () -> new ImmersivePaintingItem(baseProps()));
    Supplier<Item> GLOW_PAINTING = register("glow_painting", () -> new ImmersiveGlowPaintingItem(baseProps()));
    Supplier<Item> GRAFFITI = register("graffiti", () -> new ImmersiveGraffitiItem(baseProps()));
    Supplier<Item> GLOW_GRAFFITI = register("glow_graffiti", () -> new ImmersiveGlowGraffitiItem(baseProps()));

    static Supplier<Item> register(String name, Supplier<Item> item) {
        Supplier<Item> register = Registration.register(Registries.ITEM, Main.locate(name), item);
        items.add(register);
        return register;
    }

    static void bootstrap() {
    }

    static Item.Settings baseProps() {
        return new Item.Settings();
    }

    static Collection<ItemStack> getSortedItems() {
        return items.stream().map(i -> i.get().getDefaultStack()).toList();
    }
}
