package com.kaboomroads.lostfeatures.platform;

import com.kaboomroads.lostfeatures.platform.services.BlockEntityHelper;
import com.kaboomroads.lostfeatures.platform.services.PlatformHelper;
import com.kaboomroads.lostfeatures.platform.services.RegistryHelper;

import java.util.ServiceLoader;

public class Services {
    public static void init() {
    }

    public static final PlatformHelper PLATFORM = load(PlatformHelper.class);
    public static final RegistryHelper REGISTRY = load(RegistryHelper.class);
    public static final BlockEntityHelper BLOCK_ENTITY = load(BlockEntityHelper.class);

    public static <T> T load(Class<T> clazz) {
        return ServiceLoader.load(clazz)
                .findFirst()
                .orElseThrow(() -> new NullPointerException("Failed to load service for " + clazz.getName()));
    }
}