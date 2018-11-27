package com.clicktech.snsktv.common.inject.module;

import com.clicktech.snsktv.common.cache.CommonCache;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import io.rx_cache.internal.RxCache;


@Module
public class CacheModule {

    /**
     * 这里使用RxCache对象(ClientModule提供)实例化所有Cache接口,提供所有Cache对象
     *
     * @param rxCache
     * @return
     */
    @Singleton
    @Provides
    CommonCache provideCommonService(RxCache rxCache) {
        return rxCache.using(CommonCache.class);
    }


}
