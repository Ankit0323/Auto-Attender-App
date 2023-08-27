# Auto-Attender-App
Auto Attender is a bot online meeting attending app that provides the user with the facility to attend multiple meetings at a time. \ It is a simple platform which works to solve the problem of attending the meeting of the user. Now the user can easily attend the meeting anywhere by becoming their bot without wasting any time.Managing the user workload by enabling pre scheduling of classes and creating a file manager for storing daily documents on Day and Class basis.
## Features
1. Automatically join a meeting if available with your mic and camera off.
2. Add a random delay (10s - 180s) between joining and leaving meeting.
3. Turn off your camera and microphone.
4. It periodically looks for new meetings and joins them.
5. It uses cloud computing(Firebase cloud functions) to connect to meetings.
6. Sends notification when bot joins or leaves meeting.

#### Meet Saver:
Enhances privacy by automatically disabling the camera and microphone through the bot during meetings. Provides real-time notifications for meeting updates, ensuring users stay informed.

#### 🗒️ To-Do App:
Facilitates efficient task management with a synced notes feature based on dates. Users can create and organize notes, ensuring nothing falls through the cracks.

#### 📂 File Manager:
Introduces a practical file management solution that categorizes documents based on both the day and the class. Files are saved and synchronized for easy access and reference.

#### 💾 File Synchronization:
With the Meet Saver feature, the app centralizes all meeting links, offering a convenient repository for easy access. This helps users stay organized by ensuring they have all relevant meeting information in one place.


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
User has to Login in app to start using the services 

#### Scheduling a meeting:
         1. Add your meeting information to your scheduler:
         a)Meeting link
                 b) Start time
                 c) end time
                 d) duration
          2. when the meeting begin bot will join the meeting with your   
              profile and it will
              automatically switch off the camera and mic.
          3. After the scheduled interval the bot will automatically  
              leaves the meeting and notifies user.
              Saving and managing documents:





