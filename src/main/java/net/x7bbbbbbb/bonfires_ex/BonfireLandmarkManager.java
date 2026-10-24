package net.x7bbbbbbb.bonfires_ex;

import folk.sisby.surveyor.landmark.Landmark;
import folk.sisby.surveyor.landmark.WorldLandmarks;
import folk.sisby.surveyor.landmark.component.LandmarkComponentTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;
import wehavecookies56.bonfires.bonfire.Bonfire;

import java.util.UUID;

public class BonfireLandmarkManager {
    /*
    public static void updateBonfireLandmark(Level world, Bonfire bonfire) {
        WorldLandmarks landmarks = WorldLandmarks.of(world);
        if (landmarks == null || !bonfire.isPublic()) return;

        ResourceLocation landmarkId = ResourceLocation.fromNamespaceAndPath("bonfires_ex", "bonfire/" + bonfire.getId().toString());

        landmarks.put(Landmark.create(WorldLandmarks.GLOBAL, landmarkId, b -> LandmarkComponentTypes.forBlock(b, world, bonfire.getPos())
                .add(LandmarkComponentTypes.NAME, Component.literal(bonfire.getName() != null ? bonfire.getName() : "Bonfire"))
        ));
    }

    public static void removeBonfireLandmark(Level world, UUID bonfireId) {
        WorldLandmarks landmarks = WorldLandmarks.of(world);
        if (landmarks != null) {
            ResourceLocation landmarkId = ResourceLocation.fromNamespaceAndPath("bonfires_ex", "bonfire/" + bonfireId.toString());
            landmarks.removeAll(l -> l.id().equals(landmarkId));
        }
    }

     */
}