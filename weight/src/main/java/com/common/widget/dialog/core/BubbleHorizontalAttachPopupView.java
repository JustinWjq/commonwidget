package com.common.widget.dialog.core;

import android.content.Context;
import android.graphics.Rect;

import androidx.annotation.NonNull;

import com.common.widget.dialog.TxPopup;
import com.common.widget.dialog.enums.PopupPosition;
import com.common.widget.dialog.util.XPopupUtils;
import com.common.widget.dialog.widget.BubbleLayout;

/**
 * Description: 水平方向带气泡的Attach弹窗
 */
public class BubbleHorizontalAttachPopupView extends BubbleAttachPopupView {
    public BubbleHorizontalAttachPopupView(@NonNull Context context) {
        super(context);
    }

    @Override
    protected void initPopupContent() {
        bubbleContainer.setLook(BubbleLayout.Look.LEFT); //解决高度不正确的问题
        super.initPopupContent();
        defaultOffsetY = popupInfo.offsetY;
        defaultOffsetX = popupInfo.offsetX == 0 ? XPopupUtils.dp2px(getContext(), 2) : popupInfo.offsetX;
    }

    /**
     * 执行附着逻辑
     */
    public void doAttach() {
        final boolean isRTL = XPopupUtils.isLayoutRtl(getContext());
        float translationX = 0, translationY = 0;
        int w = getPopupContentView().getMeasuredWidth();
        int h = getPopupContentView().getMeasuredHeight();
        //0. 判断是依附于某个点还是某个View
        if (popupInfo.touchPoint != null) {
            if(TxPopup.longClickPoint!=null) popupInfo.touchPoint = TxPopup.longClickPoint;
            // 依附于指定点
            isShowLeft = popupInfo.touchPoint.x > XPopupUtils.getWindowWidth(getContext()) / 2;

            // translationX: 在左边就和点左边对齐，在右边就和其右边对齐
            if(isRTL){
                translationX = isShowLeft ?  -(XPopupUtils.getWindowWidth(getContext())-popupInfo.touchPoint.x+defaultOffsetX)
                        : -(XPopupUtils.getWindowWidth(getContext())-popupInfo.touchPoint.x-getPopupContentView().getMeasuredWidth()-defaultOffsetX);
            }else {
                translationX = isShowLeftToTarget() ? (popupInfo.touchPoint.x - w - defaultOffsetX) : (popupInfo.touchPoint.x + defaultOffsetX);
            }
            translationY = popupInfo.touchPoint.y - h * .5f + defaultOffsetY;
        } else {
            // 依附于指定View
            //1. 获取atView在屏幕上的位置
            int[] locations = new int[2];
            popupInfo.getAtView().getLocationOnScreen(locations);
            Rect rect = new Rect(locations[0], locations[1], locations[0] + popupInfo.getAtView().getMeasuredWidth(),
                    locations[1] + popupInfo.getAtView().getMeasuredHeight());

            int centerX = (rect.left + rect.right) / 2;

            isShowLeft = centerX > XPopupUtils.getWindowWidth(getContext()) / 2;
            if(isRTL){
                translationX = isShowLeft ?  -(XPopupUtils.getWindowWidth(getContext())-rect.left + defaultOffsetX)
                        : -(XPopupUtils.getWindowWidth(getContext())-rect.right-getPopupContentView().getMeasuredWidth()-defaultOffsetX);
            }else {
                translationX = isShowLeftToTarget() ? (rect.left - w - defaultOffsetX) : (rect.right + defaultOffsetX);
            }
            translationY = rect.top + (rect.height()-h)/2f + defaultOffsetY;
        }
        //设置气泡相关
        if(isShowLeftToTarget()){
            bubbleContainer.setLook(BubbleLayout.Look.RIGHT);
        }else {
            bubbleContainer.setLook(BubbleLayout.Look.LEFT);
        }
        bubbleContainer.setLookPositionCenter(true);
        bubbleContainer.invalidate();

        getPopupContentView().setTranslationX(translationX);
        getPopupContentView().setTranslationY(translationY);
        initAndStartAnimation();
    }

    private boolean isShowLeftToTarget() {
        return (isShowLeft || popupInfo.popupPosition == PopupPosition.Left)
                && popupInfo.popupPosition != PopupPosition.Right;
    }
}
