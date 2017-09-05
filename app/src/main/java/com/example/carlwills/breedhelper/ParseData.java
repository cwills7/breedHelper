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
        try {
            String logTag = ParseData.class.getSimpleName();
            Resources res = activity.getResources();
            XmlResourceParser quizDataXmlParser = res.getXml(R.xml.pokeres);

            ArrayList<String> xmlTagStack = new ArrayList<>();
            ArrayList<Pokemon> quizQuestions = new ArrayList<>();

            Pokemon currentPoke = null;

            quizDataXmlParser.next();
            int eventType = quizDataXmlParser.getEventType();
            while (eventType != XmlPullParser.END_DOCUMENT) {
                //  begin document
                if (eventType == XmlPullParser.START_DOCUMENT) {
                    Log.d(logTag, "Begin Document");
                }
                //  begin tag
                else if (eventType == XmlPullParser.START_TAG) {
                    String tagName = quizDataXmlParser.getName();
                    xmlTagStack.add(tagName);
                    Log.d(logTag, "Begin Tag " + tagName + ", depth: " + xmlTagStack.size());
                    Log.d(logTag, "Tag " + tagName + " has " + quizDataXmlParser.getAttributeCount() + " attribute(s)");

                    // this is a beginning of a quiz question tag so create a new QuizQuestion object
                    if (tagName.equals("quizquestion")) {
                        currentPoke = new Pokemon();
                    } else if (tagName.equals("answer")) {

                    }
                }
                //  end tag
                else if (eventType == XmlPullParser.END_TAG) {
                    String tagName = quizDataXmlParser.getName();
                    if (xmlTagStack.size() < 1) {

                        return null;
                    }
                    xmlTagStack.remove(xmlTagStack.size() - 1);
                    Log.d(logTag, "End Tag " + quizDataXmlParser.getName() + ", depth: " + xmlTagStack.size());

                    //  reached the end of a quizquestion definition, add it to the array
                    if (tagName.equals("quizquestion")) {
                        if (currentPoke != null)
                            quizQuestions.add(currentPoke);
                        currentPoke = null;
                    }
                }
                //  text between tag begin and end
                else if (eventType == XmlPullParser.TEXT) {
                    String currentTag = xmlTagStack.get(xmlTagStack.size() - 1);
                    String text = quizDataXmlParser.getText();
                    Log.d(logTag, "Text: " + text + ", current tag: " + currentTag + ", depth: " + xmlTagStack.size());

                    if (currentPoke == null) {
                        Log.e(logTag, "currentPoke is not initialized! text: " + text + ", current tag: " + currentTag + ", depth: " + xmlTagStack.size());
                        continue;
                    }

                }
                eventType = quizDataXmlParser.next();
            }
            Log.d(logTag, "End Document");

        } catch (XmlPullParserException e) {

        } catch (IOException i){

        }

        return new ArrayList<>();

    }
}