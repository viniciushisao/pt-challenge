# pt-challenge

## What is about
1. A List of restaurants nearby you that you can vote in which one you want to lunch.
1. It gets a list from Yelp. 
1. You can vote where you want to lunch and other people around you will see it.

## Plataform and language
1. Android plataform. 
1. To run it, do it in [Android Studio](https://developer.android.com/studio/index.html) and let it do all the stuff (gradle, java version and etc)
1. Minimum requirements:
  1. MinSdkVersion 19 (So the old devices is almost guarateed)
  1. GPS
  1. Internet connection

## Highlights
1. It has some limitations due it stores data in a free server and the data is KEY VALUE format. It was a big challenge to make it work properly.
1. It works without **ANY CONFIGURATION**. YESSSSS
1. Another goal is to make it work **WITHOUT MONEY** and ina KEY VALUE format. It was a big challenge and. No paid server nor databases.
1. I used as reference the [sample codes](https://github.com/viniciushisao/android_classes) I have when I am teaching Android. 
1. The code is supposed to work for **1 week**. After that, the free subscription will fail.

## Need to improve
1. Tests, always. Why I did not implemented tests? Because I am sure there is still things to be done before automate it. One day I will, I promise.
1. UI. I am not the best in UI coding but this is ugly AF.
1. Is it the right way to use retrofit? Not sure.

## TODO
1. Tests. Espresso? 
1. It is only tested in a old device. 
1. Notification system is still not done, but it will be easy to do since all calls are separated. Implement push notifications.
1. Make web service to remove all not valid votes.
1. Clean and refactor some classes.
1. Key value is extremaly hard and it is not eficient at all. Firebase? 
1. **REFACTOR CODE**

## Challenges
1. Learn a little bit of Retrofit (in all  my projects I implemented HTTP requests or I didn`t work on network)
1. Persist data in the internet without paying AND save on a server that is only key value.
1. I needed to update my sample codes, since some stuff was not right!
1. I did it only in 2 days and I am pretty happy with results.
