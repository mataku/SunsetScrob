package com.mataku.scrobscrob.test_helper.integration.fixture

import com.mataku.scrobscrob.core.entity.Tag
import com.mataku.scrobscrob.core.entity.TrackAlbumInfo
import com.mataku.scrobscrob.core.entity.TrackArtist
import com.mataku.scrobscrob.core.entity.TrackInfo
import com.mataku.scrobscrob.core.entity.Wiki
import kotlinx.collections.immutable.persistentListOf

val sampleTrackInfo: TrackInfo = TrackInfo(
  artist = TrackArtist(
    name = "aespaaespaaespaaespaaespaaespaaespa",
    url = ""
  ),
  listeners = "100000",
  url = "https://example.com",
  name = "Drama",
  album = TrackAlbumInfo(
    artist = "aespaaespaaespaaespaaespaaespa",
    imageList = persistentListOf(),
    title = "Drama"
  ),
  playCount = "10000",
  topTags = persistentListOf(
    Tag(name = "K-POP", url = ""),
    Tag(name = "K-POP", url = ""),
    Tag(name = "K-POP", url = ""),
  ),
  wiki = Wiki(
    published = "01 January 2023",
    content = "\"Clocks\" emerged in <b>conception during the late</b>stages into the production of Coldplay's second album, A Rush of Blood to the Head. The band's vocalist, Chris Martin, came in studio late one night. A riff popped  up in Martin's mind and wrote it on the  piano. Martin presented the riff to the band's guitarist, Jonny Buckland, who then added guitar chords on the basic track.\n\nDuring the writing of \"Clocks\", the band had already made 10 songs for the album. With this, they thought it was too late for the song's inclusion in the albumclude contrast, contradictions and urgency. Chris Martin sings of being in the state of \"helplessness ...",
    summary = "\"Clocks\" emerged in <b>conception during the late stages</b> into the production of Coldplay's second album, A Rush of Blood to the Head. The band's vocalist, Chris Martin, came in studio late one night. A riff popped  up in Martin's mind and wrote it on the  piano. Martin presented the riff to the band's guitarist, Jonny Buckland, who then added guitar chords on the basic track.\n\nDuring the writing of \"Clocks\", the band had already made 10 songs for the album. <a href=\"http://www.last.fm/music/Coldplay/_/Clocks\">Read more on Last.fm</a>.",
  )
)
