# Welcome to the project fork of the initial Jasonette project
This is a jasonette fork for android mobile application

this fork allows the call of all the Jsonette functionalities and APIs

* This fork also allows the following APIs to be called:
  * Local notification
  * Wifi state
  * Checkbox component
  * Radio component
  * Spinner
  * Progressbar
  * Dial

## This is a functional android project based entirely on Json interfaces
## The logic of the application is done by Json calls and javascript Agents

[Jasonette documentation](https://jasonelle-archive.github.io/docs/legacy/)

* Local notification :
  `"action": {"type": "$util.localNotification","options": {"title": "my title ONS","text": "my description"}}`
* Wifi state :
  `"action": {"type": "$util.wifiState","success": {"type": "$util.toast","options": {"text": "{{$jason}}" } }, "error": {"type": "$util.toast","options": {"text": "Impossible de joindre le wifi"}}}`
* Checkbox component :
  `{"type": "checkbox","name": "mycheck","hint": "my hint text","style": { "color": "white","background": "#0E111F"  } }`
* Radio component :
  `{"type": "radio","name": "radio","hint": "my hint text","value": "false","style": {"color": "white","background": "#0E111F","align": "center"}}`
* Spinner :
  `{"type": "spinner","name": "gender","data": "choix1|choix2|choix3|choix4","style": {"width": "220","height": "50","background": "#3498db","color": "#ffffff","font": "Roboto","size": "15"}}`
* Progressbar : 
  `{"type": "progressBar"}`
* Make dial :
  `"action": {"type": "$util.dial","options": {"url": "tel:50702547858214"}}`
