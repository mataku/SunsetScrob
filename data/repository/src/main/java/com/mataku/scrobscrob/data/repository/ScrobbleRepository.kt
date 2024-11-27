package com.mataku.scrobscrob.data.repository

import com.mataku.scrobscrob.core.entity.NowPlayingTrackEntity
import com.mataku.scrobscrob.core.entity.RecentTrack
import com.mataku.scrobscrob.core.entity.ScrobbleResult
import com.mataku.scrobscrob.core.entity.imageUrl
import com.mataku.scrobscrob.data.api.LastFmService
import com.mataku.scrobscrob.data.api.endpoint.ApiSignature
import com.mataku.scrobscrob.data.api.endpoint.ScrobbleEndpoint
import com.mataku.scrobscrob.data.api.endpoint.UserRecentTracksEndpoint
import com.mataku.scrobscrob.data.api.model.RecentTracksApiResponse
import com.mataku.scrobscrob.data.db.ArtworkDataStore
import com.mataku.scrobscrob.data.db.SessionKeyDataStore
import com.mataku.scrobscrob.data.db.UsernameDataStore
import com.mataku.scrobscrob.data.repository.mapper.toRecentTracks
import com.mataku.scrobscrob.data.repository.mapper.toScrobbleResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject
import javax.inject.Singleton

interface ScrobbleRepository {
  suspend fun recentTracks(page: Int): Flow<List<RecentTrack>>

  suspend fun scrobble(currentTrack: NowPlayingTrackEntity): Flow<ScrobbleResult>
}

private val json = """
  {
    "recenttracks": {
      "track": [
        {
          "artist": {
            "mbid": "09887aa7-226e-4ecc-9a0c-02d2ae5777e1",
            "#text": "カーリー・レイ・ジェプセン"
          },
          "streamable": "0",
          "image": [
            {
              "size": "small",
              "#text": "https://lastfm.freetls.fastly.net/i/u/34s/f527e1666c0619e0280ef069e1c61afd.jpg"
            },
            {
              "size": "medium",
              "#text": "https://lastfm.freetls.fastly.net/i/u/64s/f527e1666c0619e0280ef069e1c61afd.jpg"
            },
            {
              "size": "large",
              "#text": "https://lastfm.freetls.fastly.net/i/u/174s/f527e1666c0619e0280ef069e1c61afd.jpg"
            },
            {
              "size": "extralarge",
              "#text": "https://lastfm.freetls.fastly.net/i/u/300x300/f527e1666c0619e0280ef069e1c61afd.jpg"
            }
          ],
          "mbid": "",
          "album": {
            "mbid": "",
            "#text": "Kiss"
          },
          "name": "Turn Me Up",
          "url": "https://www.last.fm/music/%E3%82%AB%E3%83%BC%E3%83%AA%E3%83%BC%E3%83%BB%E3%83%AC%E3%82%A4%E3%83%BB%E3%82%B8%E3%82%A7%E3%83%97%E3%82%BB%E3%83%B3/_/Turn+Me+Up",
          "date": {
            "uts": "1704273638",
            "#text": "03 Jan 2024, 09:20"
          }
        },
        {
          "artist": {
            "mbid": "9f1560de-9a9d-482b-afa0-99938e688330",
            "#text": "Ummet Ozcan"
          },
          "streamable": "0",
          "image": [
            {
              "size": "small",
              "#text": "https://lastfm.freetls.fastly.net/i/u/34s/e8926a5d5246b73aca21d7396a894a93.jpg"
            },
            {
              "size": "medium",
              "#text": "https://lastfm.freetls.fastly.net/i/u/64s/e8926a5d5246b73aca21d7396a894a93.jpg"
            },
            {
              "size": "large",
              "#text": "https://lastfm.freetls.fastly.net/i/u/174s/e8926a5d5246b73aca21d7396a894a93.jpg"
            },
            {
              "size": "extralarge",
              "#text": "https://lastfm.freetls.fastly.net/i/u/300x300/e8926a5d5246b73aca21d7396a894a93.jpg"
            }
          ],
          "mbid": "",
          "album": {
            "mbid": "",
            "#text": "Remember the Summer (feat. KARRA)"
          },
          "name": "Remember the Summer (feat. KARRA)",
          "url": "https://www.last.fm/music/Ummet+Ozcan/_/Remember+the+Summer+(feat.+KARRA)",
          "date": {
            "uts": "1704273470",
            "#text": "03 Jan 2024, 09:17"
          }
        },
        {
          "artist": {
            "mbid": "49204a7a-ed85-407a-828f-6fd46f1d8126",
            "#text": "NewJeans"
          },
          "streamable": "0",
          "image": [
            {
              "size": "small",
              "#text": "https://lastfm.freetls.fastly.net/i/u/34s/55b73e13e3c3a49647b910111f18eb12.jpg"
            },
            {
              "size": "medium",
              "#text": "https://lastfm.freetls.fastly.net/i/u/64s/55b73e13e3c3a49647b910111f18eb12.jpg"
            },
            {
              "size": "large",
              "#text": "https://lastfm.freetls.fastly.net/i/u/174s/55b73e13e3c3a49647b910111f18eb12.jpg"
            },
            {
              "size": "extralarge",
              "#text": "https://lastfm.freetls.fastly.net/i/u/300x300/55b73e13e3c3a49647b910111f18eb12.jpg"
            }
          ],
          "mbid": "df4c608f-9487-438c-a07e-23b740977988",
          "album": {
            "mbid": "",
            "#text": "NewJeans 'Super Shy'"
          },
          "name": "Super Shy",
          "url": "https://www.last.fm/music/NewJeans/_/Super+Shy",
          "date": {
            "uts": "1704273318",
            "#text": "03 Jan 2024, 09:15"
          }
        },
        {
          "artist": {
            "mbid": "30dd043c-b250-480e-bac2-0198844cd5af",
            "#text": "紫咲シオン"
          },
          "streamable": "0",
          "image": [
            {
              "size": "small",
              "#text": "https://lastfm.freetls.fastly.net/i/u/34s/1256bb96c887888350468d2adbe9d2a3.jpg"
            },
            {
              "size": "medium",
              "#text": "https://lastfm.freetls.fastly.net/i/u/64s/1256bb96c887888350468d2adbe9d2a3.jpg"
            },
            {
              "size": "large",
              "#text": "https://lastfm.freetls.fastly.net/i/u/174s/1256bb96c887888350468d2adbe9d2a3.jpg"
            },
            {
              "size": "extralarge",
              "#text": "https://lastfm.freetls.fastly.net/i/u/300x300/1256bb96c887888350468d2adbe9d2a3.jpg"
            }
          ],
          "mbid": "c7be5e7c-f7bf-4e58-8440-43c795f20ae8",
          "album": {
            "mbid": "",
            "#text": "シャンデリア"
          },
          "name": "シャンデリア",
          "url": "https://www.last.fm/music/%E7%B4%AB%E5%92%B2%E3%82%B7%E3%82%AA%E3%83%B3/_/%E3%82%B7%E3%83%A3%E3%83%B3%E3%83%87%E3%83%AA%E3%82%A2",
          "date": {
            "uts": "1704273205",
            "#text": "03 Jan 2024, 09:13"
          }
        },
        {
          "artist": {
            "mbid": "",
            "#text": "Power Music Workout"
          },
          "streamable": "0",
          "image": [
            {
              "size": "small",
              "#text": "https://lastfm.freetls.fastly.net/i/u/34s/2a96cbd8b46e442fc41c2b86b821562f.png"
            },
            {
              "size": "medium",
              "#text": "https://lastfm.freetls.fastly.net/i/u/64s/2a96cbd8b46e442fc41c2b86b821562f.png"
            },
            {
              "size": "large",
              "#text": "https://lastfm.freetls.fastly.net/i/u/174s/2a96cbd8b46e442fc41c2b86b821562f.png"
            },
            {
              "size": "extralarge",
              "#text": "https://lastfm.freetls.fastly.net/i/u/300x300/2a96cbd8b46e442fc41c2b86b821562f.png"
            }
          ],
          "mbid": "",
          "album": {
            "mbid": "",
            "#text": "That's What I Like - Single"
          },
          "name": "That's What I Like - Workout Mix",
          "url": "https://www.last.fm/music/Power+Music+Workout/_/That%27s+What+I+Like+-+Workout+Mix",
          "date": {
            "uts": "1703999502",
            "#text": "31 Dec 2023, 05:11"
          }
        },
        {
          "artist": {
            "mbid": "b51c672b-85e0-48fe-8648-470a2422229f",
            "#text": "aespa"
          },
          "streamable": "0",
          "image": [
            {
              "size": "small",
              "#text": "https://lastfm.freetls.fastly.net/i/u/34s/07bc2400d02a125e7b1ef0858ca57d71.jpg"
            },
            {
              "size": "medium",
              "#text": "https://lastfm.freetls.fastly.net/i/u/64s/07bc2400d02a125e7b1ef0858ca57d71.jpg"
            },
            {
              "size": "large",
              "#text": "https://lastfm.freetls.fastly.net/i/u/174s/07bc2400d02a125e7b1ef0858ca57d71.jpg"
            },
            {
              "size": "extralarge",
              "#text": "https://lastfm.freetls.fastly.net/i/u/300x300/07bc2400d02a125e7b1ef0858ca57d71.jpg"
            }
          ],
          "mbid": "f758907c-a06b-4187-bba4-f8ea9790e156",
          "album": {
            "mbid": "",
            "#text": "Drama - The 4th Mini Album"
          },
          "name": "Drama",
          "url": "https://www.last.fm/music/aespa/_/Drama",
          "date": {
            "uts": "1703951436",
            "#text": "30 Dec 2023, 15:50"
          }
        },
        {
          "artist": {
            "mbid": "",
            "#text": "Chiaki Sato"
          },
          "streamable": "0",
          "image": [
            {
              "size": "small",
              "#text": "https://lastfm.freetls.fastly.net/i/u/34s/c5597e3d1161e2fdea24af328c4df134.jpg"
            },
            {
              "size": "medium",
              "#text": "https://lastfm.freetls.fastly.net/i/u/64s/c5597e3d1161e2fdea24af328c4df134.jpg"
            },
            {
              "size": "large",
              "#text": "https://lastfm.freetls.fastly.net/i/u/174s/c5597e3d1161e2fdea24af328c4df134.jpg"
            },
            {
              "size": "extralarge",
              "#text": "https://lastfm.freetls.fastly.net/i/u/300x300/c5597e3d1161e2fdea24af328c4df134.jpg"
            }
          ],
          "mbid": "",
          "album": {
            "mbid": "0201dad9-c5f7-431c-867a-09ba90ea3f3e",
            "#text": "NIGHT TAPE"
          },
          "name": "Summer Gate - edbl Remix",
          "url": "https://www.last.fm/music/Chiaki+Sato/_/Summer+Gate+-+edbl+Remix",
          "date": {
            "uts": "1703951166",
            "#text": "30 Dec 2023, 15:46"
          }
        },
        {
          "artist": {
            "mbid": "de968d0a-0176-414f-b03a-80a4c50e5cba",
            "#text": "ExWHYZ"
          },
          "streamable": "0",
          "image": [
            {
              "size": "small",
              "#text": "https://lastfm.freetls.fastly.net/i/u/34s/c4ed6edaa161ff64dc80c7fe1fdda8c7.jpg"
            },
            {
              "size": "medium",
              "#text": "https://lastfm.freetls.fastly.net/i/u/64s/c4ed6edaa161ff64dc80c7fe1fdda8c7.jpg"
            },
            {
              "size": "large",
              "#text": "https://lastfm.freetls.fastly.net/i/u/174s/c4ed6edaa161ff64dc80c7fe1fdda8c7.jpg"
            },
            {
              "size": "extralarge",
              "#text": "https://lastfm.freetls.fastly.net/i/u/300x300/c4ed6edaa161ff64dc80c7fe1fdda8c7.jpg"
            }
          ],
          "mbid": "",
          "album": {
            "mbid": "",
            "#text": "HOW HIGH?"
          },
          "name": "Shall We",
          "url": "https://www.last.fm/music/ExWHYZ/_/Shall+We",
          "date": {
            "uts": "1703950966",
            "#text": "30 Dec 2023, 15:42"
          }
        },
        {
          "artist": {
            "mbid": "",
            "#text": "Hoshimachi Suisei"
          },
          "streamable": "0",
          "image": [
            {
              "size": "small",
              "#text": "https://lastfm.freetls.fastly.net/i/u/34s/c5de4e3a93703176e9a0154e79b7c545.jpg"
            },
            {
              "size": "medium",
              "#text": "https://lastfm.freetls.fastly.net/i/u/64s/c5de4e3a93703176e9a0154e79b7c545.jpg"
            },
            {
              "size": "large",
              "#text": "https://lastfm.freetls.fastly.net/i/u/174s/c5de4e3a93703176e9a0154e79b7c545.jpg"
            },
            {
              "size": "extralarge",
              "#text": "https://lastfm.freetls.fastly.net/i/u/300x300/c5de4e3a93703176e9a0154e79b7c545.jpg"
            }
          ],
          "mbid": "",
          "album": {
            "mbid": "",
            "#text": "Specter"
          },
          "name": "ソワレ",
          "url": "https://www.last.fm/music/Hoshimachi+Suisei/_/%E3%82%BD%E3%83%AF%E3%83%AC",
          "date": {
            "uts": "1703950741",
            "#text": "30 Dec 2023, 15:39"
          }
        },
        {
          "artist": {
            "mbid": "233164a1-a581-44f7-adac-cfdae2eefb66",
            "#text": "Thomas Gold"
          },
          "streamable": "0",
          "image": [
            {
              "size": "small",
              "#text": "https://lastfm.freetls.fastly.net/i/u/34s/83e2893117e2c0c11c6246a19625b7b0.jpg"
            },
            {
              "size": "medium",
              "#text": "https://lastfm.freetls.fastly.net/i/u/64s/83e2893117e2c0c11c6246a19625b7b0.jpg"
            },
            {
              "size": "large",
              "#text": "https://lastfm.freetls.fastly.net/i/u/174s/83e2893117e2c0c11c6246a19625b7b0.jpg"
            },
            {
              "size": "extralarge",
              "#text": "https://lastfm.freetls.fastly.net/i/u/300x300/83e2893117e2c0c11c6246a19625b7b0.jpg"
            }
          ],
          "mbid": "",
          "album": {
            "mbid": "",
            "#text": "Magic"
          },
          "name": "Magic",
          "url": "https://www.last.fm/music/Thomas+Gold/_/Magic",
          "date": {
            "uts": "1703950574",
            "#text": "30 Dec 2023, 15:36"
          }
        },
        {
          "artist": {
            "mbid": "",
            "#text": "Craig Reever"
          },
          "streamable": "0",
          "image": [
            {
              "size": "small",
              "#text": "https://lastfm.freetls.fastly.net/i/u/34s/1035a034f3459742c3d525e734c57b1f.jpg"
            },
            {
              "size": "medium",
              "#text": "https://lastfm.freetls.fastly.net/i/u/64s/1035a034f3459742c3d525e734c57b1f.jpg"
            },
            {
              "size": "large",
              "#text": "https://lastfm.freetls.fastly.net/i/u/174s/1035a034f3459742c3d525e734c57b1f.jpg"
            },
            {
              "size": "extralarge",
              "#text": "https://lastfm.freetls.fastly.net/i/u/300x300/1035a034f3459742c3d525e734c57b1f.jpg"
            }
          ],
          "mbid": "",
          "album": {
            "mbid": "",
            "#text": "We're Dynamite"
          },
          "name": "We're Dynamite (Hallman Remix)",
          "url": "https://www.last.fm/music/Craig+Reever/_/We%27re+Dynamite+(Hallman+Remix)",
          "date": {
            "uts": "1703950400",
            "#text": "30 Dec 2023, 15:33"
          }
        },
        {
          "artist": {
            "mbid": "b51c672b-85e0-48fe-8648-470a2422229f",
            "#text": "aespa"
          },
          "streamable": "0",
          "image": [
            {
              "size": "small",
              "#text": "https://lastfm.freetls.fastly.net/i/u/34s/1baa7610b225c971aba5044607a2ec89.jpg"
            },
            {
              "size": "medium",
              "#text": "https://lastfm.freetls.fastly.net/i/u/64s/1baa7610b225c971aba5044607a2ec89.jpg"
            },
            {
              "size": "large",
              "#text": "https://lastfm.freetls.fastly.net/i/u/174s/1baa7610b225c971aba5044607a2ec89.jpg"
            },
            {
              "size": "extralarge",
              "#text": "https://lastfm.freetls.fastly.net/i/u/300x300/1baa7610b225c971aba5044607a2ec89.jpg"
            }
          ],
          "mbid": "3f8abbac-1eb8-443b-ae13-bd994eb3e03b",
          "album": {
            "mbid": "23f0229d-0dff-4da6-a225-c8c7ea79bb9e",
            "#text": "Better Things"
          },
          "name": "Better Things",
          "url": "https://www.last.fm/music/aespa/_/Better+Things",
          "date": {
            "uts": "1703950200",
            "#text": "30 Dec 2023, 15:30"
          }
        },
        {
          "artist": {
            "mbid": "55abbede-aedf-4d07-9b94-e4d7051f63e3",
            "#text": "Syzz"
          },
          "streamable": "0",
          "image": [
            {
              "size": "small",
              "#text": "https://lastfm.freetls.fastly.net/i/u/34s/2a96cbd8b46e442fc41c2b86b821562f.png"
            },
            {
              "size": "medium",
              "#text": "https://lastfm.freetls.fastly.net/i/u/64s/2a96cbd8b46e442fc41c2b86b821562f.png"
            },
            {
              "size": "large",
              "#text": "https://lastfm.freetls.fastly.net/i/u/174s/2a96cbd8b46e442fc41c2b86b821562f.png"
            },
            {
              "size": "extralarge",
              "#text": "https://lastfm.freetls.fastly.net/i/u/300x300/2a96cbd8b46e442fc41c2b86b821562f.png"
            }
          ],
          "mbid": "",
          "album": {
            "mbid": "",
            "#text": "Promises (feat. Michael Zhonga) [Radio Edit]"
          },
          "name": "Promises (feat. Michael Zhonga) [Radio Edit]",
          "url": "https://www.last.fm/music/Syzz/_/Promises+(feat.+Michael+Zhonga)+%5BRadio+Edit%5D",
          "date": {
            "uts": "1703950014",
            "#text": "30 Dec 2023, 15:26"
          }
        },
        {
          "artist": {
            "mbid": "3573ecd1-739b-4bf4-85e3-bcbcad20f431",
            "#text": "Nornis"
          },
          "streamable": "0",
          "image": [
            {
              "size": "small",
              "#text": "https://lastfm.freetls.fastly.net/i/u/34s/e6d048653e3600a8ee8fd38b722d6e85.jpg"
            },
            {
              "size": "medium",
              "#text": "https://lastfm.freetls.fastly.net/i/u/64s/e6d048653e3600a8ee8fd38b722d6e85.jpg"
            },
            {
              "size": "large",
              "#text": "https://lastfm.freetls.fastly.net/i/u/174s/e6d048653e3600a8ee8fd38b722d6e85.jpg"
            },
            {
              "size": "extralarge",
              "#text": "https://lastfm.freetls.fastly.net/i/u/300x300/e6d048653e3600a8ee8fd38b722d6e85.jpg"
            }
          ],
          "mbid": "",
          "album": {
            "mbid": "5cb4cfcd-c888-4dc0-a44f-fd1c11746b99",
            "#text": "あの夏のいつかは"
          },
          "name": "あの夏のいつかは",
          "url": "https://www.last.fm/music/Nornis/_/%E3%81%82%E3%81%AE%E5%A4%8F%E3%81%AE%E3%81%84%E3%81%A4%E3%81%8B%E3%81%AF",
          "date": {
            "uts": "1703949769",
            "#text": "30 Dec 2023, 15:22"
          }
        },
        {
          "artist": {
            "mbid": "",
            "#text": "Power Music Workout"
          },
          "streamable": "0",
          "image": [
            {
              "size": "small",
              "#text": "https://lastfm.freetls.fastly.net/i/u/34s/2a96cbd8b46e442fc41c2b86b821562f.png"
            },
            {
              "size": "medium",
              "#text": "https://lastfm.freetls.fastly.net/i/u/64s/2a96cbd8b46e442fc41c2b86b821562f.png"
            },
            {
              "size": "large",
              "#text": "https://lastfm.freetls.fastly.net/i/u/174s/2a96cbd8b46e442fc41c2b86b821562f.png"
            },
            {
              "size": "extralarge",
              "#text": "https://lastfm.freetls.fastly.net/i/u/300x300/2a96cbd8b46e442fc41c2b86b821562f.png"
            }
          ],
          "mbid": "",
          "album": {
            "mbid": "",
            "#text": "That's What I Like - Single"
          },
          "name": "That's What I Like - Workout Mix",
          "url": "https://www.last.fm/music/Power+Music+Workout/_/That%27s+What+I+Like+-+Workout+Mix",
          "date": {
            "uts": "1703923981",
            "#text": "30 Dec 2023, 08:13"
          }
        },
        {
          "artist": {
            "mbid": "",
            "#text": "猫又おかゆ"
          },
          "streamable": "0",
          "image": [
            {
              "size": "small",
              "#text": "https://lastfm.freetls.fastly.net/i/u/34s/94439527ecc1e3cfc2ea3b29e365d9f7.jpg"
            },
            {
              "size": "medium",
              "#text": "https://lastfm.freetls.fastly.net/i/u/64s/94439527ecc1e3cfc2ea3b29e365d9f7.jpg"
            },
            {
              "size": "large",
              "#text": "https://lastfm.freetls.fastly.net/i/u/174s/94439527ecc1e3cfc2ea3b29e365d9f7.jpg"
            },
            {
              "size": "extralarge",
              "#text": "https://lastfm.freetls.fastly.net/i/u/300x300/94439527ecc1e3cfc2ea3b29e365d9f7.jpg"
            }
          ],
          "mbid": "",
          "album": {
            "mbid": "",
            "#text": "もぐもぐYUMMY!"
          },
          "name": "もぐもぐYUMMY!",
          "url": "https://www.last.fm/music/%E7%8C%AB%E5%8F%88%E3%81%8A%E3%81%8B%E3%82%86/_/%E3%82%82%E3%81%8F%E3%82%99%E3%82%82%E3%81%8F%E3%82%99YUMMY!",
          "date": {
            "uts": "1703923784",
            "#text": "30 Dec 2023, 08:09"
          }
        },
        {
          "artist": {
            "mbid": "28c97051-750d-4a1f-a723-8a0089fcf38c",
            "#text": "hololive IDOL PROJECT"
          },
          "streamable": "0",
          "image": [
            {
              "size": "small",
              "#text": "https://lastfm.freetls.fastly.net/i/u/34s/3860b3b8c84ea06ebe2f5970e8174323.jpg"
            },
            {
              "size": "medium",
              "#text": "https://lastfm.freetls.fastly.net/i/u/64s/3860b3b8c84ea06ebe2f5970e8174323.jpg"
            },
            {
              "size": "large",
              "#text": "https://lastfm.freetls.fastly.net/i/u/174s/3860b3b8c84ea06ebe2f5970e8174323.jpg"
            },
            {
              "size": "extralarge",
              "#text": "https://lastfm.freetls.fastly.net/i/u/300x300/3860b3b8c84ea06ebe2f5970e8174323.jpg"
            }
          ],
          "mbid": "8ac44dda-228f-49cf-94eb-bafc45fe04bd",
          "album": {
            "mbid": "",
            "#text": "百花繚乱花吹雪"
          },
          "name": "百花繚乱花吹雪",
          "url": "https://www.last.fm/music/hololive+IDOL+PROJECT/_/%E7%99%BE%E8%8A%B1%E7%B9%9A%E4%B9%B1%E8%8A%B1%E5%90%B9%E9%9B%AA",
          "date": {
            "uts": "1703923564",
            "#text": "30 Dec 2023, 08:06"
          }
        },
        {
          "artist": {
            "mbid": "55abbede-aedf-4d07-9b94-e4d7051f63e3",
            "#text": "Syzz"
          },
          "streamable": "0",
          "image": [
            {
              "size": "small",
              "#text": "https://lastfm.freetls.fastly.net/i/u/34s/2a96cbd8b46e442fc41c2b86b821562f.png"
            },
            {
              "size": "medium",
              "#text": "https://lastfm.freetls.fastly.net/i/u/64s/2a96cbd8b46e442fc41c2b86b821562f.png"
            },
            {
              "size": "large",
              "#text": "https://lastfm.freetls.fastly.net/i/u/174s/2a96cbd8b46e442fc41c2b86b821562f.png"
            },
            {
              "size": "extralarge",
              "#text": "https://lastfm.freetls.fastly.net/i/u/300x300/2a96cbd8b46e442fc41c2b86b821562f.png"
            }
          ],
          "mbid": "",
          "album": {
            "mbid": "",
            "#text": "Promises (feat. Michael Zhonga) [Radio Edit]"
          },
          "name": "Promises (feat. Michael Zhonga) [Radio Edit]",
          "url": "https://www.last.fm/music/Syzz/_/Promises+(feat.+Michael+Zhonga)+%5BRadio+Edit%5D",
          "date": {
            "uts": "1703922655",
            "#text": "30 Dec 2023, 07:50"
          }
        },
        {
          "artist": {
            "mbid": "25b56d06-8c6c-451e-93e7-25f85045a79e",
            "#text": "ksuke"
          },
          "streamable": "0",
          "image": [
            {
              "size": "small",
              "#text": "https://lastfm.freetls.fastly.net/i/u/34s/e4c3717434dd35d6e7611a6f7aac6c20.png"
            },
            {
              "size": "medium",
              "#text": "https://lastfm.freetls.fastly.net/i/u/64s/e4c3717434dd35d6e7611a6f7aac6c20.png"
            },
            {
              "size": "large",
              "#text": "https://lastfm.freetls.fastly.net/i/u/174s/e4c3717434dd35d6e7611a6f7aac6c20.png"
            },
            {
              "size": "extralarge",
              "#text": "https://lastfm.freetls.fastly.net/i/u/300x300/e4c3717434dd35d6e7611a6f7aac6c20.png"
            }
          ],
          "mbid": "",
          "album": {
            "mbid": "",
            "#text": "Pool (feat. Meron Ryan)"
          },
          "name": "Pool (feat. Meron Ryan)",
          "url": "https://www.last.fm/music/ksuke/_/Pool+(feat.+Meron+Ryan)",
          "date": {
            "uts": "1703922450",
            "#text": "30 Dec 2023, 07:47"
          }
        },
        {
          "artist": {
            "mbid": "8eeb59f4-0071-4261-b1f8-91d8294622d2",
            "#text": "星街すいせい"
          },
          "streamable": "0",
          "image": [
            {
              "size": "small",
              "#text": "https://lastfm.freetls.fastly.net/i/u/34s/c29b158735ad11b97b3bb787794ba7c6.jpg"
            },
            {
              "size": "medium",
              "#text": "https://lastfm.freetls.fastly.net/i/u/64s/c29b158735ad11b97b3bb787794ba7c6.jpg"
            },
            {
              "size": "large",
              "#text": "https://lastfm.freetls.fastly.net/i/u/174s/c29b158735ad11b97b3bb787794ba7c6.jpg"
            },
            {
              "size": "extralarge",
              "#text": "https://lastfm.freetls.fastly.net/i/u/300x300/c29b158735ad11b97b3bb787794ba7c6.jpg"
            }
          ],
          "mbid": "88d6dd15-25dc-4a11-b15f-4bcb7cfd7b61",
          "album": {
            "mbid": "bb66a32d-fafa-4ed2-957e-199ef0053dbe",
            "#text": "Specter"
          },
          "name": "ソワレ",
          "url": "https://www.last.fm/music/%E6%98%9F%E8%A1%97%E3%81%99%E3%81%84%E3%81%9B%E3%81%84/_/%E3%82%BD%E3%83%AF%E3%83%AC",
          "date": {
            "uts": "1703922226",
            "#text": "30 Dec 2023, 07:43"
          }
        },
        {
          "artist": {
            "mbid": "30dd043c-b250-480e-bac2-0198844cd5af",
            "#text": "紫咲シオン"
          },
          "streamable": "0",
          "image": [
            {
              "size": "small",
              "#text": "https://lastfm.freetls.fastly.net/i/u/34s/1256bb96c887888350468d2adbe9d2a3.jpg"
            },
            {
              "size": "medium",
              "#text": "https://lastfm.freetls.fastly.net/i/u/64s/1256bb96c887888350468d2adbe9d2a3.jpg"
            },
            {
              "size": "large",
              "#text": "https://lastfm.freetls.fastly.net/i/u/174s/1256bb96c887888350468d2adbe9d2a3.jpg"
            },
            {
              "size": "extralarge",
              "#text": "https://lastfm.freetls.fastly.net/i/u/300x300/1256bb96c887888350468d2adbe9d2a3.jpg"
            }
          ],
          "mbid": "c7be5e7c-f7bf-4e58-8440-43c795f20ae8",
          "album": {
            "mbid": "",
            "#text": "シャンデリア"
          },
          "name": "シャンデリア",
          "url": "https://www.last.fm/music/%E7%B4%AB%E5%92%B2%E3%82%B7%E3%82%AA%E3%83%B3/_/%E3%82%B7%E3%83%A3%E3%83%B3%E3%83%87%E3%83%AA%E3%82%A2",
          "date": {
            "uts": "1703922091",
            "#text": "30 Dec 2023, 07:41"
          }
        },
        {
          "artist": {
            "mbid": "de968d0a-0176-414f-b03a-80a4c50e5cba",
            "#text": "ExWHYZ"
          },
          "streamable": "0",
          "image": [
            {
              "size": "small",
              "#text": "https://lastfm.freetls.fastly.net/i/u/34s/c4ed6edaa161ff64dc80c7fe1fdda8c7.jpg"
            },
            {
              "size": "medium",
              "#text": "https://lastfm.freetls.fastly.net/i/u/64s/c4ed6edaa161ff64dc80c7fe1fdda8c7.jpg"
            },
            {
              "size": "large",
              "#text": "https://lastfm.freetls.fastly.net/i/u/174s/c4ed6edaa161ff64dc80c7fe1fdda8c7.jpg"
            },
            {
              "size": "extralarge",
              "#text": "https://lastfm.freetls.fastly.net/i/u/300x300/c4ed6edaa161ff64dc80c7fe1fdda8c7.jpg"
            }
          ],
          "mbid": "",
          "album": {
            "mbid": "",
            "#text": "HOW HIGH?"
          },
          "name": "NOT SORRY",
          "url": "https://www.last.fm/music/ExWHYZ/_/NOT+SORRY",
          "date": {
            "uts": "1703750077",
            "#text": "28 Dec 2023, 07:54"
          }
        },
        {
          "artist": {
            "mbid": "30dd043c-b250-480e-bac2-0198844cd5af",
            "#text": "紫咲シオン"
          },
          "streamable": "0",
          "image": [
            {
              "size": "small",
              "#text": "https://lastfm.freetls.fastly.net/i/u/34s/1256bb96c887888350468d2adbe9d2a3.jpg"
            },
            {
              "size": "medium",
              "#text": "https://lastfm.freetls.fastly.net/i/u/64s/1256bb96c887888350468d2adbe9d2a3.jpg"
            },
            {
              "size": "large",
              "#text": "https://lastfm.freetls.fastly.net/i/u/174s/1256bb96c887888350468d2adbe9d2a3.jpg"
            },
            {
              "size": "extralarge",
              "#text": "https://lastfm.freetls.fastly.net/i/u/300x300/1256bb96c887888350468d2adbe9d2a3.jpg"
            }
          ],
          "mbid": "c7be5e7c-f7bf-4e58-8440-43c795f20ae8",
          "album": {
            "mbid": "",
            "#text": "シャンデリア"
          },
          "name": "シャンデリア",
          "url": "https://www.last.fm/music/%E7%B4%AB%E5%92%B2%E3%82%B7%E3%82%AA%E3%83%B3/_/%E3%82%B7%E3%83%A3%E3%83%B3%E3%83%87%E3%83%AA%E3%82%A2",
          "date": {
            "uts": "1703749964",
            "#text": "28 Dec 2023, 07:52"
          }
        },
        {
          "artist": {
            "mbid": "b51c672b-85e0-48fe-8648-470a2422229f",
            "#text": "aespa"
          },
          "streamable": "0",
          "image": [
            {
              "size": "small",
              "#text": "https://lastfm.freetls.fastly.net/i/u/34s/a4486e161ff5803812e27897018f5fa6.jpg"
            },
            {
              "size": "medium",
              "#text": "https://lastfm.freetls.fastly.net/i/u/64s/a4486e161ff5803812e27897018f5fa6.jpg"
            },
            {
              "size": "large",
              "#text": "https://lastfm.freetls.fastly.net/i/u/174s/a4486e161ff5803812e27897018f5fa6.jpg"
            },
            {
              "size": "extralarge",
              "#text": "https://lastfm.freetls.fastly.net/i/u/300x300/a4486e161ff5803812e27897018f5fa6.jpg"
            }
          ],
          "mbid": "3535d3ce-5dfd-4613-b35a-fcc3f7ada553",
          "album": {
            "mbid": "",
            "#text": "Life's Too Short (English Version)"
          },
          "name": "Life's Too Short (English Version)",
          "url": "https://www.last.fm/music/aespa/_/Life%27s+Too+Short+(English+Version)",
          "date": {
            "uts": "1703749276",
            "#text": "28 Dec 2023, 07:41"
          }
        },
        {
          "artist": {
            "mbid": "f4fdbb4c-e4b7-47a0-b83b-d91bbfcfa387",
            "#text": "アリアナ・グランデ"
          },
          "streamable": "0",
          "image": [
            {
              "size": "small",
              "#text": "https://lastfm.freetls.fastly.net/i/u/34s/2a96cbd8b46e442fc41c2b86b821562f.png"
            },
            {
              "size": "medium",
              "#text": "https://lastfm.freetls.fastly.net/i/u/64s/2a96cbd8b46e442fc41c2b86b821562f.png"
            },
            {
              "size": "large",
              "#text": "https://lastfm.freetls.fastly.net/i/u/174s/2a96cbd8b46e442fc41c2b86b821562f.png"
            },
            {
              "size": "extralarge",
              "#text": "https://lastfm.freetls.fastly.net/i/u/300x300/2a96cbd8b46e442fc41c2b86b821562f.png"
            }
          ],
          "mbid": "",
          "album": {
            "mbid": "",
            "#text": "Yours Truly"
          },
          "name": "Baby I",
          "url": "https://www.last.fm/music/%E3%82%A2%E3%83%AA%E3%82%A2%E3%83%8A%E3%83%BB%E3%82%B0%E3%83%A9%E3%83%B3%E3%83%87/_/Baby+I",
          "date": {
            "uts": "1703749081",
            "#text": "28 Dec 2023, 07:38"
          }
        },
        {
          "artist": {
            "mbid": "",
            "#text": "never sleeps"
          },
          "streamable": "0",
          "image": [
            {
              "size": "small",
              "#text": "https://lastfm.freetls.fastly.net/i/u/34s/687c7155aaad52575dd84624305a0cf9.jpg"
            },
            {
              "size": "medium",
              "#text": "https://lastfm.freetls.fastly.net/i/u/64s/687c7155aaad52575dd84624305a0cf9.jpg"
            },
            {
              "size": "large",
              "#text": "https://lastfm.freetls.fastly.net/i/u/174s/687c7155aaad52575dd84624305a0cf9.jpg"
            },
            {
              "size": "extralarge",
              "#text": "https://lastfm.freetls.fastly.net/i/u/300x300/687c7155aaad52575dd84624305a0cf9.jpg"
            }
          ],
          "mbid": "",
          "album": {
            "mbid": "",
            "#text": "Stay With You"
          },
          "name": "Stay With You",
          "url": "https://www.last.fm/music/never+sleeps/_/Stay+With+You",
          "date": {
            "uts": "1703748876",
            "#text": "28 Dec 2023, 07:34"
          }
        },
        {
          "artist": {
            "mbid": "30dd043c-b250-480e-bac2-0198844cd5af",
            "#text": "紫咲シオン"
          },
          "streamable": "0",
          "image": [
            {
              "size": "small",
              "#text": "https://lastfm.freetls.fastly.net/i/u/34s/1256bb96c887888350468d2adbe9d2a3.jpg"
            },
            {
              "size": "medium",
              "#text": "https://lastfm.freetls.fastly.net/i/u/64s/1256bb96c887888350468d2adbe9d2a3.jpg"
            },
            {
              "size": "large",
              "#text": "https://lastfm.freetls.fastly.net/i/u/174s/1256bb96c887888350468d2adbe9d2a3.jpg"
            },
            {
              "size": "extralarge",
              "#text": "https://lastfm.freetls.fastly.net/i/u/300x300/1256bb96c887888350468d2adbe9d2a3.jpg"
            }
          ],
          "mbid": "c7be5e7c-f7bf-4e58-8440-43c795f20ae8",
          "album": {
            "mbid": "",
            "#text": "シャンデリア"
          },
          "name": "シャンデリア",
          "url": "https://www.last.fm/music/%E7%B4%AB%E5%92%B2%E3%82%B7%E3%82%AA%E3%83%B3/_/%E3%82%B7%E3%83%A3%E3%83%B3%E3%83%87%E3%83%AA%E3%82%A2",
          "date": {
            "uts": "1703748756",
            "#text": "28 Dec 2023, 07:32"
          }
        },
        {
          "artist": {
            "mbid": "",
            "#text": "Power Music Workout"
          },
          "streamable": "0",
          "image": [
            {
              "size": "small",
              "#text": "https://lastfm.freetls.fastly.net/i/u/34s/2a96cbd8b46e442fc41c2b86b821562f.png"
            },
            {
              "size": "medium",
              "#text": "https://lastfm.freetls.fastly.net/i/u/64s/2a96cbd8b46e442fc41c2b86b821562f.png"
            },
            {
              "size": "large",
              "#text": "https://lastfm.freetls.fastly.net/i/u/174s/2a96cbd8b46e442fc41c2b86b821562f.png"
            },
            {
              "size": "extralarge",
              "#text": "https://lastfm.freetls.fastly.net/i/u/300x300/2a96cbd8b46e442fc41c2b86b821562f.png"
            }
          ],
          "mbid": "",
          "album": {
            "mbid": "",
            "#text": "2018 Non-Stop Motivation (60 Min Non-Stop Workout Mix 130 BPM)"
          },
          "name": "Sick Boy - Workout Remix 130 BPM",
          "url": "https://www.last.fm/music/Power+Music+Workout/_/Sick+Boy+-+Workout+Remix+130+BPM",
          "date": {
            "uts": "1703682491",
            "#text": "27 Dec 2023, 13:08"
          }
        },
        {
          "artist": {
            "mbid": "b51c672b-85e0-48fe-8648-470a2422229f",
            "#text": "aespa"
          },
          "streamable": "0",
          "image": [
            {
              "size": "small",
              "#text": "https://lastfm.freetls.fastly.net/i/u/34s/3b96f9008abaa1134b17b8752abc3f78.jpg"
            },
            {
              "size": "medium",
              "#text": "https://lastfm.freetls.fastly.net/i/u/64s/3b96f9008abaa1134b17b8752abc3f78.jpg"
            },
            {
              "size": "large",
              "#text": "https://lastfm.freetls.fastly.net/i/u/174s/3b96f9008abaa1134b17b8752abc3f78.jpg"
            },
            {
              "size": "extralarge",
              "#text": "https://lastfm.freetls.fastly.net/i/u/300x300/3b96f9008abaa1134b17b8752abc3f78.jpg"
            }
          ],
          "mbid": "a9ad6929-17b3-4cd9-8c73-12755e874e8d",
          "album": {
            "mbid": "",
            "#text": "MY WORLD - The 3rd Mini Album"
          },
          "name": "Salty & Sweet",
          "url": "https://www.last.fm/music/aespa/_/Salty+&+Sweet",
          "date": {
            "uts": "1703682292",
            "#text": "27 Dec 2023, 13:04"
          }
        },
        {
          "artist": {
            "mbid": "49204a7a-ed85-407a-828f-6fd46f1d8126",
            "#text": "NewJeans"
          },
          "streamable": "0",
          "image": [
            {
              "size": "small",
              "#text": "https://lastfm.freetls.fastly.net/i/u/34s/dc24b237f51f88ff55af03ec47710e4f.jpg"
            },
            {
              "size": "medium",
              "#text": "https://lastfm.freetls.fastly.net/i/u/64s/dc24b237f51f88ff55af03ec47710e4f.jpg"
            },
            {
              "size": "large",
              "#text": "https://lastfm.freetls.fastly.net/i/u/174s/dc24b237f51f88ff55af03ec47710e4f.jpg"
            },
            {
              "size": "extralarge",
              "#text": "https://lastfm.freetls.fastly.net/i/u/300x300/dc24b237f51f88ff55af03ec47710e4f.jpg"
            }
          ],
          "mbid": "bec9099f-460f-4b35-b841-9619b4546487",
          "album": {
            "mbid": "",
            "#text": "NewJeans 2nd EP 'Get Up'"
          },
          "name": "ETA",
          "url": "https://www.last.fm/music/NewJeans/_/ETA",
          "date": {
            "uts": "1703682143",
            "#text": "27 Dec 2023, 13:02"
          }
        },
        {
          "artist": {
            "mbid": "55abbede-aedf-4d07-9b94-e4d7051f63e3",
            "#text": "Syzz"
          },
          "streamable": "0",
          "image": [
            {
              "size": "small",
              "#text": "https://lastfm.freetls.fastly.net/i/u/34s/2a96cbd8b46e442fc41c2b86b821562f.png"
            },
            {
              "size": "medium",
              "#text": "https://lastfm.freetls.fastly.net/i/u/64s/2a96cbd8b46e442fc41c2b86b821562f.png"
            },
            {
              "size": "large",
              "#text": "https://lastfm.freetls.fastly.net/i/u/174s/2a96cbd8b46e442fc41c2b86b821562f.png"
            },
            {
              "size": "extralarge",
              "#text": "https://lastfm.freetls.fastly.net/i/u/300x300/2a96cbd8b46e442fc41c2b86b821562f.png"
            }
          ],
          "mbid": "",
          "album": {
            "mbid": "",
            "#text": "Promises (feat. Michael Zhonga) [Radio Edit]"
          },
          "name": "Promises (feat. Michael Zhonga) [Radio Edit]",
          "url": "https://www.last.fm/music/Syzz/_/Promises+(feat.+Michael+Zhonga)+%5BRadio+Edit%5D",
          "date": {
            "uts": "1703681959",
            "#text": "27 Dec 2023, 12:59"
          }
        },
        {
          "artist": {
            "mbid": "de968d0a-0176-414f-b03a-80a4c50e5cba",
            "#text": "ExWHYZ"
          },
          "streamable": "0",
          "image": [
            {
              "size": "small",
              "#text": "https://lastfm.freetls.fastly.net/i/u/34s/c4ed6edaa161ff64dc80c7fe1fdda8c7.jpg"
            },
            {
              "size": "medium",
              "#text": "https://lastfm.freetls.fastly.net/i/u/64s/c4ed6edaa161ff64dc80c7fe1fdda8c7.jpg"
            },
            {
              "size": "large",
              "#text": "https://lastfm.freetls.fastly.net/i/u/174s/c4ed6edaa161ff64dc80c7fe1fdda8c7.jpg"
            },
            {
              "size": "extralarge",
              "#text": "https://lastfm.freetls.fastly.net/i/u/300x300/c4ed6edaa161ff64dc80c7fe1fdda8c7.jpg"
            }
          ],
          "mbid": "",
          "album": {
            "mbid": "",
            "#text": "HOW HIGH?"
          },
          "name": "フラチナサマー",
          "url": "https://www.last.fm/music/ExWHYZ/_/%E3%83%95%E3%83%A9%E3%83%81%E3%83%8A%E3%82%B5%E3%83%9E%E3%83%BC",
          "date": {
            "uts": "1703681744",
            "#text": "27 Dec 2023, 12:55"
          }
        },
        {
          "artist": {
            "mbid": "b51c672b-85e0-48fe-8648-470a2422229f",
            "#text": "aespa"
          },
          "streamable": "0",
          "image": [
            {
              "size": "small",
              "#text": "https://lastfm.freetls.fastly.net/i/u/34s/07bc2400d02a125e7b1ef0858ca57d71.jpg"
            },
            {
              "size": "medium",
              "#text": "https://lastfm.freetls.fastly.net/i/u/64s/07bc2400d02a125e7b1ef0858ca57d71.jpg"
            },
            {
              "size": "large",
              "#text": "https://lastfm.freetls.fastly.net/i/u/174s/07bc2400d02a125e7b1ef0858ca57d71.jpg"
            },
            {
              "size": "extralarge",
              "#text": "https://lastfm.freetls.fastly.net/i/u/300x300/07bc2400d02a125e7b1ef0858ca57d71.jpg"
            }
          ],
          "mbid": "f758907c-a06b-4187-bba4-f8ea9790e156",
          "album": {
            "mbid": "",
            "#text": "Drama - The 4th Mini Album"
          },
          "name": "Drama",
          "url": "https://www.last.fm/music/aespa/_/Drama",
          "date": {
            "uts": "1703681531",
            "#text": "27 Dec 2023, 12:52"
          }
        },
        {
          "artist": {
            "mbid": "f4fdbb4c-e4b7-47a0-b83b-d91bbfcfa387",
            "#text": "アリアナ・グランデ"
          },
          "streamable": "0",
          "image": [
            {
              "size": "small",
              "#text": "https://lastfm.freetls.fastly.net/i/u/34s/2a96cbd8b46e442fc41c2b86b821562f.png"
            },
            {
              "size": "medium",
              "#text": "https://lastfm.freetls.fastly.net/i/u/64s/2a96cbd8b46e442fc41c2b86b821562f.png"
            },
            {
              "size": "large",
              "#text": "https://lastfm.freetls.fastly.net/i/u/174s/2a96cbd8b46e442fc41c2b86b821562f.png"
            },
            {
              "size": "extralarge",
              "#text": "https://lastfm.freetls.fastly.net/i/u/300x300/2a96cbd8b46e442fc41c2b86b821562f.png"
            }
          ],
          "mbid": "",
          "album": {
            "mbid": "",
            "#text": "Yours Truly"
          },
          "name": "Baby I",
          "url": "https://www.last.fm/music/%E3%82%A2%E3%83%AA%E3%82%A2%E3%83%8A%E3%83%BB%E3%82%B0%E3%83%A9%E3%83%B3%E3%83%87/_/Baby+I",
          "date": {
            "uts": "1703681367",
            "#text": "27 Dec 2023, 12:49"
          }
        },
        {
          "artist": {
            "mbid": "f4d4b515-0b74-423f-a161-db184330c37c",
            "#text": "Gym Class Heroes"
          },
          "streamable": "0",
          "image": [
            {
              "size": "small",
              "#text": "https://lastfm.freetls.fastly.net/i/u/34s/487d1bd340684cd9853124c6e9516de9.png"
            },
            {
              "size": "medium",
              "#text": "https://lastfm.freetls.fastly.net/i/u/64s/487d1bd340684cd9853124c6e9516de9.png"
            },
            {
              "size": "large",
              "#text": "https://lastfm.freetls.fastly.net/i/u/174s/487d1bd340684cd9853124c6e9516de9.png"
            },
            {
              "size": "extralarge",
              "#text": "https://lastfm.freetls.fastly.net/i/u/300x300/487d1bd340684cd9853124c6e9516de9.png"
            }
          ],
          "mbid": "04de5e93-3739-3a20-a9ba-b03c24ef4657",
          "album": {
            "mbid": "09cec8c8-4969-4fc6-9534-8238b4af2d22",
            "#text": "The Papercut Chronicles II"
          },
          "name": "Stereo Hearts (feat. Adam Levine)",
          "url": "https://www.last.fm/music/Gym+Class+Heroes/_/Stereo+Hearts+(feat.+Adam+Levine)",
          "date": {
            "uts": "1703681158",
            "#text": "27 Dec 2023, 12:45"
          }
        },
        {
          "artist": {
            "mbid": "30dd043c-b250-480e-bac2-0198844cd5af",
            "#text": "紫咲シオン"
          },
          "streamable": "0",
          "image": [
            {
              "size": "small",
              "#text": "https://lastfm.freetls.fastly.net/i/u/34s/971281ba00b095fa648e317fd5a6405d.jpg"
            },
            {
              "size": "medium",
              "#text": "https://lastfm.freetls.fastly.net/i/u/64s/971281ba00b095fa648e317fd5a6405d.jpg"
            },
            {
              "size": "large",
              "#text": "https://lastfm.freetls.fastly.net/i/u/174s/971281ba00b095fa648e317fd5a6405d.jpg"
            },
            {
              "size": "extralarge",
              "#text": "https://lastfm.freetls.fastly.net/i/u/300x300/971281ba00b095fa648e317fd5a6405d.jpg"
            }
          ],
          "mbid": "cec7e699-50b1-405e-8622-e322bee6087d",
          "album": {
            "mbid": "8e977fb2-7194-48bc-ab7b-df4ca71a04fa",
            "#text": "メイジ・オブ・ヴァイオレット"
          },
          "name": "メイジ・オブ・ヴァイオレット",
          "url": "https://www.last.fm/music/%E7%B4%AB%E5%92%B2%E3%82%B7%E3%82%AA%E3%83%B3/_/%E3%83%A1%E3%82%A4%E3%82%B8%E3%83%BB%E3%82%AA%E3%83%96%E3%83%BB%E3%83%B4%E3%82%A1%E3%82%A4%E3%82%AA%E3%83%AC%E3%83%83%E3%83%88",
          "date": {
            "uts": "1703680978",
            "#text": "27 Dec 2023, 12:42"
          }
        },
        {
          "artist": {
            "mbid": "b51c672b-85e0-48fe-8648-470a2422229f",
            "#text": "aespa"
          },
          "streamable": "0",
          "image": [
            {
              "size": "small",
              "#text": "https://lastfm.freetls.fastly.net/i/u/34s/a4486e161ff5803812e27897018f5fa6.jpg"
            },
            {
              "size": "medium",
              "#text": "https://lastfm.freetls.fastly.net/i/u/64s/a4486e161ff5803812e27897018f5fa6.jpg"
            },
            {
              "size": "large",
              "#text": "https://lastfm.freetls.fastly.net/i/u/174s/a4486e161ff5803812e27897018f5fa6.jpg"
            },
            {
              "size": "extralarge",
              "#text": "https://lastfm.freetls.fastly.net/i/u/300x300/a4486e161ff5803812e27897018f5fa6.jpg"
            }
          ],
          "mbid": "3535d3ce-5dfd-4613-b35a-fcc3f7ada553",
          "album": {
            "mbid": "",
            "#text": "Life's Too Short (English Version)"
          },
          "name": "Life's Too Short (English Version)",
          "url": "https://www.last.fm/music/aespa/_/Life%27s+Too+Short+(English+Version)",
          "date": {
            "uts": "1703680802",
            "#text": "27 Dec 2023, 12:40"
          }
        },
        {
          "artist": {
            "mbid": "",
            "#text": "猫又おかゆ"
          },
          "streamable": "0",
          "image": [
            {
              "size": "small",
              "#text": "https://lastfm.freetls.fastly.net/i/u/34s/94439527ecc1e3cfc2ea3b29e365d9f7.jpg"
            },
            {
              "size": "medium",
              "#text": "https://lastfm.freetls.fastly.net/i/u/64s/94439527ecc1e3cfc2ea3b29e365d9f7.jpg"
            },
            {
              "size": "large",
              "#text": "https://lastfm.freetls.fastly.net/i/u/174s/94439527ecc1e3cfc2ea3b29e365d9f7.jpg"
            },
            {
              "size": "extralarge",
              "#text": "https://lastfm.freetls.fastly.net/i/u/300x300/94439527ecc1e3cfc2ea3b29e365d9f7.jpg"
            }
          ],
          "mbid": "",
          "album": {
            "mbid": "",
            "#text": "もぐもぐYUMMY!"
          },
          "name": "もぐもぐYUMMY!",
          "url": "https://www.last.fm/music/%E7%8C%AB%E5%8F%88%E3%81%8A%E3%81%8B%E3%82%86/_/%E3%82%82%E3%81%8F%E3%82%99%E3%82%82%E3%81%8F%E3%82%99YUMMY!",
          "date": {
            "uts": "1703680606",
            "#text": "27 Dec 2023, 12:36"
          }
        },
        {
          "artist": {
            "mbid": "09887aa7-226e-4ecc-9a0c-02d2ae5777e1",
            "#text": "カーリー・レイ・ジェプセン"
          },
          "streamable": "0",
          "image": [
            {
              "size": "small",
              "#text": "https://lastfm.freetls.fastly.net/i/u/34s/f527e1666c0619e0280ef069e1c61afd.jpg"
            },
            {
              "size": "medium",
              "#text": "https://lastfm.freetls.fastly.net/i/u/64s/f527e1666c0619e0280ef069e1c61afd.jpg"
            },
            {
              "size": "large",
              "#text": "https://lastfm.freetls.fastly.net/i/u/174s/f527e1666c0619e0280ef069e1c61afd.jpg"
            },
            {
              "size": "extralarge",
              "#text": "https://lastfm.freetls.fastly.net/i/u/300x300/f527e1666c0619e0280ef069e1c61afd.jpg"
            }
          ],
          "mbid": "",
          "album": {
            "mbid": "",
            "#text": "Kiss"
          },
          "name": "Turn Me Up",
          "url": "https://www.last.fm/music/%E3%82%AB%E3%83%BC%E3%83%AA%E3%83%BC%E3%83%BB%E3%83%AC%E3%82%A4%E3%83%BB%E3%82%B8%E3%82%A7%E3%83%97%E3%82%BB%E3%83%B3/_/Turn+Me+Up",
          "date": {
            "uts": "1703680384",
            "#text": "27 Dec 2023, 12:33"
          }
        },
        {
          "artist": {
            "mbid": "9f1560de-9a9d-482b-afa0-99938e688330",
            "#text": "Ummet Ozcan"
          },
          "streamable": "0",
          "image": [
            {
              "size": "small",
              "#text": "https://lastfm.freetls.fastly.net/i/u/34s/e8926a5d5246b73aca21d7396a894a93.jpg"
            },
            {
              "size": "medium",
              "#text": "https://lastfm.freetls.fastly.net/i/u/64s/e8926a5d5246b73aca21d7396a894a93.jpg"
            },
            {
              "size": "large",
              "#text": "https://lastfm.freetls.fastly.net/i/u/174s/e8926a5d5246b73aca21d7396a894a93.jpg"
            },
            {
              "size": "extralarge",
              "#text": "https://lastfm.freetls.fastly.net/i/u/300x300/e8926a5d5246b73aca21d7396a894a93.jpg"
            }
          ],
          "mbid": "",
          "album": {
            "mbid": "",
            "#text": "Remember the Summer (feat. KARRA)"
          },
          "name": "Remember the Summer (feat. KARRA)",
          "url": "https://www.last.fm/music/Ummet+Ozcan/_/Remember+the+Summer+(feat.+KARRA)",
          "date": {
            "uts": "1703680216",
            "#text": "27 Dec 2023, 12:30"
          }
        },
        {
          "artist": {
            "mbid": "b51c672b-85e0-48fe-8648-470a2422229f",
            "#text": "aespa"
          },
          "streamable": "0",
          "image": [
            {
              "size": "small",
              "#text": "https://lastfm.freetls.fastly.net/i/u/34s/07bc2400d02a125e7b1ef0858ca57d71.jpg"
            },
            {
              "size": "medium",
              "#text": "https://lastfm.freetls.fastly.net/i/u/64s/07bc2400d02a125e7b1ef0858ca57d71.jpg"
            },
            {
              "size": "large",
              "#text": "https://lastfm.freetls.fastly.net/i/u/174s/07bc2400d02a125e7b1ef0858ca57d71.jpg"
            },
            {
              "size": "extralarge",
              "#text": "https://lastfm.freetls.fastly.net/i/u/300x300/07bc2400d02a125e7b1ef0858ca57d71.jpg"
            }
          ],
          "mbid": "9dd424ed-eb37-41de-a879-fa1e57332572",
          "album": {
            "mbid": "",
            "#text": "Drama - The 4th Mini Album"
          },
          "name": "Hot Air Balloon",
          "url": "https://www.last.fm/music/aespa/_/Hot+Air+Balloon",
          "date": {
            "uts": "1703680020",
            "#text": "27 Dec 2023, 12:27"
          }
        },
        {
          "artist": {
            "mbid": "",
            "#text": "Power Music Workout"
          },
          "streamable": "0",
          "image": [
            {
              "size": "small",
              "#text": "https://lastfm.freetls.fastly.net/i/u/34s/2a96cbd8b46e442fc41c2b86b821562f.png"
            },
            {
              "size": "medium",
              "#text": "https://lastfm.freetls.fastly.net/i/u/64s/2a96cbd8b46e442fc41c2b86b821562f.png"
            },
            {
              "size": "large",
              "#text": "https://lastfm.freetls.fastly.net/i/u/174s/2a96cbd8b46e442fc41c2b86b821562f.png"
            },
            {
              "size": "extralarge",
              "#text": "https://lastfm.freetls.fastly.net/i/u/300x300/2a96cbd8b46e442fc41c2b86b821562f.png"
            }
          ],
          "mbid": "",
          "album": {
            "mbid": "",
            "#text": "Dusk Till Dawn - Single"
          },
          "name": "Dusk Till Dawn - Workout Mix",
          "url": "https://www.last.fm/music/Power+Music+Workout/_/Dusk+Till+Dawn+-+Workout+Mix",
          "date": {
            "uts": "1703679750",
            "#text": "27 Dec 2023, 12:22"
          }
        },
        {
          "artist": {
            "mbid": "",
            "#text": "アイコナ・ポップ"
          },
          "streamable": "0",
          "image": [
            {
              "size": "small",
              "#text": "https://lastfm.freetls.fastly.net/i/u/34s/2a96cbd8b46e442fc41c2b86b821562f.png"
            },
            {
              "size": "medium",
              "#text": "https://lastfm.freetls.fastly.net/i/u/64s/2a96cbd8b46e442fc41c2b86b821562f.png"
            },
            {
              "size": "large",
              "#text": "https://lastfm.freetls.fastly.net/i/u/174s/2a96cbd8b46e442fc41c2b86b821562f.png"
            },
            {
              "size": "extralarge",
              "#text": "https://lastfm.freetls.fastly.net/i/u/300x300/2a96cbd8b46e442fc41c2b86b821562f.png"
            }
          ],
          "mbid": "",
          "album": {
            "mbid": "",
            "#text": "Feels In My Body"
          },
          "name": "Feels In My Body",
          "url": "https://www.last.fm/music/%E3%82%A2%E3%82%A4%E3%82%B3%E3%83%8A%E3%83%BB%E3%83%9D%E3%83%83%E3%83%97/_/Feels+In+My+Body",
          "date": {
            "uts": "1703679593",
            "#text": "27 Dec 2023, 12:19"
          }
        },
        {
          "artist": {
            "mbid": "30dd043c-b250-480e-bac2-0198844cd5af",
            "#text": "紫咲シオン"
          },
          "streamable": "0",
          "image": [
            {
              "size": "small",
              "#text": "https://lastfm.freetls.fastly.net/i/u/34s/1256bb96c887888350468d2adbe9d2a3.jpg"
            },
            {
              "size": "medium",
              "#text": "https://lastfm.freetls.fastly.net/i/u/64s/1256bb96c887888350468d2adbe9d2a3.jpg"
            },
            {
              "size": "large",
              "#text": "https://lastfm.freetls.fastly.net/i/u/174s/1256bb96c887888350468d2adbe9d2a3.jpg"
            },
            {
              "size": "extralarge",
              "#text": "https://lastfm.freetls.fastly.net/i/u/300x300/1256bb96c887888350468d2adbe9d2a3.jpg"
            }
          ],
          "mbid": "c7be5e7c-f7bf-4e58-8440-43c795f20ae8",
          "album": {
            "mbid": "",
            "#text": "シャンデリア"
          },
          "name": "シャンデリア",
          "url": "https://www.last.fm/music/%E7%B4%AB%E5%92%B2%E3%82%B7%E3%82%AA%E3%83%B3/_/%E3%82%B7%E3%83%A3%E3%83%B3%E3%83%87%E3%83%AA%E3%82%A2",
          "date": {
            "uts": "1703679480",
            "#text": "27 Dec 2023, 12:18"
          }
        },
        {
          "artist": {
            "mbid": "",
            "#text": "デヴィッド・ゲッタ"
          },
          "streamable": "0",
          "image": [
            {
              "size": "small",
              "#text": "https://lastfm.freetls.fastly.net/i/u/34s/0071d557c4461d789ebb5ca448c2bd0a.jpg"
            },
            {
              "size": "medium",
              "#text": "https://lastfm.freetls.fastly.net/i/u/64s/0071d557c4461d789ebb5ca448c2bd0a.jpg"
            },
            {
              "size": "large",
              "#text": "https://lastfm.freetls.fastly.net/i/u/174s/0071d557c4461d789ebb5ca448c2bd0a.jpg"
            },
            {
              "size": "extralarge",
              "#text": "https://lastfm.freetls.fastly.net/i/u/300x300/0071d557c4461d789ebb5ca448c2bd0a.jpg"
            }
          ],
          "mbid": "",
          "album": {
            "mbid": "",
            "#text": "Nothing But the Beat Ultimate"
          },
          "name": "Turn Me On (feat. Nicki Minaj)",
          "url": "https://www.last.fm/music/%E3%83%87%E3%83%B4%E3%82%A3%E3%83%83%E3%83%89%E3%83%BB%E3%82%B2%E3%83%83%E3%82%BF/_/Turn+Me+On+(feat.+Nicki+Minaj)",
          "date": {
            "uts": "1703679290",
            "#text": "27 Dec 2023, 12:14"
          }
        },
        {
          "artist": {
            "mbid": "",
            "#text": "MAISONdes"
          },
          "streamable": "0",
          "image": [
            {
              "size": "small",
              "#text": "https://lastfm.freetls.fastly.net/i/u/34s/23423543ac19026d124f4b2e945842be.jpg"
            },
            {
              "size": "medium",
              "#text": "https://lastfm.freetls.fastly.net/i/u/64s/23423543ac19026d124f4b2e945842be.jpg"
            },
            {
              "size": "large",
              "#text": "https://lastfm.freetls.fastly.net/i/u/174s/23423543ac19026d124f4b2e945842be.jpg"
            },
            {
              "size": "extralarge",
              "#text": "https://lastfm.freetls.fastly.net/i/u/300x300/23423543ac19026d124f4b2e945842be.jpg"
            }
          ],
          "mbid": "",
          "album": {
            "mbid": "",
            "#text": "ヨワネハキ (Snowdrop mix)"
          },
          "name": "ヨワネハキ (Snowdrop mix)",
          "url": "https://www.last.fm/music/MAISONdes/_/%E3%83%A8%E3%83%AF%E3%83%8D%E3%83%8F%E3%82%AD+(Snowdrop+mix)",
          "date": {
            "uts": "1703679128",
            "#text": "27 Dec 2023, 12:12"
          }
        },
        {
          "artist": {
            "mbid": "30dd043c-b250-480e-bac2-0198844cd5af",
            "#text": "紫咲シオン"
          },
          "streamable": "0",
          "image": [
            {
              "size": "small",
              "#text": "https://lastfm.freetls.fastly.net/i/u/34s/1256bb96c887888350468d2adbe9d2a3.jpg"
            },
            {
              "size": "medium",
              "#text": "https://lastfm.freetls.fastly.net/i/u/64s/1256bb96c887888350468d2adbe9d2a3.jpg"
            },
            {
              "size": "large",
              "#text": "https://lastfm.freetls.fastly.net/i/u/174s/1256bb96c887888350468d2adbe9d2a3.jpg"
            },
            {
              "size": "extralarge",
              "#text": "https://lastfm.freetls.fastly.net/i/u/300x300/1256bb96c887888350468d2adbe9d2a3.jpg"
            }
          ],
          "mbid": "c7be5e7c-f7bf-4e58-8440-43c795f20ae8",
          "album": {
            "mbid": "",
            "#text": "シャンデリア"
          },
          "name": "シャンデリア",
          "url": "https://www.last.fm/music/%E7%B4%AB%E5%92%B2%E3%82%B7%E3%82%AA%E3%83%B3/_/%E3%82%B7%E3%83%A3%E3%83%B3%E3%83%87%E3%83%AA%E3%82%A2",
          "date": {
            "uts": "1703679014",
            "#text": "27 Dec 2023, 12:10"
          }
        },
        {
          "artist": {
            "mbid": "25b56d06-8c6c-451e-93e7-25f85045a79e",
            "#text": "ksuke"
          },
          "streamable": "0",
          "image": [
            {
              "size": "small",
              "#text": "https://lastfm.freetls.fastly.net/i/u/34s/e4c3717434dd35d6e7611a6f7aac6c20.png"
            },
            {
              "size": "medium",
              "#text": "https://lastfm.freetls.fastly.net/i/u/64s/e4c3717434dd35d6e7611a6f7aac6c20.png"
            },
            {
              "size": "large",
              "#text": "https://lastfm.freetls.fastly.net/i/u/174s/e4c3717434dd35d6e7611a6f7aac6c20.png"
            },
            {
              "size": "extralarge",
              "#text": "https://lastfm.freetls.fastly.net/i/u/300x300/e4c3717434dd35d6e7611a6f7aac6c20.png"
            }
          ],
          "mbid": "",
          "album": {
            "mbid": "",
            "#text": "Pool (feat. Meron Ryan)"
          },
          "name": "Pool (feat. Meron Ryan)",
          "url": "https://www.last.fm/music/ksuke/_/Pool+(feat.+Meron+Ryan)",
          "date": {
            "uts": "1703678810",
            "#text": "27 Dec 2023, 12:06"
          }
        },
        {
          "artist": {
            "mbid": "b51c672b-85e0-48fe-8648-470a2422229f",
            "#text": "aespa"
          },
          "streamable": "0",
          "image": [
            {
              "size": "small",
              "#text": "https://lastfm.freetls.fastly.net/i/u/34s/07bc2400d02a125e7b1ef0858ca57d71.jpg"
            },
            {
              "size": "medium",
              "#text": "https://lastfm.freetls.fastly.net/i/u/64s/07bc2400d02a125e7b1ef0858ca57d71.jpg"
            },
            {
              "size": "large",
              "#text": "https://lastfm.freetls.fastly.net/i/u/174s/07bc2400d02a125e7b1ef0858ca57d71.jpg"
            },
            {
              "size": "extralarge",
              "#text": "https://lastfm.freetls.fastly.net/i/u/300x300/07bc2400d02a125e7b1ef0858ca57d71.jpg"
            }
          ],
          "mbid": "f758907c-a06b-4187-bba4-f8ea9790e156",
          "album": {
            "mbid": "",
            "#text": "Drama - The 4th Mini Album"
          },
          "name": "Drama",
          "url": "https://www.last.fm/music/aespa/_/Drama",
          "date": {
            "uts": "1703678600",
            "#text": "27 Dec 2023, 12:03"
          }
        },
        {
          "artist": {
            "mbid": "",
            "#text": "Power Music Workout"
          },
          "streamable": "0",
          "image": [
            {
              "size": "small",
              "#text": "https://lastfm.freetls.fastly.net/i/u/34s/2a96cbd8b46e442fc41c2b86b821562f.png"
            },
            {
              "size": "medium",
              "#text": "https://lastfm.freetls.fastly.net/i/u/64s/2a96cbd8b46e442fc41c2b86b821562f.png"
            },
            {
              "size": "large",
              "#text": "https://lastfm.freetls.fastly.net/i/u/174s/2a96cbd8b46e442fc41c2b86b821562f.png"
            },
            {
              "size": "extralarge",
              "#text": "https://lastfm.freetls.fastly.net/i/u/300x300/2a96cbd8b46e442fc41c2b86b821562f.png"
            }
          ],
          "mbid": "",
          "album": {
            "mbid": "",
            "#text": "Most Girls - Single"
          },
          "name": "Most Girls - Workout Mix",
          "url": "https://www.last.fm/music/Power+Music+Workout/_/Most+Girls+-+Workout+Mix",
          "date": {
            "uts": "1703678290",
            "#text": "27 Dec 2023, 11:58"
          }
        }
      ],
      "@attr": {
        "user": "matakucom",
        "totalPages": "2000",
        "page": "1",
        "perPage": "50",
        "total": "99956"
      }
    }
  }

""".trimIndent()

@Singleton
class ScrobbleRepositoryImpl @Inject constructor(
  private val lastFmService: LastFmService,
  private val usernameDataStore: UsernameDataStore,
  private val sessionDataStore: SessionKeyDataStore,
  private val artworkDataStore: ArtworkDataStore
) : ScrobbleRepository {
  override suspend fun recentTracks(page: Int): Flow<List<RecentTrack>> = flow<List<RecentTrack>> {
    if (true) {
      val response = RecentTracksApiResponse.parse(json)
      emit(response.toRecentTracks())
      return@flow
    }

    val username = usernameDataStore.username() ?: emit(emptyList())

    val params = mapOf(
      "user" to username,
      "limit" to 50,
      "page" to page
    )

    val endpoint = UserRecentTracksEndpoint(
      params = params
    )

    val response = lastFmService.request(endpoint)
    val recentTracks = response.toRecentTracks()
    emit(recentTracks)
    // TODO: refactor
    recentTracks.distinct().forEach { track ->
      val imageUrl = track.images.imageUrl()
      if (imageUrl != null) {
        artworkDataStore.insertArtwork(
          albumName = track.albumName,
          artist = track.artistName,
          artworkUrl = imageUrl
        )
      }
    }
  }.flowOn(Dispatchers.IO)

  override suspend fun scrobble(currentTrack: NowPlayingTrackEntity) = flow {
    val sessionKey = sessionDataStore.sessionKey()
    if (sessionKey.isNullOrEmpty()) {
      emit(ScrobbleResult(accepted = false))
      return@flow
    }
    if (currentTrack.overScrobblePoint()) {
      val params = mutableMapOf(
        "album[0]" to currentTrack.albumName,
        "artist[0]" to currentTrack.artistName,
        "sk" to sessionKey,
        "timestamp[0]" to currentTrack.timeStamp.toString(),
        "track[0]" to currentTrack.trackName,
        "method" to "track.scrobble"
      )
      val apiSig = ApiSignature.generateApiSig(params)
      params.remove("method")
      params["api_sig"] = apiSig
      val endpoint = ScrobbleEndpoint(
        params = params
      )
      val response = lastFmService.request(endpoint)
      emit(response.toScrobbleResult())
    } else {
      emit(ScrobbleResult(false))
    }

  }.flowOn(Dispatchers.IO)
}
