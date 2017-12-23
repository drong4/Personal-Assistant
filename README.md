# Personal-Assistant
I made this Android app for the final project of my programming studio class. This app is intended to be a much simpler version of an artificial intelligence personal assistant, like Siri or Alexa. The main purpose of making this app was for me to learn more about Android development and how AI personal assistants were implemented.

## Voice Commands
The following summarizes the implemented voice commands for the application. For a more detailed description of the commands, see the manual test plan: .

### Wake-up Phrase

This is a phrase that the user can say to prompt the app to start listening. The home page of the app will tell the user what that phrase is.

### Text Messaging

Saying the voice command `send text to <CONTACT>`, where `<CONTACT>` is a name of a contact on the phone, will make the app prompt the user for more information. If the app can't find <CONTACT> in the contacts, it will notify the user and tell them to try again. Otherwise, it'll prompt the user for the message. The app will repeat the desired message to the user to make sure that it's correct. After the user confirms, the text message is sent out.

### Weather

Saying the voice command `what's the weather in <CITY>`, where `<CITY>` is the name of a city, will result in the app trying to get the weather conditions at the time at that place and repeat it back. If it can't, it'll notify the user to try again with a valid city name.

### Calling

Saying the voice command `call <CONTACT>`, where `<CONTACT>` is the name of a contact on the phone, will make the app attempt to find a contact named `<CONTACT>`. If it can, it'll ask for confirmation. Otherwise, it'll notify the user that it couldn't find `<CONTACT>`.

### Web Search

Saying the voice command `search <QUERY>` or `search for <QUERY>`, where `<QUERY>` is what the user wants to search, will result in the app opening up a WebView that goes to a Google search results page with `<QUERY>` as the search query.

### Set Reminders

Saying the voice command `set reminder` will result in the app executing a number of prompts to get the information to make the reminder. This function uses the Google calendar, so it requires the user to have a Google account.

### Cancel
Saying `cancel` at any point when listening prompt is up will make the app stop listening for input.

## Settings
The user can customize the wake-up phrase in the `Settings` page. Although the user can set this to anything, it's important to note that it is recommended that the phrase be at least 3 syllables, as anything less than that will have many false positives.
