package com.clicktech.snsktv.module_discover.contract;

import com.clicktech.snsktv.arms.mvp.BaseView;
import com.clicktech.snsktv.arms.mvp.IModel;
import com.clicktech.snsktv.module_discover.ui.adapter.TemplateAndFilterAdapter;
import com.clicktech.snsktv.module_discover.ui.helper.TemplateAndFilterType;
import com.tbruyelle.rxpermissions.RxPermissions;

/**
 * 通过Template生成对应页面的MVP和Dagger代码,请注意输入框中输入的名字必须相同
 * 由于每个项目包结构都不一定相同,所以每生成一个文件需要自己导入import包名,可以在设置中设置自动导入包名
 * 请在对应包下按以下顺序生成对应代码,Contract->Model->Presenter->Activity->Module->Component
 * 因为生成Activity时,Module和Component还没生成,但是Activity中有它们的引用,所以会报错,但是不用理会
 * 继续将Module和Component生成完后,编译一下项目再回到Activity,按提示修改一个方法名即可
 * 如果想生成Fragment的相关文件,则将上面构建顺序中的Activity换为Fragment,并将Component中inject方法的参数改为此Fragment
 */

/**
 * Created by Administrator on 2017-05-06.
 */

public interface MvPreviewSecondContract {
    //对于经常使用的关于UI的方法可以定义到BaseView中,如显示隐藏进度条,和显示文字消息
    interface View extends BaseView {
        //获取权限
        RxPermissions getRxPermissions();

        //获取权限后，初始化Camera
        void initCameraPreview();

        //设置模板和滤镜的recyclerview
        void setTemplateFilterAdapter(TemplateAndFilterAdapter templateAdapter,
                                      TemplateAndFilterAdapter filterAdapter);

        //设置模板效果
        void setTemplateEffect(TemplateAndFilterType type);

        //设置滤镜效果
        void setFilterEffect(TemplateAndFilterType type);

        //歌词保存路径
        void setLyricSavePath(String path);

    }

    //Model层定义接口,外部只需关心model返回的数据,无需关心内部细节,及是否使用缓存
    interface Model extends IModel {

    }
}