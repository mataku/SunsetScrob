package com.mataku.scrobscrob.test_helper.integration.fixture

import com.mataku.scrobscrob.core.entity.ArtistInfo
import com.mataku.scrobscrob.core.entity.Stats
import com.mataku.scrobscrob.core.entity.Tag
import com.mataku.scrobscrob.core.entity.Wiki
import kotlinx.collections.immutable.persistentListOf

val sampleArtistInfo: ArtistInfo = ArtistInfo(
  name = "Drama",
  url = "",
  images = persistentListOf(),
  tags = persistentListOf(
    Tag("K-POP", ""),
    Tag("K-POP", ""),
    Tag("K-POP", ""),
    Tag("K-POP", ""),
  ),
  stats = Stats(
    listeners = "1000000",
    playCount = "10000000"
  ),
  wiki = Wiki(
    published = "01 January 2023",
    summary = "aespa is a South Korean girl group formed by SM Entertainment. The group consists of four members: Karina (카리나), Giselle (지젤),  Winter (윈터) and Ningning (닝닝). They debuted on November 17, 2020 with the single \"Black Mamba\".\n\nThe group's name, aespa, combines the English initials of \"avatar\" and \"experience\" (Avatar X Experience) with the English word \"aspect\", meaning \"two sides\", to symbolize the idea of \"meeting another self and experiencing the new world\". <a href=\"https://www.last.fm/music/aespa\">Read more on Last.fm</a>",
    content = ""
  )
)
