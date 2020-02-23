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

<img src="screens/main.png" height="662" width="447"><img src="screens/preview.png" height="662" width="447"><img src="screens/select.png" height="662" width="447"><img src="screens/saved.png" height="662" width="447"><img src="screens/setting.png" height="662" width="447">

## Features in Progress/Planned Features
- Ability to download the image
- Ability to specify a download path in settings
- Store wallpaper setting and download history to be viewed later
- Save favorite images to be viewed later
- Display list of curated subreddit reccomandations to get wallpapers from
- Randomly chosen wallpaper from list of favorites that refreshes at a specified interval
- Lite mode that loads in lower resolution images for the main screen
- Setting for only allowing downloads when connected to wifi
- GIF support. Be able to set GIFs as wallpaper
