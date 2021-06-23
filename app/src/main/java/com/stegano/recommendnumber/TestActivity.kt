package com.stegano.recommendnumber

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet
import kotlinx.android.synthetic.main.activity_test.*


class TestActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_test)

        val lineDataSet1 = LineDataSet(dataValues1(), "Data Set 1")
        val lineDataSet2 = LineDataSet(dataValues2(), "Data Set 2")

        val dataSets = ArrayList<ILineDataSet>()
        dataSets.add(lineDataSet1)
        dataSets.add(lineDataSet2)

        val data = LineData(dataSets)
        line_chart.data = data
        line_chart.invalidate()
    }

    private fun dataValues1() : ArrayList<Entry> {
        val dataValues = ArrayList<Entry>()
        dataValues.add(Entry(0f, 20f))
        dataValues.add(Entry(1f, 10f))
        dataValues.add(Entry(2f, 30f))
        dataValues.add(Entry(3f, 30f))
        dataValues.add(Entry(4f, 30f))
        dataValues.add(Entry(5f, 30f))
        dataValues.add(Entry(6f, 30f))
        return dataValues
    }

    private fun dataValues2() : ArrayList<Entry> {
        val dataValues = ArrayList<Entry>()
        dataValues.add(Entry(0f, 10f))
        dataValues.add(Entry(2f, 30f))
        dataValues.add(Entry(4f, 23f))
        dataValues.add(Entry(5f, 43f))
        return dataValues
    }
}