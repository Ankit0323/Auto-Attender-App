# Auto-Attender-App
The prime objective of “Auto Attender App“ is to develop a full fledged Android Application that can provide an easy to use interface for scheduling an online meeting and to easily manage their daily schedule.\
Making the App primarily running on cloud and reducing the user device interaction while he is in meeting.Make Bot (Javascript program) joining and leaving the meeting on time. Managing the user workload by enabling pre scheduling of classes and creating a file manager for storing daily documents on Day and Class basis.
## Features
1. Automatically join a meeting if available with your mic and camera off.
2. Add a random delay (10s - 180s) between joining and leaving meeting.
3. Turn off your camera and microphone.
4. It periodically looks for new meetings and joins them.
5. It uses cloud computing(Firebase cloud functions) to connect to meetings.
6. Sends notification when bot joins or leaves meeting.

### Working
1.When you input your meeting information, Bot will automatically called from the android app and the required information is shared with it, Bot will wait for the required time.\
2.And after wait time is over, bot will sign in into user’s google account and join the meeting.\
3.Bot is developed using javascript, running on node js environment and is deployed on google firebase cloud server, bot program is triggered using cloud functions.

### How to use
#### Login:
The following configuration is asked when you  
start the Application:\
Enter your Email \
Enter your Password\
User has to Login in app to start using the services \

#### Scheduling a meeting:
         1. Add your meeting information to your scheduler:\
         a)Meeting link\
                 b) Start time\
                 c) end time\
                 d) duration\
          2. when the meeting begin bot will join the meeting with your   
              profile and it will
              automatically switch off the camera and mic.\
          3. After the scheduled interval the bot will automatically  
              leaves the meeting and notifies user.\
              Saving and managing documents:
#### Saving and Managing Documents

This app includes some important features like \
1.Todo/Notes and file manager.\
2.Todo  is a feature to create and edit text notes. Features:\
a)No limits on note's length or number of notes (of course there's a limit to phone's storage).\
b)Creating and editing text notes.\
c)Automatically arranges PDF’ s and docs datawise.




