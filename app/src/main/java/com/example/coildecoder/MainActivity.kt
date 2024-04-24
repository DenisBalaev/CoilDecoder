package com.example.coildecoder

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.coildecoder.databinding.ActivityMainBinding
import java.math.BigInteger


class MainActivity : AppCompatActivity(R.layout.activity_main) {

    private val viewBinding by viewBinding(ActivityMainBinding::bind)

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val list = listOf("FF", "01")
        /*val res1 = decoderValueParameterDetailCoil(argList = list, radr = 1, charactersBinary = 10)
        val res2 = decoderValueParameterDetailCoil2(argList = list, radr = 1, charactersBinary = 10)
        val res3 = decoderValueParameterDetailCoil3(argList = list, radr = 1, charactersBinary = 10)
        val res4 = decoderValueParameterDetailCoil4(argList = list, radr = 1, charactersBinary = 10)
        Log.d("LIST_BINARY_MAP",res1.toString())
        Log.d("LIST_BINARY_MAP",res2.toString())
        Log.d("LIST_BINARY_MAP",res3.toString())
        Log.d("LIST_BINARY_MAP",res4.toString())
        /*Log.d("LIST_BINARY_MAP", (res2 == res1).toString())
        Log.d("LIST_BINARY_MAP", (res3 == res1).toString())
        Log.d("LIST_BINARY_MAP", (res3 == res4).toString())*/
        val str1 = res1.map { it.value }.joinToString("")
        val str2 = res2.map { it.value }.joinToString("")
        val str3 = res3.map { it.value }.joinToString("")
        val str4 = res4.map { it.value }.joinToString("")*/

        /*viewBinding.tv.text = str1 + "\n" + str2 + "\n" + str3 + "\n" + str4 + "\n"
        viewBinding.tv.text = viewBinding.tv.text.toString() + "\n" + String.format("%16s", "01FF".toDecimalMoreTwo().toString(2)).replace(' ', '0').reversed()*/

        //val hex = list.reversed().joinToString("")
        val hex = "AA01AA01AA01AA01AA01AA01AA01AA01AA01AA01AA01AA01AA01AA01AA01AA01"
        val bin = toHexToBinary(hex,8)
        //val bin = DatatypeConverter.printHexBinary(DatatypeConverter.parseHexBinary(hex))
        /*val bin3 = HexFormat.of().parseHex(hex)
        val bin4 = Hex.decodeHex("00A0BF") \\ http://commons.apache.org/codec/
        val bin5 = BaseEncoding.base16().decode(string)
        val bin = "c000060000".decodeHex().toString()  //https://square.github.io/okio/ */
        Log.d("LIST_BINARY_MAP", hex)
        Log.d(
            "LIST_BINARY_MAP",
            (bin == "10101010000000011010101000000001101010100000000110101010000000011010101000000001101010100000000110101010000000011010101000000001").toString()
        )
        viewBinding.tv.text = bin + " ${bin?.length}"
    }

    private fun toHexToBinary(hex: String, charactersBinary:Int): String? {
        val bin = BigInteger(hex, 16).toString(2)
        return String.format("%${charactersBinary}s", bin).replace(' ', '0')
    }

    private fun decoderValueParameterDetailCoil4(
        argList: List<String>,
        radr: Int,
        charactersBinary: Int
    ): List<ParameterValueUI> {
        val valueInt = argList.reversed().joinToString(separator = "").toDecimalMoreTwo()
        return String.format("%${charactersBinary}s", valueInt.toString(2)).replace(' ', '0')
            .reversed().mapIndexed { index, c ->
                ParameterValueUI(pin = radr + index, value = c.toString())
            }
    }

    private fun decoderValueParameterDetailCoil(
        argList: List<String>,
        radr: Int,
        charactersBinary: Int
    ): List<ParameterValueUI> {
        var pin = radr
        val maxRadr = radr + charactersBinary - 1
        val countListToList = 2
        val countBit = countListToList * 8
        val list = mutableListOf<ParameterValueUI>()

        argList.reversed().chunked(countListToList)
            .map { it.joinToString(separator = "") }
            .forEach { str ->
                val stringBinary = str.toDecimalMoreTwo().toBinaryStringCustom(countBit).reversed()
                for (c in stringBinary) {
                    if (pin <= maxRadr) {
                        list.add(ParameterValueUI(pin = pin, value = c.toString()))
                        pin += 1
                    } else {
                        break
                    }
                }
            }
        return list
    }

    private fun decoderValueParameterDetailCoil2(
        argList: List<String>,
        radr: Int,
        charactersBinary: Int
    ): List<ParameterValueUI> {
        val countListToList = 2
        val countBit = countListToList * 8
        val list = argList.reversed().chunked(countListToList)
            .map { it.joinToString(separator = "") }.joinToString(separator = "") { str ->
                str.toDecimalMoreTwo().toBinaryStringCustom(countBit).reversed()
            }
            .substring(0, charactersBinary)

        Log.d("LIST_BINARY_MAP_Item", list.toString())

        return list.mapIndexed { index, c ->
            ParameterValueUI(pin = radr + index, value = c.toString())
        }
    }

    private fun decoderValueParameterDetailCoil3(
        radr: Int,
        charactersBinary: Int,
        argList: List<String>
    ): List<ParameterValueUI> {
        /*val str = argList.reversed().joinToString(separator = "")
        val stringBinary = str.toDecimalMoreTwo().toBinaryStringCustom(charactersBinary).reversed()
        val list = stringBinary.mapIndexed { index, c ->
            ParameterValueUI(pin = radr + index, value = c.toString())
        }*/

        return argList.reversed().joinToString(separator = "")
            .toDecimalMoreTwo().toBinaryStringCustom(charactersBinary).reversed()
            .mapIndexed { index, c ->
                ParameterValueUI(pin = radr + index, value = c.toString())
            }

        //return list
    }


}

data class ParameterValueUI(
    val pin: Int,
    val value: String
)

fun String.toDecimalMoreTwo(): Int {
    return Integer.parseInt(this, 16)
}

fun Int.toBinaryStringCustom(charactersBinary: Int): String {
    return String.format("%${charactersBinary}s", Integer.toBinaryString(this)).replace(' ', '0')
}

fun String.toDec(): String {
    return "%0d".format(this)
}




