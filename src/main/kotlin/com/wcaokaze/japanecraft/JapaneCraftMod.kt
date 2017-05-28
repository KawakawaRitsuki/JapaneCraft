package com.wcaokaze.japanecraft

import com.wcaokaze.japanecraft.RomajiConverter.Output
import com.wcaokaze.util.parseJson
import cpw.mods.fml.common.FMLCommonHandler
import cpw.mods.fml.common.Mod
import cpw.mods.fml.common.event.FMLInitializationEvent
import cpw.mods.fml.common.event.FMLPreInitializationEvent
import cpw.mods.fml.common.eventhandler.SubscribeEvent
import cpw.mods.fml.common.network.NetworkCheckHandler
import cpw.mods.fml.relauncher.Side
import net.minecraft.util.ChatComponentText
import net.minecraftforge.common.MinecraftForge
import net.minecraftforge.common.config.Configuration
import net.minecraftforge.event.ServerChatEvent
import java.io.File
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*

@Mod(modid = "japanecraft", version = "0.3.2")
class JapaneCraftMod {
  private var romajiConverter = RomajiConverter(mapOf(
      "-"    to Output("ー"),
      "~"    to Output("〜"),
      "."    to Output("。"),
      ","    to Output("、"),
      "z/"   to Output("・"),
      "z."   to Output("…"),
      "z,"   to Output("‥"),
      "zh"   to Output("←"),
      "zj"   to Output("↓"),
      "zk"   to Output("↑"),
      "zl"   to Output("→"),
      "z-"   to Output("〜"),
      "z["   to Output("『"),
      "z]"   to Output("』"),
      "["    to Output("「"),
      "]"    to Output("」"),
      "va"   to Output("ゔぁ"),
      "vi"   to Output("ゔぃ"),
      "vu"   to Output("ゔ"),
      "ve"   to Output("ゔぇ"),
      "vo"   to Output("ゔぉ"),
      "vya"  to Output("ゔゃ"),
      "vyi"  to Output("ゔぃ"),
      "vyu"  to Output("ゔゅ"),
      "vye"  to Output("ゔぇ"),
      "vyo"  to Output("ゔょ"),
      "qq"   to Output("っ", nextInput = "q"),
      "vv"   to Output("っ", nextInput = "v"),
      "ll"   to Output("っ", nextInput = "l"),
      "xx"   to Output("っ", nextInput = "x"),
      "kk"   to Output("っ", nextInput = "k"),
      "gg"   to Output("っ", nextInput = "g"),
      "ss"   to Output("っ", nextInput = "s"),
      "zz"   to Output("っ", nextInput = "z"),
      "jj"   to Output("っ", nextInput = "j"),
      "tt"   to Output("っ", nextInput = "t"),
      "dd"   to Output("っ", nextInput = "d"),
      "hh"   to Output("っ", nextInput = "h"),
      "ff"   to Output("っ", nextInput = "f"),
      "bb"   to Output("っ", nextInput = "b"),
      "pp"   to Output("っ", nextInput = "p"),
      "mm"   to Output("っ", nextInput = "m"),
      "yy"   to Output("っ", nextInput = "y"),
      "rr"   to Output("っ", nextInput = "r"),
      "ww"   to Output("っ", nextInput = "w"),
      "www"  to Output("w",  nextInput = "ww"),
      "cc"   to Output("っ", nextInput = "c"),
      "kya"  to Output("きゃ"),
      "kyi"  to Output("きぃ"),
      "kyu"  to Output("きゅ"),
      "kye"  to Output("きぇ"),
      "kyo"  to Output("きょ"),
      "gya"  to Output("ぎゃ"),
      "gyi"  to Output("ぎぃ"),
      "gyu"  to Output("ぎゅ"),
      "gye"  to Output("ぎぇ"),
      "gyo"  to Output("ぎょ"),
      "sya"  to Output("しゃ"),
      "syi"  to Output("しぃ"),
      "syu"  to Output("しゅ"),
      "sye"  to Output("しぇ"),
      "syo"  to Output("しょ"),
      "sha"  to Output("しゃ"),
      "shi"  to Output("し"),
      "shu"  to Output("しゅ"),
      "she"  to Output("しぇ"),
      "sho"  to Output("しょ"),
      "zya"  to Output("じゃ"),
      "zyi"  to Output("じぃ"),
      "zyu"  to Output("じゅ"),
      "zye"  to Output("じぇ"),
      "zyo"  to Output("じょ"),
      "tya"  to Output("ちゃ"),
      "tyi"  to Output("ちぃ"),
      "tyu"  to Output("ちゅ"),
      "tye"  to Output("ちぇ"),
      "tyo"  to Output("ちょ"),
      "cha"  to Output("ちゃ"),
      "chi"  to Output("ち"),
      "chu"  to Output("ちゅ"),
      "che"  to Output("ちぇ"),
      "cho"  to Output("ちょ"),
      "cya"  to Output("ちゃ"),
      "cyi"  to Output("ちぃ"),
      "cyu"  to Output("ちゅ"),
      "cye"  to Output("ちぇ"),
      "cyo"  to Output("ちょ"),
      "dya"  to Output("ぢゃ"),
      "dyi"  to Output("ぢぃ"),
      "dyu"  to Output("ぢゅ"),
      "dye"  to Output("ぢぇ"),
      "dyo"  to Output("ぢょ"),
      "tsa"  to Output("つぁ"),
      "tsi"  to Output("つぃ"),
      "tse"  to Output("つぇ"),
      "tso"  to Output("つぉ"),
      "tha"  to Output("てゃ"),
      "thi"  to Output("てぃ"),
      "t'i"  to Output("てぃ"),
      "thu"  to Output("てゅ"),
      "the"  to Output("てぇ"),
      "tho"  to Output("てょ"),
      "t'yu" to Output("てゅ"),
      "dha"  to Output("でゃ"),
      "dhi"  to Output("でぃ"),
      "d'i"  to Output("でぃ"),
      "dhu"  to Output("でゅ"),
      "dhe"  to Output("でぇ"),
      "dho"  to Output("でょ"),
      "d'yu" to Output("でゅ"),
      "twa"  to Output("とぁ"),
      "twi"  to Output("とぃ"),
      "twu"  to Output("とぅ"),
      "twe"  to Output("とぇ"),
      "two"  to Output("とぉ"),
      "t'u"  to Output("とぅ"),
      "dwa"  to Output("どぁ"),
      "dwi"  to Output("どぃ"),
      "dwu"  to Output("どぅ"),
      "dwe"  to Output("どぇ"),
      "dwo"  to Output("どぉ"),
      "d'u"  to Output("どぅ"),
      "nya"  to Output("にゃ"),
      "nyi"  to Output("にぃ"),
      "nyu"  to Output("にゅ"),
      "nye"  to Output("にぇ"),
      "nyo"  to Output("にょ"),
      "hya"  to Output("ひゃ"),
      "hyi"  to Output("ひぃ"),
      "hyu"  to Output("ひゅ"),
      "hye"  to Output("ひぇ"),
      "hyo"  to Output("ひょ"),
      "bya"  to Output("びゃ"),
      "byi"  to Output("びぃ"),
      "byu"  to Output("びゅ"),
      "bye"  to Output("びぇ"),
      "byo"  to Output("びょ"),
      "pya"  to Output("ぴゃ"),
      "pyi"  to Output("ぴぃ"),
      "pyu"  to Output("ぴゅ"),
      "pye"  to Output("ぴぇ"),
      "pyo"  to Output("ぴょ"),
      "fa"   to Output("ふぁ"),
      "fi"   to Output("ふぃ"),
      "fu"   to Output("ふ"),
      "fe"   to Output("ふぇ"),
      "fo"   to Output("ふぉ"),
      "fya"  to Output("ふゃ"),
      "fyu"  to Output("ふゅ"),
      "fyo"  to Output("ふょ"),
      "hwa"  to Output("ふぁ"),
      "hwi"  to Output("ふぃ"),
      "hwe"  to Output("ふぇ"),
      "hwo"  to Output("ふぉ"),
      "hwyu" to Output("ふゅ"),
      "mya"  to Output("みゃ"),
      "myi"  to Output("みぃ"),
      "myu"  to Output("みゅ"),
      "mye"  to Output("みぇ"),
      "myo"  to Output("みょ"),
      "rya"  to Output("りゃ"),
      "ryi"  to Output("りぃ"),
      "ryu"  to Output("りゅ"),
      "rye"  to Output("りぇ"),
      "ryo"  to Output("りょ"),
      "n'"   to Output("ん"),
      "nn"   to Output("ん"),
      "n"    to Output("ん"),
      "xn"   to Output("ん"),
      "a"    to Output("あ"),
      "i"    to Output("い"),
      "u"    to Output("う"),
      "wu"   to Output("う"),
      "e"    to Output("え"),
      "o"    to Output("お"),
      "xa"   to Output("ぁ"),
      "xi"   to Output("ぃ"),
      "xu"   to Output("ぅ"),
      "xe"   to Output("ぇ"),
      "xo"   to Output("ぉ"),
      "la"   to Output("ぁ"),
      "li"   to Output("ぃ"),
      "lu"   to Output("ぅ"),
      "le"   to Output("ぇ"),
      "lo"   to Output("ぉ"),
      "lyi"  to Output("ぃ"),
      "xyi"  to Output("ぃ"),
      "lye"  to Output("ぇ"),
      "xye"  to Output("ぇ"),
      "ye"   to Output("いぇ"),
      "ka"   to Output("か"),
      "ki"   to Output("き"),
      "ku"   to Output("く"),
      "ke"   to Output("け"),
      "ko"   to Output("こ"),
      "xka"  to Output("ヵ"),
      "xke"  to Output("ヶ"),
      "lka"  to Output("ヵ"),
      "lke"  to Output("ヶ"),
      "ga"   to Output("が"),
      "gi"   to Output("ぎ"),
      "gu"   to Output("ぐ"),
      "ge"   to Output("げ"),
      "go"   to Output("ご"),
      "sa"   to Output("さ"),
      "si"   to Output("し"),
      "su"   to Output("す"),
      "se"   to Output("せ"),
      "so"   to Output("そ"),
      "ca"   to Output("か"),
      "ci"   to Output("し"),
      "cu"   to Output("く"),
      "ce"   to Output("せ"),
      "co"   to Output("こ"),
      "qa"   to Output("くぁ"),
      "qi"   to Output("くぃ"),
      "qu"   to Output("く"),
      "qe"   to Output("くぇ"),
      "qo"   to Output("くぉ"),
      "kwa"  to Output("くぁ"),
      "kwi"  to Output("くぃ"),
      "kwu"  to Output("くぅ"),
      "kwe"  to Output("くぇ"),
      "kwo"  to Output("くぉ"),
      "gwa"  to Output("ぐぁ"),
      "gwi"  to Output("ぐぃ"),
      "gwu"  to Output("ぐぅ"),
      "gwe"  to Output("ぐぇ"),
      "gwo"  to Output("ぐぉ"),
      "za"   to Output("ざ"),
      "zi"   to Output("じ"),
      "zu"   to Output("ず"),
      "ze"   to Output("ぜ"),
      "zo"   to Output("ぞ"),
      "ja"   to Output("じゃ"),
      "ji"   to Output("じ"),
      "ju"   to Output("じゅ"),
      "je"   to Output("じぇ"),
      "jo"   to Output("じょ"),
      "jya"  to Output("じゃ"),
      "jyi"  to Output("じぃ"),
      "jyu"  to Output("じゅ"),
      "jye"  to Output("じぇ"),
      "jyo"  to Output("じょ"),
      "ta"   to Output("た"),
      "ti"   to Output("ち"),
      "tu"   to Output("つ"),
      "tsu"  to Output("つ"),
      "te"   to Output("て"),
      "to"   to Output("と"),
      "da"   to Output("だ"),
      "di"   to Output("ぢ"),
      "du"   to Output("づ"),
      "de"   to Output("で"),
      "do"   to Output("ど"),
      "xtu"  to Output("っ"),
      "xtsu" to Output("っ"),
      "ltu"  to Output("っ"),
      "ltsu" to Output("っ"),
      "na"   to Output("な"),
      "ni"   to Output("に"),
      "nu"   to Output("ぬ"),
      "ne"   to Output("ね"),
      "no"   to Output("の"),
      "ha"   to Output("は"),
      "hi"   to Output("ひ"),
      "hu"   to Output("ふ"),
      "fu"   to Output("ふ"),
      "he"   to Output("へ"),
      "ho"   to Output("ほ"),
      "ba"   to Output("ば"),
      "bi"   to Output("び"),
      "bu"   to Output("ぶ"),
      "be"   to Output("べ"),
      "bo"   to Output("ぼ"),
      "pa"   to Output("ぱ"),
      "pi"   to Output("ぴ"),
      "pu"   to Output("ぷ"),
      "pe"   to Output("ぺ"),
      "po"   to Output("ぽ"),
      "ma"   to Output("ま"),
      "mi"   to Output("み"),
      "mu"   to Output("む"),
      "me"   to Output("め"),
      "mo"   to Output("も"),
      "xya"  to Output("ゃ"),
      "lya"  to Output("ゃ"),
      "ya"   to Output("や"),
      "wyi"  to Output("ゐ"),
      "xyu"  to Output("ゅ"),
      "lyu"  to Output("ゅ"),
      "yu"   to Output("ゆ"),
      "wye"  to Output("ゑ"),
      "xyo"  to Output("ょ"),
      "lyo"  to Output("ょ"),
      "yo"   to Output("よ"),
      "ra"   to Output("ら"),
      "ri"   to Output("り"),
      "ru"   to Output("る"),
      "re"   to Output("れ"),
      "ro"   to Output("ろ"),
      "xwa"  to Output("ゎ"),
      "lwa"  to Output("ゎ"),
      "wa"   to Output("わ"),
      "wi"   to Output("うぃ"),
      "we"   to Output("うぇ"),
      "wo"   to Output("を"),
      "wha"  to Output("うぁ"),
      "whi"  to Output("うぃ"),
      "whu"  to Output("う"),
      "whe"  to Output("うぇ"),
      "who"  to Output("うぉ")
  ))

  private lateinit var timeFormatter: DateFormat
  private lateinit var variableExpander: VariableExpander

  @Mod.EventHandler
  fun preInit(event: FMLPreInitializationEvent) {
    loadConfig(File("config/JapaneCraft.cfg")) {
      val chatMsgFormat = it.getString("chat", "format",
          "<\$username> \$rawMessage\$n  §b\$convertedMessage",
          "The format for chat messages")

      variableExpander = VariableExpander(chatMsgFormat)

      val timeFormat = it.getString("time", "format",
          "HH:mm:ss", "The format for `\$time` in chat format")

      timeFormatter = SimpleDateFormat(timeFormat)
    }
  }

  @Mod.EventHandler
  fun init(event: FMLInitializationEvent) {
    MinecraftForge.EVENT_BUS.register(this)
  }

  @SubscribeEvent
  fun onServerChat(event: ServerChatEvent) {
    val (rawMessage, convertedMessage) = event.convertMessage()

    val variableMap = mapOf(
        "n"                to "\n",
        "$"                to "\$",
        "username"         to event.username,
        "time"             to timeFormatter.format(Date()),
        "rawMessage"       to rawMessage,
        "convertedMessage" to convertedMessage
    )

    variableExpander.expand(variableMap).split('\n').forEach {
      FMLCommonHandler
          .instance()
          .minecraftServerInstance
          .configurationManager
          .sendChatMsg(ChatComponentText(it))
    }

    event.isCanceled = true
  }

  @NetworkCheckHandler
  fun netCheckHandler(mods: Map<String, String>, side: Side): Boolean {
    return true
  }

  private fun ServerChatEvent.convertMessage(): Pair<String, String> {
    val enMsg = message
    val jpMsg = enMsg.toJapanese()

    return when {
      //                                     raw   to converted
      enMsg.any { it >= 0x80.toChar() }   -> ""    to enMsg
      enMsg.filter { it != '`' } == jpMsg -> ""    to enMsg
      else                                -> enMsg to jpMsg
    }
  }

  private fun String.toJapanese(): String {
    val romajiStr = this

    return buildString {
      for ((index, str) in romajiStr.split('`').withIndex()) {
        if (index % 2 != 0) {
          append(str)
        } else {
          for (word in str.split(' ')) {
            when {
              word.isEmpty() -> {
                append(' ')
              }

              word.first().isUpperCase() -> {
                append(word)
                append(' ')
              }

              else -> {
                append(romajiConverter.convert(word))
                append(' ')
              }
            }
          }

          deleteCharAt(lastIndex)
        }
      }
    }
  }

  private fun loadConfig(file: File, loadOperation: (Configuration) -> Unit) {
    val config = Configuration(file)
    config.load()
    loadOperation(config)
    config.save()
  }
}
