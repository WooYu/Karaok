package com.clicktech.snsktv.entity;

import com.clicktech.snsktv.arms.base.BaseResponse;

import java.util.List;

public class SingerAlbumResponse extends BaseResponse {

    private List<SingerAlbumEntity> worksPhotosList;

    public List<SingerAlbumEntity> getWorksPhotosList() {
        return worksPhotosList;
    }

    public void setWorksPhotosList(List<SingerAlbumEntity> worksPhotosList) {
        this.worksPhotosList = worksPhotosList;
    }
}
