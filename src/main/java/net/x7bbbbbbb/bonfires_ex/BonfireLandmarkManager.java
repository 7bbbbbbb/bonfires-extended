package net.x7bbbbbbb.bonfires_ex;

import folk.sisby.surveyor.landmark.Landmark;
import folk.sisby.surveyor.landmark.WorldLandmarks;
import folk.sisby.surveyor.landmark.component.LandmarkComponentTypes;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;
import wehavecookies56.bonfires.bonfire.Bonfire;

import java.util.UUID;

public class BonfireLandmarkManager {

    public static void updateBonfireLandmark(World world, Bonfire bonfire) {
        WorldLandmarks landmarks = WorldLandmarks.of(world);
        if (landmarks == null || !bonfire.isPublic()) return;

        Identifier landmarkId = Identifier.of(BonfiresExtended.MOD_ID, "bonfire/" + bonfire.getId().toString());

        landmarks.put(Landmark.create(WorldLandmarks.GLOBAL, landmarkId, b -> LandmarkComponentTypes.forBlock(b, world, bonfire.getPos())
                .add(LandmarkComponentTypes.NAME, Text.literal(bonfire.getName() != null ? bonfire.getName() : "Bonfire"))
        ));
    }

    public static void removeBonfireLandmark(World world, UUID bonfireId) {
        WorldLandmarks landmarks = WorldLandmarks.of(world);
        if (landmarks != null) {
            Identifier landmarkId = Identifier.of(BonfiresExtended.MOD_ID, "bonfire/" + bonfireId.toString());
            landmarks.removeAll(l -> l.id().equals(landmarkId));
        }
    }
}