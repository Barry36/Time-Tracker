# Time Tracker

### Project Information
* Name: TimeTracker
* Package Name: com.cs446.group18.timetracker
* Language: Java
* Min SDK: API26
* Target SDK: API29
* Device/Simulator: Pixel 4, API 29

### Google Map API Key
For this project, we used **BuildConfigs** to hide Google Map **API_KEY** that is not checked into source control. **BuildConfigs** will parse **API_KEY** from **apikey.properties** file.<br /><br />
To enable geolocation service, please create a file named **apikey.properties** in project root directory ("TimeTracker") with the value for **API_KEY**:
```
API_KEY="YOUR_API_KEY"
```

### Branch Note
* master: branch containing all changes with sample time entry data commented out
* develop: branch containing all changes with sample data
* other branch: used during development, do not contain all changes, please ingore
