package com.mataku.scrobscrob.ui_common.molecule

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.mataku.scrobscrob.ui_common.SunsetTextStyle
import com.mataku.scrobscrob.ui_common.style.LocalAppTheme
import com.mataku.scrobscrob.ui_common.style.SunsetThemePreview
import com.mataku.scrobscrob.ui_common.style.accentColor

@Composable
fun SimpleWiki(
  name: String,
  url: String,
  modifier: Modifier = Modifier,
  onUrlTap: (String) -> Unit
) {
  Column(
    modifier = modifier
      .fillMaxWidth(),
    verticalArrangement = Arrangement.Center
  ) {
    Text(
      text = "About $name",
      style = SunsetTextStyle.headline
    )

    Spacer(modifier = Modifier.height(16.dp))

    Text(
      text = "Read more on Last.fm...",
      style = SunsetTextStyle.label.copy(
        color = LocalAppTheme.current.accentColor()
      ),
      modifier = Modifier
        .clickable(
          interactionSource = remember { MutableInteractionSource() },
          indication = null,
          enabled = true,
          onClickLabel = "Read $name more click",
          role = null
        ) {
          onUrlTap.invoke(url)
        },
      textAlign = TextAlign.Center
    )
  }
}

@Preview(showBackground = true)
@Composable
private fun SimpleWikiPreview() {
  SunsetThemePreview {
    Surface {
      Row(
        modifier = Modifier
          .padding(16.dp)
      ) {
        SimpleWiki(
          name = "aespa",
          url = "https://mataku.com",
          onUrlTap = {}
        )
      }
    }
  }
}
