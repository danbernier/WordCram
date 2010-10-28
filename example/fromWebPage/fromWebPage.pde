/*
Copyright 2010 Daniel Bernier

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
 */

/*
This sketch shows how to make a WordCram from any webpage.
It uses the WordCram blog, and was used to make the image in
the javadocs (http://wordcram.googlecode.com/svn/javadoc/index.html).

Minya Nouvelle font available at http://www.1001fonts.com/font_details.html?font_id=59
 */

import wordcram.*;

size(800, 400);
colorMode(HSB);
background(255);

WordCram wc = new WordCram(this)
  .fromWebPage("http://wordcram.wordpress.com")
  .withFont("Minya Nouvelle")
  .withColorer(Colorers.twoHuesRandomSatsOnWhite(this))
  .sizedByWeight(7, 100);

wc.drawAll();
