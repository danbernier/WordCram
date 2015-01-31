---
layout: post
---


{% for post in site.posts %}
### [{{post.title}}]({{site.baseurl}}{{post.url}})

{{post.date | date: "%Y %h %d"}}

{{post.excerpt}}
{% endfor %}
