package com.example.danielrong.personalassistant.commands;

/**
 * Created by danielrong on 12/2/17.
 */

public class WebSearchCommand {
    /**
     * Parses the user input and returns the words after the first occurrence of "for" or "search" in 'input'.
     * If there is no occurrence of "for" or "search", return null.
     * "for" has a higher priority
     *
     * @param input the raw command supplied by user
     * @return the intended search query
     * */
    public static String getSearchQuery(String input){
        String recipient = null;

        int indFor = input.toLowerCase().indexOf("for");
        int indSearch = input.toLowerCase().indexOf("search");

        if(indFor != -1 && (indFor + "for".length()+1) < input.length()){
            //If "for" is a substring and is not the last word...
            //Get the substring after "for"
            recipient = input.substring(indFor + "for".length()+1, input.length());
        }

        if(recipient == null && indSearch != -1 && (indSearch + "search".length()+1) < input.length()){
            //If there's no occurrence of "for", and "search" is a substring and is not the last word...
            //Get the substring after "search"
            recipient = input.substring(indSearch + "search".length()+1, input.length());
        }
        return recipient;
    }
}
