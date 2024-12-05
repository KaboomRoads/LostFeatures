package com.kaboomroads.lostfeatures.particle.custom;

import com.kaboomroads.lostfeatures.utils.Utils;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.*;
import net.minecraft.core.particles.SimpleParticleType;
import org.jetbrains.annotations.NotNull;

public class FireflyParticle extends TextureSheetParticle {
    protected boolean on = true;
    protected float brightness = 0;

    protected FireflyParticle(ClientLevel clientLevel, double x, double y, double z, float p_171908_, float p_171909_, float p_171910_, double p_171911_, double p_171912_, double p_171913_, SpriteSet spriteSet, double xd, double yd, double zd) {
        super(clientLevel, x, y, z, xd, yd, zd);
        this.friction = 1f;
        this.xd *= p_171908_;
        this.yd *= p_171909_;
        this.zd *= p_171910_;
        this.xd += p_171911_;
        this.yd += p_171912_;
        this.zd += p_171913_;
        this.quadSize *= 1.25F;
        this.lifetime = 100;
        this.setSpriteFromAge(spriteSet);
        this.rCol = 1f;
        this.gCol = 1f;
        this.bCol = 1f;
        this.hasPhysics = false;
    }

    @Override
    public void tick() {
        super.tick();
        animate();
    }

    private void animate() {
        if (brightness >= 1 && on) on = false;
        else if (brightness <= 0 && !on) on = true;
        brightness = on
                ? Utils.interpolateLinear(brightness, 1, 0.25f)
                : Utils.interpolateLinear(brightness, 0, 0.1f);
        this.rCol = brightness;
        this.gCol = brightness;
        this.bCol = brightness;
    }

    @NotNull
    @Override
    public ParticleRenderType getRenderType() {
        return ParticleRenderType.PARTICLE_SHEET_OPAQUE;
    }

    public record Provider(SpriteSet sprites) implements ParticleProvider<SimpleParticleType> {
        @Override
        public Particle createParticle(@NotNull SimpleParticleType simpleParticleType, @NotNull ClientLevel clientLevel, double x, double y, double z, double dx, double dy, double dz) {
            return new FireflyParticle(clientLevel, x, y, z, 0.5f, -0.1f, 0.5f, 0, 0, 0, this.sprites, dx, dy, dz);
        }
    }
}
