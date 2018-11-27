package com.clicktech.snsktv.common.net;


import com.clicktech.snsktv.arms.http.BaseServiceManager;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * AppComponent将所有的Service注入到ServiceManager中，所有的Model层都可以拿到此对象，
 * 意味着每个Model都可以请求任意API
 */
@Singleton
public class ServiceManager implements BaseServiceManager {
    private CommonService mCommonService;

    /**
     * 如果需要添加service只需在构造方法中添加对应的service,在提供get方法返回出去,
     * 只要在ServiceModule提供了该service
     * Dagger2会自行注入
     *
     * @param commonService
     */
    @Inject
    public ServiceManager(CommonService commonService) {
        this.mCommonService = commonService;
    }

    public CommonService getCommonService() {
        return mCommonService;
    }


    /**
     * 这里可以释放一些资源(注意这里是单例，即不需要在activity的生命周期调用)
     */
    @Override
    public void onDestory() {

    }
}
