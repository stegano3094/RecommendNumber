package com.stegano.recommendnumber

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.WindowManager
import android.widget.Toast
import com.stegano.recommendnumber.db.LottoDB
import com.stegano.recommendnumber.db.LottoEntity
import com.stegano.recommendnumber.db.ParseNumber
import kotlinx.android.synthetic.main.activity_splash.*
import okhttp3.*
import org.json.JSONObject
import java.lang.Exception
import java.lang.Runnable


class SplashActivity : AppCompatActivity() {
    val TAG = "SplashActivity"
    lateinit var result: String
    var lottoDB: LottoDB? = null
    var per: Int = 0
    var runningThread = false
    var getThread: Thread? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)  // 로딩 중 화면 켜짐 유지

        /**
         * 6월 22일 새벽에 보니까 서버 점검을 하더라... 그래서 서버 점검 때 데이터를 못가져오는 것을
         * 방지하지 위해서 초기에 한번만 모든 데이터를 가져온 후 로컬 데이터에 저장시켜 사용한다.
         */

        lottoDB = LottoDB.getInstance(this)

        // 데이터 베이스 초기화 코드
//        val dbClearTestRunnable = Runnable {
//            Log.e(TAG, "Runnable: DB getAll : ${lottoDB?.lottoEntityDAO()?.getAll()}")
//            Log.e(TAG, "Runnable: DB deleteAll : ${lottoDB?.lottoEntityDAO()?.deleteAll()}")
//            Log.e(TAG, "Runnable: DB가 모두 삭제되었습니다.")
//            Log.e(TAG, "Runnable: DB getAll : ${lottoDB?.lottoEntityDAO()?.getAll()}")
//        }
//        val dbClearTestThread = Thread(dbClearTestRunnable)
//        dbClearTestThread.start()

        // 데이터 로딩 전에 이미 파일이 있는지 확인하는 코드 필요함

        // 데이터 로딩
        getThread = Thread(getLottoData())
        getThread!!.start()
    }

    private fun getLottoData(): Runnable {
        return Runnable {
            Thread.sleep(200)  // 초기화할 수 있는 시간을 벌어주기 위해 넣음

            if (true) {  // 데이터가 비어있으면 처음부터 로딩 (대신 스킵 있음)
                runningThread = true
                val latest = 40
                for (i in 1..latest) {
                    if (lottoDB?.lottoEntityDAO()?.findInTable(i) == null) {
                        Log.e(TAG, "getLottoData: lottoDB에 $i 번째 데이터가 없어 다운로드 진행합니다.")
                    } else {  // 이미 로딩된 부분은 넘어가는 코드
                        Log.e(TAG, "getLottoData: lottoDB에 $i 번째에 데이터가 이미 있습니다.")
                        continue
                    }

                    val lottoUrl = "https://www.dhlottery.co.kr/common.do?method=getLottoNumber&drwNo="
                    val getDataUrl = lottoUrl + i  // 실제 데이터를 가져올 주소

                    try {
                        val client = OkHttpClient()
                        val request = Request.Builder().url(getDataUrl).build()
                        val response: Response =
                            client.newCall(request).execute()  // 동기방식으로 데이터 가져옴

                        val resToString = response.body?.string()
                        if (!resToString.isNullOrBlank()) {  // 받아온 데이터의 값이 있을 때 실행
                            val entity = stringToLottoEntity(resToString)
                            lottoDB?.lottoEntityDAO()?.insert(entity)
                            //Log.e(TAG, "getAllRunnable: DB : ${lottoDB?.lottoEntityDAO()?.getAll()}")
                        }

                        // 에러가 없으면 UI로 현재 다운로드된 퍼센트 확인
                        per = i * 100 / latest
                        changeUI()
                        Log.e(TAG, "getLottoData: i : $i, per : ${per}%")
                    } catch (e: Exception) {
                        Log.e(TAG, "getLottoData: OkHttpClient 에러 발생, i : $i")
                        e.printStackTrace()
                    }
                }  // for
                Thread.sleep(700)
                changeActivity()  // 다운로드 완료 후 메인화면으로 이동
            }  // if
        }
    }

    private fun changeActivity() {
        //Log.e(TAG, "changeActivity: per : ${per}%")
        per = 100
        changeUI()

        runOnUiThread {  // 다운로드 완료 시 메인화면으로 이동
            Log.e(TAG, "getLottoData: load End")
            Toast.makeText(applicationContext, "다운로드 완료하였습니다.", Toast.LENGTH_SHORT).show()
            startActivity(Intent(applicationContext, TestActivity::class.java))
            finish()
        }
    }

    private fun changeUI() {
        runOnUiThread {
            progressBar.setProgress(per)
            percent.setText("${per}%")
        }
    }

    fun stringToLottoEntity(data: String): LottoEntity {
        val jsonObject = JSONObject(data)  // String -> JSONObject
        Log.i(TAG, "GetJsonParse: jsonObject : $jsonObject")
        val resultLottoEntity = LottoEntity(
            jsonObject.get(ParseNumber.drwNo.toString()) as Int,
            jsonObject.get(ParseNumber.drwtNo1.toString()) as Int,
            jsonObject.get(ParseNumber.drwtNo2.toString()) as Int,
            jsonObject.get(ParseNumber.drwtNo3.toString()) as Int,
            jsonObject.get(ParseNumber.drwtNo4.toString()) as Int,
            jsonObject.get(ParseNumber.drwtNo5.toString()) as Int,
            jsonObject.get(ParseNumber.drwtNo6.toString()) as Int,
            jsonObject.get(ParseNumber.bnusNo.toString()) as Int
        )

        Log.i(TAG, "GetJsonParse: resultLottoEntity : $resultLottoEntity")
        return resultLottoEntity
    }

    override fun onDestroy() {
        super.onDestroy()
        getThread = null
        runningThread = false  // 스레드 루프문 종료 (앱 닫아도 돌아가서 넣어둠)
        LottoDB.destroyInstance()
    }
}