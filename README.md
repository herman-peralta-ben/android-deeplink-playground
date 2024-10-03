# Android deep links playground

![Demo](doc/CatchDeeplink.mp4)

Use the following command to launch a deeplink:
```bash
adb shell 'am start -d "<YOUR DEEPLINK>"'"
```
For this sample app, test the following deep links:
```bash
adb shell 'am start -d "testing://www.testing.com/abc?name=herman&hobbies=coding,drums,games&age=36"'
```
```bash
adb shell 'am start -d "testing://test-playground/a/b/c?planId=myReallyLongId;type=demo&userIds=ga,gb,gc,gd&placeId=123;type=Park"'
```
