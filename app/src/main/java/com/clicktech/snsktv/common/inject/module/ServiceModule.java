package com.clicktech.snsktv.common.inject.module;


import com.clicktech.snsktv.common.net.CommonService;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import retrofit2.Retrofit;

/**
 * 提供RetrofitApi对应的Service，这些Service对象在AppComponent中注入ServiceManager(
 * 需要继承BaseServiceManager)中统一管理
 */
@Module
public class ServiceModule {

    /**
     * 这里使用Retrofit对象(ClientModule提供)实例化Service接口,提供所有Service对象(
     * 可以根据不同的逻辑划分多个Service接口)
     *
     * @param retrofit
     * @return
     */
    @Singleton
    @Provides
    CommonService provideCommonService(Retrofit retrofit) {
        return retrofit.create(CommonService.class);
    }

}
