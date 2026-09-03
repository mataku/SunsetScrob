package com.mataku.scrobscrob.ui_common.extension

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.LinkAnnotation
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.collections.shouldBeEmpty
import io.kotest.matchers.collections.shouldHaveSize
import io.kotest.matchers.shouldBe

class HtmlAnnotatedStringSpec : DescribeSpec({
  val linkStyle = SpanStyle(color = Color.Red)

  describe("htmlToAnnotatedString") {
    it("returns plain text unchanged") {
      val result = "Clocks by Coldplay".htmlToAnnotatedString(linkStyle)
      result.text shouldBe "Clocks by Coldplay"
      result.spanStyles.shouldBeEmpty()
    }

    it("maps <b> and <strong> to bold") {
      val result = "a <b>bold</b> and <strong>strong</strong>".htmlToAnnotatedString(linkStyle)
      result.text shouldBe "a bold and strong"
      result.spanStyles shouldHaveSize 2
      result.spanStyles[0].item.fontWeight shouldBe FontWeight.Bold
      result.spanStyles[0].start shouldBe 2
      result.spanStyles[0].end shouldBe 6
      result.spanStyles[1].start shouldBe 11
      result.spanStyles[1].end shouldBe 17
    }

    it("maps <i> and <em> to italic") {
      val result = "<i>it</i><em>em</em>".htmlToAnnotatedString(linkStyle)
      result.text shouldBe "item"
      result.spanStyles.map { it.item.fontStyle } shouldBe listOf(FontStyle.Italic, FontStyle.Italic)
    }

    it("maps <a href> to a url link with the link style") {
      val result = "Read more on <a href=\"https://www.last.fm/music/Coldplay\">Last.fm</a>.".htmlToAnnotatedString(linkStyle)
      result.text shouldBe "Read more on Last.fm."
      val links = result.getLinkAnnotations(0, result.text.length)
      links shouldHaveSize 1
      (links[0].item as LinkAnnotation.Url).url shouldBe "https://www.last.fm/music/Coldplay"
      links[0].start shouldBe 13
      links[0].end shouldBe 20
      result.spanStyles.single().item shouldBe linkStyle
    }

    it("turns <br> and </p> into line breaks") {
      "one<br>two<br/>three<p>four</p>".htmlToAnnotatedString(linkStyle).text shouldBe "one\ntwo\nthree\nfour\n"
    }

    it("decodes entities") {
      "Tom &amp; Jerry &lt;3 &quot;hi&quot; &#39;yo&#39; a&nbsp;b &#169; &#x263A;".htmlToAnnotatedString(linkStyle).text shouldBe
        "Tom & Jerry <3 \"hi\" 'yo' a b © ☺"
    }

    it("drops unknown tags but keeps their text") {
      "<span class=\"x\">kept</span><img src=\"a.png\">".htmlToAnnotatedString(linkStyle).text shouldBe "kept"
    }

    it("keeps text after an unclosed tag") {
      "start <b>never closed".htmlToAnnotatedString(linkStyle).text shouldBe "start never closed"
    }

    it("keeps a lone < that is not a tag") {
      "a < b".htmlToAnnotatedString(linkStyle).text shouldBe "a < b"
    }
  }
})
