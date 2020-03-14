package com.example.spk


class Fuzzy {
    private var decisionIndex : Float = 0F
    private var Rekomendasi = false

    fun fuzzy(gaji:Int, tanggungan: Int, luas: Int){
        val gaji1 = gaji.toFloat()
        val tanggungan1 = tanggungan.toFloat()
        val luas1 = luas.toFloat()
        var fkAturan = FloatArray(18)
        var indexAturan = FloatArray(18)
        var gSedikit  = 0F
        var gSedang  = 0F
        var gBanyak  = 0F
        var tSedikit  = 0F
        var tBanyak  = 0F
        var lSempit  = 0F
        var lSedang  = 0F
        var lLuas  = 0F

        //fungsi keanggotaan gaji

        if(gaji in 0..1000000){
            gSedikit = (1).toFloat()
        }else if(gaji in 1000001..2000000){
            println("please")
            gSedikit = ((2000000-gaji1)/(2000000-1000000)) //turun
            gSedang = ((gaji1-1000000)/(2000000-1000000)) //naik
        }else if (gaji in 2000001..3000000){
            gSedang =(1).toFloat()
        }else if(gaji in 3000001..4500000){
            gSedang = ((4500000-gaji1)/(4500000-3000000))
            gBanyak = ((gaji1-3000000)/(4500000-3000000))
        }else if(gaji > 4500000){
            gBanyak = (1).toFloat()
        }

        //fungsi keanggotaan luas

        if(luas in 0..60){
            lSempit = (1).toFloat()
        }else if(luas in 61..72){
            lSempit = ((72-luas1)/(72-60)).toFloat()
            lSedang = ((luas1-60)/(72-60)).toFloat()
        }else if(luas in 73..80){
            lSedang = (1).toFloat()
        }else if(luas in 81..95){
            lSedang = ((95-luas1)/(95-80)).toFloat()
            lLuas = ((luas1-80)/(95-80)).toFloat()
        }else if(luas > 95){
            lLuas = (1).toFloat()
        }

        //fungsi kenggotaan tanggungan
        if(tanggungan in 0..2){
            tSedikit = (1).toFloat()
        }else if(tanggungan in 3..5){
            tSedikit = ((5-tanggungan1)/(5-2)).toFloat()
            tBanyak = ((tanggungan1-2)/(5-2)).toFloat()
        }else if(tanggungan > 5){
            tBanyak = (1).toFloat()
        }
        println("Gaji Sedikit : $gSedikit | Gaji Sedang : $gSedang | Gaji Banyak : $gBanyak")
        println("Luas Sempit : $lSempit | Luas Sedang : $lSedang | Luas Luas : $lLuas")
        println("Tanggungan Sedikit : $tSedikit | Tanggungan Banyak : $tBanyak")

        fkAturan[0] = minOf(gSedikit,lSempit,tSedikit)
        fkAturan[1] = minOf(gSedikit,lSempit,tBanyak)
        fkAturan[2] = minOf(gSedikit,lSedang,tSedikit)
        fkAturan[3] = minOf(gSedikit,lSedang,tBanyak)
        fkAturan[4] = minOf(gSedikit,lLuas,tSedikit)
        fkAturan[5] = minOf(gSedikit,lLuas,tBanyak)
        fkAturan[6] = minOf(gSedang,lSempit,tSedikit)
        fkAturan[7] = minOf(gSedang,lSempit,tBanyak)
        fkAturan[8] = minOf(gSedang,lSedang,tSedikit)
        fkAturan[9] = minOf(gSedang,lSedang,tBanyak)
        fkAturan[10] = minOf(gSedang,lLuas,tSedikit)
        fkAturan[11] = minOf(gSedang,lLuas,tBanyak)
        fkAturan[12] = minOf(gBanyak,lSempit,tSedikit)
        fkAturan[13] = minOf(gBanyak,lSempit,tBanyak)
        fkAturan[14] = minOf(gBanyak,lSedang,tSedikit)
        fkAturan[15] = minOf(gBanyak,lSedang,tBanyak)
        fkAturan[16] = minOf(gBanyak,lLuas,tSedikit)
        fkAturan[17] = minOf(gBanyak,lLuas,tBanyak)


        indexAturan[0] = (0.8).toFloat()
        indexAturan[1] = (0.8).toFloat()
        indexAturan[2] = (0.8).toFloat()
        indexAturan[3] = (0.8).toFloat()
        indexAturan[4] = (0.4).toFloat()
        indexAturan[5] = (0.8).toFloat()
        indexAturan[6] = (0.4).toFloat()
        indexAturan[7] = (0.8).toFloat()
        indexAturan[8] = (0.4).toFloat()
        indexAturan[9] = (0.8).toFloat()
        indexAturan[10] = (0.4).toFloat()
        indexAturan[11] = (0.4).toFloat()
        indexAturan[12] = (0.4).toFloat()
        indexAturan[13] = (0.4).toFloat()
        indexAturan[14] = (0.4).toFloat()
        indexAturan[15] = (0.4).toFloat()
        indexAturan[16] = (0.4).toFloat()
        indexAturan[17] = (0.4).toFloat()


        //defuzzifikasi


        var penyebut = 0F
        var pembagi = 0F

        for (x in 0 until 18){
            if(fkAturan[x]!= 0F){
                penyebut += (fkAturan[x]*indexAturan[x])
                pembagi += fkAturan[x]
            }
            println("Aturan Index "+x+" : "+indexAturan[x])
            println("Aturan FK "+x+" : "+fkAturan[x])
        }
        println("Penyebut : "+penyebut)
        println("Pembagi : "+pembagi)

        this.decisionIndex = penyebut/pembagi
        println("Decission Index : "+this.decisionIndex)
        //akhir defuzzifikasi
        setRekomendasi(penyebut/pembagi)


    }
    fun setRekomendasi(dI : Float){
        var selisih1 = 0F
        var selisih2 = 0F

        if(dI >= 0.8F){
            selisih1 = dI - 0.8F
        }else if(dI < 0.8F){
            selisih1 = 0.8F - dI
        }

        if(dI >= 0.4F){
            selisih2 = dI - 0.4F
        }else if(dI < 0.4F){
            selisih2 = 0.4F - dI
        }
        println("selisih 1 "+selisih1)
        println("selisih 2 "+selisih2)
        if(selisih1<=selisih2){
            this.Rekomendasi = true
        }else if(selisih2<selisih1){
            this.Rekomendasi = false
        }
    }



    fun getRekomendasi(): Boolean {
        return this.Rekomendasi
    }

    fun getDecissionIndex(): Float {
        return this.decisionIndex
    }
}