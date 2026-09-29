package com.genesisdynamics.projectgenesis.command;

import com.genesisdynamics.projectgenesis.player.EnergyManager;
import com.genesisdynamics.projectgenesis.player.StabilityManager;
import com.genesisdynamics.projectgenesis.progression.PowerProgressionManager;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.FloatArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;

/**
 * Debug/admin commands: /genesis serum|reset|energy|stability|unlock.
 *
 * <p>All subcommands that mutate state require operator permission level 2.</p>
 */
public final class GenesisCommand {

    private GenesisCommand() {
    }

    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(Commands.literal("genesis")
                .requires(source -> source.hasPermission(2))

                .then(Commands.literal("serum")
                        .then(Commands.argument("type", StringArgumentType.word())
                                .executes(ctx -> setSerum(ctx.getSource(), StringArgumentType.getString(ctx, "type"), ctx.getSource().getPlayerOrException()))
                                .then(Commands.argument("target", EntityArgument.player())
                                        .executes(ctx -> setSerum(ctx.getSource(), StringArgumentType.getString(ctx, "type"), EntityArgument.getPlayer(ctx, "target"))))))

                .then(Commands.literal("reset")
                        .executes(ctx -> reset(ctx.getSource(), ctx.getSource().getPlayerOrException()))
                        .then(Commands.argument("target", EntityArgument.player())
                                .executes(ctx -> reset(ctx.getSource(), EntityArgument.getPlayer(ctx, "target")))))

                .then(Commands.literal("energy")
                        .then(Commands.argument("amount", FloatArgumentType.floatArg(0, 1000))
                                .executes(ctx -> setEnergy(ctx.getSource(), FloatArgumentType.getFloat(ctx, "amount"), ctx.getSource().getPlayerOrException()))))

                .then(Commands.literal("stability")
                        .then(Commands.argument("amount", FloatArgumentType.floatArg(0, 100))
                                .executes(ctx -> setStability(ctx.getSource(), FloatArgumentType.getFloat(ctx, "amount"), ctx.getSource().getPlayerOrException()))))

                .then(Commands.literal("unlock")
                        .then(Commands.argument("stage", com.mojang.brigadier.arguments.IntegerArgumentType.integer(1, 4))
                                .executes(ctx -> setStage(ctx.getSource(), com.mojang.brigadier.arguments.IntegerArgumentType.getInteger(ctx, "stage"), ctx.getSource().getPlayerOrException()))))
        );
    }

    private static int setSerum(CommandSourceStack source, String type, ServerPlayer target) {
        String powerId = switch (type.toLowerCase()) {
            case "atlas" -> "genesis_atlas";
            case "volt" -> "genesis_volt";
            case "helios" -> "genesis_helios";
            case "vector" -> "genesis_vector";
            default -> null;
        };
        if (powerId == null) {
            source.sendFailure(Component.literal("Unknown serum type: " + type + " (expected atlas|volt|helios|vector)"));
            return 0;
        }
        PowerProgressionManager.setSerumType(target, type.toLowerCase());
        source.sendSuccess(() -> Component.literal("Granted " + type + " serum to " + target.getGameProfile().getName()), true);
        return 1;
    }

    private static int reset(CommandSourceStack source, ServerPlayer target) {
        PowerProgressionManager.reset(target);
        source.sendSuccess(() -> Component.literal("Reset Genesis data for " + target.getGameProfile().getName()), true);
        return 1;
    }

    private static int setEnergy(CommandSourceStack source, float amount, ServerPlayer target) {
        EnergyManager.setEnergy(target, amount);
        source.sendSuccess(() -> Component.literal("Set energy to " + amount), true);
        return 1;
    }

    private static int setStability(CommandSourceStack source, float amount, ServerPlayer target) {
        StabilityManager.setInstability(target, amount);
        source.sendSuccess(() -> Component.literal("Set instability to " + amount), true);
        return 1;
    }

    private static int setStage(CommandSourceStack source, int stage, ServerPlayer target) {
        PowerProgressionManager.setStage(target, stage);
        source.sendSuccess(() -> Component.literal("Set power stage to " + stage), true);
        return 1;
    }
}
