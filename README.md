# RedditWall
An app that pulls images from a specified subreddit to set as device wallpaper
- This app takes a query and uses Reddit's REST api to pull data in a JSON format
- Parses the JSON data returned by the REST api and scrapes for image urls
- Loads the image urls to be set as wallpapers

## Features
- Loads images from a specified subreddit into a gridview user interface
- Loads more images on scroll
- Preview image and have the option to set it as a wallpaper
- Options between setting the home or lock screen wallpaper
- Specify the image resolution in settings
- Specify a default subreddit to load from on start up
- Sort Reddit images by new or by hot
- Save favorite subreddits using a Room database. Click on saved sub to search for images
- Long press a saved sub to set it as the default one

![Alt text](https://github.com/meh430/RedditWall/blob/master/screens/main.png "Screen")
![Alt text](https://github.com/meh430/RedditWall/blob/master/screens/preview.png "Screen")
![Alt text](https://github.com/meh430/RedditWall/blob/master/screens/select.png "Screen")
![Alt text](https://github.com/meh430/RedditWall/blob/master/screens/saved.png "Screen")
![Alt text](https://github.com/meh430/RedditWall/blob/master/screens/setting.png "Screen")

## Features in Progress
- Ability to download the image
- Ability to specify a download path in settings
- Store wallpaper setting and download history to be viewed later
- Save favorite images to be viewed later
- Display list of curated subreddit reccomandations to get wallpapers from
