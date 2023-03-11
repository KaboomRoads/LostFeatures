package com.kaboomroads.lostfeatures.particle.custom;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.*;
import net.minecraft.core.particles.SimpleParticleType;
import org.jetbrains.annotations.NotNull;

public class TermiteParticle extends TextureSheetParticle {
    protected TermiteParticle(ClientLevel clientLevel, double x, double y, double z, SpriteSet spriteSet, double xd, double yd, double zd) {
        super(clientLevel, x, y, z, xd, yd, zd);
        this.xd = xd;
        this.yd = yd;
        this.zd = zd;
        this.friction = 1.0F;
        this.quadSize *= 0.75F;
        this.lifetime = 30;
        this.setSpriteFromAge(spriteSet);
        this.hasPhysics = false;
    }

    @NotNull
    @Override
    public ParticleRenderType getRenderType() {
        return ParticleRenderType.PARTICLE_SHEET_OPAQUE;
    }

    public record Provider(SpriteSet sprites) implements ParticleProvider<SimpleParticleType> {
        @Override
        public Particle createParticle(@NotNull SimpleParticleType simpleParticleType, @NotNull ClientLevel clientLevel, double x, double y, double z, double dx, double dy, double dz) {
            return new TermiteParticle(clientLevel, x, y, z, sprites, dx, dy, dz);
        }
    }
}
