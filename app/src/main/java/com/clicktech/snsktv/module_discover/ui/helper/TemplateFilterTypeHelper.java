package com.clicktech.snsktv.module_discover.ui.helper;

import android.support.annotation.NonNull;

import com.clicktech.snsktv.R;
import com.clicktech.snsktv.entity.MVConfig;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by Administrator on 2017-05-08.
 */

public class TemplateFilterTypeHelper {

    /**
     * 获取mv的配置信息
     *
     * @param filterTypes
     * @return
     */
    public static List<MVConfig> TemplateFilterType2MVConfig(TemplateAndFilterType[] filterTypes) {
        if (null == filterTypes)
            return null;

        List<MVConfig> configList = new ArrayList<>();
        for (TemplateAndFilterType filterType : filterTypes) {
            configList.add(getMvConfig(filterType));
        }

        return configList;
    }

    @NonNull
    public static MVConfig getMvConfig(TemplateAndFilterType filterType) {
        if (null == filterType)
            return null;

        MVConfig mvConfig = new MVConfig();
        switch (filterType) {
            case STANDARD:
                mvConfig.setEffectpicture(R.mipmap.choruslayout_leftright);
                mvConfig.setConfigname(R.string.mvpreview_template1);
                mvConfig.setLayouttype(R.integer.chorustype_leftright);
                break;
            case FULLSCREEN:
                mvConfig.setEffectpicture(R.mipmap.choruslayou_fullscreen);
                mvConfig.setConfigname(R.string.mvpreview_template2);
                mvConfig.setLayouttype(R.integer.chorustype_fullscreen);
                break;
            case FRONTBACK:
                mvConfig.setEffectpicture(R.mipmap.choruslayout_frontback);
                mvConfig.setConfigname(R.string.mvpreview_template3);
                mvConfig.setLayouttype(R.integer.chorustype_frontback);
                break;

            case ORIGINAL:
                mvConfig.setEffectpicture(R.mipmap.filter_thumb_original);
                mvConfig.setConfigname(R.string.mvpreview_filter1);
                mvConfig.setConfigdesc("原图");
                mvConfig.setConfig("");
                break;
            case SUNDAE:
                mvConfig.setEffectpicture(R.mipmap.filter_thumb_cream);
                mvConfig.setConfigname(R.string.mvpreview_filter2);
                mvConfig.setConfigdesc("效果1");
                mvConfig.setConfig("@adjust brightness 0.48");
                break;
            case SWEET:
                mvConfig.setEffectpicture(R.mipmap.filter_thumb_moonshine);
                mvConfig.setConfigname(R.string.mvpreview_filter3);
                mvConfig.setConfigdesc("效果2");
                mvConfig.setConfig("@adjust saturation 0");
                break;
            case MOSCOW:
                mvConfig.setEffectpicture(R.mipmap.filter_thumb_moscow);
                mvConfig.setConfigname(R.string.mvpreview_filter4);
                mvConfig.setConfigdesc("效果3");
                mvConfig.setConfig("@adjust brightness 0.37 @adjust contrast 0.98 @adjust saturation 1.12\n");
                break;
            case SMMCL:
                mvConfig.setEffectpicture(R.mipmap.filter_thumb_skylark);
                mvConfig.setConfigname(R.string.mvpreview_filter5);
                mvConfig.setConfigdesc("效果4");
                mvConfig.setConfig("@adjust exposure 0.3 @adjust brightness 0.06");
                break;
            case SUNSHINE:
                mvConfig.setEffectpicture(R.mipmap.filter_thumb_skincare);
                mvConfig.setConfigname(R.string.mvpreview_filter6);
                mvConfig.setConfigdesc("效果5");
                mvConfig.setConfig("@adjust saturation 0.81 @adjust exposure 0.18");
                break;
            case NARA:
                mvConfig.setEffectpicture(R.mipmap.filter_thumb_matcha);
                mvConfig.setConfigname(R.string.mvpreview_filter7);
                mvConfig.setConfigdesc("效果6");
                mvConfig.setConfig("@adjust exposure 0.26 @adjust hue 0.122173");
                break;
            case SEOUL:
                mvConfig.setEffectpicture(R.mipmap.filter_thumb_nara);
                mvConfig.setConfigname(R.string.mvpreview_filter8);
                mvConfig.setConfigdesc("效果7");
                mvConfig.setConfig("@adjust exposure 0.18 @adjust saturation 1.56 @adjust contrast 1.24 @adjust brightness 0.01");
                break;
            default:
                mvConfig.setConfig("");
                break;
        }
        return mvConfig;
    }

}
