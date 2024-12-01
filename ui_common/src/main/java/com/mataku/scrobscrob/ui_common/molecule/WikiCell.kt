package com.mataku.scrobscrob.ui_common.molecule

import android.text.SpannableStringBuilder
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.text.HtmlCompat
import com.mataku.scrobscrob.core.entity.Wiki
import com.mataku.scrobscrob.ui_common.SunsetTextStyle
import com.mataku.scrobscrob.ui_common.extension.toAnnotatedString
import com.mataku.scrobscrob.ui_common.style.SunsetThemePreview

@Composable
fun WikiCell(
  wiki: Wiki,
  name: String,
  modifier: Modifier = Modifier,
  onUrlTap: (String) -> Unit
) {
  Column(
    modifier = modifier
      .fillMaxWidth()
  ) {
    Text(
      text = "About $name",
      style = SunsetTextStyle.headline
    )

    Spacer(modifier = Modifier.height(16.dp))

    wiki.summary?.let { content ->
      val spannable = SpannableStringBuilder(content).toString()
      val spanned = HtmlCompat.fromHtml(
        spannable,
        HtmlCompat.FROM_HTML_MODE_COMPACT
      )

      val text = spanned.toAnnotatedString()

      Text(
        text = text,
        style = SunsetTextStyle.label.copy(
          color = MaterialTheme.colorScheme.onSecondary
        )
      )
    }
  }
}

@Preview(showBackground = true)
@Composable
private fun WikiPreview() {
  val trackWiki = Wiki(
    published = "01 January 2023",
    content = "\"Clocks\" emerged in <b>conception during the late</b>stages into the production of Coldplay's second album, A Rush of Blood to the Head. The band's vocalist, Chris Martin, came in studio late one night. A riff popped  up in Martin's mind and wrote it on the  piano. Martin presented the riff to the band's guitarist, Jonny Buckland, who then added guitar chords on the basic track.\n\nDuring the writing of \"Clocks\", the band had already made 10 songs for the album. With this, they thought it was too late for the song's inclusion in the albumclude contrast, contradictions and urgency. Chris Martin sings of being in the state of \"helplessness ...",
    summary = "\"Clocks\" emerged in <b>conception during the late stages</b> into the production of Coldplay's second album, A Rush of Blood to the Head. The band's vocalist, Chris Martin, came in studio late one night. A riff popped  up in Martin's mind and wrote it on the  piano. Martin presented the riff to the band's guitarist, Jonny Buckland, who then added guitar chords on the basic track.\n\nDuring the writing of \"Clocks\", the band had already made 10 songs for the album. <a href=\"http://www.last.fm/music/Coldplay/_/Clocks\">Read more on Last.fm</a>.",
  )
  SunsetThemePreview {
    Surface {
      WikiCell(
        wiki = trackWiki,
        name = "Clocks",
        modifier = Modifier,
        onUrlTap = {}
      )
    }
  }
}
