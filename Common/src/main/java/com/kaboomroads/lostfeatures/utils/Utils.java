package com.kaboomroads.lostfeatures.utils;

import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.network.chat.Component;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;
import org.joml.Math;

public class Utils {
    public static int loopClamp(int num, int min, int max) {
        if (num > max) num = num % max - 1;
        if (num < min) num = max - (Math.abs(num) % max);
        return num;
    }

    public static float interpolateLinear(float from, float to, float speed) {
        if (Mth.abs(from - to) > speed) return from + speed * Math.signum(to - from);
        return to;
    }

    public static double interpolateLinear(double from, double to, double speed) {
        if (Math.abs(from - to) > speed) return from + speed * Math.signum(to - from);
        return to;
    }

    public static boolean entityIsDamageable(Entity entity) {
        if (entity instanceof LivingEntity livingEntity && livingEntity.isAlive() && !livingEntity.isInvulnerable())
            return (livingEntity instanceof Player player && !player.getAbilities().invulnerable) || !(livingEntity instanceof Player);
        return false;
    }

    public static Vec3 interpolateLinear(Vec3 a, Vec3 b, double t) {
        double x = interpolateLinear(a.x, b.x, t);
        double y = interpolateLinear(a.y, b.y, t);
        double z = interpolateLinear(a.z, b.z, t);
        return new Vec3(x, y, z);
    }

    public static Vec3 lerp(Vec3 a, Vec3 b, double t) {
        double x = Math.lerp(a.x, b.x, t);
        double y = Math.lerp(a.y, b.y, t);
        double z = Math.lerp(a.z, b.z, t);
        return new Vec3(x, y, z);
    }

    public static void sendClientMessage(Component message) {
        LocalPlayer player = Minecraft.getInstance().player;
        if (player != null) player.sendSystemMessage(message);
    }

    public static <T> void sendClientMessage(T message) {
        sendClientMessage(Component.literal(String.valueOf(message)));
    }
}
