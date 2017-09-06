package com.example.carlwills.breedhelper;

import android.app.Activity;
import android.content.res.Resources;
import android.content.res.XmlResourceParser;
import android.util.Log;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

public class ParseData {

    public static ArrayList<Pokemon> parse(Activity activity) {
        ArrayList<Pokemon> pokeList = new ArrayList<>();

        try {
            String logTag = ParseData.class.getSimpleName();
            Resources res = activity.getResources();
            XmlResourceParser pokeParser = res.getXml(R.xml.pokeres);

            ArrayList<String> xmlTagStack = new ArrayList<>();

            Pokemon currentPoke = null;

            pokeParser.next();
            int eventType = pokeParser.getEventType();
            while (eventType != XmlPullParser.END_DOCUMENT) {
                //  begin document
                if (eventType == XmlPullParser.START_DOCUMENT) {
                    Log.d(logTag, "Begin Document");
                }
                //  begin tag
                else if (eventType == XmlPullParser.START_TAG) {
                    String tagName = pokeParser.getName();
                    xmlTagStack.add(tagName);
                    Log.d(logTag, "Begin Tag " + tagName + ", depth: " + xmlTagStack.size());
                    Log.d(logTag, "Tag " + tagName + " has " + pokeParser.getAttributeCount() + " attribute(s)");

                    // this is a beginning of a quiz question tag so create a new QuizQuestion object
                    if (tagName.equals("pokemon")) {
                        currentPoke = new Pokemon();
                    } else if (tagName.equals("answer")) {

                    }
                }
                //  end tag
                else if (eventType == XmlPullParser.END_TAG) {
                    String tagName = pokeParser.getName();
                    if (xmlTagStack.size() < 1) {

                        return null;
                    }
                    xmlTagStack.remove(xmlTagStack.size() - 1);
                    Log.d(logTag, "End Tag " + pokeParser.getName() + ", depth: " + xmlTagStack.size());

                    //  reached the end of a quizquestion definition, add it to the array
                    if (tagName.equals("pokemon")) {
                        if (currentPoke != null)
                        pokeList.add(currentPoke);

                        System.out.println(currentPoke.toString());
                        currentPoke = null;
                    }
                }
                //  text between tag begin and end
                else if (eventType == XmlPullParser.TEXT) {
                    String currentTag = xmlTagStack.get(xmlTagStack.size() - 1);
                    String text = pokeParser.getText();
                    switch (currentTag){
                        case "name": currentPoke.setName(text);
                                     break;
                        case "ability1": currentPoke.setAbility1(text);
                                        break;
                        case "ability2": currentPoke.setAbility2(text);
                            break;
                        case "hiddenAbility": currentPoke.setHiddenAbility(text);
                            break;
                        case "move": handleMoves(xmlTagStack, text, currentPoke);
                            break;
                    }

                    Log.d(logTag, "Text: " + text + ", current tag: " + currentTag + ", depth: " + xmlTagStack.size());

                    if (currentPoke == null) {
                        Log.e(logTag, "currentPoke is not initialized! text: " + text + ", current tag: " + currentTag + ", depth: " + xmlTagStack.size());

                    }

                }

                eventType = pokeParser.next();
            }
            Log.d(logTag, "End Document");

        } catch (XmlPullParserException e) {

        } catch (IOException i){

        }

        return pokeList;

    }

    private static Pokemon handleMoves(ArrayList<String> tagStack, String text, Pokemon pokemon) {
        String moveStackTag = tagStack.get(tagStack.size() - 2);

        switch(moveStackTag){
            case "eggMovesLearn": pokemon.getEggMovesLearn().add(text);
                break;
            case "eggMovesPass": pokemon.getEggMovesPass().add(text);
                break;
            case "otherMoves": pokemon.getOtherMoves().add(text);
                break;
        }

        return pokemon;
    }
}