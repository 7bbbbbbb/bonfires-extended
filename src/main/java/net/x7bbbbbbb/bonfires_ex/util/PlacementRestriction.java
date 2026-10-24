package net.x7bbbbbbb.bonfires_ex.util;

import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.phys.BlockHitResult;

public class PlacementRestriction {

    private final ResourceLocation targetBlockId;
    private final ResourceLocation dimensionId;
    private final String customMessage;
    private final boolean wholeDimension;
    private final double maxRadiusSq;

    /**
     * @param targetBlockId The block to restrict
     * @param dimensionId   The dimension where the restriction applies
     * @param customMessage Translation key (or text) shown to the player when placement is blocked
     * @param maxRadius     Radius from (0,0) where placement is forbidden. If 0 (or less),
     *                      the restriction applies to the whole dimension.
     */
    public PlacementRestriction(ResourceLocation targetBlockId, ResourceLocation dimensionId, String customMessage, double maxRadius) {
        this.targetBlockId = targetBlockId;
        this.dimensionId = dimensionId;
        this.customMessage = customMessage;
        this.wholeDimension = maxRadius <= 0;
        this.maxRadiusSq = maxRadius * maxRadius;
    }

    public boolean blockPlacement(Player player, Level world, InteractionHand hand, BlockHitResult hitResult) {
        ItemStack stack = player.getItemInHand(hand);
        if (!(stack.getItem() instanceof BlockItem blockItem)) return false;

        Block block = blockItem.getBlock();
        if (!BuiltInRegistries.BLOCK.getKey(block).equals(targetBlockId)) return false;

        ResourceKey<Level> key = world.dimension();
        if (!key.location().equals(dimensionId)) return false;

        if (wholeDimension) return true;

        BlockPos pos = new BlockPlaceContext(player, hand, stack, hitResult).getClickedPos();
        double distanceSq = (double) pos.getX() * pos.getX() + (double) pos.getZ() * pos.getZ();
        return distanceSq <= maxRadiusSq;
    }

    public String getCustomMessage() {
        return customMessage;
    }
}