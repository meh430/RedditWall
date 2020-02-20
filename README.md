# RedditWall
An app that pulls images from a specified subreddit to set as device wallpaper
- This app takes a query and uses Reddit's REST api to pull data in a JSON format
- Parses the JSON data returned by the REST api and scrapes for image urls
- Loads the image urls to be set as wallpapers.

## Features
- Loads images from a specified subreddit into a gridview user interface
- Loads more images on scroll
- Preview image and have the option to set it as a wallpaper
- Options between setting the home or lock screen wallpaper
- Specify the image resolution in settings
- Sort Reddit images by new or by hot

![Alt text](/RedditWall/screens/main.png?raw=true "Main Screen")

## Features in Progress
- Ability to download the image
- Ability to specify a download path in settings
- Ability to save a list of favorite subreddits to pull images from
- Store wallpaper setting and download history to be viewed later
