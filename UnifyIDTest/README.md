## How to install apps

[iOS]

The iOS app is available on Diawi so please download and install it on your iPhone here.

Available soon

[Android]

- Requirement : Android OS 6.0 (API 23+)


The Android app is available on Diawi so you can install it on your Android Phone here.

Available soon

You can manually download and install the Android Apk here.
(When using a mobile device to download the android app directly from Github, a warning will pop up saying the file is too big to show. When this happens, click Desktop Version in the bottom right corner. From there, click View Raw in the grey box at the bottom of the screen. Then click download.)

https://github.com/GuiTuring/TuringTest/blob/master/UnifyIDTest/download_app_here/android/UnifyIDTest.apk

## Things to do:

For this challenge, we’re building a facial recognition platform that's running on mobile devices.
For that, we need to take 10 screenshots of the user's face and store them in a secure way, so
that they cannot be read/overwritten by attackers.
Your task is to make a usable app for iOS using Swift 3 and Xcode 8 and an Android app using
Java and Android studio. These will be two seperate apps but can leverage the same design.
Both apps should have a single button. When you press it, it will open the front camera of the
iOS or Android device, take 10 pictures of whatever is there, with an interval of 0.5s between
pictures for a total of 5 sec. Snapshots or video recording and snapshoting the video and post
processing later is up to you, this is why this challenge is so open ended.
The biggest challenge though is saving those 10 pictures in a secure persistent storage Android
or the iOS device, (you should use Apple's API. Hint: There's a way to do this with an iCloud
keychain) You should not upload these pictures though, they should be securely saved locally.
(What does securely mean to you is part of this challenge)
You can create a public github repo for this project, use whatever libraries or pods you wish.
Please include instructions on how-to build your project in the README.md file in the root of
your repository as well as a section called Further Considerations​ in which you describe
issues you had while working on this challenge, as well ideas to make this better in the future.
Generally, the time constraint is to create an app in 3 hours but since you will be building two we
understand you might need to go over. Reply as soon as you are complete with the github link
to jobs@unify.id. We will judge that you are inside the time constraints from whatever is earlier,
your email timestamp or your last commit in that repo.
Best of luck.
UnifyID Recruiting Team

## Development tools and languages

- iOS: Xcode 9 / Swift 3.0
- Android: Android Studio 3.1 /Java

## How to store images securely 

The method to store secure images is to store encrypted images to internal storage on devices.

Encrypt algorithm: AES

Encrypt key : generated and managed by KeyStore

