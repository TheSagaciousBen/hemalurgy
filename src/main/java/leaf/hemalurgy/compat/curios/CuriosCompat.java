/*
 * File created ~ 24 - 4 - 2021 ~ Leaf
 */

package leaf.hemalurgy.compat.curios;

import net.minecraft.world.inventory.InventoryMenu;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.InterModComms;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.event.lifecycle.InterModEnqueueEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import top.theillusivec4.curios.api.CuriosApi;
import top.theillusivec4.curios.api.SlotTypeMessage;
import top.theillusivec4.curios.api.SlotTypePreset;

public class CuriosCompat
{
	private static boolean curiosModDetected;


	public static boolean CuriosIsPresent()
	{
		return curiosModDetected;
	}

	public static void init()
	{
		curiosModDetected = ModList.get().isLoaded("curios");

		if (!curiosModDetected)
		{
			return;
		}

		IEventBus modBus = FMLJavaModLoadingContext.get().getModEventBus();
		modBus.addListener(CuriosCompat::onEnqueueIMC);
	}

	private static void onEnqueueIMC(InterModEnqueueEvent event)
	{
		if (!curiosModDetected)
		{
			return;
		}

		InterModComms.sendTo(CuriosApi.MODID, SlotTypeMessage.REGISTER_TYPE, () -> SlotTypePreset.BACK.getMessageBuilder().build());

		SlotTypePreset[] twoSlot = {
				SlotTypePreset.HEAD,
				SlotTypePreset.HANDS,
				SlotTypePreset.BODY,
		};

		for (SlotTypePreset type : twoSlot)
		{
			InterModComms.sendTo(CuriosApi.MODID, SlotTypeMessage.REGISTER_TYPE, () -> type.getMessageBuilder().size(2).build());
		}
		InterModComms.sendTo(CuriosApi.MODID, SlotTypeMessage.REGISTER_TYPE, () -> SlotTypePreset.BRACELET.getMessageBuilder().size(4).build());

		//custom slots
		InterModComms.sendTo(CuriosApi.MODID, SlotTypeMessage.REGISTER_TYPE, () -> new SlotTypeMessage.Builder("legs").priority(450).size(2).icon(InventoryMenu.EMPTY_ARMOR_SLOT_LEGGINGS).build());
		InterModComms.sendTo(CuriosApi.MODID, SlotTypeMessage.REGISTER_TYPE, () -> new SlotTypeMessage.Builder("feet").priority(500).size(2).icon(InventoryMenu.EMPTY_ARMOR_SLOT_BOOTS).build());
		InterModComms.sendTo(CuriosApi.MODID, SlotTypeMessage.REGISTER_TYPE, () -> new SlotTypeMessage.Builder("lynchpin").priority(59).size(1).icon(InventoryMenu.EMPTY_ARMOR_SLOT_HELMET).build());


	}
}
