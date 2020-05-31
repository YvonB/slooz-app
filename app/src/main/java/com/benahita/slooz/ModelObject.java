package com.benahita.slooz;

public enum ModelObject {
    FIRST(R.string.first, R.layout.view_first_slide),
    SECOND(R.string.second, R.layout.view_second_slide);

    private int mTitleResId;
    private int mLayoutResId;

    ModelObject(int titleResId, int layoutResId) {
        mTitleResId = titleResId;
        mLayoutResId = layoutResId;
    }

    public int getTitleResId() {
        return mTitleResId;
    }

    public int getLayoutResId() {
        return mLayoutResId;
    }
}
