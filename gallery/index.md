---
title: Gallery
layout: default
---

Looking for some inspiration? Curious about WordCram, but don't know where to
start?  Take a look around.

{% for gallery in site.data.gallery %}

## {{ gallery.title }}

![]({{site.baseurl}}/gallery/{{gallery.path}}.png)

{% highlight java %}
{% include_relative {{gallery.path}}.pde %}
{% endhighlight %}

{% endfor %}

More is coming soon.
