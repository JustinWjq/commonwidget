package com.common.widget.recyclerviewadapterhelper.provider//package com.txt.video.widget
//
//import android.view.View
//import com.chad.library.adapter.base.entity.node.BaseNode
//import com.chad.library.adapter.base.provider.BaseNodeProvider
//import com.chad.library.adapter.base.viewholder.BaseViewHolder
//import com.txt.video.R
//import com.txt.video.net.bean.LevelItem1
//
///**
// * Created by JustinWjq
// * @date 2020/8/31.
// * description：
// */
//public class RootNodeProvider : BaseNodeProvider(){
//
//    override val itemViewType: Int
//        get() = 0
//    override val layoutId: Int
//        get() = R.layout.tx_layout_item_head
//
//    override fun convert(helper: BaseViewHolder, item: BaseNode) {
//        val levelItem1 = item as LevelItem1
//        helper.setText(R.id.tx_tv_headtitle,levelItem1.title)
//        if (levelItem1.isExpanded) {
//            helper.setImageResource(R.id.tx_iv_arrow,R.drawable.tx_icon_bottom_arrow)
//        }else{
//            helper.setImageResource(R.id.tx_iv_arrow,R.drawable.tx_icon_right_arrow)
//        }
//
//    }
//
//    override fun onClick(helper: BaseViewHolder, view: View, data: BaseNode, position: Int) {
//        getAdapter()?.expandOrCollapse(position)
//    }
//
//
//}