# Screenshot tool most likely for monitoring in organisations
This tools allows you to specify a URL with login credentials with wich the application will take screenshots in a configured interval.
You can use it to frequently generate screenshot from monitoring tools like grafana or kibana (not exclusively). 

## Configuration
Basic settings such as image height/width and implicit wait before each action.
```properties
selenium.implicitWait=100
selenium.browser.height=1080
selenium.browser.width=1920
```

Additional configuration such as output directory and timestamp format wich will be rendered inside of the screenshots (if desired)
```properties
screenshot.outputDirectory=screenshots
screenshot.date-format=yyyy-MM-dd HH:mm:ss
screenshot.date-font-size=24
```

AWS Configuration
```properties
screenshot.aws.enabled=true
screenshot.aws.region=<YOUR-REGION(e.g. eu-central-1)>
screenshot.aws.key_id=<YOUR-ACCESS-KEY-ID>
screenshot.aws.access_key=<YOUR-SECRET-ACCESS-KEY>
```
AWS Secrets need to be specified as `aws:<SECRET-ID>` in Web-UI password field
