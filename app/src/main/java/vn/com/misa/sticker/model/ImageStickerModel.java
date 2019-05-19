package vn.com.misa.sticker.model;

import vn.com.misa.sticker.custom.ImageSticker;

public class ImageStickerModel {
    private ImageSticker mImage;
    private String path;

    public ImageStickerModel() {
    }

    public ImageStickerModel(ImageSticker mImage, String path) {
        this.mImage = mImage;
        this.path = path;
    }

    public ImageSticker getmImage() {
        return mImage;
    }

    public void setmImage(ImageSticker mImage) {
        this.mImage = mImage;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }
}
