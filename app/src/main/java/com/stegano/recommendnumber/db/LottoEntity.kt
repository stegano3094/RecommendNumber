package com.stegano.recommendnumber.db

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "lottoNumber")
data class LottoEntity(
    @PrimaryKey var round: Int,
    var drwtNo1: Int,
    var drwtNo2: Int,
    var drwtNo3: Int,
    var drwtNo4: Int,
    var drwtNo5: Int,
    var drwtNo6: Int,
    var bnusNo: Int
)

enum class ParseNumber {
    drwNo, drwtNo1, drwtNo2, drwtNo3, drwtNo4, drwtNo5, drwtNo6, bnusNo
}