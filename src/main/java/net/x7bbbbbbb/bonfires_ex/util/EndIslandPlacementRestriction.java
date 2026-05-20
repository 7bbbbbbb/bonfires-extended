package net.x7bbbbbbb.bonfires_ex.util;

import net.fabricmc.fabric.api.event.player.UseBlockCallback;
import net.minecraft.block.Block;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class EndIslandPlacementRestriction {

    private final Identifier targetBlockId;
    private final String customMessage;
    private final double maxRadiusSq;

    /**
     * @param targetBlockId The Identifier of the block to restrict (e.g., Identifier.of("minecraft", "tnt"))
     * @param customMessage The message displayed to the player when placement is blocked
     * @param maxRadius     The radius from (0,0) in the End where placement is forbidden (Central island is ~100 blocks)
     */
    public EndIslandPlacementRestriction(Identifier targetBlockId, String customMessage, double maxRadius) {
        this.targetBlockId = targetBlockId;
        this.customMessage = customMessage;
        this.maxRadiusSq = maxRadius * maxRadius;
    }

    public void register() {
        UseBlockCallback.EVENT.register((player, world, hand, hitResult) -> {
            ItemStack stack = player.getStackInHand(hand);

            if (stack.getItem() instanceof BlockItem blockItem) {
                Block block = blockItem.getBlock();
                Identifier blockId = Registries.BLOCK.getId(block);
                if (blockId.equals(targetBlockId)) {
                    if (world.getRegistryKey() == World.END) {
                        BlockPos placedPos = hitResult.getBlockPos().offset(hitResult.getSide());
                        double distanceSq = (placedPos.getX() * placedPos.getX()) + (placedPos.getZ() * placedPos.getZ());
                        if (distanceSq <= maxRadiusSq) {
                            player.sendMessage(Text.translatable(customMessage), true);
                            return ActionResult.FAIL;
                        }
                    }
                }
            }
            return ActionResult.PASS;
        });
    }
}