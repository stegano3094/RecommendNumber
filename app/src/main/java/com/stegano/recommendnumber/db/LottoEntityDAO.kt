package com.stegano.recommendnumber.db

import androidx.room.*

@Dao
interface LottoEntityDAO {
    @Insert
    fun insert(user: LottoEntity)

    @Update
    fun update(user: LottoEntity)

    @Delete
    fun delete(user: LottoEntity)

    // 프라이빗키로 lottoNumber 테이블의 값이 존재하는지 확인
    @Query("SELECT * FROM lottoNumber WHERE round = :i")
    fun findInTable(i : Int) : LottoEntity

    // 프라이빗키로 lottoNumber 테이블의 값을 삭제함
    @Query("DELETE FROM lottoNumber WHERE round = :i")
    fun deleteByKey(i : Int)

    // lottoNumber 테이블의 모든 값을 삭제함
    @Query("DELETE FROM lottoNumber")
    fun deleteAll()

    @Query("SELECT * FROM lottoNumber")
    fun getAll() : List<LottoEntity>
}