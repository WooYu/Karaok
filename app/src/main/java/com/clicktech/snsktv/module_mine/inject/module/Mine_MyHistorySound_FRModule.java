package com.clicktech.snsktv.module_mine.inject.module;


/**
 * 通过Template生成对应页面的MVP和Dagger代码,请注意输入框中输入的名字必须相同
 * 由于每个项目包结构都不一定相同,所以每生成一个文件需要自己导入import包名,可以在设置中设置自动导入包名
 * 请在对应包下按以下顺序生成对应代码,Contract->Model->Presenter->Activity->Module->Component
 * 因为生成Activity时,Module和Component还没生成,但是Activity中有它们的引用,所以会报错,但是不用理会
 * 继续将Module和Component生成完后,编译一下项目再回到Activity,按提示修改一个方法名即可
 * 如果想生成Fragment的相关文件,则将上面构建顺序中的Activity换为Fragment,并将Component中inject方法的参数改为此Fragment
 */

import com.clicktech.snsktv.arms.inject.scope.ActivityScope;
import com.clicktech.snsktv.module_mine.contract.Mine_MyHistorySound_FRContract;
import com.clicktech.snsktv.module_mine.model.Mine_MyHistorySound_FRModel;

import dagger.Module;
import dagger.Provides;

/**
 * Created by Administrator on 2017/3/24.
 */

@Module
public class Mine_MyHistorySound_FRModule {
    private Mine_MyHistorySound_FRContract.View view;

    /**
     * 构建Mine_MyHistorySound_FRModule时,将View的实现类传进来,这样就可以提供View的实现类给presenter
     *
     * @param view
     */
    public Mine_MyHistorySound_FRModule(Mine_MyHistorySound_FRContract.View view) {
        this.view = view;
    }

    @ActivityScope
    @Provides
    Mine_MyHistorySound_FRContract.View provideMine_MyHistorySound_FRView() {
        return this.view;
    }

    @ActivityScope
    @Provides
    Mine_MyHistorySound_FRContract.Model provideMine_MyHistorySound_FRModel(Mine_MyHistorySound_FRModel model) {
        return model;
    }
}