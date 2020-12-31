package com.mehul.redditwall.objects

data class Subreddit(var subName: String,
                     var subDesc: String,
                     var subscribers: Int,
                     var subIcon: String)