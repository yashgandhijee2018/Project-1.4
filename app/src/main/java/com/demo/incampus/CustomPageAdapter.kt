package com.demo.incampus

import android.content.Context
import android.view.LayoutInflater
import android.view.TextureView
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.NonNull
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentPagerAdapter
import androidx.viewpager.widget.PagerAdapter


class CustomPageAdapter : PagerAdapter {
    val mContext: Context
    val mListScreen: List<ScreenItem>
    constructor(mContext: Context, mListScreen: List<ScreenItem>){
        this.mContext=mContext
        this.mListScreen=mListScreen
    }

    @NonNull
    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val inflater = mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val layoutScreen = inflater.inflate(R.layout.layout_screen,null)
        val img = layoutScreen.findViewById<View>(R.id.intro_img) as ImageView
        val title = layoutScreen.findViewById<View>(R.id.intro_title) as TextView
        val desc = layoutScreen.findViewById<View>(R.id.intro_description) as TextView

        title.text=(mListScreen[position].title)
        desc.text=(mListScreen[position].desc)
        img.setImageResource(mListScreen[position].pic)

        container.addView(layoutScreen)

        return layoutScreen

    }

    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return view == `object`
    }

    override fun getCount(): Int {
        return mListScreen.size
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        container.removeView(`object` as View)
    }

}