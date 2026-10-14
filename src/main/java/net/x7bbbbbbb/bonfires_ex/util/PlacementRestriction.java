package net.x7bbbbbbb.bonfires_ex.util;

import net.minecraft.block.Block;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.registry.RegistryKey;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.entity.player.PlayerEntity;

public class PlacementRestriction {

    private final Identifier targetBlockId;
    private final Identifier dimensionId;
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
    public PlacementRestriction(Identifier targetBlockId, Identifier dimensionId, String customMessage, double maxRadius) {
        this.targetBlockId = targetBlockId;
        this.dimensionId = dimensionId;
        this.customMessage = customMessage;
        this.wholeDimension = maxRadius <= 0;
        this.maxRadiusSq = maxRadius * maxRadius;
    }

    public boolean blockPlacement(PlayerEntity player, World world, Hand hand, BlockHitResult hitResult) {
        ItemStack stack = player.getStackInHand(hand);
        if (!(stack.getItem() instanceof BlockItem blockItem)) return false;

        Block block = blockItem.getBlock();
        if (!Registries.BLOCK.getId(block).equals(targetBlockId)) return false;

        RegistryKey<World> key = world.getRegistryKey();
        if (!key.getValue().equals(dimensionId)) return false;

        if (wholeDimension) return true;

        BlockPos pos = new ItemPlacementContext(player, hand, stack, hitResult).getBlockPos();
        double distanceSq = (double) pos.getX() * pos.getX() + (double) pos.getZ() * pos.getZ();
        return distanceSq <= maxRadiusSq;
    }

    public String getCustomMessage() {
        return customMessage;
    }
}