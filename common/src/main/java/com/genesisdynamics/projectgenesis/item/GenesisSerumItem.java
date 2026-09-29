package com.genesisdynamics.projectgenesis.item;

import com.genesisdynamics.projectgenesis.progression.PowerProgressionManager;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import java.util.List;

/**
 * A Genesis Serum vial. Using it grants the matching power set via Palladium,
 * provided the player does not already carry a different serum.
 */
public class GenesisSerumItem extends Item {

    private final String serumType;

    public GenesisSerumItem(Properties properties, String serumType, String powerId) {
        super(properties.stacksTo(1));
        this.serumType = serumType;
        // powerId is kept for future use (e.g. loot-injection data), the power
        // itself is granted by GenesisPowerProvider based on the serum type.
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);
        if (level.isClientSide) {
            return InteractionResultHolder.success(stack);
        }

        if (PowerProgressionManager.hasPower(player) && !PowerProgressionManager.getSerumType(player).equals(serumType)) {
            player.displayClientMessage(Component.translatable("item.projectgenesis.serum.already_powered"), true);
            return InteractionResultHolder.fail(stack);
        }

        PowerProgressionManager.setSerumType(player, serumType);
        PowerProgressionManager.setStage(player, PowerProgressionManager.STAGE_MANIFESTATION);

        if (!player.getAbilities().instabuild) {
            stack.shrink(1);
        }
        player.displayClientMessage(Component.translatable("item.projectgenesis.serum.awakened", serumType), true);
        return InteractionResultHolder.consume(stack);
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> tooltip, TooltipFlag flag) {
        tooltip.add(Component.translatable("item.projectgenesis.serum.tooltip." + serumType));
    }
}
