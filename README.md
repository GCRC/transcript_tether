[![Build Status](https://travis-ci.com/GCRC/transcript_tether.svg?branch=master)](https://travis-ci.com/GCRC/transcript_tether)

# transcript_tether
Cross-platform java library and command-line utility to generate a subtitle file given a video and transcript using YouTube APIs.

# Obtaining
If you don't already have a working Java 8 runtime, [Download](http://www.oracle.com/technetwork/java/javase/jre8-downloads-2133155.html) and install.
Windows 10 Note: [Setting JAVA_HOME environment variables](https://www.mkyong.com/java/how-to-set-java_home-on-windows-10/)

Download the latest [transcript_tether-x.y.z.jar](https://github.com/GCRC/transcript_tether/releases) file.

# Usage
```
java -jar transcript_tether.jar [-c <arg>] [-i <arg>] [-l <arg>] [-o <arg>] -t <arg> [-v <arg>]
       
    -c, --credential <arg>          (Required on first run) Provide credential file for google api.
    -i, --videoId <arg>             (Optional) The videoId of an existing video on youtube.
                                        (e.g. https://youtu.be/{VIDEOID} or https://www.youtube.com/watch?v={VIDEOID})
    -l, --language <arg>            (Optional) Indicate the language code of the audio track in the video.
                                        (One of en, fr, de, ja, es, it, zh, cn) (Default en)
    -o,--output_path <arg>          (Optional) The output directory for the .srt file. Default is current folder.
    -t,--transcript_input <arg>     (Required) The transcript file for the video.
    -v,--video_input <arg>          (Required unless existing videoId is provided) The video file for tethering.
```
Examples:
```
java -jar transcript_tether-x.y.z.jar -v test1.mp4 -t test1.txt -c client_secret.json
java -jar transcript_tether-x.y.z.jar -v test2.mp4 -t test2.txt
java -jar transcript_tether-x.y.z.jar -i youtubeID -t test3.txt
```

## Additional Info
1. First, to install and deploy the software, you need to enable the youtube Data API v3 "https://console.developers.google.com/apis/library/youtube.googleapis.com". 

2. Second, at the first execution, the program will prompt up for you to create the OAUTH2.0 json file. Just follow the process, download the secret.json file and rerun the program with "-c {secret.json}". The json file will be cached to the user.home folder, so that you don't need to enter the secret info at next time.

3. The youtube uploading service has a 20 min length limitation for uploaded video. To remove this limitation, perform the verification on youtube account at "https://www.youtube.com/my_videos_upload_verify"

4. The video should be uploaded to the same youtube account, the youtube account authorizes the API.

5. The youtube channel needs to be created, since the program upload the video to users' personal channel (Privated).

# Build from source
Use git to clone the repository and run:

> gradle exec

The executable is located inside {PROJECT_FOLDER}/build/libs/

# Licensing

transcript_tether tool is copyright 2018 Geomatics and Cartographic Research Centre and licensed under the MIT license. See the LICENSE file for details.

Additional libraries are included at build time. Google APIs Client Library For Java, Google APIs Client Library For Java, google-api-services-youtube:v3, and Apache Commons CLI are licensed under the Apache 2.0 License (https://www.apache.org/licenses/LICENSE-2.0.html)
