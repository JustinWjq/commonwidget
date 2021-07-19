package com.common.widget.recyclerviewadapterhelper.provider//package com.txt.video.widget
//
//import android.view.View
//import android.widget.ImageView
//import com.bumptech.glide.Glide
//import com.chad.library.adapter.base.entity.node.BaseNode
//import com.chad.library.adapter.base.provider.BaseNodeProvider
//import com.chad.library.adapter.base.viewholder.BaseViewHolder
//import com.txt.video.R
//import com.txt.video.widget.utils.DatetimeUtil.UTCToCST
//import com.txt.video.net.bean.FileBean
//import com.txt.video.net.utils.TxLogUtils.i
//import java.text.ParseException
//
///**
// * Created by JustinWjq
// * @date 2020/8/31.
// * description：
// */
//public class RootItemNodeProvider : BaseNodeProvider() {
//
//    override val itemViewType: Int
//        get() = 1
//    override val layoutId: Int
//        get() = R.layout.tx_adapter_file_list_item
//
//    override fun convert(baseViewHolder: BaseViewHolder, itemBean: BaseNode) {
//
//        val listBean = itemBean as FileBean.ListBean
//        val name: String = listBean.name
//
//        baseViewHolder.setText(R.id.tx_tv_file_name, name)
//        try {
//            val split = name.split("\\.".toRegex()).toTypedArray()
//            val suffix = split[1]
//            i("suffix--$suffix")
//            if (suffix == "ppt" || suffix == "pptx") {
//                baseViewHolder.setImageResource(R.id.tx_iv_file_pic, R.drawable.tx_ppt)
//            } else if (suffix == "pdf") {
//                baseViewHolder.setImageResource(R.id.tx_iv_file_pic, R.drawable.tx_icon_pdf)
//            } else if (suffix == "word" || suffix == "docx") {
//                baseViewHolder.setImageResource(R.id.tx_iv_file_pic, R.drawable.tx_icon_word)
//            } else {
//                val view = baseViewHolder.getView<ImageView>(R.id.tx_iv_file_pic)
//
//                Glide.with(view.context).load(listBean.url).placeholder(R.drawable.tx_pic_default)
//                    .into(view)
//
////                baseViewHolder.setImageResource(R.id.tx_iv_file_pic, R.drawable.tx_pic_default)
//            }
//        } catch (e: Exception) {
//        }
//
//
//        try {
//            baseViewHolder.setText(R.id.tx_tv_file_time, UTCToCST(listBean.getCtime()))
//        } catch (e: ParseException) {
//            e.printStackTrace()
//        }
//
//    }
//
//    override fun onClick(helper: BaseViewHolder, view: View, data: BaseNode, position: Int) {
//        super.onClick(helper, view, data, position)
//
//    }
//
//
//}