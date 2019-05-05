package com.example.webmoments.ui.activity

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.MenuItem
import com.example.webmoments.ui.fragment.MomentDetailFragment
import com.example.webmoments.R
import com.example.webmoments.dummy.MomentsContent
import com.example.webmoments.entity.Moment
import com.example.webmoments.util.ImageBuilderUtil
import kotlinx.android.synthetic.main.activity_moment_detail.*

class MomentDetailActivity : AppCompatActivity() {

    lateinit var moment: Moment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_moment_detail)
        setSupportActionBar(detail_toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        moment = MomentsContent.ITEM_MAP[intent.getStringExtra(MomentDetailFragment.ARG_ITEM_ID)] as Moment

        btnNew.setOnClickListener { view ->
            val intent = Intent(view.context, MomentFormActivity::class.java).apply {
                putExtra("moment_id", moment.id)
            }
            view.context.startActivity(intent)
        }


        if (savedInstanceState == null) {
            if (moment.imagePath.isNotEmpty()) {
                imgMoment.setImageBitmap(ImageBuilderUtil.prepare(
                    moment.imagePath,
                    1000,
                    500
                ))
            }
            val fragment = MomentDetailFragment().apply {
                arguments = Bundle().apply {
                    putString(
                        MomentDetailFragment.ARG_ITEM_ID,
                        intent.getStringExtra(MomentDetailFragment.ARG_ITEM_ID)
                    )
                }
            }

            supportFragmentManager.beginTransaction()
                .add(R.id.moment_detail_container, fragment)
                .commit()
        }
    }

    override fun onOptionsItemSelected(item: MenuItem) =
        when (item.itemId) {
            android.R.id.home -> {
                navigateUpTo(Intent(this, MomentListActivity::class.java))
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
}
