package com.clicktech.snsktv.module_mine.inject.module;

import com.clicktech.snsktv.arms.inject.scope.ActivityScope;
import com.clicktech.snsktv.module_mine.contract.Mine_Chrous_SingedListContract;
import com.clicktech.snsktv.module_mine.model.Mine_Chrous_SingedListModel;

import dagger.Module;
import dagger.Provides;

/**
 * 通过Template生成对应页面的MVP和Dagger代码,请注意输入框中输入的名字必须相同
 * 由于每个项目包结构都不一定相同,所以每生成一个文件需要自己导入import包名,可以在设置中设置自动导入包名
 * 请在对应包下按以下顺序生成对应代码,Contract->Model->Presenter->Activity->Module->Component
 * 因为生成Activity时,Module和Component还没生成,但是Activity中有它们的引用,所以会报错,但是不用理会
 * 继续将Module和Component生成完后,编译一下项目再回到Activity,按提示修改一个方法名即可
 * 如果想生成Fragment的相关文件,则将上面构建顺序中的Activity换为Fragment,并将Component中inject方法的参数改为此Fragment
 */

/**
 * Created by Administrator on 2017/3/23.
 */

@Module
public class Mine_Chrous_SingedListModule {
    private Mine_Chrous_SingedListContract.View view;

    /**
     * 构建Mine_Chrous_SingedListModule时,将View的实现类传进来,这样就可以提供View的实现类给presenter
     *
     * @param view
     */
    public Mine_Chrous_SingedListModule(Mine_Chrous_SingedListContract.View view) {
        this.view = view;
    }

    @ActivityScope
    @Provides
    Mine_Chrous_SingedListContract.View provideMine_Chrous_SingedListView() {
        return this.view;
    }

    @ActivityScope
    @Provides
    Mine_Chrous_SingedListContract.Model provideMine_Chrous_SingedListModel(Mine_Chrous_SingedListModel model) {
        return model;
    }
}