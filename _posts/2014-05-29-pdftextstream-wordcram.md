---
layout: post
title: PDFTextStream + WordCram
date: 2014-05-29 12:34:44.000000000 -04:00
tags:
- related
status: publish
type: post
published: true
---

Snowtide Informatics has announced that their PDF text-extraction software, PDFTextStream, [is now free for use in single-threaded applications](http://cemerick.com/2012/08/02/pdftextstream-now-available-free-as-in-beer/).

This means you could take any PDF, extract its text, and make a word cloud from it - or even *piles* of PDFs. If you don't have piles of PDFs, you can find a bunch available for free through [The Hacker Shelf](http://hackershelf.com/browse/).

For instance, here's a word cloud I made from Dick Gabriel's *Patterns of Software*, which he makes [freely available on his site](http://dreamsongs.net/):

<img title="Patterns of Software, wordcrammed" src="{{site.baseurl}}/assets/patterns-of-software.png" />

(As good as the wordcram is, I highly recommend reading the book in its entirety, with all the words in their proper order.)

Here's the code I used to create it:

{% highlight java linenos %}
import com.snowtide.pdf.fonts.*;
import com.snowtide.util.*;
import com.snowtide.pdf.afm.*;
import com.snowtide.commons_logging.*;
import com.snowtide.pdf.forms.*;
import com.snowtide.pdf.parser.*;
import com.snowtide.pdf.annot.*;
import com.snowtide.pdf.util.*;
import com.snowtide.pdf.*;
import pdfts.examples.*;
import com.snowtide.pdf.lucene.*;
import com.snowtide.util.logging.*;
import com.snowtide.pdf.layout.*;
import com.snowtide.io.*;
import com.snowtide.commons_logging.impl.*;

import wordcram.*;

void setup()
  size(1100, 600);
  background(255);

  String text = "";
  try {
    text = loadPdf("~/reading/PatternsOfSoftware.pdf");
  }
  catch (Exception x) {
    println(x);
    exit();
  }

  new WordCram(this)
    .fromTextString(text)
    .withFont("DevanagariMT")
    .withWordPadding(1)
    .angledBetween(radians(10), radians(-10))
    .drawAll();

  save("patterns-of-software.png");
}

String loadPdf(String pdfFilePath) throws IOException {

  PDFTextStream pdfts = new PDFTextStream(pdfFilePath);
  StringBuilder text = new StringBuilder(1024);
  pdfts.pipe(new OutputTarget(text));
  pdfts.close();
  System.out.printf("The text extracted from %s is:", pdfFilePath);
  System.out.println(text);

  return text.toString();
}
{% endhighlight %}
