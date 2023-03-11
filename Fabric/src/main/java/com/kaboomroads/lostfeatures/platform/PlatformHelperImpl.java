package com.kaboomroads.lostfeatures.platform;

import com.kaboomroads.lostfeatures.platform.services.PlatformHelper;

public class PlatformHelperImpl implements PlatformHelper {
    public Platform getPlatform() {
        return Platform.FABRIC;
    }
}
